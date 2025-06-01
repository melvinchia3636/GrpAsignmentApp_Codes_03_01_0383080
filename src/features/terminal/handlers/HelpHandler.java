package features.terminal.handlers;


import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.KeywordArgument;
import core.cli.arguments.PositionalArgument;
import core.cli.commands.CommandInstance;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.CommandRegistrar;

public class HelpHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        String targetCommandName = argsMap.get("command");

        if (targetCommandName == null) {
            printGlobalHelp();
            return;
        }

        CommandInstance commandInstance = CommandRegistrar.getCommandByName(targetCommandName);

        if (commandInstance == null) {
            OutputUtils.printError("Command not found: " + targetCommandName);
        } else {
            printCommandHelp(commandInstance);
        }

    }

    /**
     * Will be called when the user types 'help' without specifying a command.
     * Prints the global help message with usage instructions, examples, and a list of available commands.
     */
    private static void printGlobalHelp() {
        String usageMsg = new Chalk("Usage:").yellow().bold() + "\n" +
                "    " + new Chalk("<command>").bold().green() + " [positional_arguments] [--keyword-arguments]\n\n";

        String noteMsg = new Chalk("Note:").yellow().bold() + "\n" +
                "    Type " + new Chalk("'help -c <command>'").bold().green() + " to get detailed help for a specific command.\n" +
                "    If no positional arguments are provided, you will be prompted to enter them interactively.\n\n";

        String examplesMsg = new Chalk("Examples:").yellow().bold() + "\n" +
                "    " + new Chalk("login").bold().green() + " johndoe 12345678 --save-session\n" +
                "    " + new Chalk("help").bold().green() + " -c login\n\n";

        StringBuilder commandsMsg = new StringBuilder(
                new Chalk("Available Commands:").yellow().bold() + "\n"
        );

        int maxCommandLength = CommandRegistrar.getMaxCommandLength();

        // Dynamically append command names and descriptions into the help message
        for (CommandInstance command : CommandRegistrar.commandInstances) {
            String commandName = command.name;
            String commandDescription = command.description;

            String firstSentenceOfDescription = commandDescription.contains(".")
                    ? commandDescription.substring(0, commandDescription.indexOf(".") + 1)
                    : commandDescription;

            commandsMsg.append("    ")
                    .append(new Chalk(commandName).bold().green())
                    .append(String.format("%" + (maxCommandLength - commandName.length() + 4) + "s", ""))
                    .append(firstSentenceOfDescription)
                    .append("\n");
        }

        System.out.println(
                usageMsg + noteMsg + examplesMsg + commandsMsg
        );
    }

    /**
     * Will be called when the user types 'help -c <command>'.
     * Prints detailed help for a specific command, including its usage, arguments, and examples.
     *
     * @param commandInstance The CommandInstance object representing the command to display help for.
     */
    private static void printCommandHelp(CommandInstance commandInstance) {
        String description = commandInstance.description;

        int fullStopIndex = description.indexOf(".");
        fullStopIndex = fullStopIndex == -1 ? description.length() : fullStopIndex + 1;

        String firstSentence = description.substring(0, fullStopIndex);
        String remainingDescription = description.substring(fullStopIndex).trim();

        String usageMsg = new Chalk("Usage:").yellow().bold() + "\n" +
                "    " + new Chalk(commandInstance.name).bold().green() + " [positional_arguments] [--keyword-arguments]\n\n";

        String descriptionMsg = new Chalk("Description:").yellow().bold() + "\n" +
                formatStringWithIndentation(firstSentence) + "\n\n" +
                (remainingDescription.isEmpty() ? "" : formatStringWithIndentation(remainingDescription) + "\n\n");

        String exampleMsg = new Chalk("Example:").yellow().bold() + "\n" +
                "    " + new Chalk(commandInstance.name).bold().green() + " " + commandInstance.example + "\n\n";

        StringBuilder argsMsg = new StringBuilder();
        appendPositionalArgumentsToArgsMsg(argsMsg, commandInstance);
        appendKeywordArgumentsToArgsMsg(argsMsg, commandInstance);

        System.out.println(
                usageMsg + exampleMsg + descriptionMsg + argsMsg
        );
    }

    private static void appendPositionalArgumentsToArgsMsg(
            StringBuilder argsMsg,
            CommandInstance commandInstance
    ) {
        PositionalArgument[] positionalArgs = commandInstance.args.positionalArguments;

        if (positionalArgs.length == 0) {
            return;
        }

        int maxTypeStringLength = ArgumentDataType.getMaxTypeStringLength();
        int maxPositionalArgNameLength = commandInstance.args.getMaxPositionalArgumentNameLength();

        argsMsg.append(new Chalk("Positional Arguments:").yellow().bold()).append("\n");

        for (PositionalArgument arg : positionalArgs) {
            String argName = arg.name;
            String argDescription = arg.description;

            argsMsg.append("    ")
                    .append(new Chalk(argName).bold().blue())
                    .append(String.format("%" + (maxPositionalArgNameLength - argName.length() + 4) + "s", ""))
                    .append(new Chalk(String.format("[%s]", arg.dataType.type)).bold().purple())
                    .append(String.format("%" + (maxTypeStringLength - arg.dataType.type.length() + 4) + "s", ""))
                    .append(argDescription)
                    .append("\n");

        }
        argsMsg.append("\n");
    }

    private static void appendKeywordArgumentsToArgsMsg(
            StringBuilder argsMsg,
            CommandInstance commandInstance
    ) {
        KeywordArgument[] keywordArgs = commandInstance.args.keywordArguments;

        if (keywordArgs.length == 0) {
            return;
        }

        int maxTypeStringLength = ArgumentDataType.getMaxTypeStringLength();
        int maxKeywordArgNameLength = commandInstance.args.getMaxKeywordArgumentNameLength();

        argsMsg.append(new Chalk("Keyword Arguments:").yellow().bold()).append("\n");

        for (KeywordArgument arg : keywordArgs) {
            String argName = arg.name;
            String argAbbreviation = arg.abbreviation;
            String argDescription = arg.description;

            argsMsg.append("    ")
                    .append(new Chalk(String.format("-%s, --%s", argAbbreviation, argName)).bold().blue())
                    .append(String.format("%" + (maxKeywordArgNameLength - argName.length() + 4) + "s", ""))
                    .append(new Chalk(String.format("[%s]", arg.dataType.type)).bold().purple())
                    .append(String.format("%" + (maxTypeStringLength - arg.dataType.type.length() + 4) + "s", ""))
                    .append(arg.required ? new Chalk("(required)").bold().red() : new Chalk("(optional)").bold().green())
                    .append("    ")
                    .append(argDescription)
                    .append("\n");
        }
    }

    /**
     * Formats a string to fit within a specified line length, adding indentation.
     * This is used to format the description in the help message.
     *
     * @param str The string to format.
     * @return The formatted string with proper indentation and line breaks.
     */
    private static String formatStringWithIndentation(String str) {
        final int MAX_LINE_LENGTH = 80; // Maximum line length
        final int INDENTATION = 4; // Number of spaces for indentation

        StringBuilder formatted = new StringBuilder(
                String.format("%" + INDENTATION + "s", "")
        );
        String[] words = str.split(" ");
        int currentLineLength = 0;

        for (String word : words) {
            // If adding the next word exceeds the line length, start a new line
            if (currentLineLength + word.length() + 1 > MAX_LINE_LENGTH) {
                formatted.append("\n").append(
                        String.format("%" + INDENTATION + "s", "")
                );
                currentLineLength = INDENTATION; // Reset current line length to indentation
            } else if (currentLineLength > 0) {
                formatted.append(" "); // Add space before the next word
                currentLineLength++; // Increment for the space
            } else {
                currentLineLength = INDENTATION; // Start with indentation
            }
            formatted.append(word);
            currentLineLength += word.length(); // Update current line length
        }

        return formatted.toString();
    }
}
