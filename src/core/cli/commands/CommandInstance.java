package core.cli.commands;

import core.cli.arguments.ArgumentList;
import core.cli.arguments.KeywordArgument;
import core.cli.arguments.PositionalArgument;
import core.utils.SimpleMap;

/**
 * This Command class represents a command that can be executed with a specific handler.
 * It includes the command name, a description, a list of arguments, and a handler that processes the command.
 */
public class CommandInstance {
    public final String name;
    public final String description;
    public final ArgumentList args;
    public final Handler handler;

    /**
     * Constructs a Command with the given command name, argument list, and handler.
     *
     * @param name        the name of the command
     * @param description a brief description of the command
     * @param args        the list of arguments for the command
     * @param handler     the handler that will process the command
     */
    public CommandInstance(String name, String description, ArgumentList args, Handler handler) {
        this.name = name;
        this.description = description;
        this.args = args;
        this.handler = handler;
    }

    /**
     * Constructs a Command with the given command name and handler, without any arguments.
     *
     * @param name        the name of the command
     * @param description a brief description of the command
     * @param handler     the handler that will process the command
     */
    public CommandInstance(String name, String description, Handler handler) {
        this.name = name;
        this.description = description;
        this.args = new ArgumentList(new PositionalArgument[0], new KeywordArgument[0]);
        this.handler = handler;
    }

    /**
     * This function will validate the parsed command arguments, then turn them into a map
     * This map will then be assigned to the handler's argsMap field that can be used inside the handler.
     * It will then call the handler's run method to execute the command with the provided arguments.
     *
     * @param parsedCommand the parsed command containing arguments
     */
    public void execute(CommandParser.ParsedCommand parsedCommand) {
        SimpleMap<String, String> argsMap = CommandParser.validateAndGenerateArgsMap(parsedCommand, this.args);

        // If argsMap is null, it means validation failed or no arguments were provided
        // Since the error handling is done in the validateAndGenerateArgsMap method,
        // we simply return here to avoid executing the handler with invalid arguments.
        if (argsMap == null) {
            return;
        }

        handler.argsMap = argsMap;
        handler.run();
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
