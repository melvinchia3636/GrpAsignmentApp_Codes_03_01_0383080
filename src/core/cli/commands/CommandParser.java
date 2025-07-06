package core.cli.commands;

import core.cli.arguments.*;
import core.terminal.OutputUtils;
import core.instances.ListOfPairs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * CommandParser is responsible for parsing raw command-line arguments into structured command and argument objects.
 * It also contains method that validates the arguments against allowed definitions and generates a map of argument names to values.
 */
public class CommandParser {
    /**
     * Parses the raw command-line arguments into a ParsedCommand object.
     * Handles extraction of the command, positional arguments, and keyword arguments.
     *
     * @param input The raw command string.
     * @return ParsedCommand object containing the command, positional arguments, and keyword arguments.
     */
    public static ParsedCommand parseFromRaw(String input) {
        ParsedCommand parsed;

        String[] args = tokenize(input);

        if (args.length == 0) {
            return null;
        }

        parsed = new ParsedCommand();
        parsed.command = args[0];

        int indexOfFirstPositionalArg = getIndexOfFirstPositionalArg(args);

        parsed.positionalArgs = Arrays.copyOfRange(args, 1, indexOfFirstPositionalArg);
        args = Arrays.copyOfRange(args, indexOfFirstPositionalArg, args.length);
        parsed.keywordArgs = parseKeywordArgs(args, parsed.command);

        return parsed;
    }

    /**
     * Validates the parsed command arguments against the allowed argument definitions and generates a map of argument names to values.
     *
     * @param parsedCommand    The parsed command containing the command name, positional and keyword arguments.
     * @param allowedArguments The allowed argument definitions (positional and keyword).
     * @return SimpleMap mapping argument names to their values.
     */
    public static ListOfPairs<String, String> validateAndGenerateArgsMap(
            ParsedCommand parsedCommand,
            ArgumentList allowedArguments,
            String commandPath
    ) {
        if (allowedArguments == null) {
            allowedArguments = new ArgumentList();
        }

        PositionalArgument[] positionalArgs = allowedArguments.getPositionalArguments();
        KeywordArgument[] keywordArgs = allowedArguments.getKeywordArguments();

        ListOfPairs<String, String> argsMap = new ListOfPairs<>();
        boolean positionalArgsNeedPrompting = false;

        if (parsedCommand.positionalArgs.length != 0) {
            validateAndMapPositionalArgs(parsedCommand, positionalArgs, argsMap, commandPath);
        } else {
            // Flag to indicate that positional arguments need prompting
            // Will do it later if keyword arguments are valid
            positionalArgsNeedPrompting = true;
        }

        checkInvalidKeywordArgs(parsedCommand, keywordArgs, commandPath);
        validateAndMapKeywordArgs(parsedCommand, keywordArgs, argsMap, commandPath);

        // This is the last thing we do, since if there is something wrong with the keyword arguments,
        // we should exit the process and should not prompt the user for positional arguments.
        if (positionalArgsNeedPrompting) {
            promptPositionalArgs(positionalArgs, argsMap);
        }

        return argsMap;
    }

    /**
     * Tokenizes the input string into an array of arguments, handling quoted strings and escaped characters.
     * It supports both single and double quotes for grouping arguments and allows escaping quotes with a backslash.
     *
     * @param input The raw command string to tokenize.
     * @return An array of tokens (arguments) extracted from the input string.
     * @throws IllegalArgumentException if there is an unclosed quote in the input string.
     */
    private static String[] tokenize(String input) {
        ArrayList<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        Character quoteChar = null;
        boolean escaped = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            // If the character is escaped, we just append it to the current token
            // Since each backslash only escapes the next character,
            // we can reset the escaped flag after processing it
            if (escaped) {
                current.append(c);
                escaped = false;
                continue;
            }

            // If the character is a backslash, we set the escaped flag and continue to the next character
            if (c == '\\') {
                escaped = true;
                continue;
            }

            if ((c == '"' || c == '\'') && quoteChar == null) { // Enter quote mode if we're not already in it
                quoteChar = c;
                continue;
            } else if (quoteChar != null && c == quoteChar) { // Exit quote mode if we encounter the same quote character
                quoteChar = null;
                continue;
            }

            // If the character is a whitespace and no quote is currently open, we treat it as a token separator
            // Thus, we add the current token to the list and reset it
            if (Character.isWhitespace(c) && quoteChar == null) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                continue;
            }

            current.append(c);
        }

        // After processing all characters, we check if there is an unclosed quote, and if so, throw an exception
        if (quoteChar != null) {
            throw new IllegalArgumentException("Unclosed quote: " + quoteChar);
        }

        // If there is any remaining text in the current token, we add it to the list
        if (current.length() > 0) {
            tokens.add(current.toString());
        }

        // Convert the list of tokens to an array and return it
        return tokens.toArray(new String[0]);
    }

    /**
     * Parses the positional arguments from the command-line arguments.
     * It identifies the first positional argument and returns all arguments before it.
     *
     * @param args The array of command-line arguments.
     * @return An array of positional arguments.
     */
    private static int getIndexOfFirstPositionalArg(String[] args) {
        int indexOfFirstPositionalArg = 0;
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                indexOfFirstPositionalArg = i;
                break;
            }
        }

        if (indexOfFirstPositionalArg == 0) {
            indexOfFirstPositionalArg = args.length;
        }

        return indexOfFirstPositionalArg;
    }

    /**
     * Parses the keyword arguments from the command-line arguments.
     * It identifies arguments that start with '-' or '--' and maps them to their values.
     *
     * @param args The array of command-line arguments.
     * @return A SimpleMap containing keyword argument names and their corresponding values.
     * @throws IllegalArgumentException if an argument does not start with '-' or '--'.
     */
    private static ListOfPairs<String, String> parseKeywordArgs(String[] args, String commandPath) {
        ListOfPairs<String, String> keywordArguments = new ListOfPairs<>();

        for (int i = 0; i < args.length; i++) {
            if (!args[i].startsWith("-")) {
                throw new CommandError(
                        commandPath,
                        "Invalid argument format: " + args[i] + ". " +
                        "Keyword arguments should start with '-' or '--'."
                );
            }

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
                if (argName.length() > 1) {
                    throw new CommandError(
                            commandPath,
                            "Invalid argument format: -" + argName + ". " +
                                    "Prefix '-' should only be used for single-character arguments. " +
                                    "Do you mean to use '--" + argName + "'?"
                    );
                }
            }

            keywordArguments.put(argName, argValue);
        }

        return keywordArguments;
    }

    /**
     * Prompts the user for positional arguments if they are not provided in the command line.
     * It validates the input against the expected data type and stores it in the argsMap.
     *
     * @param allowedPositionalArgs The array of allowed positional arguments with their names and data types.
     * @param argsMap               The map to store the validated positional arguments.
     */
    private static void promptPositionalArgs(
            PositionalArgument[] allowedPositionalArgs,
            ListOfPairs<String, String> argsMap
    ) {
        Scanner scanner = new Scanner(System.in);
        for (PositionalArgument positionalArgs : allowedPositionalArgs) {
            while (true) {
                System.out.print(positionalArgs.getPromptText() + " ");
                String input = scanner.nextLine().trim();
                ArgumentDataType dataType = positionalArgs.getDataType();

                if (dataType.isInvalid(input)) {
                    printArgsError(positionalArgs, true);
                    continue;
                }

                argsMap.put(positionalArgs.getName(), input);
                break;
            }
        }
    }

    /**
     * Validates and maps positional arguments from the parsed command to the argsMap.
     * Checks if the number of provided positional arguments matches the expected count,
     * validates each argument against its expected data type, and stores them in the argsMap.
     *
     * @param parsedCommand  The parsed command containing positional argument values.
     * @param positionalArgs The array of allowed positional arguments with their names and data types.
     * @param argsMap        The map to store the validated positional arguments.
     * @throws IllegalArgumentException if the number of arguments or their types are invalid.
     */
    private static void validateAndMapPositionalArgs(
            ParsedCommand parsedCommand,
            PositionalArgument[] positionalArgs,
            ListOfPairs<String, String> argsMap,
            String commandPath
    ) {
        // Check if the number of positional arguments matches the expected count
        if (positionalArgs.length != parsedCommand.positionalArgs.length) {
            String expectedArgs = positionalArgs.length > 0 ?
                    String.join(", ",
                            Arrays.stream(positionalArgs)
                                    .map(arg -> String.format(
                                            "%s (%s)",
                                            arg.getName(),
                                            arg.getDataType().getType()
                                    ))
                                    .toArray(String[]::new)
                    ) : "none";

            throw new CommandError(
                    commandPath,
                    String.format(
                            "Expected %d positional arguments, but got %d. Expected arguments: %s",
                            positionalArgs.length,
                            parsedCommand.positionalArgs.length,
                            expectedArgs
                    )
            );
        }

        // Validate and map positional arguments
        for (int i = 0; i < positionalArgs.length; i++) {
            String argName = positionalArgs[i].getName();
            String argValue = parsedCommand.positionalArgs[i];

            // Check if the argument value matches the expected data type
            ArgumentDataType targetDataType = positionalArgs[i].getDataType();
            if (targetDataType.isInvalid(argValue)) {
               printArgsError(positionalArgs[i]);
            }

            argsMap.put(argName, argValue);
        }
    }

    /**
     * Checks if the keyword arguments in the parsed command are valid against the allowed keyword arguments.
     * If any invalid keyword argument is found, it throws an IllegalArgumentException.
     *
     * @param parsedCommand The parsed command containing keyword arguments.
     * @param keywordArgs   The array of allowed keyword arguments.
     * @throws IllegalArgumentException if an invalid keyword argument is found.
     */
    private static void checkInvalidKeywordArgs(
            ParsedCommand parsedCommand,
            KeywordArgument[] keywordArgs,
            String commandPath
    ) {
        for (ListOfPairs.Entry<String, String> arg : parsedCommand.keywordArgs.entries()) {
            boolean isValid = false;
            String argName = arg.getKey();

            // Check if the argument is in the allowed arguments
            for (KeywordArgument allowedArg : keywordArgs) {
                if (argName.equals(allowedArg.getName()) || argName.equals(allowedArg.getAbbreviation())) {
                    isValid = true;
                    break;
                }
            }

            // If not valid, print error and exit
            if (!isValid) {
                throw new CommandError(commandPath, "Invalid keyword argument: " + argName);
            }
        }
    }

    /**
     * Validates and maps keyword arguments from the parsed command to the argsMap.
     * Checks if each required keyword argument is present, validates its value type, and stores it in the argsMap.
     *
     * @param parsedCommand The parsed command containing keyword arguments.
     * @param keywordArgs   The array of allowed keyword arguments with their names, abbreviations, and data types.
     * @param argsMap       The map to store the validated keyword arguments.
     * @throws IllegalArgumentException if a required argument is missing or has an invalid type.
     */
    private static void validateAndMapKeywordArgs(
            ParsedCommand parsedCommand,
            KeywordArgument[] keywordArgs,
            ListOfPairs<String, String> argsMap,
            String commandPath
    ) {
        for (KeywordArgument requiredArgument : keywordArgs) {
            ListOfPairs.Entry<String, String> targetArg = null;

            String targetName = requiredArgument.getName();
            String targetAbbreviation = requiredArgument.getAbbreviation();
            ArgumentDataType targetDataType = requiredArgument.getDataType();

            for (ListOfPairs.Entry<String, String> arg : parsedCommand.keywordArgs.entries()) {
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

            if (targetArg == null) {
                if (!requiredArgument.isRequired()) {
                    continue;
                }

                throw new CommandError(
                        commandPath,
                        "Argument not found: --" + targetName + " or -" + targetAbbreviation
                );
            }

            if (targetDataType.isInvalid(targetArg.getValue())) {
                printArgsError(requiredArgument);
            }

            argsMap.put(targetName, targetArg.getValue());
        }
    }

    /**
     * Prints an error message for a positional argument that has an invalid type.
     * It checks if the argument is of type "enum" and prints the valid options, or prints the expected type otherwise.
     *
     * @param args The argument that has an invalid type.
     */
    private static void printArgsError(Argument args, boolean noError) {
        ArgumentDataType dataType = args.getDataType();
        String argumentType = args instanceof PositionalArgument ?
                "Positional" : "Keyword";

        if (dataType.getType().equals("enum")) {
            String options = String.join(", ", dataType.getOptions());

            OutputUtils.printError(
                    argumentType + " argument: \"" +
                            args.getName() +
                            "\" has invalid type. Argument type should be one of: " +
                            options,
                    false
            );

            if (!noError) throw new Error();
        }

        OutputUtils.printError(
                argumentType + " argument: \"" +
                        args.getName() +
                        "\" has invalid type. Argument type should be: " +
                        dataType.getType(),
                false
        );

        if (!noError) throw new Error();
    }

    private static void printArgsError(Argument args) {
        printArgsError(args, false);
    }

    /**
     * ParsedCommand represents the result of parsing a raw command-line input.
     * It contains the command name, a 1D array of positional arguments, and a Map object containing keyword arguments.
     */
    public static class ParsedCommand {
        public String command;
        public String[] positionalArgs;
        public ListOfPairs<String, String> keywordArgs;

        @Override
        public String toString() {
            return "ParsedCommand{" +
                    "command='" + command + '\'' +
                    ", positionalArgs=" + Arrays.toString(positionalArgs) +
                    ", keywordArgs=" + keywordArgs +
                    '}';
        }
    }
}
