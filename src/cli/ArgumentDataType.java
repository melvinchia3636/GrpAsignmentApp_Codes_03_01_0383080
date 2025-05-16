package cli;

public enum ArgumentDataType {
    STRING("string"),
    INTEGER("integer"),
    FLOAT("float"),
    BOOLEAN("boolean");

    private final String type;

    ArgumentDataType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public boolean isValidType(String value) {
        switch (this) {
            case STRING:
                return true;
            case INTEGER:
                return value.matches("^\\d+$");
            case FLOAT:
                return value.matches("^\\d+\\.\\d+$");
            case BOOLEAN:
                return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
            default:
                return false;
        }
    }
}
