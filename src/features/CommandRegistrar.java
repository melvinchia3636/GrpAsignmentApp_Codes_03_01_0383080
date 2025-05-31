package features;

import core.cli.commands.CommandInstance;
import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.KeywordArgument;
import core.cli.arguments.PositionalArgument;
import features.terminal.ExitHandler;
import features.auth.handlers.LoginHandler;
import features.terminal.ClearScreenHandler;

/**
 * CommandRegistrar is responsible for registering all commands available in the application.
 * It contains a static array of Command objects, each representing a command with its arguments and handler.
 * This class serves as a central point for command management, allowing easy addition or modification of commands.
 */
public class CommandRegistrar {
    public static CommandInstance[] commandInstances = new CommandInstance[]{
            new CommandInstance(
                    "login",
                    "Login to the system with your credentials.",
                    new ArgumentList(
                            new PositionalArgument[]{
                                    new PositionalArgument("username", "Username of the your profile", ArgumentDataType.STRING),
                                    new PositionalArgument("password", "Password of the your profile", ArgumentDataType.STRING)
                            },
                            new KeywordArgument[] {
                                    new KeywordArgument("remember-me", "r", "Whether to remember the user for future logins", ArgumentDataType.FLAG, false),
                            }
                    ), new LoginHandler()),
            new CommandInstance(
                    "help",
                    "Display help information for commands.",
                    new ArgumentList(
                            new KeywordArgument[]{
                                    new KeywordArgument("command", "c", "The command to get help for", ArgumentDataType.STRING, false)
                            }
                    ),
                    new features.terminal.HelpHandler()
            ),
            new CommandInstance(
                    "clear",
                    "Clear the terminal screen. " +
                            "Note: This command only works in a terminal environment, not in an IDE console. " +
                            "It will not work in the screen that gets pop up when you simply press the run button " +
                            "in IntelliJ IDEA or similar IDEs. So please, run this system in an actual terminal.",
                    new ClearScreenHandler()
            ),
            new CommandInstance(
                    "exit",
                    "Exit the application.",
                    new ExitHandler()
            )
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
