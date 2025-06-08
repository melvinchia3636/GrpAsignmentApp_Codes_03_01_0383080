package features.auth.handlers.profile;

import core.cli.commands.CommandInstance;

public class ViewProfileHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        System.out.println("Displaying user profile information...");
    }
}
