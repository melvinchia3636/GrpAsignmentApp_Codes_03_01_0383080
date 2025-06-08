package features.auth.commands.profile;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.PositionalArgument;
import core.cli.commands.CommandInstance;
import features.auth.handlers.profile.EditProfileHandler;

public class EditProfileCommand extends CommandInstance {
    public EditProfileCommand() {
        super(
                "edit",
                "Edit the current user's profile information.",
                "country MY",
                new ArgumentList(
                        new PositionalArgument[] {
                                new PositionalArgument("key", "The profile field to edit (e.g., country)", ArgumentDataType.STRING),
                                new PositionalArgument("value", "The new value for the profile field", ArgumentDataType.STRING)
                        }
                ),
                new EditProfileHandler()
        );
    }
}
