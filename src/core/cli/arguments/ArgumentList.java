package core.cli.arguments;

/**
 * ArgumentList holds the definitions for positional and keyword arguments for a command.
 */
public class ArgumentList {
    public final PositionalArgument[] positionalArguments;
    public final KeywordArgument[] keywordArguments;

    /**
     * Constructs an ArgumentList with the given positional and keyword arguments.
     *
     * @param positionalArguments array of positional argument definitions
     * @param keywordArguments array of keyword argument definitions
     */
    public ArgumentList(PositionalArgument[] positionalArguments, KeywordArgument[] keywordArguments) {
        this.positionalArguments = positionalArguments;
        this.keywordArguments = keywordArguments;
    }

    /**
     * Constructs an ArgumentList with only positional arguments.
     *
     * @param positionalArguments array of positional argument definitions
     */
    public ArgumentList(PositionalArgument[] positionalArguments) {
        this.positionalArguments = positionalArguments;
        this.keywordArguments = new KeywordArgument[0]; // Initialize with empty keyword arguments
    }

    /**
     * Constructs an ArgumentList with only keyword arguments.
     *
     * @param keywordArguments array of keyword argument definitions
     */
    public ArgumentList(KeywordArgument[] keywordArguments) {
        this.keywordArguments = keywordArguments;
        this.positionalArguments = new PositionalArgument[0]; // Initialize with empty positional arguments
    }

    /**
     * Returns the maximum length of the names of positional arguments defined in this ArgumentList.
     *
     * @return the maximum length of positional argument names
     */
    public int getMaxPositionalArgumentNameLength() {
        int maxLength = 0;
        for (PositionalArgument arg : positionalArguments) {
            if (arg.name.length() > maxLength) {
                maxLength = arg.name.length();
            }
        }
        return maxLength;
    }

    /**
     * Returns the maximum length of the names of keyword arguments defined in this ArgumentList.
     *
     * @return the maximum length of keyword argument names
     */
    public int getMaxKeywordArgumentNameLength() {
        int maxLength = 0;
        for (KeywordArgument arg : keywordArguments) {
            if (arg.name.length() > maxLength) {
                maxLength = arg.name.length();
            }
        }
        return maxLength;
    }
}
