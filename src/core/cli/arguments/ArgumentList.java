package core.cli.arguments;

import java.util.ArrayList;

/**
 * ArgumentList holds the definitions for positional and keyword arguments for a command.
 */
public class ArgumentList {
    private final PositionalArgument[] positionalArguments;
    private final KeywordArgument[] keywordArguments;

    /**
     * Constructs an ArgumentList with the given arguments.
     * Automatically categorizes the arguments into positional and keyword arguments.
     *
     * @param arguments an array of arrays of KeywordArgument or PositionalArgument objects
     */
    public ArgumentList(Argument... arguments) {
        ArrayList<PositionalArgument> positionalArgs = new ArrayList<>();
        ArrayList<KeywordArgument> keywordArgs = new ArrayList<>();

        for (Argument arg : arguments) {
            if (arg instanceof PositionalArgument) {
                positionalArgs.add((PositionalArgument) arg);
            } else if (arg instanceof KeywordArgument) {
                keywordArgs.add((KeywordArgument) arg);
            }
        }

        this.positionalArguments = positionalArgs.toArray(new PositionalArgument[0]);
        this.keywordArguments = keywordArgs.toArray(new KeywordArgument[0]);
    }

    /**
     * Returns the maximum length of the names of positional arguments defined in this ArgumentList.
     *
     * @return the maximum length of positional argument names
     */
    public int getMaxPositionalArgumentNameLength() {
        int maxLength = 0;
        for (PositionalArgument arg : getPositionalArguments()) {
            if (arg.getName().length() > maxLength) {
                maxLength = arg.getName().length();
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
        for (KeywordArgument arg : getKeywordArguments()) {
            if (arg.getName().length() > maxLength) {
                maxLength = arg.getName().length();
            }
        }
        return maxLength;
    }

    /**
     * Gets all positional arguments in this argument list.
     *
     * @return an array of positional arguments
     */
    public PositionalArgument[] getPositionalArguments() {
        return positionalArguments;
    }

    /**
     * Gets all keyword arguments in this argument list.
     *
     * @return an array of keyword arguments
     */
    public KeywordArgument[] getKeywordArguments() {
        return keywordArguments;
    }

    /**
     * Returns a string representation of this argument list.
     *
     * @return a string containing all positional and keyword arguments
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ArgumentList{");
        sb.append("positionalArguments=[");
        for (PositionalArgument arg : positionalArguments) {
            sb.append(arg.toString()).append(", ");
        }
        sb.append("], keywordArguments=[");
        for (KeywordArgument arg : keywordArguments) {
            sb.append(arg.toString()).append(", ");
        }
        sb.append("]}");
        return sb.toString();
    }
}
