package features.modules.DailyEcoChallenge.commands;

import core.cli.commands.CommandInstance;
import features.modules.DailyEcoChallenge.handlers.ChallengeRefreshHandler;

public class ChallengeRefreshCommand extends CommandInstance {
    public ChallengeRefreshCommand() {
        super(
                "refresh",
                "Refreshes the daily eco challenge of the day (can only be used once per day)",
                "",
                new ChallengeRefreshHandler()
        );
    }
}
