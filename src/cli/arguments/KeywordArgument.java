package cli.arguments;

/**
 * KeywordArgument represents a keyword (flag or option) argument for a command.
 */
public class KeywordArgument {
    /** The full name of the argument (e.g., "output") */
    public String name;
    /** The abbreviation (e.g., "o" for output) */
    public String abbreviation;
    /** The expected data type of the argument */
    public ArgumentDataType dataType;
    /** Whether this argument is required */
    public boolean required;

    /**
     * Constructs a KeywordArgument with the given properties.
     *
     * @param name the full name of the argument
     * @param abbreviation the abbreviation (single character)
     * @param dataType the expected data type
     * @param required whether the argument is required
     */
    public KeywordArgument(String name, String abbreviation, ArgumentDataType dataType, boolean required) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.dataType = dataType;
        this.required = required;
    }
}
