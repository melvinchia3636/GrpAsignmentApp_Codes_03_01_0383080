package core.cli.arguments;

import core.terminal.Chalk;

/**
 * PositionalArgument represents a positional argument for a command.
 */
public class PositionalArgument {
    public final String name;
    public final String description;
    public final ArgumentDataType dataType;
    public final String promptText;

    /**
     * Constructs a PositionalArgument with the given name and data type.
     *
     * @param name the name of the argument
     * @param description a brief description of the argument
     * @param dataType the expected data type
     */
    public PositionalArgument(String name, String description, ArgumentDataType dataType, String promptText) {
        if (dataType == ArgumentDataType.FLAG) {
            throw new IllegalArgumentException("Positional arguments cannot be of type FLAG");
        }

        this.name = name;
        this.description = description;
        this.dataType = dataType;
        this.promptText = promptText != null ? promptText : "Please enter a value for " + new Chalk(name).blue().bold() + ":";
    }

    public PositionalArgument(String name, String description, ArgumentDataType dataType) {
        this(name, description, dataType, null);
    }
}
