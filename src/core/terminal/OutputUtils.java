package core.terminal;

import core.manager.GlobalManager;
import features.auth.data.UserManager;

/**
 * Collection of utility methods for outputting stuff to the terminal.
 */
public class OutputUtils {
    /**
     * Prints the header of the application to the terminal.
     * This includes the logo and a welcome message.
     */
    public static void printHeader() {
        UserManager userManager = GlobalManager.getInstance().getUserManager();

        System.out.println("\n" +
                new Chalk("__________").bold().green() + "                  ______________________\n" +
                new Chalk("___  ____/___________").bold().green() + "       __  ____/__  /____  _/\n" +
                new Chalk("__  __/  _  ___/  __ \\").bold().green() + "_______  /    __  /  __  /  \n" +
                new Chalk("_  /___  / /__ / /_/ /").bold().green() + "/_____/ /___  _  /____/ /   \n" +
                new Chalk("/_____/  \\___/ \\____/").bold().green() + "       \\____/  /_____/___/   \n");
        System.out.println(("Welcome to the "
                + new Chalk("🌿Eco-CLI").bold().green()
                + ", your comprehensive personal climate console!")
        );
        if (userManager.isLoggedIn) {
            System.out.println("Logged in as: " + new Chalk(userManager.getUsername()).bold().cyan());
        } else {
            System.out.println(new Chalk("You are not logged in. Type 'login' or 'signup' to get started.").bold().yellow());
        }
        System.out.println(new Chalk("\nType 'help' for detailed usage.").yellow());

        // Check if user is using a real terminal
        // IDE-integrated consoles often has issues with ANSI escape codes and other terminal features
        if (System.console() == null) {
            System.out.println(new Chalk("\nWarning: This is not a real terminal.\n" +
                    "Using the system in emulator like the one in IntellJ Idea is not recommended. \n" +
                    "Please use a terminal for the best experience.").red());
        }
    }

    public static void printSuccess(String message) {
        System.out.println(new Chalk("✔ ").green() + message);
    }

    public static void printError(String message) {
        printError(message, true, null);
    }

    public static void printError(String message, boolean printHelpString) {
        printError(message, printHelpString, null);
    }

    public static void printError(String message, String commandName) {
        printError(message, true, commandName);
    }

    public static void printError(String message, boolean printHelpString, String commandName) {
        System.err.println(new Chalk("✘ " + message).red());

        if (!printHelpString) return;

        if (commandName == null || commandName.isEmpty()) {
            System.out.println(new Chalk("Type 'help' for detailed usage.").yellow());
        } else {
            System.out.println(new Chalk("Type 'help -c " + commandName + "' for detailed usage.").yellow());
        }
    }
}
