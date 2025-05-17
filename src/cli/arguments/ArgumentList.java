package cli.arguments;

/**
 * ArgumentList holds the definitions for positional and keyword arguments for a command.
 */
public class ArgumentList {
    public PositionalArgument[] positionalArguments;
    public KeywordArgument[] keywordArguments;

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
}
