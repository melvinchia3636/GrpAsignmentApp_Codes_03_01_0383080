package features.modules.DailyEcoChallenge.data;

import core.instances.Timestamp;
import core.io.CSVParser;
import core.io.IOManager;
import core.manager.GlobalManager;
import features.auth.data.UserManager;
import features.modules.DailyEcoChallenge.instances.Challenge;
import features.modules.DailyEcoChallenge.instances.ChallengeRecord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

/**
 * Manages daily eco challenges and their completion records.
 * Handles storing and retrieving challenge completion data,
 * managing today's challenge assignment, and providing statistics
 * about challenge completion patterns.
 */
public class ChallengeManager {
    private ArrayList<ChallengeRecord> records;

    /**
     * Initializes the challenge manager by loading existing records from file.
     */
    public void init() {
        IOManager ioManager = GlobalManager.getInstance().getIOManager();

        if (!ioManager.existsFile("challenge_records")) {
            records = new ArrayList<>();
            ioManager.writeToFile("challenge_records", "");
            return;
        }

        String csvString = ioManager.parseStringFromFile("challenge_records");
        Object[] csvData = CSVParser.parseCSVString(csvString).stream().sorted(
                Comparator.comparingLong(a -> Long.parseLong(a[2]))
        ).toArray();
        records = new ArrayList<>();

        for (int i = 0; i < csvData.length; i++) {
            String[] row = (String[]) csvData[i];
            if (row.length < 4) continue; // Skip invalid rows

            String challengeId = row[0];
            String status = row[1];
            Timestamp timestamp;
            String notes = row[3];

            try {
                timestamp = new Timestamp(Long.parseLong(row[2]));
            } catch (NumberFormatException e) {
                continue; // Skip rows with invalid timestamp
            }

            try {
                Challenge challenge = Challenges.getChallengeById(challengeId);
                ChallengeRecord record = new ChallengeRecord(i, challenge, status, timestamp, notes);
                records.add(record);
            } catch (IllegalArgumentException e) {
                // Skip records with invalid challenge IDs
            }
        }
    }

    /**
     * Gets today's assigned challenge. If no challenge is assigned for today,
     * assigns a random challenge and stores it in the user profile.
     *
     * @return Today's challenge
     */
    public Challenge getTodaysChallenge() {
        UserManager userManager = GlobalManager.getInstance().getUserManager();
        String todayChallengeId = userManager.getTodayChallengeId();

        // Check if we need to assign a new challenge for today
        if (todayChallengeId == null || todayChallengeId.isEmpty() || !isTodaysChallengeValid(todayChallengeId)) {
            // Assign a new random challenge
            Challenge newChallenge = Challenges.getRandomChallenge();
            userManager.setTodayChallengeId(newChallenge.getId());
            return newChallenge;
        }

        try {
            return Challenges.getChallengeById(todayChallengeId);
        } catch (IllegalArgumentException e) {
            // Challenge ID is invalid, assign a new one
            Challenge newChallenge = Challenges.getRandomChallenge();
            userManager.setTodayChallengeId(newChallenge.getId());
            return newChallenge;
        }
    }

    /**
     * Assigns a new random challenge for today, replacing any existing assignment.
     *
     * @return The newly assigned challenge
     */
    public Challenge refreshTodaysChallenge() {
        UserManager userManager = GlobalManager.getInstance().getUserManager();
        Challenge newChallenge = Challenges.getRandomChallenge();
        userManager.setTodayChallengeId(newChallenge.getId());
        return newChallenge;
    }

    /**
     * Records the completion of a challenge.
     *
     * @param challenge The challenge that was completed
     * @param status The completion status ("completed", "skipped", "failed")
     * @param notes Optional notes about the completion
     */
    public void recordChallenge(Challenge challenge, String status, String notes) {
        int newIndex = records.size();
        Timestamp timestamp = new Timestamp();
        ChallengeRecord record = new ChallengeRecord(newIndex, challenge, status, timestamp, notes);
        records.add(record);
        writeRecordsToFile();
    }

    /**
     * Gets all challenge records.
     *
     * @return List of all challenge records
     */
    public ArrayList<ChallengeRecord> getRecords() {
        return records;
    }

    /**
     * Gets all challenge records for a specific date.
     *
     * @param date The date to filter by
     * @return Array of challenge records for the specified date
     */
    public ChallengeRecord[] getRecordsByDate(Timestamp date) {
        return records.stream()
                .filter(record -> record.getTimestamp().getYear() == date.getYear() &&
                        record.getTimestamp().getMonth() == date.getMonth() &&
                        record.getTimestamp().getDay() == date.getDay())
                .toArray(ChallengeRecord[]::new);
    }

    /**
     * Gets challenge records for the last X days.
     *
     * @param days Number of days to look back
     * @return Array of challenge records from the last X days
     */
    public ChallengeRecord[] getRecordsForLastXDays(int days) {
        long startTimestamp = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L);
        return records.stream()
                .filter(record -> record.getTimestamp().getTimestamp() >= startTimestamp)
                .toArray(ChallengeRecord[]::new);
    }

    /**
     * Gets the current streak of completed challenges.
     *
     * @return Number of consecutive days with completed challenges
     */
    public int getCurrentStreak() {
        if (records.isEmpty()) return 0;

        int streak = 0;
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        // Check each day going backwards from today
        for (int daysBack = 0; daysBack < records.size(); daysBack++) {
            Calendar checkDate = (Calendar) today.clone();
            checkDate.add(Calendar.DATE, -daysBack);
            
            Timestamp dateToCheck = new Timestamp(checkDate.getTimeInMillis());
            ChallengeRecord[] dayRecords = getRecordsByDate(dateToCheck);
            
            boolean hasCompletedChallenge = false;
            for (ChallengeRecord record : dayRecords) {
                if (record.isCompleted()) {
                    hasCompletedChallenge = true;
                    break;
                }
            }
            
            if (hasCompletedChallenge) {
                streak++;
            } else {
                break;
            }
        }

        return streak;
    }

    /**
     * Gets statistics about challenge completion.
     *
     * @return Array containing [totalChallenges, completed, skipped, failed]
     */
    public int[] getChallengeStats() {
        int total = records.size();
        int completed = 0;
        int skipped = 0;
        int failed = 0;

        for (ChallengeRecord record : records) {
            switch (record.getStatus().toLowerCase()) {
                case "completed":
                    completed++;
                    break;
                case "skipped":
                    skipped++;
                    break;
                case "failed":
                    failed++;
                    break;
            }
        }

        return new int[]{total, completed, skipped, failed};
    }

    /**
     * Clears all challenge records from memory only.
     */
    public void clearRecords() {
        records.clear();
    }

    /**
     * Checks if today's challenge assignment is still valid (hasn't been completed yet today).
     *
     * @param challengeId The challenge ID to check
     * @return true if the challenge is valid for today, false otherwise
     */
    private boolean isTodaysChallengeValid(String challengeId) {
        Timestamp today = new Timestamp();
        ChallengeRecord[] todayRecords = getRecordsByDate(today);
        
        // Check if this challenge was already completed today
        for (ChallengeRecord record : todayRecords) {
            if (record.getChallenge().getId().equals(challengeId) && record.isCompleted()) {
                return false; // Challenge was already completed today
            }
        }
        
        return true; // Challenge is still valid
    }

    /**
     * Writes all records to the file.
     */
    private void writeRecordsToFile() {
        IOManager ioManager = GlobalManager.getInstance().getIOManager();
        String csvString = toCSVString();
        ioManager.writeToFile("challenge_records", csvString);
    }

    /**
     * Converts all records to CSV string format.
     *
     * @return CSV string representation of all records
     */
    private String toCSVString() {
        ArrayList<String[]> csvData = new ArrayList<>();
        for (ChallengeRecord record : records) {
            csvData.add(record.toArray());
        }
        return CSVParser.toCSVString(csvData);
    }

    /**
     * Checks if the user has already completed a challenge today.
     *
     * @return true if a challenge has been completed today, false otherwise
     */
    public boolean hasCompletedChallengeToday() {
        Timestamp today = new Timestamp();
        ChallengeRecord[] todayRecords = getRecordsByDate(today);
        
        for (ChallengeRecord record : todayRecords) {
            if (record.isCompleted()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the user has already skipped a challenge today.
     *
     * @return true if a challenge has been skipped today, false otherwise
     */
    public boolean hasSkippedChallengeToday() {
        Timestamp today = new Timestamp();
        ChallengeRecord[] todayRecords = getRecordsByDate(today);
        
        for (ChallengeRecord record : todayRecords) {
            if (record.isSkipped()) {
                return true;
            }
        }
        return false;
    }
}
