package core.terminal;


/**
 * Chalk is a utility class for formatting strings with ANSI color codes.
 * It allows chaining methods to apply multiple styles to a string.
 */
public class Chalk {
    // ANSI color code constants
    private static final String ANSI_RESET = "\u001B[0m";

    private static final String ANSI_BOLD = "\u001B[1m";
    private static final String ANSI_UNDERLINE = "\u001B[4m";

    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private String string;

    public Chalk(String string) {
        this.string = string;
    }

    public Chalk bold() {
        this.string = ANSI_BOLD + this.string + ANSI_RESET;
        return this;
    }

    public Chalk underline() {
        this.string = ANSI_UNDERLINE + this.string + ANSI_RESET;
        return this;
    }

    public Chalk red() {
        this.string = ANSI_RED + this.string + ANSI_RESET;
        return this;
    }

    public Chalk green() {
        this.string = ANSI_GREEN + this.string + ANSI_RESET;
        return this;
    }

    public Chalk yellow() {
        this.string = ANSI_YELLOW + this.string + ANSI_RESET;
        return this;
    }

    public Chalk blue() {
        this.string = ANSI_BLUE + this.string + ANSI_RESET;
        return this;
    }

    public Chalk purple() {
        this.string = ANSI_PURPLE + this.string + ANSI_RESET;
        return this;
    }

    public Chalk cyan() {
        this.string = ANSI_CYAN + this.string + ANSI_RESET;
        return this;
    }

    public Chalk black() {
        this.string = ANSI_BLACK + this.string + ANSI_RESET;
        return this;
    }

    public Chalk white() {
        this.string = ANSI_WHITE + this.string + ANSI_RESET;
        return this;
    }

    public String toString() {
        return this.string;
    }
}
