package features.terminal.commands;

import core.cli.commands.CommandInstance;
import features.terminal.handlers.ClearScreenHandler;

public class ClearScreenCommand extends CommandInstance {
    public ClearScreenCommand() {
        super(
                "clear",
                "Clear the terminal screen. " +
                        "Note: This command only works in a terminal environment, not in an IDE console. " +
                        "It will not work in the screen that gets pop up when you simply press the run button " +
                        "in IntelliJ IDEA or similar IDEs. So please, run this system in an actual terminal.",
                "",
                new ClearScreenHandler()
        );
    }
}
