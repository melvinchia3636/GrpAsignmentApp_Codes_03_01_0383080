package core.cli.arguments;

/**
 * Represents a command-line argument with its name, description, and data type.
 * This is the base class for all types of arguments in the CLI system.
 */
public class Argument {
    private final String name;
    private final String description;
    private final ArgumentDataType dataType;

    /**
     * Constructs a new Argument with the specified name, description, and data type.
     *
     * @param name        the name of the argument
     * @param description a description of what the argument represents
     * @param dataType    the expected data type for this argument
     */
    public Argument(String name, String description, ArgumentDataType dataType) {
        this.name = name;
        this.description = description;
        this.dataType = dataType;
    }

    /**
     * Gets the name of this argument.
     *
     * @return the argument name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of this argument.
     *
     * @return the argument description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the data type of this argument.
     *
     * @return the argument data type
     */
    public ArgumentDataType getDataType() {
        return dataType;
    }

    /**
     * Returns a string representation of this argument.
     *
     * @return a string containing the argument's name, description, and data type
     */
    @Override
    public String toString() {
        return "Argument{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dataType=" + dataType +
                '}';
    }
}
