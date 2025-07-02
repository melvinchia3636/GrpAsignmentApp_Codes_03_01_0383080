package features.modules.DailyEcoChallenge.handlers;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.modules.DailyEcoChallenge.data.ChallengeManager;
import features.modules.DailyEcoChallenge.instances.Challenge;

import java.util.Scanner;

public class ChallengeCompleteHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        ChallengeManager challengeManager = GlobalManager.getInstance().getChallengeManager();
        
        if (challengeManager.hasCompletedChallengeToday()) {
            OutputUtils.printSectionHeader("🎉", "Already Completed Today!");
            OutputUtils.printSuccess("You've already completed a challenge today!");
            OutputUtils.printInfo("Great job! Come back tomorrow for a new challenge. 🌱");
            OutputUtils.printTip("Check your progress with 'challenge streak' or 'challenge history'.");
            return;
        }
        
        Challenge todaysChallenge = challengeManager.getTodaysChallenge();
        
        OutputUtils.printSectionHeader("🌱", "Complete Today's Challenge");
        OutputUtils.printDataRow("Challenge", todaysChallenge.getDescription());
        OutputUtils.printDataRow("Difficulty", new Chalk(todaysChallenge.getDifficulty()).bold());
        
        String notes;

        Scanner scanner = new Scanner(System.in);

        System.out.print("Did you complete this challenge? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        
        if (!response.equalsIgnoreCase("yes")) {
            OutputUtils.printWarning("No problem! Try again tomorrow or use 'challenge skip' if you need to.");
            return;
        }

        System.out.print("Any notes about your completion (optional): ");
        notes = scanner.nextLine().trim();

        challengeManager.recordChallenge(todaysChallenge, "completed", notes);

        int streak = challengeManager.getCurrentStreak();
        
        OutputUtils.printSectionHeader("🎉", "Congratulations! Challenge Completed!");
        OutputUtils.printStatistic("🔥", "Current streak", streak + " days", "yellow");

        if (streak == 1) {
            OutputUtils.printEncouragement("✨ Great start! Keep up the momentum!");
        } else if (streak == 7) {
            OutputUtils.printEncouragement("🌟 Amazing! You've completed a full week!");
        } else if (streak == 30) {
            OutputUtils.printEncouragement("🏆 Incredible! A full month of eco-friendly actions!");
        } else if (streak % 10 == 0) {
            OutputUtils.printEncouragement("🚀 Outstanding dedication to the environment!");
        }
    }
}
