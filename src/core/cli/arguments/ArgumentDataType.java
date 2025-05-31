package core.cli.arguments;

/**
 * ArgumentDataType defines the data types that can be used for command arguments.
 * It includes methods to validate if a given value matches the expected data type.
 * <p>
 * Originally implemented using enums, but switched to a class since the project doesn't allow enums :(
 */
public class ArgumentDataType {
    public static final ArgumentDataType STRING = new ArgumentDataType("string");
    public static final ArgumentDataType INTEGER = new ArgumentDataType("integer");
    public static final ArgumentDataType FLOAT = new ArgumentDataType("float");
    public static final ArgumentDataType FLAG = new ArgumentDataType("flag");

    private final String type;

    private ArgumentDataType(String type) {
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
        if (this == STRING) {
            // String should be non-null and non-empty string
            return value == null || value.isEmpty();
        } else if (this == INTEGER) {
            // Integer should only be digits
            return value == null || !value.matches("^\\d+$");
        } else if (this == FLOAT) {
            // Float should be digits with a decimal point
            return value == null || !value.matches("^\\d+\\.\\d+$");
        } else if (this == FLAG) {
            // flag Should be null
            return value != null;
        } else {
            return true;
        }
    }

    /**
     * Returns the maximum length of the type strings for all defined data types.
     *
     * @return the maximum length of type strings
     */
    public static int getMaxTypeStringLength() {
        return Math.max(Math.max(STRING.getType().length(), INTEGER.getType().length()),
                Math.max(FLOAT.getType().length(), FLAG.getType().length()));
    }
}
