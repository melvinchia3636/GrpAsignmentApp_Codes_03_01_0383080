package cli.arguments;

/**
 * ArgumentDataType defines the supported data types for command-line arguments.
 */
public enum ArgumentDataType {
    STRING("string"),
    INTEGER("integer"),
    FLOAT("float"),
    FLAG("flag");

    private final String type;

    ArgumentDataType(String type) {
        this.type = type;
    }

    /**
     * Returns the string representation of the data type.
     *
     * @return the type as a string
     */
    public String getType() {
        return type;
    }

    /**
     * Checks if the given value doesn't match this data type.
     *
     * @param value the value to check
     * @return true if the value is invalid for this data type, false otherwise
     */
    public boolean isInvalid(String value) {
        switch (this) {
            case STRING:
                // String should be non-null and non-empty string
                return value == null || value.isEmpty();
            case INTEGER:
                // Integer should only be digits
                return !value.matches("^\\d+$");
            case FLOAT:
                // Float should be digits with a decimal point
                return !value.matches("^\\d+\\.\\d+$");
            case FLAG:
                // flag Should be null
                return value != null;
            default:
                return true;
        }
    }
}
