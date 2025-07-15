package core.cli;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.KeywordArgument;
import core.cli.commands.CommandError;
import core.cli.commands.CommandInstance;
import core.cli.commands.CommandParser;
import core.cli.commands.CommandRegistrar;
import core.instances.ListOfKVs;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

/**
 * The REPL (Read-Eval-Print Loop) class is responsible for starting the command-line interface
 * and processing user commands in a loop.
 * It reads user input, parses it, and executes the corresponding command.
 */
public class REPL {
    private static final ArgumentList ENTRYPOINT_ALLOWED_ARGUMENTS = new ArgumentList(
            new KeywordArgument(
                    "dataset-path",
                    "p",
                    "Path to the global carbon emissions dataset file.",
                    ArgumentDataType.STRING,
                    true
            )
    );

    /**
     * Starts the REPL loop, which continuously prompts the user for input,
     * parses the input into commands, and executes the corresponding command.
     * All the exceptions are caught and handled here, regardless of where they occur in the command execution flow.
     *
     * @param args The command-line arguments passed to the application.
     */
    public static void start(String[] args) {
        initializeGlobalManager(args);
        System.out.println(new Chalk("Starting the REPL...").bold().green());
        OutputUtils.printHeader();

        // noinspection InfiniteLoopStatement - This is a REPL loop and the user is expected to exit manually.
        while (true) {
            // noinspection CatchMayIgnoreException
            try {
                CommandParser.ParsedCommand parsedUserInput = promptAndParseCommand();
                findAndDispatchCommand(parsedUserInput);
            } catch (CommandError e) {
                OutputUtils.printError(e.getMessage(), e.getCommand());
            } catch (Throwable e) {
                if (e.getMessage() == null || e.getMessage().isEmpty()) {
                    continue;
                }

                OutputUtils.printError(e.getMessage());
            }
        }
    }

    /**
     * Initializes the GlobalManager instance with the dataset path provided in the command-line arguments.
     * If the dataset path is invalid, it prints an error message and exits the application.
     *
     * @param args The command-line arguments passed to the application.
     */
    private static void initializeGlobalManager(String[] args) {
        CommandParser.ParsedCommand parsedCommand = CommandParser.parseFromRaw(args, false);
        ListOfKVs<String, String> argsMap = CommandParser.validateAndGenerateArgsMap(
                parsedCommand,
                ENTRYPOINT_ALLOWED_ARGUMENTS,
                "main"
        );

        String datasetPath = argsMap.get("dataset-path");
        try {
            validateDatasetPath(datasetPath);
        } catch (IllegalArgumentException e) {
            OutputUtils.printError(e.getMessage(), false);
            System.exit(1);
        }

        GlobalManager.createInstance(datasetPath);
    }

    /**
     * Validates the provided dataset path to ensure it points to a valid CSV file.
     * Throws an IllegalArgumentException if the path is invalid or does not point to a CSV file.
     *
     * @param datasetPath The path to the dataset file.
     * @throws IllegalArgumentException if the path is invalid or not a CSV file.
     */
    private static void validateDatasetPath(String datasetPath) {
        Path path = Paths.get(datasetPath).toAbsolutePath();

        if (!path.toFile().exists() || !path.toFile().isFile()) {
            throw new IllegalArgumentException("The provided dataset path does not point to a valid file: " + datasetPath);
        }

        if (!datasetPath.endsWith(".csv")) {
            throw new IllegalArgumentException("The provided dataset path must point to a CSV file: " + datasetPath);
        }
    }

    /**
     * Prompts the user for input, reads the command line, and parses it into a command object.
     * If the input is invalid, it will print an error message and return null.
     *
     * @return A ParsedCommand object containing the command and its arguments, or null if parsing fails.
     */
    private static CommandParser.ParsedCommand promptAndParseCommand() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(new Chalk("\n> ").bold().green());
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
        CommandInstance targetCommand = CommandRegistrar.getCommandByName(parsedUserInput.getCommand());

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
        if (parsedUserInput.getPositionalArgs().length == 0) {
            throw new CommandError(
                    fromCommand.getFullPath(),
                    "Command: " + fromCommand.getName() + " requires a sub-command."
            );
        }

        parsedUserInput.setCommand(parsedUserInput.getPositionalArgs()[0]);
        parsedUserInput.setPositionalArgs(Arrays.copyOfRange(
                parsedUserInput.getPositionalArgs(),
                1,
                parsedUserInput.getPositionalArgs().length
        ));

        CommandInstance subCommand = fromCommand.getSubCommandByName(parsedUserInput.getCommand());

        if (!subCommand.isHasSubCommands()) {
            subCommand.execute(parsedUserInput);
            return;
        }

        // If the sub-command has further sub-commands, we need to continue searching recursively.
        findAndDispatchCommand(parsedUserInput, subCommand);
    }
}
