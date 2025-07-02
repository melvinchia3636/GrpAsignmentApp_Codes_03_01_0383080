package features.modules.DailyEcoChallenge.handlers;

import core.cli.commands.CommandInstance;
import core.instances.SimpleMap;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import features.modules.DailyEcoChallenge.data.ChallengeManager;
import features.modules.DailyEcoChallenge.instances.ChallengeRecord;

import java.util.Calendar;

public class ChallengeStreakHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        ChallengeManager challengeManager = GlobalManager.getInstance().getChallengeManager();
        int currentStreak = challengeManager.getCurrentStreak();
        int[] stats = challengeManager.getChallengeStats();
        
        ChallengeRecord[] recentRecords = challengeManager.getRecordsForLastXDays(7);
        
        System.out.println("🔥 Your Eco Challenge Streak");
        System.out.println("═══════════════════════════════════════");
        System.out.println("Current Streak: " + new Chalk(String.valueOf(currentStreak)).yellow().bold() + " days");
        
        if (currentStreak == 0) {
            System.out.println("💡 " + new Chalk("Start your streak today!").cyan());
        } else if (currentStreak == 1) {
            System.out.println("🌱 " + new Chalk("Great start! Keep it going!").green());
        } else if (currentStreak < 7) {
            System.out.println("🚀 " + new Chalk("Building momentum!").green());
        } else if (currentStreak < 30) {
            System.out.println("⭐ " + new Chalk("Fantastic consistency!").green().bold());
        } else {
            System.out.println("🏆 " + new Chalk("Environmental champion!").green().bold());
        }
        
        System.out.println("\n📊 Overall Statistics:");
        System.out.println("───────────────────────────────────────");
        System.out.println("Total Challenges: " + stats[0]);
        System.out.println("✅ Completed: " + new Chalk(String.valueOf(stats[1])).green() + 
                          (stats[0] > 0 ? String.format(" (%.1f%%)", (stats[1] * 100.0 / stats[0])) : ""));
        System.out.println("⏭️  Skipped: " + new Chalk(String.valueOf(stats[2])).yellow() + 
                          (stats[0] > 0 ? String.format(" (%.1f%%)", (stats[2] * 100.0 / stats[0])) : ""));
        System.out.println("❌ Failed: " + new Chalk(String.valueOf(stats[3])).red() + 
                          (stats[0] > 0 ? String.format(" (%.1f%%)", (stats[3] * 100.0 / stats[0])) : ""));
        
        if (recentRecords.length > 0) {
            System.out.println("\n📅 Last 7 Days Activity:");
            System.out.println("───────────────────────────────────────");
            
            // Group by date and show activity
            SimpleMap<String, String> dailyActivity = getDailyActivity(recentRecords);

            // Show the last 7 days
            java.util.Calendar cal = Calendar.getInstance();
            for (int i = 6; i >= 0; i--) {
                cal.add(java.util.Calendar.DATE, i == 6 ? -6 : 1);
                String date = String.format("%04d-%02d-%02d", 
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH));
                
                String activity = dailyActivity.get(date);
                if (activity == null) {
                    activity = "No activity";
                }

                System.out.println(date + ": " + activity);
            }
        }
        
        System.out.println("\n💪 Keep pushing forward! Every action makes a difference! 🌍");
    }

    private static SimpleMap<String, String> getDailyActivity(ChallengeRecord[] recentRecords) {
        SimpleMap<String, String> dailyActivity = new SimpleMap<>();
        for (ChallengeRecord record : recentRecords) {
            String date = String.format("%04d-%02d-%02d",
                record.getTimestamp().getYear(),
                record.getTimestamp().getMonth(),
                record.getTimestamp().getDay());

            String status = record.getStatus();
            String emoji = status.equals("completed") ? "✅" :
                          status.equals("skipped") ? "⏭️" : "❌";

            dailyActivity.put(date, emoji + " " + status);
        }
        return dailyActivity;
    }
}
