package core.cli.arguments;

import core.terminal.Chalk;

/**
 * PositionalArgument represents a positional argument for a command.
 */
public class PositionalArgument extends Argument {
    private final String promptText;

    /**
     * Constructs a PositionalArgument with the given name and data type.
     *
     * @param name the name of the argument
     * @param description a brief description of the argument
     * @param dataType the expected data type
     */
    public PositionalArgument(String name, String description, ArgumentDataType dataType, String promptText) {
        super(name, description, dataType);

        if (dataType == ArgumentDataType.FLAG) {
            throw new IllegalArgumentException("Positional arguments cannot be of type FLAG");
        }

        this.promptText = promptText != null ? promptText : "Please enter a value for " + new Chalk(name).blue().bold() + ":";
    }

    public PositionalArgument(String name, String description, ArgumentDataType dataType) {
        this(name, description, dataType, null);
    }

    public String getPromptText() {
        return promptText;
    }

    @Override
    public String toString() {
        return "PositionalArgument{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", dataType=" + getDataType() +
                ", promptText='" + promptText + '\'' +
                '}' + "\n";
    }
}
