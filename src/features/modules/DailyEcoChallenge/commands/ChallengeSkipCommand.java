package features.modules.DailyEcoChallenge.commands;

import core.cli.commands.CommandInstance;
import features.modules.DailyEcoChallenge.handlers.ChallengeSkipHandler;

public class ChallengeSkipCommand extends CommandInstance {
    public ChallengeSkipCommand() {
        super(
                "skip",
                "Skip today's eco challenge",
                "",
                new ChallengeSkipHandler()
        );
    }
}
