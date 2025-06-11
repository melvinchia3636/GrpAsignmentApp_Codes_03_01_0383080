package features.terminal.functions.help;

import core.cli.commands.CommandInstance;
import core.cli.commands.CommandNamespace;
import core.cli.commands.CommandRegistrar;
import core.terminal.Chalk;

import java.util.Arrays;

import static features.terminal.utils.HelpUtils.appendCommandsToMsg;

/**
 * This class builds the global help message for the terminal.
 * It provides usage instructions, examples, and a list of available commands.
 * The help message can be displayed in a tree structure if specified.
 */
public class GlobalHelpBuilder {
    private static final String usageMsg = String.format(
            "%s\n  %s [positional_arguments] [--keyword-arguments]",
            new Chalk("Usage:").yellow().bold(),
                new Chalk("<command>").green()
        );

    private static final String noteMsg = new Chalk("Notes:").yellow().bold() + "\n" +
            "  - Type " + new Chalk("'help -c <command>'").bold().green() + " to get detailed help for a specific command.\n" +
            "  - Type " + new Chalk("'help -c <command>.<sub-command>.[...]'").bold().green() + " to get detailed help for a specific sub-command.\n" +
            "  - If no positional arguments are provided, you will be prompted to enter them interactively.";

    private static final String examplesMsg = new Chalk("Examples:").yellow().bold() + "\n" +
            "  " + new Chalk("login").bold().green() + " johndoe 12345678 --save-session\n" +
            "  " + new Chalk("help").bold().green() + " -c login\n" +
            "  " + new Chalk("footprint log").bold().green() + " driving 1.5";

    /**
     * Will be called when the user types 'help' without specifying a command.
     * Prints the global help message with usage instructions, examples, and a list of available commands.
     *
     * @param showTree Whether to display commands in a tree structure.
     */
    public static void printGlobalHelp(boolean showTree) {
        String commandsMsg = getCommandsMsg(showTree);

        String[] sections = {
                usageMsg,
                noteMsg,
                examplesMsg,
                commandsMsg
        };

        System.out.println(String.join("\n\n", sections));
    }

    /**
     * Generates a message containing the list of available commands.
     *
     * @param showTree Whether to display commands in a tree structure.
     * @return A formatted string containing the available commands.
     */
    private static String getCommandsMsg(
            boolean showTree
    ) {
        StringBuilder commandsMsg = new StringBuilder(
                new Chalk("Available Commands:").yellow().bold() + "\n"
        );

        for (CommandNamespace commandNamespace : CommandRegistrar.commandInstances) {
            if (commandNamespace.isDisabled()) continue;

            if (commandNamespace.name != null) {
                commandsMsg
                        .append("\n  ")
                        .append(new Chalk(commandNamespace.name).underline().bold().yellow())
                        .append("\n");
            }

            CommandInstance[] filteredCommands = Arrays.stream(commandNamespace.commands)
                    .filter(command -> !command.isDisabled())
                    .toArray(CommandInstance[]::new);

            for (int i = 0; i < filteredCommands.length; i++) {
                appendCommandsToMsg(
                        commandsMsg,
                        filteredCommands[i],
                        "  ",
                        i == filteredCommands.length - 1,
                        showTree
                );
            }
        }

        return commandsMsg.toString().replaceFirst("\n$", "");
    }
}
