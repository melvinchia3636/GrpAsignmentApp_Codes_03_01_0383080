package core.cli;

import core.cli.commands.CommandInstance;
import core.cli.commands.CommandParser;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.CommandRegistrar;

import java.util.Scanner;

/**
 * The REPL (Read-Eval-Print Loop) class is responsible for starting the command-line interface
 * and processing user commands in a loop.
 * It reads user input, parses it, and executes the corresponding command.
 */
public class REPL {
    /**
     * Prompts the user for input, reads the command line, and parses it into a command object.
     * If the input is invalid, it will print an error message and return null.
     *
     * @return A ParsedCommand object containing the command and its arguments, or null if parsing fails.
     */
    private static CommandParser.ParsedCommand promptAndParseCommand() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(new Chalk("❯ ").bold().green());
        String input = scanner.nextLine().trim();

        return CommandParser.parseFromRaw(input);
    }

    /**
     * Finds the command that matches the user's input and dispatches it for execution.
     * If no command matches, it prints an error message.
     *
     * @param parsedUserInput The parsed command input from the user.
     */
    private static void findAndDispatchCommand(CommandParser.ParsedCommand parsedUserInput) {
        boolean commandFound = false;

        for (CommandInstance commandInstance : CommandRegistrar.commandInstances) {
            if (commandInstance.name.equals(parsedUserInput.command)) {
                commandInstance.execute(parsedUserInput);
                commandFound = true;
                break;
            }
        }

        if (!commandFound) {
            OutputUtils.printError("Command not found: " + parsedUserInput.command);
        }
    }

    /**
     * Starts the REPL loop, which continuously prompts the user for input,
     * parses the input into commands, and executes the corresponding command.
     */
    public static void start() {
        System.out.println(new Chalk("Starting the REPL...").bold().green());
        OutputUtils.printHeader();

        // noinspection InfiniteLoopStatement - This is a REPL loop and the user is expected to exit manually.
        while (true) {
            CommandParser.ParsedCommand parsedUserInput = promptAndParseCommand();

            // If an error occurs during parsing, it will be handled in the CommandParser and a null will be returned.
            // Since the error message is already printed, we can safely skip further processing.
            if (parsedUserInput == null) {
                continue;
            }

            findAndDispatchCommand(parsedUserInput);
        }
    }
}
