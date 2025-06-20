package features.terminal.functions.help;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.KeywordArgument;
import core.cli.arguments.PositionalArgument;
import core.cli.commands.CommandInstance;
import core.terminal.Chalk;

import java.util.ArrayList;
import java.util.Arrays;

import static features.terminal.utils.HelpUtils.appendCommandsToMsg;

/**
 * This class is responsible for building and displaying help messages for specific commands.
 * It formats the command usage, description, positional arguments, keyword arguments,
 * and available sub-commands in a user-friendly manner.
 * The help message can be displayed in a tree structure if specified.
 */
public class CommandHelpBuilder {
    /**
     * Will be called when the user types 'help -c <command>'.
     * Prints detailed help for a specific command, including its usage, arguments, and examples.
     *
     * @param commandInstance The CommandInstance object representing the command to display help for.
     */
    public static void printCommandHelp(CommandInstance commandInstance, boolean showTree) {
        ArrayList<String> fullCommandPath = commandInstance.getFullPath();
        String description = commandInstance.getDescription();

        // Split the description into the first sentence and the remaining description.
        int fullStopIndex = description.indexOf(".");
        fullStopIndex = fullStopIndex == -1 ? description.length() : fullStopIndex + 1;

        String firstSentence = description.substring(0, fullStopIndex);
        String remainingDescription = description.substring(fullStopIndex).trim();

        String usageMsg = String.format(
                "%s\n  %s%s%s%s",
                new Chalk("Usage:").yellow().bold(),
                new Chalk(String.join(" ", fullCommandPath)).bold().green(),
                commandInstance.isHasSubCommands()
                        ? " <sub-command>"
                        : "",
                commandInstance.getArgs().getPositionalArguments().length > 0 ?
                        " " + new Chalk(
                                String.join(" ",
                                        Arrays.stream(commandInstance.getArgs().getPositionalArguments())
                                                .map(e -> "<" + e.getName() + ">")
                                                .toArray(String[]::new)
                                )
                        ).blue() : "",
                commandInstance.getArgs().getKeywordArguments().length > 0 ? " [--keyword-arguments]" : ""
        );

        String descriptionMsg = String.format(
                "%s\n%s%s",
                new Chalk("Description:").yellow().bold(),
                formatStringWithIndentation(firstSentence),
                remainingDescription.isEmpty()
                        ? ""
                        : "\n\n" + formatStringWithIndentation(remainingDescription)
        );

        String exampleMsg = commandInstance.getExample() != null
                ? String.format(
                "%s\n  %s %s",
                new Chalk("Example:").yellow().bold(),
                new Chalk(String.join(" ", fullCommandPath)).bold().green(),
                commandInstance.getExample()
        )
                : "";

        String positionalArgsMsg = getPositionalArgsMsg(commandInstance);
        String keywordArgsMsg = getKeywordArgsMsg(commandInstance);
        String commandsMsg = getCommandsMsg(commandInstance, showTree);

        String[] sections = {
                usageMsg,
                descriptionMsg,
                exampleMsg,
                positionalArgsMsg,
                keywordArgsMsg,
                commandsMsg
        };

        String output = String.join(
                "\n\n",
                Arrays.stream(sections)
                        .filter(e -> !e.isEmpty())
                        .toArray(String[]::new)
        ).replaceFirst("\n$", "");

        System.out.println(output);
    }

    /**
     * Generates a message containing the available sub-commands of a command.
     *
     * @param commandInstance The CommandInstance object representing the command.
     * @param showTree        Whether to display the commands in a tree structure.
     */
    private static String getCommandsMsg(
            CommandInstance commandInstance,
            boolean showTree
    ) {
        if (!commandInstance.isHasSubCommands()) {
            return "";
        }

        StringBuilder subCommandsMsg = new StringBuilder(
                new Chalk("Available Commands:").yellow().bold() + "\n"
        );

        CommandInstance[] filteredCommands = Arrays.stream(commandInstance.getSubCommands())
                .filter(command -> !command.isDisabled())
                .toArray(CommandInstance[]::new);

        int maxCommandLength = Arrays.stream(filteredCommands)
                .mapToInt(command -> command.getName().length())
                .max()
                .orElse(0);

        for (int i = 0; i < filteredCommands.length; i++) {
            appendCommandsToMsg(
                    subCommandsMsg,
                    filteredCommands[i],
                    "",
                    maxCommandLength,
                    i == filteredCommands.length - 1,
                    showTree
            );
        }

        return subCommandsMsg.toString();
    }

    /**
     * Generates a message containing the positional arguments of a command.
     *
     * @param commandInstance The CommandInstance object representing the command.
     * @return A formatted string containing the positional arguments and their descriptions.
     */
    private static String getPositionalArgsMsg(
            CommandInstance commandInstance
    ) {
        StringBuilder argsMsg = new StringBuilder();
        PositionalArgument[] positionalArgs = commandInstance.getArgs().getPositionalArguments();

        if (positionalArgs.length == 0) {
            return "";
        }

        int maxTypeStringLength = ArgumentDataType.getMaxTypeStringLength();
        int maxPositionalArgNameLength = commandInstance.getArgs().getMaxPositionalArgumentNameLength();

        argsMsg.append(new Chalk("Positional Arguments:").yellow().bold()).append("\n");

        for (PositionalArgument arg : positionalArgs) {
            String argName = arg.getName();
            String argDescription = arg.getDescription();

            argsMsg.append("  ")
                    .append(new Chalk(argName).bold().blue())
                    .append(String.format("%" + (maxPositionalArgNameLength - argName.length() + 4) + "s", ""))
                    .append(new Chalk(String.format("[%s]", arg.getDataType().getType())).bold().purple())
                    .append(String.format("%" + (maxTypeStringLength - arg.getDataType().getType().length() + 4) + "s", ""))
                    .append(argDescription)
                    .append("\n");
        }

        return argsMsg.toString();
    }

    /**
     * Generates a message containing the keyword arguments of a command.
     *
     * @param commandInstance The CommandInstance object representing the command.
     * @return A formatted string containing the keyword arguments and their descriptions.
     */
    private static String getKeywordArgsMsg(
            CommandInstance commandInstance
    ) {
        StringBuilder argsMsg = new StringBuilder();
        KeywordArgument[] keywordArgs = commandInstance.getArgs().getKeywordArguments();

        if (keywordArgs.length == 0) {
            return "";
        }

        int maxTypeStringLength = ArgumentDataType.getMaxTypeStringLength();
        int maxKeywordArgNameLength = commandInstance.getArgs().getMaxKeywordArgumentNameLength();

        argsMsg.append(new Chalk("Keyword Arguments:").yellow().bold()).append("\n");

        for (KeywordArgument arg : keywordArgs) {
            String argName = arg.getName();
            String argAbbreviation = arg.getAbbreviation();
            String argDescription = arg.getDescription();

            argsMsg.append("  ")
                    .append(new Chalk(String.format("-%s, --%s", argAbbreviation, argName)).bold().blue())
                    .append(String.format("%" + (maxKeywordArgNameLength - argName.length() + 4) + "s", ""))
                    .append(new Chalk(String.format("[%s]", arg.getDataType().getType())).bold().purple())
                    .append(String.format("%" + (maxTypeStringLength - arg.getDataType().getType().length() + 4) + "s", ""))
                    .append(arg.isRequired() ? new Chalk("(required)").bold().red() : new Chalk("(optional)").bold().green())
                    .append("    ")
                    .append(argDescription)
                    .append("\n");
        }

        return argsMsg.toString();
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
        final int INDENTATION = 2; // Number of spaces for indentation

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
