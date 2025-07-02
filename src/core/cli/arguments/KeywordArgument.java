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

    /**
     * Gets the abbreviation (short form) of this keyword argument.
     *
     * @return the argument abbreviation
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Checks if this keyword argument is required.
     *
     * @return true if the argument is required, false otherwise
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Returns a string representation of this keyword argument.
     *
     * @return a string containing all properties of the keyword argument
     */
    @Override
    public String toString() {
        return "KeywordArgument{" +
                "name='" + getName() + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", description='" + getDescription() + '\'' +
                ", dataType=" + getDataType() +
                ", required=" + required +
                '}';
    }
}
