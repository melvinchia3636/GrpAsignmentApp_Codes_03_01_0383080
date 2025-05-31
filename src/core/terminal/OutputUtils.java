package core.terminal;

public class OutputUtils {
    public static void printHeader() {
        System.out.println("\n" +
                new Chalk("__________").bold().green().toString() + "                  ______________________\n" +
                new Chalk("___  ____/___________").bold().green().toString() + "       __  ____/__  /____  _/\n" +
                new Chalk("__  __/  _  ___/  __ \\").bold().green().toString() + "_______  /    __  /  __  /  \n" +
                new Chalk("_  /___  / /__ / /_/ /").bold().green().toString() + "/_____/ /___  _  /____/ /   \n" +
                new Chalk("/_____/  \\___/ \\____/").bold().green().toString() + "       \\____/  /_____/___/   \n");
        System.out.println(("Welcome to the " + new Chalk("Eco-CLI").bold().green() + ", your comprehensive personal climate console!"));
        System.out.println(new Chalk("Type 'help' for detailed usage.").yellow().toString());

        // Check if user is using a real terminal
        // IDE-integrated consoles often has issues with ANSI escape codes and other terminal features
        if (System.console() == null) {
            System.out.println(new Chalk("\nWarning: This is not a real terminal.\n" +
                    "Using the system in emulator like the one in IntellJ Idea is not recommended. \n" +
                    "Please use a terminal for the best experience.").red().toString());
        }
    }

    public static void error(String message) {
        System.err.println(new Chalk(message).red().toString());
        System.out.println(new Chalk("Type 'help' for detailed usage.").yellow().toString());
    }
}
