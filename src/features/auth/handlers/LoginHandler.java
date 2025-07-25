package features.auth.handlers;

import core.cli.commands.CommandInstance;
import core.io.IOManager;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.auth.data.UserManager;

public class LoginHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        IOManager ioManager = GlobalManager.getInstance().getIOManager();
        UserManager userManager = GlobalManager.getInstance().getUserManager();

        String username = argsMap.get("username");
        String password = argsMap.get("password");

        if (!ioManager.userProfileExists(username)) {
            throw new IllegalArgumentException("User profile does not exist for username: " + username);
        }

        userManager.login(username, password);
        OutputUtils.printSuccess("Login successful for user: " + new Chalk(username).bold().cyan());
    }
}
