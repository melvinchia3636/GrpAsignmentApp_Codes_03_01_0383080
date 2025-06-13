package features.terminal.utils;

import core.cli.commands.CommandInstance;
import core.terminal.Chalk;

import java.util.Arrays;

public class HelpUtils {
    /**
     * Appends the command tree to the provided StringBuilder.
     *
     * @param commandsMsg   The StringBuilder to append the commands to.
     * @param commandInstance The command instance to process.
     * @param prefix        The prefix for the current command level.
     * @param isLast        Whether this is the last command at this level.
     * @param showTree      Whether to show the tree structure.
     */
    public static void appendCommandsToMsg(
            StringBuilder commandsMsg,
            CommandInstance commandInstance,
            String prefix,
            int maxCommandLength,
            boolean isLast,
            boolean showTree
    ) {
        String commandDescription = commandInstance.description;

        String firstSentenceOfDescription = commandDescription.contains(".")
                ? commandDescription.substring(0, commandDescription.indexOf(".") + 1)
                : commandDescription;

        commandsMsg.append(prefix);

        if (showTree) {
            commandsMsg.append(isLast ? "└── " : "├── ");
        } else {
            commandsMsg.append("  ");
        }

        commandsMsg.append(new Chalk(commandInstance.name).bold().green())
                .append(String.format("%" + (maxCommandLength - commandInstance.name.length() + 6) + "s", " "))
                .append(firstSentenceOfDescription)
                .append("\n");

        if (!commandInstance.hasSubCommands || !showTree) return;

        CommandInstance[] subCommands = Arrays.stream(commandInstance.subCommands)
                .filter(sub -> !sub.isDisabled())
                .toArray(CommandInstance[]::new);

        int _maxCommandLength = Arrays.stream(subCommands)
                .mapToInt(sub -> sub.name.length())
                .max()
                .orElse(0);

        for (int i = 0; i < subCommands.length; i++) {
            boolean last = (i == subCommands.length - 1);
            String newPrefix = prefix + (isLast ? "    " : "│   ");
            appendCommandsToMsg(commandsMsg, subCommands[i], newPrefix, _maxCommandLength, last, true);
        }
    }
}
