package features.modules.DailyEcoChallenge.handlers;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.modules.DailyEcoChallenge.data.ChallengeManager;
import features.modules.DailyEcoChallenge.instances.Challenge;

import java.util.Scanner;

public class ChallengeSkipHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        ChallengeManager challengeManager = GlobalManager.getInstance().getChallengeManager();
        
        if (challengeManager.hasCompletedChallengeToday()) {
            OutputUtils.printSectionHeader("Already Completed Today!");
            OutputUtils.printSuccess("You've already completed a challenge today!");
            OutputUtils.printInfo("Great job! No need to skip since you've already succeeded.");
            OutputUtils.printInfo("Come back tomorrow for a new challenge!");
            return;
        }
        
        if (challengeManager.hasSkippedChallengeToday()) {
            OutputUtils.printSectionHeader("Already Skipped Today");
            OutputUtils.printWarning("You've already skipped today's challenge.");
            OutputUtils.printEncouragement("Tomorrow brings a new opportunity!");
            return;
        }
        
        Challenge todaysChallenge = challengeManager.getTodaysChallenge();
        
        OutputUtils.printSectionHeader("Skip Today's Challenge");
        OutputUtils.printDataRow("Challenge", todaysChallenge.getDescription());
        OutputUtils.printDataRow("Difficulty", new Chalk(todaysChallenge.getDifficulty()).bold());
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Why are you skipping this challenge? (optional): ");
        String reason = scanner.nextLine().trim();
        
        System.out.print("Are you sure you want to skip this challenge? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        
        if (!response.equalsIgnoreCase("yes")) {
            OutputUtils.printSuccess("Skip cancelled. Your challenge is still active!");
            OutputUtils.printEncouragement("You've got this!");
            return;
        }

        String notes = reason.isEmpty() ? "Challenge skipped" : "Skipped: " + reason;
        challengeManager.recordChallenge(todaysChallenge, "skipped", notes);

        OutputUtils.printSectionHeader("Challenge Skipped");
        OutputUtils.printEncouragement("Don't worry! Tomorrow brings a new opportunity to make a difference.");
        OutputUtils.printTip("Remember: Every small action counts towards a greener future!");
    }
}
