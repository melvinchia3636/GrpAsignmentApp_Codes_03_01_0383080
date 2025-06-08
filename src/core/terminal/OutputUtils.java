package core.terminal;

/**
 * Collection of utility methods for outputting stuff to the terminal.
 */
public class OutputUtils {
    /**
     * Prints the header of the application to the terminal.
     * This includes the logo and a welcome message.
     */
    public static void printHeader() {
        System.out.println("\n" +
                new Chalk("__________").bold().green() + "                  ______________________\n" +
                new Chalk("___  ____/___________").bold().green() + "       __  ____/__  /____  _/\n" +
                new Chalk("__  __/  _  ___/  __ \\").bold().green() + "_______  /    __  /  __  /  \n" +
                new Chalk("_  /___  / /__ / /_/ /").bold().green() + "/_____/ /___  _  /____/ /   \n" +
                new Chalk("/_____/  \\___/ \\____/").bold().green() + "       \\____/  /_____/___/   \n");
        System.out.println(("Welcome to the "
                + new Chalk("Eco-CLI").bold().green()
                + ", your comprehensive personal climate console!")
        );
        System.out.println(new Chalk("Type 'help' for detailed usage.").yellow());

        // Check if user is using a real terminal
        // IDE-integrated consoles often has issues with ANSI escape codes and other terminal features
        if (System.console() == null) {
            System.out.println(new Chalk("\nWarning: This is not a real terminal.\n" +
                    "Using the system in emulator like the one in IntellJ Idea is not recommended. \n" +
                    "Please use a terminal for the best experience.").red());
        }
    }

    /**
     * Prints an error message to the terminal in red.
     * Also suggests typing 'help' for detailed usage.
     *
     * @param message The error message to print.
     */
    public static void printError(String message) {
        System.err.println(new Chalk(message).red());
        System.out.println(new Chalk("Type 'help' for detailed usage.").yellow());
    }

    /**
     * Prints an error message to the terminal in red.
     * Provide an option to disable the help string suggestion.
     *
     * @param message         The error message to print.
     * @param printHelpString Whether to print the help string or not.
     */
    public static void printError(String message, boolean printHelpString) {
        System.err.println(new Chalk(message).red());
        if (printHelpString) {
            System.out.println(new Chalk("Type 'help' for detailed usage.").yellow());
        }
    }

    public static void printError(String message, String commandName) {
        System.err.println(new Chalk(message).red());

        if (commandName == null || commandName.isEmpty()) {
            System.out.println(new Chalk("Type 'help' for detailed usage.").yellow());
            return;
        }

        System.out.println(new Chalk("Type 'help -c " + commandName + "' for detailed usage.").yellow());
    }
}
