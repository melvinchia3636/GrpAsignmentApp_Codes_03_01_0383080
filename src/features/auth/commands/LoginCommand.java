package features.auth.commands;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.KeywordArgument;
import core.cli.arguments.PositionalArgument;
import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import features.auth.UserManager;
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
                        },
                        new KeywordArgument[]{
                                new KeywordArgument("remember-me", "r", "Whether to remember the user for future logins", ArgumentDataType.FLAG, false),
                        }
                ),
                new LoginHandler()
        );
    }

    /**
     * Checks if the command is enabled based on the user's login status.
     * The command is enabled only if the user is not logged in.
     *
     * @return true if the user is not logged in, false otherwise
     */
    @Override
    public boolean isDisabled() {
        UserManager userManager = GlobalManager.getInstance().getUserManager();
        return userManager.isLoggedIn;
    }
}
