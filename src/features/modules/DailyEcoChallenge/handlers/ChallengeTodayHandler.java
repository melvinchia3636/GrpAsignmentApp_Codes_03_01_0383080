package features.modules.DailyEcoChallenge.handlers;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.modules.DailyEcoChallenge.data.ChallengeManager;
import features.modules.DailyEcoChallenge.instances.Challenge;

public class ChallengeTodayHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        ChallengeManager challengeManager = GlobalManager.getInstance().getChallengeManager();
        Challenge todaysChallenge = challengeManager.getTodaysChallenge();
        
        OutputUtils.printSectionHeader("Today's Eco Challenge");
        
        OutputUtils.printDataRow("Challenge ID", todaysChallenge.getId());
        OutputUtils.printDataRow("Difficulty", new Chalk(todaysChallenge.getDifficulty()).bold());
        OutputUtils.printDataRow("Description", todaysChallenge.getDescription());
        
        if (challengeManager.hasCompletedChallengeToday()) {
            OutputUtils.printStatus("completed", "COMPLETED TODAY!");
            OutputUtils.printEncouragement("Amazing work! Come back tomorrow for a new challenge!");
        } else if (challengeManager.hasSkippedChallengeToday()) {
            OutputUtils.printStatus("skipped", "Skipped today");
            OutputUtils.printEncouragement("Tomorrow is a new opportunity!");
        } else {
            OutputUtils.printStatus("in-progress", "Pending");
            OutputUtils.printEncouragement("Ready to make a difference?");
            OutputUtils.printTip("Use 'challenge complete' when you're done!");
        }
    }
}
