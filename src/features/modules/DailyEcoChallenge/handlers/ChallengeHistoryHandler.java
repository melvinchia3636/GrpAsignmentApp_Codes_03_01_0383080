package features.modules.DailyEcoChallenge.handlers;

import core.cli.commands.CommandInstance;
import core.instances.SimpleMap;
import core.instances.Timestamp;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.modules.DailyEcoChallenge.data.ChallengeManager;
import features.modules.DailyEcoChallenge.instances.ChallengeRecord;

import java.util.ArrayList;

public class ChallengeHistoryHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        ChallengeManager challengeManager = GlobalManager.getInstance().getChallengeManager();
        ArrayList<ChallengeRecord> allRecords = challengeManager.getRecords();
        
        System.out.println("\n📜 Your Eco Challenge History");
        System.out.println("═══════════════════════════════════════");
        
        if (allRecords.isEmpty()) {
            OutputUtils.printError("No challenge records found yet.", false);
            System.out.println(new Chalk("Start your eco journey today with 'challenge today'! 🌱").yellow());
            return;
        }
        
        SimpleMap<String, ChallengeRecord> recordsByDate = new SimpleMap<>();
        
        for (ChallengeRecord record : allRecords) {
            Timestamp timestamp = record.getTimestamp();
            String dateKey = String.format("%04d-%02d-%02d", 
                timestamp.getYear(), timestamp.getMonth(), timestamp.getDay());

            if (!recordsByDate.containsKey(dateKey)) {
                recordsByDate.put(dateKey, record);
            }
        }
        
        ArrayList<String> sortedDates = new ArrayList<>(recordsByDate.keys());
        sortedDates.sort(java.util.Collections.reverseOrder());
        
        for (String date : sortedDates) {
            ChallengeRecord record = recordsByDate.get(date);
            System.out.println("\n📅 " + new Chalk(date).bold());
            System.out.println("───────────────────────────────────────");
            
            String status = record.getStatus();
            String emoji = status.equals("completed") ? "✅" :
                          status.equals("skipped") ? "⏭️" : "❌";

            Chalk statusColor = status.equals("completed") ? new Chalk(status).green() :
                               status.equals("skipped") ? new Chalk(status).yellow() :
                               new Chalk(status).red();

            System.out.println(emoji + " " + statusColor.bold() + " - " + record.getChallenge().getDescription());
            System.out.println("   Difficulty: " + record.getChallenge().getDifficulty());

            if (!record.getNotes().isEmpty()) {
                System.out.println("   Notes: " + new Chalk(record.getNotes()).cyan());
            }

            Timestamp timestamp = record.getTimestamp();
            System.out.println("   Time: " + String.format("%02d:%02d",
                timestamp.getHour(), timestamp.getMinute()));
        }
        
        int[] stats = challengeManager.getChallengeStats();
        int streak = challengeManager.getCurrentStreak();
        
        System.out.println("\n📊 Summary:");
        System.out.println("───────────────────────────────────────");
        System.out.println("🔥 Current Streak: " + new Chalk(String.valueOf(streak)).yellow().bold() + " days");
        System.out.println("📈 Total Records: " + stats[0]);
        System.out.println("✅ Completed: " + new Chalk(String.valueOf(stats[1])).green() + 
                          (stats[0] > 0 ? String.format(" (%.1f%%)", (stats[1] * 100.0 / stats[0])) : ""));
        
        System.out.println("\n🌍 Keep making a positive impact on our planet! 🌱");
    }
}
