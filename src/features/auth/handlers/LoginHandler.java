package features.auth.handlers;

import core.cli.commands.CommandInstance;

/**
 * LoginHandler handles the login command and argument validation.
 */
public class LoginHandler extends CommandInstance.Handler {
    /**
     * Handles the login process using the parsed command.
     */
    @Override
    public void run() {
        String username = argsMap.get("username");
        String password = argsMap.get("password");
    }
}
