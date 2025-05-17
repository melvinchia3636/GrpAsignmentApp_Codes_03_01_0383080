package terminal;

public class OutputUtils {
    public static void error(String message) {
        System.err.println(AnsiColorUtils.formatStringWithColor(message, "red"));
        System.out.println(AnsiColorUtils.formatStringWithColor("Type 'co2cli.jar help' for detailed usage.", "yellow"));
    }
}
