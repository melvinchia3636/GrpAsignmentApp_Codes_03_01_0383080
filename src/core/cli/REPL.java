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
     * Starts the REPL loop, which continuously prompts the user for input,
     * parses the input into commands, and executes the corresponding command.
     */
    public static void start() {
        OutputUtils.printHeader();
        Scanner scanner = new Scanner(System.in);

        // noinspection InfiniteLoopStatement - This is a REPL loop and the user is expected to exit manually.
        while (true) {
            // Prompt the user for command input, split the input by whitespace, and dump it into the CommandParser
            System.out.print(new Chalk("❯ ").bold().green().toString());
            String input = scanner.nextLine().trim();
            CommandParser.ParsedCommand parsedUserInput = CommandParser.parseFromRaw(input);

            // If an error occurs during parsing, it will be handled in the CommandParser and a null will be returned.
            // Since the error message is already printed, we can safely skip further processing.
            if (parsedUserInput == null) {
                continue;
            }

            boolean commandFound = false;

            // Loop through the commands and execute the matching one
            for (CommandInstance commandInstance : CommandRegistrar.commandInstances) {
                if (commandInstance.name.equals(parsedUserInput.command)) {
                    commandInstance.execute(parsedUserInput);
                    commandFound = true;
                    break;
                }
            }

            // If no command matches, print an error message
            if (!commandFound) {
                OutputUtils.error("Command not found: " + parsedUserInput.command);
            }
        }
    }
}
