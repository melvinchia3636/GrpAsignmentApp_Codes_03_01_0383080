package core.cli.commands;

import features.auth.commands.LoginCommand;
import features.auth.commands.LogoutCommand;
import features.auth.commands.ProfileCommand;
import features.auth.commands.SignupCommand;

import features.modules.CarbonFootprintAnalyzer.commands.*;

import features.terminal.commands.ClearScreenCommand;
import features.terminal.commands.ExitCommand;
import features.terminal.commands.HelpCommand;

import java.util.ArrayList;

/**
 * CommandRegistrar is responsible for registering all commands available in the application.
 * It contains a static array of Command objects, each representing a command with its arguments and handler.
 * This class serves as a central point for command management, allowing easy addition or modification of commands.
 */
public class CommandRegistrar {
    public static CommandNamespace[] commandInstances = {
            new CommandNamespace("General", new CommandInstance[]{
                    new HelpCommand(),
                    new ClearScreenCommand(),
                    new ExitCommand()
            }),
            new CommandNamespace("Authentication", new CommandInstance[]{
                    new LoginCommand(),
                    new SignupCommand(),
                    new ProfileCommand(),
                    new LogoutCommand()
            }),
            new CommandNamespace("Modules", new CommandInstance[]{
                    new FootprintCommand()
            })
    };

    /*
     * Static block to ensure that all command names are unique across all registered commands.
     * If a duplicate command name is found, an IllegalArgumentException is thrown.
     */
    static {
        ArrayList<String> commandNames = new ArrayList<>();
        for (CommandNamespace namespace : commandInstances) {
            for (CommandInstance command : namespace.commands) {
                if (commandNames.contains(command.name)) {
                    throw new IllegalArgumentException("Duplicate command name found: " + command.name);
                }
                commandNames.add(command.name);
            }
        }
    }

    /**
     * Retrieves a CommandInstance by its name.
     *
     * @param name the name of the command to retrieve
     * @return the CommandInstance if found
     * @throws IllegalArgumentException if no command matches the given name
     * @throws IllegalArgumentException if the command is not enabled
     */
    public static CommandInstance getCommandByName(String name) {
        for (CommandNamespace commandNamespace : commandInstances) {
            for (CommandInstance command : commandNamespace.commands) {
                if (command.name.equalsIgnoreCase(name)) {
                    if (command.isDisabled()) {
                        throw new IllegalArgumentException("Command is not accessible: " + name);
                    }

                    return command;
                }
            }
        }

        throw new IllegalArgumentException("Command not found: " + name);
    }
}
