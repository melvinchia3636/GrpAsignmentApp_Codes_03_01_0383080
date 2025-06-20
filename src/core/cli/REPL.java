package core.cli;

import core.cli.commands.CommandError;
import core.cli.commands.CommandInstance;
import core.cli.commands.CommandParser;
import core.cli.commands.CommandRegistrar;
import core.terminal.Chalk;
import core.terminal.OutputUtils;

import java.util.Arrays;
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
        System.out.print(new Chalk("\n❯ ").bold().green());
        String input = scanner.nextLine().trim();

        return CommandParser.parseFromRaw(input);
    }

    /**
     * Finds the command by its name and dispatches it for execution.
     * If the command has subcommands, it will recursively find the appropriate subcommand.
     *
     * @param parsedUserInput The parsed command input from the user.
     */
    private static void findAndDispatchCommand(CommandParser.ParsedCommand parsedUserInput) {
        CommandInstance targetCommand = CommandRegistrar.getCommandByName(parsedUserInput.command);

        if (!targetCommand.isHasSubCommands()) {
            targetCommand.execute(parsedUserInput);
            return;
        }

        findAndDispatchCommand(parsedUserInput, targetCommand);
    }


    /**
     * Finds the command by its name and dispatches it for execution.
     * If the command has subcommands, it will recursively find the appropriate subcommand.
     *
     * @param parsedUserInput The parsed command input from the user.
     * @param fromCommand     The command instance from which to start searching for subcommands.
     */
    private static void findAndDispatchCommand(
            CommandParser.ParsedCommand parsedUserInput,
            CommandInstance fromCommand
    ) {
        if (parsedUserInput.positionalArgs.length == 0) {
            throw new CommandError(
                    fromCommand.getFullPath(),
                    "Command: " + fromCommand.getName() + " requires a sub-command."
            );
        }

        parsedUserInput.command = parsedUserInput.positionalArgs[0];
        parsedUserInput.positionalArgs = Arrays.copyOfRange(
                parsedUserInput.positionalArgs,
                1,
                parsedUserInput.positionalArgs.length
        );

        CommandInstance subCommand = fromCommand.getSubCommandByName(parsedUserInput.command);

        if (!subCommand.isHasSubCommands()) {
            subCommand.execute(parsedUserInput);
            return;
        }

        // If the sub-command has further sub-commands, we need to continue searching recursively.
        findAndDispatchCommand(parsedUserInput, subCommand);
    }

    /**
     * Starts the REPL loop, which continuously prompts the user for input,
     * parses the input into commands, and executes the corresponding command.
     * All the exceptions are caught and handled here, regardless of where they occur in the command execution flow.
     */
    public static void start() {
        System.out.println(new Chalk("Starting the REPL...").bold().green());
        OutputUtils.printHeader();

        // noinspection InfiniteLoopStatement - This is a REPL loop and the user is expected to exit manually.
        while (true) {
            try {
                CommandParser.ParsedCommand parsedUserInput = promptAndParseCommand();

                // If an error occurs during parsing, it will be handled in the CommandParser and a null will be returned.
                // Since the error message is already printed, we can safely skip further processing.
                if (parsedUserInput == null) {
                    continue;
                }

                findAndDispatchCommand(parsedUserInput);
            } catch (CommandError e) {
                OutputUtils.printError(e.getMessage(), e.getCommand());
            } catch (Throwable e) {
                OutputUtils.printError(e.getMessage());
            }
        }
    }
}
