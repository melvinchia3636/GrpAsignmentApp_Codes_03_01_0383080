package core.cli.arguments;

import core.terminal.Chalk;

/**
 * PositionalArgument represents a positional argument for a command.
 */
public class PositionalArgument extends Argument {
    private final String promptText;

    /**
     * Constructs a PositionalArgument with the given name, description, data type, and prompt text.
     *
     * @param name        the name of the argument
     * @param description a brief description of the argument
     * @param dataType    the expected data type (cannot be FLAG)
     * @param promptText  the text to display when prompting for this argument
     * @throws IllegalArgumentException if dataType is FLAG (not allowed for positional arguments)
     */
    public PositionalArgument(String name, String description, ArgumentDataType dataType, String promptText) {
        super(name, description, dataType);

        if (dataType == ArgumentDataType.FLAG) {
            throw new IllegalArgumentException("Positional arguments cannot be of type FLAG");
        }

        this.promptText = promptText != null ? promptText : "Please enter a value for " + new Chalk(name).blue().bold() + ":";
    }

    /**
     * Constructs a PositionalArgument with default prompt text.
     *
     * @param name        the name of the argument
     * @param description a brief description of the argument
     * @param dataType    the expected data type (cannot be FLAG)
     */
    public PositionalArgument(String name, String description, ArgumentDataType dataType) {
        this(name, description, dataType, null);
    }

    /**
     * Gets the prompt text displayed when requesting this argument.
     *
     * @return the prompt text
     */
    public String getPromptText() {
        return promptText;
    }

    /**
     * Returns a string representation of this positional argument.
     *
     * @return a string containing all properties of the positional argument
     */
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
