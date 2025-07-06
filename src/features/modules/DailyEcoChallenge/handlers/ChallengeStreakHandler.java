package features.modules.DailyEcoChallenge.handlers;

import core.cli.commands.CommandInstance;
import core.instances.SimpleMap;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
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
        
        OutputUtils.printSectionHeader("Your Eco Challenge Streak");
        OutputUtils.printStatistic("Current Streak", currentStreak + " days", "yellow");
        
        if (currentStreak == 0) {
            OutputUtils.printTip("Start your streak today!");
        } else if (currentStreak == 1) {
            OutputUtils.printEncouragement("Great start! Keep it going!");
        } else if (currentStreak < 7) {
            OutputUtils.printEncouragement("Building momentum!");
        } else if (currentStreak < 30) {
            OutputUtils.printEncouragement("Fantastic consistency!");
        } else {
            OutputUtils.printEncouragement("Environmental champion!");
        }
        
        String[][] overallStats = {
            {"Total Challenges", String.valueOf(stats[0]), "blue"},
            {"Completed", stats[1] + (stats[0] > 0 ? String.format(" (%.1f%%)", (stats[1] * 100.0 / stats[0])) : ""), "green"},
            {"Skipped", stats[2] + (stats[0] > 0 ? String.format(" (%.1f%%)", (stats[2] * 100.0 / stats[0])) : ""), "yellow"},
            {"Failed", stats[3] + (stats[0] > 0 ? String.format(" (%.1f%%)", (stats[3] * 100.0 / stats[0])) : ""), "red"}
        };
        
        OutputUtils.printSummaryBox("Overall Statistics", overallStats);
        
        if (recentRecords.length > 0) {
            OutputUtils.printSubsectionHeader("Last 7 Days Activity");
            
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

                OutputUtils.printDataRow(date, activity);
            }
        }
        
        OutputUtils.printClosingMessage("Keep pushing forward! Every action makes a difference!");
    }

    private static SimpleMap<String, String> getDailyActivity(ChallengeRecord[] recentRecords) {
        SimpleMap<String, String> dailyActivity = new SimpleMap<>();
        for (ChallengeRecord record : recentRecords) {
            String date = String.format("%04d-%02d-%02d",
                record.getTimestamp().getYear(),
                record.getTimestamp().getMonth(),
                record.getTimestamp().getDay());

            String status = record.getStatus();

            switch (status) {
                case "completed":
                    status = new Chalk("[COMPLETED]").green().bold().toString();
                    break;
                case "skipped":
                    status = new Chalk("[SKIPPED]").yellow().bold().toString();
                    break;
                case "failed":
                    status = new Chalk("[FAILED]").red().bold().toString();
                    break;
                default:
                    status = new Chalk("[UNKNOWN]").white().bold().toString();
            }

            dailyActivity.put(date, status);
        }
        return dailyActivity;
    }
}
