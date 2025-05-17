package app;

import auth.LoginHandler;
import cli.CommandParser;
import terminal.OutputUtils;

/**
 * Main is the entry point for the application.
 */
public class Main {
    /**
     * The main method parses the command-line arguments and dispatches commands.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // Parse the command and arguments
        CommandParser.ParsedCommand parsedCommand = CommandParser.parseFromRaw(args);

        // Dispatch based on the command
        switch (parsedCommand.command) {
            case "login":
                LoginHandler.login(parsedCommand);
                break;
            default:
                OutputUtils.error("Unknown command: " + parsedCommand.command);
                break;
        }
    }
}
