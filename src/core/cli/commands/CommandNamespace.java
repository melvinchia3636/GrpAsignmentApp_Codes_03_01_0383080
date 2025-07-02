package core.cli.commands;

public class CommandNamespace {
    private final String name;
    private final CommandInstance[] commands;

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

    public String getName() {
        return name;
    }

    public CommandInstance[] getCommands() {
        return commands;
    }

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
