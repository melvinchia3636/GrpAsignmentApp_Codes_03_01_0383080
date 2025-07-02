package features.auth.commands;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.PositionalArgument;
import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import features.auth.data.UserManager;
import features.auth.handlers.SignupHandler;

public class SignupCommand extends CommandInstance {
    public SignupCommand() {
        super(
                "signup",
                "Create a new user account.",
                "johndoe mypassword",
                new ArgumentList(
                        new PositionalArgument("username", "Username for the new account", ArgumentDataType.STRING),
                        new PositionalArgument("password", "Password for the new account", ArgumentDataType.STRING)
                ),
                new SignupHandler()
        );
    }

    @Override
    public boolean isDisabled() {
        UserManager userManager = GlobalManager.getInstance().getUserManager();
        return userManager.isLoggedIn;
    }
}
