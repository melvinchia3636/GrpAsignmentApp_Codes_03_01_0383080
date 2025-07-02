package features.modules.DailyEcoChallenge.commands;

import core.cli.commands.CommandInstance;
import features.modules.DailyEcoChallenge.handlers.ChallengeHistoryHandler;

public class ChallengeHistoryCommand extends CommandInstance {
    public ChallengeHistoryCommand() {
        super(
                "history",
                "View your challenge history",
                "",
                new ChallengeHistoryHandler()
        );
    }
}
