package features.auth.handlers;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.OutputUtils;

public class LogoutHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        GlobalManager.getInstance().reset();

        OutputUtils.printSuccess("You have been logged out successfully.");
    }
}
