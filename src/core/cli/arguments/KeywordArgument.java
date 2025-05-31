package core.cli.arguments;

/**
 * KeywordArgument represents a keyword (flag or option) argument for a command.
 */
public class KeywordArgument {
    public final String name;
    public final String abbreviation;
    public final String description;
    public final ArgumentDataType dataType;
    public final boolean required;

    /**
     * Constructs a KeywordArgument with the given properties.
     *
     * @param name the full name of the argument
     * @param abbreviation the abbreviation (single character)
     * @param description a brief description of the argument
     * @param dataType the expected data type
     * @param required whether the argument is required
     */
    public KeywordArgument(String name, String abbreviation, String description, ArgumentDataType dataType, boolean required) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.description = description;
        this.dataType = dataType;
        this.required = required;
    }
}
