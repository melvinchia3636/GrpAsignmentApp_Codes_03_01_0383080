package core.cli.arguments;

/**
 * ArgumentList holds the definitions for positional and keyword arguments for a command.
 */
public class ArgumentList {
    private PositionalArgument[] positionalArguments = new PositionalArgument[0];
    private KeywordArgument[] keywordArguments = new KeywordArgument[0];

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
    }

    /**
     * Constructs an ArgumentList with only keyword arguments.
     *
     * @param keywordArguments array of keyword argument definitions
     */
    public ArgumentList(KeywordArgument[] keywordArguments) {
        this.keywordArguments = keywordArguments;
    }

    /**
     * Returns the positional arguments defined in this ArgumentList.
     *
     * @return an array of PositionalArgument objects
     */
    public PositionalArgument[] getPositionalArguments() {
        return positionalArguments;
    }

    /**
     * Returns the maximum length of the names of positional arguments defined in this ArgumentList.
     *
     * @return the maximum length of positional argument names
     */
    public int getMaxPositionalArgumentNameLength() {
        int maxLength = 0;
        for (PositionalArgument arg : positionalArguments) {
            if (arg.getName().length() > maxLength) {
                maxLength = arg.getName().length();
            }
        }
        return maxLength;
    }

    /**
     * Returns the keyword arguments defined in this ArgumentList.
     *
     * @return an array of KeywordArgument objects
     */
    public KeywordArgument[] getKeywordArguments() {
        return keywordArguments;
    }

    /**
     * Returns the maximum length of the names of keyword arguments defined in this ArgumentList.
     *
     * @return the maximum length of keyword argument names
     */
    public int getMaxKeywordArgumentNameLength() {
        int maxLength = 0;
        for (KeywordArgument arg : keywordArguments) {
            if (arg.getName().length() > maxLength) {
                maxLength = arg.getName().length();
            }
        }
        return maxLength;
    }
}
