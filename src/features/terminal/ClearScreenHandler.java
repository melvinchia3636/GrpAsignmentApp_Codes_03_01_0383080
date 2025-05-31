package features.terminal;

import core.cli.commands.CommandInstance;
import core.terminal.OutputUtils;

import java.io.IOException;

public class ClearScreenHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        if (System.console() == null) {
            System.err.println("This command can only be run in a terminal.");
            return;
        }

        try {
            // Clear the console screen
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }

            OutputUtils.printHeader();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error clearing the screen: " + e.getMessage());
        }
    }
}
