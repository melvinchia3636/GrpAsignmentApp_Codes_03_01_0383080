package features.auth.commands;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import features.auth.data.UserManager;
import features.auth.handlers.ListProfileHandler;

public class ListProfiles extends CommandInstance {
    public ListProfiles() {
        super(
                "list-profiles",
                "Lists all profiles in the system.",
                "",
                new ListProfileHandler()
        );
    }

    @Override
    public boolean isDisabled() {
        UserManager userManager = GlobalManager.getInstance().getUserManager();
        return userManager.isLoggedIn;
    }
}
