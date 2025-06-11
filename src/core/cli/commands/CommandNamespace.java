package core.cli.commands;

public class CommandNamespace {
    public final String name;
    public final CommandInstance[] commands;

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
        for (CommandInstance command : commands) {
            if (!command.isDisabled()) {
                return false;
            }
        }
        return true;
    }
}
