package app;

import auth.LoginHandler;
import cli.CommandParser;
import cli.ParsedCommand;

public class Main {
    public static void main(String[] args) {
        ParsedCommand parsedCommand = CommandParser.parseFromRaw(args);

        switch (parsedCommand.command) {
            case "login":
                LoginHandler.login(parsedCommand.args);
                break;
        }
    }
}
