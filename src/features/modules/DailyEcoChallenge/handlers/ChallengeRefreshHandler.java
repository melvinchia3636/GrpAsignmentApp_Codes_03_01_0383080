package features.modules.DailyEcoChallenge.handlers;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import features.modules.DailyEcoChallenge.data.ChallengeManager;
import features.modules.DailyEcoChallenge.instances.Challenge;

import java.util.Scanner;

public class ChallengeRefreshHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        ChallengeManager challengeManager = GlobalManager.getInstance().getChallengeManager();
        
        if (challengeManager.hasCompletedChallengeToday()) {
            System.out.println("🎉 " + new Chalk("You've already completed a challenge today!").green().bold());
            System.out.println("═══════════════════════════════════════");
            System.out.println("Great job! No need to refresh since you've already succeeded. 🌱");
            System.out.println("Come back tomorrow for a new challenge!");
            return;
        }
        
        Challenge currentChallenge = challengeManager.getTodaysChallenge();
        
        System.out.println("🔄 Refresh Today's Challenge");
        System.out.println("═══════════════════════════════════════");
        System.out.println("Current Challenge: " + currentChallenge.getDescription());
        System.out.println("Difficulty: " + new Chalk(currentChallenge.getDifficulty()).bold());
        System.out.println("═══════════════════════════════════════");
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Are you sure you want to get a new challenge? This cannot be undone. (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        
        if (response.equals("y") || response.equals("yes")) {
            Challenge newChallenge = challengeManager.refreshTodaysChallenge();
            
            System.out.println("\n✨ " + new Chalk("New challenge assigned!").green().bold());
            System.out.println("═══════════════════════════════════════");
            System.out.println("New Challenge: " + newChallenge.getDescription());
            System.out.println("Difficulty: " + new Chalk(newChallenge.getDifficulty()).bold());
            System.out.println("═══════════════════════════════════════");
            System.out.println("Ready for your new eco-friendly adventure? 🌱");
        } else {
            System.out.println("\n" + new Chalk("Refresh cancelled. Your current challenge remains active.").yellow());
        }
    }
}
