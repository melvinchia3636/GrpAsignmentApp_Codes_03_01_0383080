package features.modules.DailyEcoChallenge.commands;

import core.cli.commands.CommandInstance;
import features.modules.DailyEcoChallenge.handlers.ChallengeTodayHandler;

public class ChallengeTodayCommand extends CommandInstance {
    public ChallengeTodayCommand() {
        super(
                "today",
                "Get today's eco challenge",
                "",
                new ChallengeTodayHandler()
        );
    }
}
