package features.auth.handlers;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.OutputUtils;
import features.auth.UserManager;

public class LogoutHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        UserManager userManager = GlobalManager.getInstance().getUserManager();
        userManager.reset();

        OutputUtils.printSuccess("You have been logged out successfully.");
    }
}
