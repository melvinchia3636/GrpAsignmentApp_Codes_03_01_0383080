package features.modules.DailyEcoChallenge.commands;

import core.cli.commands.CommandInstance;

public class ChallengeCommands extends CommandInstance {
    public ChallengeCommands() {
        super(
                "challenge",
                "Manage your daily eco challenges",
                new CommandInstance[] {
                        new ChallengeTodayCommand(),
                        new ChallengeCompleteCommand(),
                        new ChallengeSkipCommand(),
                        new ChallengeRefreshCommand(),
                        new ChallengeStreakCommand(),
                        new ChallengeHistoryCommand()
                }
        );
    }

}
