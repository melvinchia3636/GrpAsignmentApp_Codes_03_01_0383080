package core.cli.commands;

import java.util.ArrayList;

/**
 * CommandError is a custom exception that requires a command path to be printed to the help string
 * that will be displayed after the error message: "Type 'help -c &lt;command&gt;' for more information".
 */
public class CommandError extends RuntimeException {
    private final String commandPath;
    private final String errorMessage;

    /**
     * Constructs a CommandError with the specified command name and error message.
     *
     * @param commandPath  the name of the command that caused the error
     * @param errorMessage the error message describing the issue
     */
    public CommandError(String commandPath, String errorMessage) {
        super(errorMessage);

        this.commandPath = commandPath;
        this.errorMessage = errorMessage;
    }

    /**
     * Constructs a CommandError with the specified command path and error message.
     *
     * @param commandPath  the path of the command that caused the error
     * @param errorMessage the error message describing the issue
     */
    public CommandError(ArrayList<String> commandPath, String errorMessage) {
        this(String.join(".", commandPath), errorMessage);
    }

    /**
     * Returns the full command path that caused the error.
     *
     * @return the command path as a string
     */
    public String getCommand() {
        return String.join(".", commandPath);
    }

    /**
     * Returns the error message associated with this CommandError.
     *
     * @return the error message
     */
    @Override
    public String getMessage() {
        return errorMessage;
    }
}
