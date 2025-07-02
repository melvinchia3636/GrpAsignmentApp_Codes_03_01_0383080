package features.modules.DailyEcoChallenge.handlers;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import features.modules.DailyEcoChallenge.data.ChallengeManager;
import features.modules.DailyEcoChallenge.instances.Challenge;

public class ChallengeTodayHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        ChallengeManager challengeManager = GlobalManager.getInstance().getChallengeManager();
        Challenge todaysChallenge = challengeManager.getTodaysChallenge();
        
        System.out.println("🌱 Today's Eco Challenge:");
        System.out.println("═══════════════════════════════════════");
        System.out.println("Challenge ID: " + todaysChallenge.getId());
        System.out.println("Difficulty: " + todaysChallenge.getDifficulty());
        System.out.println("Description: " + todaysChallenge.getDescription());
        System.out.println("═══════════════════════════════════════");
        
        if (challengeManager.hasCompletedChallengeToday()) {
            System.out.println("Status: " + new Chalk("✅ COMPLETED TODAY!").green().bold());
            System.out.println("🎉 Amazing work! Come back tomorrow for a new challenge!");
        } else if (challengeManager.hasSkippedChallengeToday()) {
            System.out.println("Status: " + new Chalk("⏭️ Skipped today").yellow().bold());
            System.out.println("💪 Tomorrow is a new opportunity!");
        } else {
            System.out.println("Status: " + new Chalk("⏳ Pending").cyan().bold());
            System.out.println("Ready to make a difference? 💪");
            System.out.println("\nUse 'challenge complete' when you're done!");
        }
    }
}
