package terminal;

public class AnsiColorUtils {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    public static String formatStringWithColor(String string, String color) {
        String targetColor = "";

        switch (color) {
            case "black":
                targetColor = ANSI_BLACK;
                break;
            case "red":
                targetColor = ANSI_RED;
                break;
            case "green":
                targetColor = ANSI_GREEN;
                break;
            case "yellow":
                targetColor = ANSI_YELLOW;
                break;
            case "blue":
                targetColor = ANSI_BLUE;
                break;
            case "purple":
                targetColor = ANSI_PURPLE;
                break;
            case "cyan":
                targetColor = ANSI_CYAN;
                break;
            case "white":
                targetColor = ANSI_WHITE;
                break;
            default:
                throw new IllegalArgumentException("Invalid color: " + color);
        }

        return targetColor + string + ANSI_RESET;
    }
}
