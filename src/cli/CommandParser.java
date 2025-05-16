package cli;

import terminal.AnsiColorUtils;
import utils.SimpleMap;

import java.util.Arrays;

public class CommandParser {
    public static ParsedCommand parseFromRaw(String[] args) {
        ParsedCommand parsedCommandAndArgs = new ParsedCommand();

        if (args.length == 0) {
            System.out.println(AnsiColorUtils.formatStringWithColor("Command is required. Example usage: 'co2cli.jar <command> <arguments>'", "red"));
            System.exit(1);
        }

        parsedCommandAndArgs.command = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);

        if ((args.length % 2) != 0) {
            System.out.println(AnsiColorUtils.formatStringWithColor("Amount of argument name and value doesn't match", "red"));
            System.exit(1);
        }

        int argumentPairCount = args.length / 2;
        String[][] arguments = new String[argumentPairCount][2];

        for (int i = 0; i < argumentPairCount; i++) {
            String argumentName = args[i * 2];
            String argumentValue = args[i * 2 + 1];

            if (!argumentName.startsWith("-")) {
                System.out.println(AnsiColorUtils.formatStringWithColor(
                        "Argument: " + argumentName + " is invalid. Argument should start with a dash ('-')", "red"
                ));
                System.exit(1);
            }

            arguments[i][0] = argumentName;
            arguments[i][1] = argumentValue;
        }

        parsedCommandAndArgs.args = arguments;

        return parsedCommandAndArgs;
    }

    public static SimpleMap<String, String> validateAndGenerateArgsMap(String[][] args, Argument[] requiredArguments) {
        SimpleMap<String, String> argsMap = new SimpleMap<>();

        for (Argument requiredArgument : requiredArguments) {
            String[] targetArg = null;

            for (String[] arg : args) {
                String argName = arg[0];

                boolean fullNameCommandFound = argName.startsWith("--") && argName.equals("--" + requiredArgument.name);
                boolean abbreviatedCommandFound = argName.startsWith("-") && argName.equals("-" + requiredArgument.abbreviation);

                if (fullNameCommandFound || abbreviatedCommandFound) {
                    targetArg = arg;
                    break;
                }
            }

            if (targetArg == null) {
                System.out.println(AnsiColorUtils.formatStringWithColor(
                        "Argument not found: --" + requiredArgument.name + " or -" + requiredArgument.abbreviation, "red"
                ));
                System.exit(1);
            }

            if (!requiredArgument.dataType.isValidType(targetArg[1])) {
                System.out.println(AnsiColorUtils.formatStringWithColor(
                        "Argument: " + targetArg[0] + " has invalid type. Argument type should be: " + requiredArgument.dataType.getType(), "red"
                ));
                System.exit(1);
            }

            argsMap.put(requiredArgument.name, targetArg[1]);
        }

        return argsMap;
    }
}
