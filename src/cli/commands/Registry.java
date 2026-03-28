package cli.commands;

import java.util.Map;

public interface Registry {
    void register(Map<String, Command> commands);
}
