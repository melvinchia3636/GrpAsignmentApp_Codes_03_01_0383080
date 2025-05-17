package auth;

import cli.CommandParser;
import cli.CommandParser.*;
import cli.arguments.ArgumentDataType;
import cli.arguments.ArgumentList;
import cli.arguments.KeywordArgument;
import cli.arguments.PositionalArgument;
import utils.SimpleMap;

/**
 * LoginHandler handles the login command and argument validation.
 */
public class LoginHandler {
    // Allowed arguments for the login command
    private static final ArgumentList ALLOWED_ARGS = new ArgumentList(
            new PositionalArgument[]{
                    new PositionalArgument("username", ArgumentDataType.STRING),
                    new PositionalArgument("password", ArgumentDataType.STRING),
            },
            new KeywordArgument[]{
                    new KeywordArgument("colored", "c", ArgumentDataType.FLAG, false),
            }
    );

    /**
     * Handles the login process using the parsed command.
     *
     * @param parsedCommand the parsed command containing arguments
     */
    public static void login(ParsedCommand parsedCommand) {
        // Validate and extract arguments
        SimpleMap<String, String> argsMap = CommandParser.validateAndGenerateArgsMap(parsedCommand, ALLOWED_ARGS);

        String username = argsMap.get("username");
        String password = argsMap.get("password");
    }
}
