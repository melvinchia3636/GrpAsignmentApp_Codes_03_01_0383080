package core.cli.arguments;

import java.util.Objects;

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
    private String[] options = null;

    private ArgumentDataType(String type) {
        this.type = type;
    }

    public ArgumentDataType(String type, String[] options) {
        if (!type.equals("enum")) {
            throw new IllegalArgumentException("Only 'enum' type can have options");
        }

        this.type = type;
        this.options = options;
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
            return value == null || !value.matches("^\\d+(\\.\\d+)?$");
        } else if (this == FLAG) {
            // flag Should be null
            return value != null;
        } else if (this.getType().equals("enum")) {
            // Enum should be one of the defined options
            if (getOptions() == null || getOptions().length == 0) {
                throw new IllegalStateException("Enum type must have options defined");
            }
            for (String option : getOptions()) {
                if (Objects.equals(option, value)) {
                    return false; // Valid option found
                }
            }
            return true; // No valid option found
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

    public String getType() {
        return type;
    }

    public String[] getOptions() {
        return options;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ArgumentDataType{");
        sb.append("type='").append(type).append('\'');
        if (options != null) {
            sb.append(", options=[");
            for (int i = 0; i < options.length; i++) {
                sb.append(options[i]);
                if (i < options.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append(']');
        }
        sb.append('}');
        return sb.toString();
    }
}
