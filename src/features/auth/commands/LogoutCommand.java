package features.auth.commands;

import core.cli.commands.CommandInstance;
import features.auth.handlers.LogoutHandler;

public class LogoutCommand extends CommandInstance {
    public LogoutCommand() {
        super(
                "logout",
                "Log out of your account",
                "",
                new LogoutHandler()
        );

        this.setAuthRequired(true);
    }
}
