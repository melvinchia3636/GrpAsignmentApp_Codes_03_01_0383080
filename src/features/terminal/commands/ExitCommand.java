package features.terminal.commands;

import core.cli.commands.CommandInstance;
import features.terminal.handlers.ExitHandler;

public class ExitCommand extends CommandInstance {
    public ExitCommand() {
        super(
                "exit",
                "Exit the application.",
                "",
                new ExitHandler()
        );
    }
}
