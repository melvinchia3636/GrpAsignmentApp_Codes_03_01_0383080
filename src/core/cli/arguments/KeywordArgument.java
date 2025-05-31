package core.cli.arguments;

/**
 * KeywordArgument represents a keyword (flag or option) argument for a command.
 */
public class KeywordArgument {
    private final String name;
    private final String abbreviation;
    private final String description;
    private final ArgumentDataType dataType;
    private final boolean required;

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

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getDescription() {
        return description;
    }

    public ArgumentDataType getDataType() {
        return dataType;
    }

    public boolean isRequired() {
        return required;
    }
}
