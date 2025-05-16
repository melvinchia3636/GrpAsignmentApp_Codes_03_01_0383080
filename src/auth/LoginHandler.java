package auth;

import cli.Argument;
import cli.ArgumentDataType;
import cli.CommandParser;
import utils.SimpleMap;

public class LoginHandler {
    private static final Argument[] REQUIRED_ARGS = {
        new Argument("username", "u", ArgumentDataType.STRING),
        new Argument("password", "p", ArgumentDataType.STRING)
    };

    public static void login(String[][] args) {
        SimpleMap<String, String> argsMap = CommandParser.validateAndGenerateArgsMap(args, REQUIRED_ARGS);

        String username = argsMap.get("username");
        String password = argsMap.get("password");
    }
}
