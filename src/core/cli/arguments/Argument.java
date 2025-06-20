package core.cli.arguments;

public class Argument {
    private final String name;
    private final String description;
    private final ArgumentDataType dataType;

    public Argument(String name, String description, ArgumentDataType dataType) {
        this.name = name;
        this.description = description;
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArgumentDataType getDataType() {
        return dataType;
    }
}
