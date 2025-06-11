package features.terminal.handlers;


import core.cli.commands.CommandError;
import core.cli.commands.CommandInstance;
import core.cli.commands.CommandRegistrar;
import features.terminal.functions.help.CommandHelpBuilder;
import features.terminal.functions.help.GlobalHelpBuilder;

import java.util.Arrays;

public class HelpHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        String targetCommandNames = argsMap.get("command");
        boolean showTree = argsMap.containsKey("tree");

        // If no specific command is provided, show global help
        if (targetCommandNames == null) {
            GlobalHelpBuilder.printGlobalHelp(showTree);
            return;
        }

        String[] splitCommandNames = Arrays.stream(targetCommandNames.split("\\.")).filter(
                name -> !name.isEmpty()
        ).toArray(String[]::new);

        CommandInstance commandInstance = getTargetCommandInstance(splitCommandNames);
        CommandHelpBuilder.printCommandHelp(commandInstance, showTree);
    }

    /**
     * Retrieves the target command instance layer by layer based on the user input command names.
     *
     * @param splitCommandNames The split command names to find the command instance.
     * @return The CommandInstance corresponding to the provided command names.
     * @throws CommandError If the command or sub-command is not found or is disabled.
     */
    private CommandInstance getTargetCommandInstance(String[] splitCommandNames) {
        CommandInstance commandInstance =  CommandRegistrar.getCommandByName(splitCommandNames[0]);

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

        return commandInstance;
    }
}
