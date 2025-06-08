package features.auth.commands.profile;

import core.cli.commands.CommandInstance;
import features.auth.handlers.profile.ViewProfileHandler;

public class ViewProfileCommand extends CommandInstance {
    public ViewProfileCommand() {
        super(
                "view",
                "View the current user's profile information.",
                "", // No arguments for view command
                new ViewProfileHandler()
        );
    }
}
