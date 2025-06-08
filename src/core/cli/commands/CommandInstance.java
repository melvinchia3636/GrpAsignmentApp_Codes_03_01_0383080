package core.cli.commands;

import core.cli.arguments.ArgumentList;
import core.cli.arguments.KeywordArgument;
import core.cli.arguments.PositionalArgument;
import core.models.SimpleMap;

import java.util.ArrayList;

/**
 * Abstract class representing a command instance in the command-line interface.
 * It contains the command's name, description, example usage, argument list, and a handler for execution.
 * It also supports nested subcommands and provides methods for executing the command and retrieving its full path.
 */
public abstract class CommandInstance {
    public final String name;
    public final String description;
    public final String example;
    public final ArgumentList args;
    public boolean hasSubCommands;
    public final CommandInstance[] subCommands;
    public CommandInstance parentCommand = null;
    protected final Handler handler;

    /**
     * Logic to construct a CommandInstance with the given parameters.
     * This constructor is private to enforce the use of the public constructors
     * Contains the logic to assign appropriate values to the fields based on whether subcommands are provided or not.
     *
     * @param name        the name of the command
     * @param description a brief description of the command
     * @param example     an example of how to use the command
     * @param args        the list of arguments for the command
     * @param handler     the handler that will process the command
     * @param subCommands an array of subcommands associated with this command
     */
    private CommandInstance(String name, String description, String example, ArgumentList args, Handler handler, CommandInstance[] subCommands) {
        this.name = name;
        this.description = description;
        this.example = example;

        if (subCommands != null) {
            this.args = new ArgumentList(new PositionalArgument[0], new KeywordArgument[0]);
            this.handler = null;
            this.subCommands = subCommands;
            for (CommandInstance subCommand : subCommands) {
                subCommand.parentCommand = this; // Set the parent command for each subcommand
            }

            this.hasSubCommands = true;
            return;
        }

        this.args = args;
        this.handler = handler;
        this.subCommands = new CommandInstance[0];
        this.hasSubCommands = false;
    }

    /**
     * Constructs a Command with the given command name and handler, without any arguments.
     *
     * @param name        the name of the command
     * @param description a brief description of the command
     * @param example     an example of how to use the command
     * @param handler     the handler that will process the command
     */
    protected CommandInstance(String name, String description, String example, Handler handler) {
        this(name, description, example, new ArgumentList(new PositionalArgument[0], new KeywordArgument[0]), handler, null);
    }

    /**
     * Constructs a Command with the given command name, description, example, arguments, and handler.
     *
     * @param name        the name of the command
     * @param description a brief description of the command
     * @param example     an example of how to use the command
     * @param args        the list of arguments for the command
     * @param handler     the handler that will process the command
     */
    protected CommandInstance(String name, String description, String example, ArgumentList args, Handler handler) {
        this(name, description, example, args, handler, null);
    }

    /**
     * Constructs a Command with the given command name, description, and subcommands.
     *
     * @param name        the name of the command
     * @param description a brief description of the command
     * @param subCommands an array of subcommands associated with this command
     */
    protected CommandInstance(String name, String description, CommandInstance[] subCommands) {
        this(name, description, null, new ArgumentList(new PositionalArgument[0], new KeywordArgument[0]), null, subCommands);
    }

    /**
     * This function will validate the parsed command arguments, then turn them into a map
     * This map will then be assigned to the handler's argsMap field that can be used inside the handler.
     * It will then call the handler's run method to execute the command with the provided arguments.
     *
     * @param parsedCommand the parsed command containing arguments
     */
    public void execute(CommandParser.ParsedCommand parsedCommand) {
        SimpleMap<String, String> argsMap = CommandParser.validateAndGenerateArgsMap(
                parsedCommand,
                this.args,
                String.join(".", getFullPath())
            );

        if (handler == null) {
            throw new IllegalStateException("Handler is not set for command: " + name);
        }

        handler.argsMap = argsMap;
        handler.run();
    }

    public boolean isDisabled() {
        return false;
    }

    /**
     * Retrieves a sub-command by its name.
     *
     * @param name the name of the sub-command to retrieve
     * @return the CommandInstance if found, or null if no sub-command matches the name
     * @throws CommandError if this command does not have sub-commands
     */
    public CommandInstance getSubCommandByName(String name) {
        if (!hasSubCommands) {
            throw new CommandError(getFullPath(), "Command " + this.name + " does not have sub-commands.");
        }

        for (CommandInstance subCommand : subCommands) {
            if (subCommand.name.equals(name)) {
                if (subCommand.isDisabled()) {
                    throw new CommandError(
                        subCommand.getFullPath(),
                        "Command is not accessible: " + subCommand.name
                    );
                }

                return subCommand;
            }
        }

        return null;
    }

    /**
     * Retrieves the full path of the command, including its parent commands.
     * This method constructs a list of command names from the current command up to the root command.
     *
     * @return an ArrayList containing the full path of command names
     */
    public ArrayList<String> getFullPath() {
        CommandInstance currentCommand = this;
        ArrayList<String> path = new ArrayList<>();

        while (currentCommand != null) {
            path.add(0, currentCommand.name);
            currentCommand = currentCommand.parentCommand;
        }

        return path;
    }

    /**
     * Abstract class representing a handler for a command.
     * It contains a map of arguments and an abstract run method that must be implemented by subclasses.
     */
    public abstract static class Handler {
        protected SimpleMap<String, String> argsMap;

        public abstract void run();
    }
}
