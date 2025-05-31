package features.terminal;


import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.KeywordArgument;
import core.cli.arguments.PositionalArgument;
import core.cli.commands.CommandInstance;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.CommandRegistrar;

public class HelpHandler extends CommandInstance.Handler {
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

    /**
     * Will be called when the user types 'help' without specifying a command.
     * Prints the global help message with usage instructions, examples, and a list of available commands.
     */
    private static void printGlobalHelp() {
        String usageMsg = new Chalk("USAGE:").yellow().bold() + "\n" +
                "    " + new Chalk("<command>").bold().green().toString() + " [positional_arguments] [--keyword-arguments]\n\n";

        String noteMsg = new Chalk("NOTE:").yellow().bold() + "\n" +
                "    Type " + new Chalk("'help -c <command>'").bold().green().toString() + " to get detailed help for a specific command.\n" +
                "    If no positional arguments are provided, you will be prompted to enter them interactively.\n\n";

        String examplesMsg = new Chalk("EXAMPLES:").yellow().bold() + "\n" +
                "    " + new Chalk("login").bold().green().toString() + " johndoe 12345678 --save-session\n" +
                "    " + new Chalk("help").bold().green().toString() + " -c login\n\n";

        StringBuilder commandsMsg = new StringBuilder(
                new Chalk("COMMANDS:").yellow().bold() + "\n"
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
                    .append(new Chalk(commandName).bold().green().toString())
                    .append(String.format("%" + (maxCommandLength - commandName.length() + 4) + "s", ""))
                    .append(firstSentenceOfDescription)
                    .append("\n");
        }

        System.out.println(
                usageMsg +
                        noteMsg +
                        examplesMsg +
                        commandsMsg
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

        String usageMsg = new Chalk("USAGE:").yellow().bold() + "\n" +
                "    " + new Chalk(commandInstance.name).bold().green().toString() + " [positional_arguments] [--keyword-arguments]\n\n";

        String descriptionMsg = new Chalk("DESCRIPTION:").yellow().bold() + "\n" +
                formatStringWithIndentation(firstSentence) + "\n\n" +
                (remainingDescription.isEmpty() ? "" : formatStringWithIndentation(remainingDescription) + "\n\n");

        StringBuilder argsMsg = new StringBuilder();

        int maxTypeStringLength = ArgumentDataType.getMaxTypeStringLength();

        PositionalArgument[] positionalArgs = commandInstance.args.positionalArguments;
        KeywordArgument[] keywordArgs = commandInstance.args.keywordArguments;

        if (positionalArgs.length != 0) {
            argsMsg.append(new Chalk("POSITIONAL ARGUMENTS:").yellow().bold()).append("\n");

            int maxPositionalArgNameLength = commandInstance.args.getMaxPositionalArgumentNameLength();

            for (PositionalArgument arg : positionalArgs) {
                String argName = arg.name;
                String argDescription = arg.description;

                argsMsg.append("    ")
                        .append(new Chalk(argName).bold().blue().toString())
                        .append(String.format("%" + (maxPositionalArgNameLength - argName.length() + 4) + "s", ""))
                        .append(new Chalk(String.format("[%s]", arg.dataType.type)).bold().purple().toString())
                        .append(String.format("%" + (maxTypeStringLength - arg.dataType.type.length() + 4) + "s", ""))
                        .append(argDescription)
                        .append("\n");

            }
            argsMsg.append("\n");
        }

        if (keywordArgs.length != 0) {
            argsMsg.append(new Chalk("KEYWORD ARGUMENTS:").yellow().bold()).append("\n");

            int maxKeywordArgNameLength = commandInstance.args.getMaxKeywordArgumentNameLength();

            for (KeywordArgument arg : keywordArgs) {
                String argName = arg.name;
                String argAbbreviation = arg.abbreviation;
                String argDescription = arg.description;

                argsMsg.append("    ")
                        .append(new Chalk(String.format("-%s, --%s", argAbbreviation, argName)).bold().blue().toString())
                        .append(String.format("%" + (maxKeywordArgNameLength - argName.length() + 4) + "s", ""))
                        .append(new Chalk(String.format("[%s]", arg.dataType.type)).bold().purple().toString())
                        .append(String.format("%" + (maxTypeStringLength - arg.dataType.type.length() + 4) + "s", ""))
                        .append(arg.required ? new Chalk("(required)").bold().red().toString() : new Chalk("(optional)").bold().green().toString())
                        .append("    ")
                        .append(argDescription)
                        .append("\n");
            }
            argsMsg.append("\n");
        }


        System.out.println(
                usageMsg +
                        descriptionMsg +
                        argsMsg
        );
    }

    @Override
    public void run() {
        String targetCommandName = argsMap.get("command");

        if (targetCommandName == null) {
            printGlobalHelp();
            return;
        }

        CommandInstance commandInstance = CommandRegistrar.getCommandByName(targetCommandName);

        if (commandInstance == null) {
            OutputUtils.error("Command not found: " + targetCommandName);
        } else {
            printCommandHelp(commandInstance);
        }
    }
}
