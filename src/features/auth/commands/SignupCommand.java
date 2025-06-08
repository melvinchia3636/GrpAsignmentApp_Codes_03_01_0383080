package features.auth.commands;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.KeywordArgument;
import core.cli.arguments.PositionalArgument;
import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import features.auth.UserManager;
import features.auth.handlers.SignupHandler;

public class SignupCommand extends CommandInstance {
    public SignupCommand() {
        super(
                "signup",
                "Create a new user account.",
                "johndoe mypassword -c MY -r",
                new ArgumentList(
                        new PositionalArgument[]{
                                new PositionalArgument("username", "Username for the new account", ArgumentDataType.STRING),
                                new PositionalArgument("password", "Password for the new account", ArgumentDataType.STRING)
                        },
                        new KeywordArgument[]{
                                new KeywordArgument("country", "c", "Country of the user (ISO 3166-1 alpha-2 code)", ArgumentDataType.STRING, false),
                                new KeywordArgument("remember-me", "r", "Whether to remember the user for future logins", ArgumentDataType.FLAG, false)
                        }
                ),
                new SignupHandler()
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
