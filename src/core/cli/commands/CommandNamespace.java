package core.cli.commands;

/**
 * Represents a namespace that groups related commands together.
 * A namespace contains a collection of commands that share similar functionality or purpose.
 */
public class CommandNamespace {
    private final String name;
    private final CommandInstance[] commands;

    /**
     * Constructs a new CommandNamespace with the specified name and commands.
     *
     * @param name     the name of the namespace
     * @param commands the array of commands in this namespace
     */
    public CommandNamespace(String name, CommandInstance[] commands) {
        this.name = name;
        this.commands = commands;
    }

    /**
     * Checks if all commands in this namespace are disabled.
     *
     * @return true if all commands are disabled, false otherwise
     */
    public boolean isDisabled() {
        for (CommandInstance command : getCommands()) {
            if (!command.isDisabled()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the name of this namespace.
     *
     * @return the namespace name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets all commands in this namespace.
     *
     * @return an array of command instances
     */
    public CommandInstance[] getCommands() {
        return commands;
    }

    /**
     * Returns a string representation of this command namespace.
     *
     * @return a string containing the namespace name and all its commands
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CommandNamespace{")
          .append("name='").append(name).append('\'')
          .append(", commands=[");
        for (CommandInstance command : commands) {
            sb.append(command.toString()).append(", ");
        }
        if (commands.length > 0) {
            sb.setLength(sb.length() - 2); // Remove trailing comma and space
        }
        sb.append("]}");
        return sb.toString();
    }
}
