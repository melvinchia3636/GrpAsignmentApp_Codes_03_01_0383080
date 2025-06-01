package features;

import core.cli.commands.CommandInstance;

import features.auth.commands.LoginCommand;
import features.auth.commands.SignupCommand;

import features.terminal.commands.ClearScreenCommand;
import features.terminal.commands.ExitCommand;
import features.terminal.commands.HelpCommand;

/**
 * CommandRegistrar is responsible for registering all commands available in the application.
 * It contains a static array of Command objects, each representing a command with its arguments and handler.
 * This class serves as a central point for command management, allowing easy addition or modification of commands.
 */
public class CommandRegistrar {
    public static CommandInstance[] commandInstances = new CommandInstance[]{
            new LoginCommand(),
            new SignupCommand(),
            new HelpCommand(),
            new ClearScreenCommand(),
            new ExitCommand()
    };

    /**
     * Retrieves the maximum length of command names from all registered commands.
     *
     * @return the maximum length of command names
     */
    public static int getMaxCommandLength() {
        int maxLength = 0;
        for (CommandInstance command : commandInstances) {
            String commandName = command.name;

            if (commandName.length() > maxLength) {
                maxLength = commandName.length();
            }
        }
        return maxLength;
    }

    /**
     * Retrieves a CommandInstance by its name.
     *
     * @param name the name of the command to retrieve
     * @return the CommandInstance if found, or null if no command matches the name
     */
    public static CommandInstance getCommandByName(String name) {
        for (CommandInstance command : commandInstances) {
            if (command.name.equalsIgnoreCase(name)) {
                return command;
            }
        }
        return null; // Return null if no command matches the name
    }
}
