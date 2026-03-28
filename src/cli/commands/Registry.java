package cli.commands;
import cli.CommandHandler;
import java.util.*;

public interface Registry {
    void register(Map<String, Command> commands);
}
