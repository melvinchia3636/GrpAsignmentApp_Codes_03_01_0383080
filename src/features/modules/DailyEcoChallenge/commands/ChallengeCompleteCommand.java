package features.modules.DailyEcoChallenge.commands;

import core.cli.commands.CommandInstance;
import features.modules.DailyEcoChallenge.handlers.ChallengeCompleteHandler;

public class ChallengeCompleteCommand extends CommandInstance {
    public ChallengeCompleteCommand() {
        super(
                "complete",
                "Mark today's eco challenge as complete",
                "",
                new ChallengeCompleteHandler()
        );
    }
}
