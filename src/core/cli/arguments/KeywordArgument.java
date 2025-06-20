package core.cli.arguments;

/**
 * KeywordArgument represents a keyword (flag or option) argument for a command.
 */
public class KeywordArgument extends Argument {
    private final String abbreviation;
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
        super(name, description, dataType);

        this.abbreviation = abbreviation;
        this.required = required;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public boolean isRequired() {
        return required;
    }
}
