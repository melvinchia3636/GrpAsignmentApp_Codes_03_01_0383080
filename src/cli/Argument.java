package cli;

public class Argument {
    String name;
    String abbreviation;
    ArgumentDataType dataType;

    public Argument(String name, String abbreviation, ArgumentDataType dataType) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.dataType = dataType;
    }
}
