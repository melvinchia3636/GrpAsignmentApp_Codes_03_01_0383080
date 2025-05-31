package core.cli.commands;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.KeywordArgument;
import core.cli.arguments.PositionalArgument;
import core.terminal.OutputUtils;
import core.utils.SimpleMap;

import java.util.Arrays;
import java.util.Scanner;

/**
 * CommandParser is responsible for parsing raw command-line arguments into structured command and argument objects.
 * It also contains method that validates the arguments against allowed definitions and generates a map of argument names to values.
 */
public class CommandParser {
    /**
     * Prompts the user for positional arguments if they are not provided in the command line.
     * It validates the input against the expected data type and stores it in the argsMap.
     *
     * @param allowedPositionalArgs The array of allowed positional arguments with their names and data types.
     * @param argsMap               The map to store the validated positional arguments.
     */
    private static void promptPositionalArguments(PositionalArgument[] allowedPositionalArgs, SimpleMap<String, String> argsMap) {
        Scanner scanner = new Scanner(System.in);
        for (PositionalArgument positionalArgs : allowedPositionalArgs) {
            while (true) {
                System.out.print("Enter " + positionalArgs.getName() + ": ");
                String input = scanner.nextLine().trim();

                // Validate the input against the expected data type
                if (positionalArgs.getDataType().isInvalid(input)) {
                    OutputUtils.error(
                            "Positional argument: \"" + positionalArgs.getName() + "\" has invalid type. Argument type should be: " + positionalArgs.getDataType().getType()
                    );
                    continue;
                }

                argsMap.put(positionalArgs.getName(), input);
                break; // Exit loop if valid input is provided
            }
        }
    }

    /**
     * Parses the raw command-line arguments into a ParsedCommand object.
     * Handles extraction of the command, positional arguments, and keyword arguments.
     *
     * @param input The raw command string.
     * @return ParsedCommand object containing the command, positional arguments, and keyword arguments.
     */
    public static ParsedCommand parseFromRaw(String input) {
        // Split the input by whitespace
        // and trim each argument to remove leading/trailing spaces
        String[] args = Arrays.stream(input.trim().split("\\s+")).filter(
                arg -> !arg.isEmpty()
        ).toArray(String[]::new);

        if (args.length == 0) {
            return null;
        }

        ParsedCommand parsedCommandAndArgs = new ParsedCommand();

        // The first argument is always the command
        parsedCommandAndArgs.command = args[0];

        // Find the index of the first positional argument (first argument starting with '-')
        int indexOfFirstPositionalArg = 0;
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                indexOfFirstPositionalArg = i;
                break;
            }
        }

        // If no positional arguments, set index to end of args
        if (indexOfFirstPositionalArg == 0) {
            indexOfFirstPositionalArg = args.length;
        }

        // Extract positional arguments
        parsedCommandAndArgs.positionalArgs = Arrays.copyOfRange(args, 1, indexOfFirstPositionalArg);

        // Prepare keyword arguments array
        args = Arrays.copyOfRange(args, indexOfFirstPositionalArg, args.length);

        // 2D array to hold keyword arguments (name, value)
        SimpleMap<String, String> keywordArguments = new SimpleMap<>();

        // Parse keyword arguments
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                String argName = args[i];
                String argValue = null;

                // If next argument is not a flag, treat as value
                if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                    argValue = args[i + 1];
                    i++;
                }

                // Remove leading dashes from argument name
                if (argName.startsWith("--")) {
                    argName = argName.substring(2);
                } else {
                    argName = argName.substring(1);
                }

                keywordArguments.put(argName, argValue);
            } else {
                OutputUtils.error("Invalid argument: " + args[i]);
                return null;
            }
        }

        parsedCommandAndArgs.keywordArgs = keywordArguments;

        return parsedCommandAndArgs;
    }

    /**
     * Validates the parsed command arguments against the allowed argument definitions and generates a map of argument names to values.
     *
     * @param parsedCommand    The parsed command containing the command name, positional and keyword arguments.
     * @param allowedArguments The allowed argument definitions (positional and keyword).
     * @return SimpleMap mapping argument names to their values.
     */
    public static SimpleMap<String, String> validateAndGenerateArgsMap(ParsedCommand parsedCommand, ArgumentList allowedArguments) {
        // If neither positional nor keyword arguments are needed, return null
        if (allowedArguments == null) {
            allowedArguments = new ArgumentList(new PositionalArgument[0], new KeywordArgument[0]);
        }

        PositionalArgument[] positionalArgs = allowedArguments.getPositionalArguments();
        KeywordArgument[] keywordArgs = allowedArguments.getKeywordArguments();

        SimpleMap<String, String> argsMap = new SimpleMap<>();
        boolean positionalArgsNeedPrompting = false;

        // Only parse positional arguments if there is any
        if (parsedCommand.positionalArgs.length != 0) {
            // Check if the number of positional arguments matches the expected count
            if (positionalArgs.length != parsedCommand.positionalArgs.length) {
                OutputUtils.error(
                        "Invalid number of positional arguments. Expected: " + positionalArgs.length +
                                ", Found: " + parsedCommand.positionalArgs.length
                );

                return null;
            }

            // Validate and map positional arguments
            for (int i = 0; i < positionalArgs.length; i++) {
                String argName = positionalArgs[i].getName();
                String argValue = parsedCommand.positionalArgs[i];

                // Check if the argument value matches the expected data type
                ArgumentDataType targetDataType = positionalArgs[i].getDataType();
                if (targetDataType.isInvalid(argValue)) {
                    OutputUtils.error(
                            "Positional argument: \"" + argName + "\" has invalid type. Argument type should be: " + targetDataType.getType()
                    );
                    return null;
                }

                argsMap.put(argName, argValue);
            }
        } else {
            // Flag to indicate that positional arguments need prompting
            // Will do it later if keyword arguments are valid
            positionalArgsNeedPrompting = true;
        }

        // Check if there is any invalid keyword argument
        for (SimpleMap.Entry<String, String> arg : parsedCommand.keywordArgs.entries()) {
            boolean isValid = false;
            String argKey = arg.getKey();

            // Check if the argument is in the allowed arguments
            for (KeywordArgument allowedArg : keywordArgs) {
                if (argKey.equals(allowedArg.getName()) || argKey.equals(allowedArg.getAbbreviation())) {
                    isValid = true;
                    break;
                }
            }

            // If not valid, print error and exit
            if (!isValid) {
                OutputUtils.error("Invalid keyword argument: " + argKey);
                return null;
            }
        }

        // Validate and map keyword arguments
        for (KeywordArgument requiredArgument : keywordArgs) {
            SimpleMap.Entry<String, String> targetArg = null;

            String targetName = requiredArgument.getName();
            String targetAbbreviation = requiredArgument.getAbbreviation();
            ArgumentDataType targetDataType = requiredArgument.getDataType();

            // Search for the argument by name or abbreviation
            for (SimpleMap.Entry<String, String> arg : parsedCommand.keywordArgs.entries()) {
                String argName = arg.getKey();

                // Check if the argument matches either the full name or the abbreviation
                // If the argument has more than one character, we check the full name, otherwise the abbreviation
                boolean commandFound = argName.length() > 1 ?
                        argName.equals(targetName) :
                        argName.equals(targetAbbreviation);

                if (commandFound) {
                    targetArg = arg;
                    break;
                }
            }

            // If required argument is missing, print error and exit
            if (targetArg == null) {
                if (!requiredArgument.isRequired()) {
                    continue;
                }

                OutputUtils.error(
                        "Argument not found: --" + targetName + " or -" + targetAbbreviation
                );
                return null;
            }

            // Validate the argument value type
            if (targetDataType.isInvalid(targetArg.getValue())) {
                OutputUtils.error(
                        "Argument: \"" + targetArg.getKey() + "\" has invalid type. " + (
                                targetDataType == ArgumentDataType.FLAG ?
                                        "It is a flag and hence should not have any value" :
                                        "Argument type should be: " + targetDataType.getType()
                        ));
                return null;
            }

            argsMap.put(targetName, targetArg.getValue());
        }

        // This is the last thing we do, since if there is something wrong with the keyword arguments,
        // we should exit the process and should not prompt the user for positional arguments.
        if (positionalArgsNeedPrompting) {
            promptPositionalArguments(positionalArgs, argsMap);
        }

        return argsMap;
    }

    /**
     * ParsedCommand represents the result of parsing a raw command-line input.
     * It contains the command name, positional arguments, and keyword arguments.
     */
    public static class ParsedCommand {
        /**
         * The command name (e.g., 'login', 'signup', etc.).
         */
        public String command;

        /**
         * Array of positional argument values, in the order they appear.
         */
        public String[] positionalArgs;

        /**
         * 2D array of keyword arguments, where each element is a pair: [argument name, argument value].
         * For example: { {"output", "file.txt"}, {"v", null} }
         * The first element is the argument name (without dashes), the second is its value (or null if flag).
         */
        public SimpleMap<String, String> keywordArgs;
    }
}
