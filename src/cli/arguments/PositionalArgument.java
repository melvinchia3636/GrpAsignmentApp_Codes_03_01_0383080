package cli.arguments;

/**
 * PositionalArgument represents a positional argument for a command.
 */
public class PositionalArgument {
    /** The name of the argument */
    public String name;
    /** The expected data type of the argument */
    public ArgumentDataType dataType;

    /**
     * Constructs a PositionalArgument with the given name and data type.
     *
     * @param name the name of the argument
     * @param dataType the expected data type
     */
    public PositionalArgument(String name, ArgumentDataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }
}
