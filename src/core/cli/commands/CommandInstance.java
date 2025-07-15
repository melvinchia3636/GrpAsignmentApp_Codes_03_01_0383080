package core.cli.commands;

import core.cli.arguments.ArgumentList;
import core.manager.GlobalManager;
import core.instances.ListOfKVs;
import features.auth.data.UserManager;

import java.util.ArrayList;

/**
 * Abstract class representing a command instance in the command-line interface.
 * It contains the command's name, description, example usage, argument list, and a handler for execution.
 * It also supports nested subcommands and provides methods for executing the command and retrieving its full path.
 */
public abstract class CommandInstance {
    private final String name;
    private final String description;
    private final String example;
    private final ArgumentList args;
    private final boolean hasSubCommands;
    private final CommandInstance[] subCommands;
    private final Handler handler;
    private CommandInstance parentCommand = null;

    private boolean authRequired = false;

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
            this.args = new ArgumentList();
            this.handler = null;
            this.subCommands = subCommands;
            for (CommandInstance subCommand : subCommands) {
                subCommand.setParentCommand(this); // Set the parent command for each subcommand
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
        this(name, description, example, new ArgumentList(), handler, null);
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
        this(name, description, null, new ArgumentList(), null, subCommands);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getExample() {
        return example;
    }

    public ArgumentList getArgs() {
        return args;
    }

    public boolean isHasSubCommands() {
        return hasSubCommands;
    }

    public CommandInstance[] getSubCommands() {
        return subCommands;
    }

    public Handler getHandler() {
        return handler;
    }

    public CommandInstance getParentCommand() {
        return parentCommand;
    }

    public void setParentCommand(CommandInstance parentCommand) {
        this.parentCommand = parentCommand;
    }

    public boolean isAuthRequired() {
        return authRequired;
    }

    public void setAuthRequired(boolean authRequired) {
        this.authRequired = authRequired;
    }

    /**
     * This function will validate the parsed command arguments, then turn them into a map
     * This map will then be assigned to the handler's argsMap field that can be used inside the handler.
     * It will then call the handler's run method to execute the command with the provided arguments.
     *
     * @param parsedCommand the parsed command containing arguments
     */
    public void execute(CommandParser.ParsedCommand parsedCommand) {
        ListOfKVs<String, String> argsMap = CommandParser.validateAndGenerateArgsMap(
                parsedCommand,
                this.getArgs(),
                String.join(".", getFullPath())
            );

        if (getHandler() == null) {
            throw new IllegalStateException("Handler is not set for command: " + getName());
        }

        getHandler().argsMap = argsMap;
        getHandler().run();
    }

    public boolean isDisabled() {
        if (isAuthRequired()) {
            UserManager userManager = GlobalManager.getInstance().getUserManager();

            return !userManager.isLoggedIn;
        }

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
        if (!isHasSubCommands()) {
            throw new CommandError(getFullPath(), "Command " + this.getName() + " does not have sub-commands.");
        }

        for (CommandInstance subCommand : getSubCommands()) {
            if (subCommand.getName().equals(name)) {
                if (subCommand.isDisabled()) {
                    throw new CommandError(
                        subCommand.getFullPath(),
                        "Command is not accessible: " + subCommand.getName()
                    );
                }

                return subCommand;
            }
        }

        throw new CommandError(
            getFullPath(),
            "Sub-command '" + name + "' not found in command: " + this.getName()
        );
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
            path.add(0, currentCommand.getName());
            currentCommand = currentCommand.getParentCommand();
        }

        return path;
    }

    /**
     * Abstract class representing a handler for a command.
     * It contains a map of arguments and an abstract run method that must be implemented by subclasses.
     */
    public abstract static class Handler {
        protected ListOfKVs<String, String> argsMap;

        public abstract void run();
    }

    @Override
    public String toString() {
        return "CommandInstance{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", example='" + example + '\'' +
                ", args=" + args +
                ", hasSubCommands=" + hasSubCommands +
                ", subCommands=" + subCommands.length +
                ", handler=" + (handler != null ? handler.getClass().getSimpleName() : "null") +
                ", authRequired=" + authRequired +
                '}';
    }
}
