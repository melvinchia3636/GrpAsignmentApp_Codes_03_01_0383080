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
        
        OutputUtils.printSectionHeader("Your Eco Challenge History");
        
        if (allRecords.isEmpty()) {
            OutputUtils.printError("No challenge records found yet.", false);
            OutputUtils.printTip("Start your eco journey today with 'challenge today'! 🌱");
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
            OutputUtils.printSubsectionHeader(date);
            
            String status = record.getStatus();
            OutputUtils.printStatus(status, record.getChallenge().getDescription());
            OutputUtils.printDataRow("Difficulty", record.getChallenge().getDifficulty());

            if (!record.getNotes().isEmpty()) {
                OutputUtils.printDataRow("Notes", new Chalk(record.getNotes()).cyan());
            }

            Timestamp timestamp = record.getTimestamp();
            OutputUtils.printDataRow("Time", String.format("%02d:%02d",
                timestamp.getHour(), timestamp.getMinute()));
        }
        
        int[] stats = challengeManager.getChallengeStats();
        int streak = challengeManager.getCurrentStreak();
        
        String[][] summaryStats = {
            {"Current Streak", streak + " days", "yellow"},
            {"Total Records", String.valueOf(stats[0]), "blue"},
            {"Completed", stats[1] + (stats[0] > 0 ? String.format(" (%.1f%%)", (stats[1] * 100.0 / stats[0])) : ""), "green"}
        };
        
        OutputUtils.printSummaryBox("Summary", summaryStats);
        OutputUtils.printClosingMessage("🌍 Keep making a positive impact on our planet! 🌱");
    }
}
