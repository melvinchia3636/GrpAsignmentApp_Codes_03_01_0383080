package features.modules.DailyEcoChallenge.handlers;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import features.modules.DailyEcoChallenge.data.ChallengeManager;
import features.modules.DailyEcoChallenge.instances.Challenge;

import java.util.Scanner;

public class ChallengeCompleteHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        ChallengeManager challengeManager = GlobalManager.getInstance().getChallengeManager();
        
        if (challengeManager.hasCompletedChallengeToday()) {
            System.out.println("\n🎉 " + new Chalk("You've already completed a challenge today!").green().bold());
            System.out.println("═══════════════════════════════════════");
            System.out.println("Great job! Come back tomorrow for a new challenge. 🌱");
            System.out.println("You can check your progress with 'challenge streak' or 'challenge history'.");
            return;
        }
        
        Challenge todaysChallenge = challengeManager.getTodaysChallenge();
        
        System.out.println("\n🌱 Complete Today's Challenge");
        System.out.println("═══════════════════════════════════════");
        System.out.println("Challenge: " + todaysChallenge.getDescription());
        System.out.println("Difficulty: " + new Chalk(todaysChallenge.getDifficulty()).bold());
        System.out.println("═══════════════════════════════════════");
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Did you complete this challenge? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        
        if (!response.equalsIgnoreCase("yes")) {
            System.out.println("\n" + new Chalk("No problem! Try again tomorrow or use 'challenge skip' if you need to.").yellow());
            return;
        }

        System.out.print("Any notes about your completion (optional): ");
        String notes = scanner.nextLine().trim();

        challengeManager.recordChallenge(todaysChallenge, "completed", notes);

        int streak = challengeManager.getCurrentStreak();
        System.out.println("\n🎉 " + new Chalk("Congratulations! Challenge completed!").green().bold());
        System.out.println("🔥 Current streak: " + new Chalk(String.valueOf(streak)).yellow().bold() + " days");

        if (streak == 1) {
            System.out.println(new Chalk("✨ Great start! Keep up the momentum!").purple());
        } else if (streak == 7) {
            System.out.println(new Chalk("🌟 Amazing! You've completed a full week!").purple());
        } else if (streak == 30) {
            System.out.println(new Chalk("🏆 Incredible! A full month of eco-friendly actions!").purple());
        } else if (streak % 10 == 0) {
            System.out.println(new Chalk("🚀 Outstanding dedication to the environment!").purple());
        }
    }
}
