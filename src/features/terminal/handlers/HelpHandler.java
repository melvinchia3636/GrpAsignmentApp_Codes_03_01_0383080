package features.terminal.handlers;


import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.KeywordArgument;
import core.cli.arguments.PositionalArgument;
import core.cli.commands.CommandError;
import core.cli.commands.CommandInstance;
import core.terminal.Chalk;
import core.cli.CommandRegistrar;

import java.util.ArrayList;
import java.util.Arrays;

public class HelpHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        String targetCommandNames = argsMap.get("command");

        if (targetCommandNames == null) {
            printGlobalHelp();
            return;
        }

        String[] splitCommandNames = Arrays.stream(targetCommandNames.split("\\.")).filter(
                name -> !name.isEmpty()
        ).toArray(String[]::new);

        CommandInstance commandInstance = CommandRegistrar.getCommandByName(splitCommandNames[0]);

        for (int i = 1; i < splitCommandNames.length; i++) {
            if (!commandInstance.hasSubCommands) {
                throw new CommandError(
                        String.join(".", Arrays.copyOfRange(splitCommandNames, 0, i)),
                        String.format(
                                "Command '%s' does not have sub-commands. Cannot access '%s'.",
                                String.join(".", Arrays.copyOfRange(splitCommandNames, 0, i)),
                                String.join(".", Arrays.copyOfRange(splitCommandNames, i, splitCommandNames.length))
                        ));
            }

            String subCommandName = splitCommandNames[i];
            commandInstance = commandInstance.getSubCommandByName(subCommandName);

            if (commandInstance == null) {
                throw new CommandError(
                        String.join(".", Arrays.copyOfRange(splitCommandNames, 0, i)),
                        String.format(
                                "Sub-command '%s' not found in command '%s'.",
                                subCommandName,
                                String.join(".", Arrays.copyOfRange(splitCommandNames, 0, i + 1))
                        )
                );
            }

            if (commandInstance.isDisabled()) {
                throw new IllegalArgumentException(
                        String.format(
                                "Sub-command '%s' is not enabled in command '%s'.",
                                subCommandName,
                                String.join(".", Arrays.copyOfRange(splitCommandNames, 0, i + 1))
                        )
                );
            }
        }

        printCommandHelp(commandInstance);
    }

    /**
     * Will be called when the user types 'help' without specifying a command.
     * Prints the global help message with usage instructions, examples, and a list of available commands.
     */
    private static void printGlobalHelp() {
        String usageMsg = new Chalk("Usage:").yellow().bold() + "\n" +
                "    " + new Chalk("<command>").bold().green() + " [positional_arguments] [--keyword-arguments]";

        String noteMsg = new Chalk("Note:").yellow().bold() + "\n" +
                "    - Type " + new Chalk("'help -c <command>'").bold().green() + " to get detailed help for a specific command.\n" +
                "    - Type " + new Chalk("'help -c <command>.<sub-command>.[...]'").bold().green() + " to get detailed help for a specific sub-command.\n" +
                "    - If no positional arguments are provided, you will be prompted to enter them interactively.";

        String examplesMsg = new Chalk("Examples:").yellow().bold() + "\n" +
                "    " + new Chalk("login").bold().green() + " johndoe 12345678 --save-session\n" +
                "    " + new Chalk("help").bold().green() + " -c login";

        StringBuilder commandsMsg = new StringBuilder(
                new Chalk("Available Commands:").yellow().bold() + "\n"
        );

        int maxCommandLength = CommandRegistrar.getMaxCommandLength();

        // Dynamically append command names and descriptions into the help message
        for (CommandInstance command : CommandRegistrar.commandInstances) {
            if (command.isDisabled()) {
                continue; // Skip commands that are not enabled
            }

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

        String[] sections = {
                usageMsg,
                noteMsg,
                examplesMsg,
                commandsMsg.toString()
        };

        System.out.println(String.join("\n\n", sections));
    }

    /**
     * Will be called when the user types 'help -c <command>'.
     * Prints detailed help for a specific command, including its usage, arguments, and examples.
     *
     * @param commandInstance The CommandInstance object representing the command to display help for.
     */
    private static void printCommandHelp(CommandInstance commandInstance) {
        ArrayList<String> fullCommandPath = commandInstance.getFullPath();
        String description = commandInstance.description;

        int fullStopIndex = description.indexOf(".");
        fullStopIndex = fullStopIndex == -1 ? description.length() : fullStopIndex + 1;

        String firstSentence = description.substring(0, fullStopIndex);
        String remainingDescription = description.substring(fullStopIndex).trim();

        String usageMsg = String.format(
                "%s\n    %s%s [positional_arguments] [--keyword-arguments]",
                new Chalk("Usage:").yellow().bold(),
                new Chalk(String.join(" ", fullCommandPath)).green(),
                commandInstance.hasSubCommands
                        ? " <sub-command>"
                        : ""
        );

        String descriptionMsg = String.format(
                "%s\n%s%s",
                new Chalk("Description:").yellow().bold(),
                formatStringWithIndentation(firstSentence),
                remainingDescription.isEmpty()
                        ? ""
                        : "\n\n" + formatStringWithIndentation(remainingDescription)
        );

        String exampleMsg = commandInstance.example != null
                ? String.format(
                "%s\n    %s %s",
                new Chalk("Example:").yellow().bold(),
                new Chalk(String.join(" ", fullCommandPath)).bold().green(),
                commandInstance.example
        )
                : "";

        String argsMsg = getArgsMsg(commandInstance);
        String subCommandsMsg = getSubCommandsMsg(commandInstance);

        String[] sections = {
                usageMsg,
                descriptionMsg,
                exampleMsg,
                argsMsg,
                subCommandsMsg
        };

        String output = String.join(
                "\n\n",
                Arrays.stream(sections)
                        .filter(e -> !e.isEmpty())
                        .toArray(String[]::new)
        );

        System.out.println(output);
    }

    private static String getSubCommandsMsg(
            CommandInstance commandInstance
    ) {
        if (!commandInstance.hasSubCommands) {
            return "";
        }

        StringBuilder subCommandsMsg = new StringBuilder(
                new Chalk("Sub-Commands:").yellow().bold() + "\n"
        );

        CommandInstance[] subCommands = commandInstance.subCommands;

        int maxCommandNameLength = CommandRegistrar.getMaxCommandLength();

        for (CommandInstance subCommand : subCommands) {
            String subCommandName = subCommand.name;
            String subCommandDescription = subCommand.description;

            subCommandsMsg.append("    ")
                    .append(new Chalk(subCommandName).bold().green())
                    .append(String.format("%" + (maxCommandNameLength - subCommandName.length() + 4) + "s", ""))
                    .append(subCommandDescription)
                    .append("\n");
        }

        return subCommandsMsg.toString();
    }

    private static String getArgsMsg(
            CommandInstance commandInstance
    ) {
        StringBuilder argsMsg = new StringBuilder();

        if (!commandInstance.hasSubCommands) {
            appendPositionalArgsToMsg(argsMsg, commandInstance);
            appendKeywordArgsToMsg(argsMsg, commandInstance);
        }

        return argsMsg.toString();
    }

    private static void appendPositionalArgsToMsg(
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
    }

    private static void appendKeywordArgsToMsg(
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
     * Please don't mind the comments on almost every line, since this function is a little unintuitive
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
