package features.auth.handlers;

import core.cli.commands.CommandInstance;
import core.utils.ContextManager;
import features.auth.AuthContext;

/**
 * LoginHandler handles the login command and argument validation.
 */
public class LoginHandler extends CommandInstance.Handler {
    /**
     * Handles the login process using the parsed command.
     */
    @Override
    public void run() {
        AuthContext authContext = ContextManager.getInstance().getAuthContext();
        System.out.println(authContext.getUsername());

        String username = argsMap.get("username");
        String password = argsMap.get("password");
    }
}
