package cli;

import cli.arguments.ArgumentDataType;
import cli.arguments.ArgumentList;
import cli.arguments.KeywordArgument;
import terminal.OutputUtils;
import utils.SimpleMap;

import java.util.Arrays;

/**
 * CommandParser is responsible for parsing raw command-line arguments into structured command and argument objects.
 * It also contains method that validates the arguments against allowed definitions and generates a map of argument names to values.
 */
public class CommandParser {
    /**
     * Parses the raw command-line arguments into a ParsedCommand object.
     * Handles extraction of the command, positional arguments, and keyword arguments.
     *
     * @param args The raw command-line arguments.
     * @return ParsedCommand object containing the command, positional arguments, and keyword arguments.
     */
    public static ParsedCommand parseFromRaw(String[] args) {
        ParsedCommand parsedCommandAndArgs = new ParsedCommand();

        // Handle the case where no arguments are provided
        if (args.length == 0) {
            OutputUtils.error("Command is required. Example usage: 'co2cli.jar <command> <positional_arguments> <keyword_arguments>'");
            System.exit(1);
        }

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
                System.exit(1);
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
        SimpleMap<String, String> argsMap = new SimpleMap<>();

        // Check if the number of positional arguments matches the expected count
        if (allowedArguments.positionalArguments.length != parsedCommand.positionalArgs.length) {
            OutputUtils.error(
                    "Invalid number of positional arguments. Expected: " + allowedArguments.positionalArguments.length +
                            ", Found: " + parsedCommand.positionalArgs.length
            );
            System.exit(1);
        }

        // Validate and map positional arguments
        for (int i = 0; i < allowedArguments.positionalArguments.length; i++) {
            String argName = allowedArguments.positionalArguments[i].name;
            String argValue = parsedCommand.positionalArgs[i];

            // Check if the argument value matches the expected data type
            if (allowedArguments.positionalArguments[i].dataType.isInvalid(argValue)) {
                OutputUtils.error(
                        "Positional argument: \"" + argName + "\" has invalid type. Argument type should be: " + allowedArguments.positionalArguments[i].dataType.getType()
                );
                System.exit(1);
            }

            argsMap.put(argName, argValue);
        }

        // Check if there is any invalid keyword argument
        for (SimpleMap.Entry<String, String> arg : parsedCommand.keywordArgs.entries()) {
            boolean isValid = false;
            String argKey = arg.getKey();

            // Check if the argument is in the allowed arguments
            for (KeywordArgument allowedArg : allowedArguments.keywordArguments) {
                if (argKey.equals(allowedArg.name) || argKey.equals(allowedArg.abbreviation)) {
                    isValid = true;
                    break;
                }
            }

            // If not valid, print error and exit
            if (!isValid) {
                OutputUtils.error("Invalid keyword argument: " + argKey);
                System.exit(1);
            }
        }

        // Validate and map keyword arguments
        for (KeywordArgument requiredArgument : allowedArguments.keywordArguments) {
            SimpleMap.Entry<String, String> targetArg = null;

            // Search for the argument by name or abbreviation
            for (SimpleMap.Entry<String, String> arg : parsedCommand.keywordArgs.entries()) {
                String argName = arg.getKey();

                boolean commandFound = argName.length() > 1 ?
                        argName.equals(requiredArgument.name) :
                        argName.equals(requiredArgument.abbreviation);

                if (commandFound) {
                    targetArg = arg;
                    break;
                }
            }

            // If required argument is missing, print error and exit
            if (targetArg == null) {
                if (!requiredArgument.required) {
                    continue;
                }

                OutputUtils.error(
                        "Argument not found: --" + requiredArgument.name + " or -" + requiredArgument.abbreviation
                );
                System.exit(1);
            }

            // Validate the argument value type
            if (requiredArgument.dataType.isInvalid(targetArg.getValue())) {
                OutputUtils.error(
                        "Argument: \"" + targetArg.getKey() + "\" has invalid type. " + (
                                requiredArgument.dataType == ArgumentDataType.FLAG ?
                                        "It is a flag and hence should not have any value" :
                                        "Argument type should be: " + requiredArgument.dataType.getType()
                        ));
                System.exit(1);
            }

            argsMap.put(requiredArgument.name, targetArg.getValue());
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
