package features.auth.commands;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.PositionalArgument;
import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import features.auth.data.UserManager;
import features.auth.handlers.LoginHandler;

public class LoginCommand extends CommandInstance {
    public LoginCommand() {
        super(
                "login",
                "Login to the system with your credentials.",
                "johndoe mypassword -r",
                new ArgumentList(
                        new PositionalArgument[]{
                                new PositionalArgument("username", "Username of the your profile", ArgumentDataType.STRING),
                                new PositionalArgument("password", "Password of the your profile", ArgumentDataType.STRING)
                        }
                ),
                new LoginHandler()
        );
    }

    @Override
    public boolean isDisabled() {
        UserManager userManager = GlobalManager.getInstance().getUserManager();
        return userManager.isLoggedIn;
    }
}
