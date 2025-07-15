package core.terminal;

import core.manager.GlobalManager;
import features.auth.data.UserManager;

/**
 * Collection of utility methods for outputting stuff to the terminal.
 */
public class OutputUtils {
    // Standardized styling constants
    private static final String SEPARATOR_HEAVY = "═══════════════════════════════════════";
    private static final String SEPARATOR_LIGHT = "───────────────────────────────────────";

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
                + new Chalk("Eco-CLI").bold().green()
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
        System.out.println(new Chalk("[SUCCESS] ").green() + message);
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
        System.err.println(new Chalk("[ERROR] " + message).red());

        if (!printHelpString) return;

        if (commandName == null || commandName.isEmpty()) {
            System.out.println(new Chalk("Type 'help' for detailed usage.").yellow());
        } else {
            System.out.println(new Chalk("Type 'help -c " + commandName + "' for detailed usage.").yellow());
        }
    }

    // Enhanced styling methods for consistent formatting

    /**
     * Prints a section header with emoji and heavy separator
     */
    public static void printSectionHeader(String title) {
        System.out.println("\n" + new Chalk(title).bold());
        System.out.println(SEPARATOR_HEAVY);
    }

    /**
     * Prints a subsection header with light separator
     */
    public static void printSubsectionHeader(String title) {
        System.out.println("\n" +  new Chalk(title).bold());
        System.out.println(SEPARATOR_LIGHT);
    }

    /**
     * Prints a data row with consistent formatting
     */
    public static void printDataRow(String label, String value) {
        System.out.printf("  %-25s %s%n", label + ":", value);
    }

    /**
     * Prints a data row with colored value
     */
    public static void printDataRow(String label, Chalk valueWithColor) {
        System.out.printf("  %-25s %s%n", label + ":", valueWithColor.toString());
    }

    /**
     * Prints a status with appropriate color coding
     */
    public static void printStatus(String status, String description) {
        Chalk statusColor;
        
        switch (status.toLowerCase()) {
            case "completed":
                statusColor = new Chalk(status).green().bold();
                break;
            case "skipped":
                statusColor = new Chalk(status).yellow().bold();
                break;
            case "failed":
            case "missed":
                statusColor = new Chalk(status).red().bold();
                break;
            case "in-progress":
            case "active":
                statusColor = new Chalk(status).blue().bold();
                break;
            default:
                statusColor = new Chalk(status).white().bold();
        }
        
        System.out.println(statusColor + " - " + description);
    }

    /**
     * Prints a statistic with icon and colored value
     */
    public static void printStatistic(String label, String value, String color) {
        Chalk coloredValue;
        switch (color.toLowerCase()) {
            case "green":
                coloredValue = new Chalk(value).green().bold();
                break;
            case "yellow":
                coloredValue = new Chalk(value).yellow().bold();
                break;
            case "red":
                coloredValue = new Chalk(value).red().bold();
                break;
            case "blue":
                coloredValue = new Chalk(value).blue().bold();
                break;
            case "cyan":
                coloredValue = new Chalk(value).cyan().bold();
                break;
            case "purple":
                coloredValue = new Chalk(value).purple().bold();
                break;
            default:
                coloredValue = new Chalk(value).white().bold();
        }
        System.out.println(label + ": " + coloredValue);
    }

    /**
     * Prints an info message with standard formatting
     */
    public static void printInfo(String message) {
        System.out.println(new Chalk("[INFO] " + message).blue());
    }

    /**
     * Prints a warning message with standard formatting
     */
    public static void printWarning(String message) {
        System.out.println(new Chalk("[WARNING] " + message).yellow());
    }

    /**
     * Prints a tip/suggestion with standard formatting
     */
    public static void printTip(String message) {
        System.out.println(new Chalk("[TIP] " + message).cyan());
    }

    /**
     * Prints an encouragement message with standard formatting
     */
    public static void printEncouragement(String message) {
        System.out.println(new Chalk(message).purple());
    }

    /**
     * Prints a chart bar for data visualization
     */
    public static void printChartBar(String label, double value, double maxValue, int width) {
        int barLength = (int) Math.round((value / maxValue) * width);
        
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < barLength; i++) {
            bar.append("O");
        }
        for (int i = barLength; i < width; i++) {
            bar.append(" ");
        }
        
        System.out.printf("%-8s | %s %.2f%n", label, 
            new Chalk(bar.toString()).green(), value);
    }

    /**
     * Prints a summary box with statistics
     */
    public static void printSummaryBox(String title, String[][] stats) {
        printSubsectionHeader(title);
        for (String[] stat : stats) {
            if (stat.length >= 2) {
                String color = stat.length > 2 ? stat[2] : "white";
                printStatistic(stat[0], stat[1], color);
            }
        }
    }

    /**
     * Prints a closing message with spacing
     */
    public static void printClosingMessage(String message) {
        System.out.println("\n" + new Chalk(message).green() + "\n");
    }
}
