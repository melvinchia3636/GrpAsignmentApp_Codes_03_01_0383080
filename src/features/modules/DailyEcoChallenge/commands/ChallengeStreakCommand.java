package features.modules.DailyEcoChallenge.commands;

import core.cli.commands.CommandInstance;
import features.modules.DailyEcoChallenge.handlers.ChallengeStreakHandler;

public class ChallengeStreakCommand extends CommandInstance {
    public ChallengeStreakCommand() {
        super(
                "streak",
                "View your current eco challenge streak",
                "",
                new ChallengeStreakHandler()
        );
    }
}
