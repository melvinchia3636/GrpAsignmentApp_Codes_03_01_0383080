package features.modules.DailyEcoChallenge.handlers;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.modules.DailyEcoChallenge.data.ChallengeManager;
import features.modules.DailyEcoChallenge.instances.Challenge;

import java.util.Scanner;

public class ChallengeRefreshHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        ChallengeManager challengeManager = GlobalManager.getInstance().getChallengeManager();
        
        if (challengeManager.hasCompletedChallengeToday()) {
            OutputUtils.printSectionHeader("Already Completed Today!");
            OutputUtils.printSuccess("You've already completed a challenge today!");
            OutputUtils.printInfo("Great job! No need to refresh since you've already succeeded.");
            OutputUtils.printInfo("Come back tomorrow for a new challenge!");
            return;
        }
        
        Challenge currentChallenge = challengeManager.getTodaysChallenge();
        
        OutputUtils.printSectionHeader("Refresh Today's Challenge");
        OutputUtils.printDataRow("Current Challenge", currentChallenge.getDescription());
        OutputUtils.printDataRow("Difficulty", new Chalk(currentChallenge.getDifficulty()).bold());
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Are you sure you want to get a new challenge? This cannot be undone. (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        
        if (response.equals("y") || response.equals("yes")) {
            Challenge newChallenge = challengeManager.refreshTodaysChallenge();
            
            OutputUtils.printSectionHeader("New Challenge Assigned!");
            OutputUtils.printDataRow("New Challenge", newChallenge.getDescription());
            OutputUtils.printDataRow("Difficulty", new Chalk(newChallenge.getDifficulty()).bold());
            OutputUtils.printEncouragement("Ready for your new eco-friendly adventure?");
        } else {
            OutputUtils.printWarning("Refresh cancelled. Your current challenge remains active.");
        }
    }
}
