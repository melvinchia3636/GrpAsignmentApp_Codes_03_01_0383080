package features.auth.commands;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import features.auth.UserManager;
import features.auth.commands.profile.EditProfileCommand;
import features.auth.commands.profile.ViewProfileCommand;

public class ProfileCommand extends CommandInstance {
    public ProfileCommand() {
        super(
                "profile",
                "Display or manage user profile information.",
                new CommandInstance[]{
                        new ViewProfileCommand(),
                        new EditProfileCommand()
                }
        );
    }

    @Override
    public boolean isDisabled() {
        UserManager userManager = GlobalManager.getInstance().getUserManager();

        return !userManager.isLoggedIn;
    }
}
