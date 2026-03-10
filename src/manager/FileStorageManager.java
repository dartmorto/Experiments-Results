import java.io.*;
import org.json.*;

public class FileStorageManager {
    private String filePath;

    public FileStorageManager(String filePath) {
        this.filePath = filePath;
    }

    // Method to read data from a JSON file
    public JSONObject readData() throws IOException, JSONException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                contentBuilder.append(sCurrentLine);
            }
        }
        return new JSONObject(contentBuilder.toString());
    }

    // Method to write data to a JSON file
    public void writeData(JSONObject jsonObject) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(jsonObject.toString(4)); // 4 spaces for indentation
        }
    }
}