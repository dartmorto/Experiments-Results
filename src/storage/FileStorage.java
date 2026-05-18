package storage;

import manager.CollectionManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Objects;

/**
 * Хранилище данных в файле.
 */
public final class FileStorage {

    private final FileValidator validator = new FileValidator();

    public void save(CollectionManager manager, String filePath) throws IOException {
        save(manager, Path.of(requirePath(filePath)));
    }

    public void save(CollectionManager manager, Path filePath) throws IOException {
        Objects.requireNonNull(manager, "manager");
        Path path = requirePath(filePath);

        Path parent = path.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        StorageData localData = StorageData.from(manager);
        validator.validate(localData);

        try (FileChannel channel = FileChannel.open(
                path,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE
        );
              FileLock ignored = channel.lock()) {
            writeLocked(channel, localData);
        }
    }

    public void load(CollectionManager manager, String filePath) throws IOException {
        load(manager, Path.of(requirePath(filePath)));
    }

    public void load(CollectionManager manager, Path filePath) throws IOException {
        Objects.requireNonNull(manager, "manager");

        StorageData loadedData = read(filePath);
        manager.replaceData(loadedData.getExperiments(), loadedData.getRuns(), loadedData.getResults());
    }

    private StorageData read(Path filePath) throws IOException {
        Path path = requireReadableFile(filePath);

        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
             FileLock ignored = channel.lock(0L, Long.MAX_VALUE, true)) {
            return readLocked(channel);
        }
    }

    private StorageData readLocked(FileChannel channel) throws IOException {
        if (channel.size() == 0) {
            return new StorageData(Map.of(), Map.of(), Map.of());
        }
        if (channel.size() > Integer.MAX_VALUE) {
            throw new IOException("файл слишком большой для загрузки");
        }

        ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
        channel.position(0);
        while (buffer.hasRemaining() && channel.read(buffer) != -1) {
            // read to buffer
        }

        try (java.io.ObjectInputStream input = new java.io.ObjectInputStream(
                new ByteArrayInputStream(buffer.array())
        )) {
            Object object = input.readObject();
            if (!(object instanceof StorageData)) {
                throw new IllegalArgumentException("ожидался снимок данных StorageData");
            }
            StorageData data = (StorageData) object;
            validator.validate(data);
            return data;
        } catch (StreamCorruptedException | OptionalDataException | EOFException e) {
            throw new IOException("формат файла поврежден или не является файлом данных", e);
        } catch (InvalidClassException e) {
            throw new IOException("версия классов в файле несовместима: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new IOException("файл содержит неизвестный тип данных: " + e.getMessage(), e);
        } catch (ClassCastException e) {
            throw new IOException("файл содержит данные неправильного типа: " + e.getMessage(), e);
        }
    }

    private void writeLocked(FileChannel channel, StorageData data) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (java.io.ObjectOutputStream output = new java.io.ObjectOutputStream(bytes)) {
            output.writeObject(data);
        }

        channel.position(0);
        channel.truncate(0);
        channel.write(ByteBuffer.wrap(bytes.toByteArray()));
        channel.force(true);
    }

    private String requirePath(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("путь к файлу не указан");
        }
        return filePath.trim();
    }

    private Path requirePath(Path filePath) {
        Objects.requireNonNull(filePath, "filePath");
        return filePath;
    }

    private Path requireReadableFile(Path filePath) throws IOException {
        Path path = requirePath(filePath);

        if (!Files.exists(path)) {
            throw new IOException("файл не существует: " + path);
        }
        if (!Files.isRegularFile(path)) {
            throw new IOException("путь не является файлом: " + path);
        }
        if (!Files.isReadable(path)) {
            throw new IOException("файл недоступен для чтения: " + path);
        }

        return path;
    }
}
