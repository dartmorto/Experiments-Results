package cli.commands;

import java.util.Map;

/**
 * Интерфейс регистрации команды.
 * Позволяет добавить команду в карту доступных команд CLI.
 */

public interface Registry {
    void register(Map<String, Command> commands);
}
