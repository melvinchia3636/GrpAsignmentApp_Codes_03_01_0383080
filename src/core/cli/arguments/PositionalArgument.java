package core.cli.arguments;

/**
 * PositionalArgument represents a positional argument for a command.
 */
public class PositionalArgument {
    private final String name;
    private final String description;
    private final ArgumentDataType dataType;

    /**
     * Constructs a PositionalArgument with the given name and data type.
     *
     * @param name the name of the argument
     * @param description a brief description of the argument
     * @param dataType the expected data type
     */
    public PositionalArgument(String name, String description, ArgumentDataType dataType) {
        if (dataType == ArgumentDataType.FLAG) {
            throw new IllegalArgumentException("Positional arguments cannot be of type FLAG");
        }

        this.name = name;
        this.description = description;
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArgumentDataType getDataType() {
        return dataType;
    }
}
