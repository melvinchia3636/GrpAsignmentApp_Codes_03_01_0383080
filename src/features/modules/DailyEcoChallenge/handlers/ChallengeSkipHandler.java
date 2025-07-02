package features.modules.DailyEcoChallenge.handlers;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import features.modules.DailyEcoChallenge.data.ChallengeManager;
import features.modules.DailyEcoChallenge.instances.Challenge;

import java.util.Scanner;

public class ChallengeSkipHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        ChallengeManager challengeManager = GlobalManager.getInstance().getChallengeManager();
        
        if (challengeManager.hasCompletedChallengeToday()) {
            System.out.println("🎉 " + new Chalk("You've already completed a challenge today!").green().bold());
            System.out.println("═══════════════════════════════════════");
            System.out.println("Great job! No need to skip since you've already succeeded. 🌱");
            System.out.println("Come back tomorrow for a new challenge!");
            return;
        }
        
        if (challengeManager.hasSkippedChallengeToday()) {
            System.out.println("⏭️  " + new Chalk("You've already skipped today's challenge.").yellow().bold());
            System.out.println("═══════════════════════════════════════");
            System.out.println("Tomorrow brings a new opportunity! 🌱");
            return;
        }
        
        Challenge todaysChallenge = challengeManager.getTodaysChallenge();
        
        System.out.println("⏭️  Skip Today's Challenge");
        System.out.println("═══════════════════════════════════════");
        System.out.println("Challenge: " + todaysChallenge.getDescription());
        System.out.println("Difficulty: " + new Chalk(todaysChallenge.getDifficulty()).bold());
        System.out.println("═══════════════════════════════════════");
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Why are you skipping this challenge? (optional): ");
        String reason = scanner.nextLine().trim();
        
        System.out.print("Are you sure you want to skip this challenge? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        
        if (!response.equalsIgnoreCase("yes")) {
            System.out.println("\n" + new Chalk("Skip cancelled. Your challenge is still active!").green());
            System.out.println("You've got this! 💪");
            return;
        }

        String notes = reason.isEmpty() ? "Challenge skipped" : "Skipped: " + reason;
        challengeManager.recordChallenge(todaysChallenge, "skipped", notes);

        System.out.println("\n⏭️  " + new Chalk("Challenge skipped.").yellow().bold());
        System.out.println("Don't worry! Tomorrow brings a new opportunity to make a difference. 🌱");
        System.out.println("Remember: Every small action counts towards a greener future!");
    }
}
