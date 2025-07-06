package features.modules.GreenHabitTracker.data;

import core.instances.Timestamp;
import core.io.CSVParser;
import core.io.IOManager;
import core.manager.GlobalManager;
import features.modules.GreenHabitTracker.instances.Habit;
import features.modules.GreenHabitTracker.instances.HabitRecord;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Manages habits and habit completion records for the logged-in user.
 * Handles creation, storage, retrieval, and analysis of habit tracking data.
 */
public class HabitManager {
    private ArrayList<Habit> habits;
    private ArrayList<HabitRecord> records;

    /**
     * Initializes the HabitManager by loading existing habits and records from storage.
     * If no files exist, creates new empty lists.
     */
    public void init() {
        IOManager ioManager = GlobalManager.getInstance().getIOManager();
        
        // Initialize habits
        if (!ioManager.existsFile("habits")) {
            habits = new ArrayList<>();
            ioManager.writeToFile("habits", "");
        } else {
            loadHabits(ioManager);
        }
        
        // Initialize records
        if (!ioManager.existsFile("habit_records")) {
            records = new ArrayList<>();
            ioManager.writeToFile("habit_records", "");
        } else {
            loadRecords(ioManager);
        }
    }

    /**
     * Loads habits from the storage file.
     *
     * @param ioManager the IO manager instance
     */
    private void loadHabits(IOManager ioManager) {
        String csvString = ioManager.parseStringFromFile("habits");
        Object[] csvData = CSVParser.parseCSVString(csvString).toArray();
        habits = new ArrayList<>();

        for (Object obj : csvData) {
            String[] row = (String[]) obj;
            if (row.length < 2) continue; // Skip invalid rows

            try {
                int id = Integer.parseInt(row[0]);
                String name = row[1];
                habits.add(new Habit(id, name));
            } catch (NumberFormatException e) {
                // Skip rows with invalid number formats
            }
        }
    }

    /**
     * Loads habit records from the storage file.
     *
     * @param ioManager the IO manager instance
     */
    private void loadRecords(IOManager ioManager) {
        String csvString = ioManager.parseStringFromFile("habit_records");
        Object[] csvData = CSVParser.parseCSVString(csvString).stream().sorted(
                Comparator.comparingLong(a -> Long.parseLong(((String[]) a)[1]))
        ).toArray();
        records = new ArrayList<>();

        for (Object obj : csvData) {
            String[] row = (String[]) obj;
            if (row.length < 2) continue; // Skip invalid rows

            try {
                int habitId = Integer.parseInt(row[0]);
                long timestamp = Long.parseLong(row[1]);
                records.add(new HabitRecord(habitId, new Timestamp(timestamp)));
            } catch (NumberFormatException e) {
                // Skip rows with invalid number formats
            }
        }
    }

    /**
     * Saves habits to the storage file.
     */
    private void saveHabits() {
        IOManager ioManager = GlobalManager.getInstance().getIOManager();
        StringBuilder csvString = new StringBuilder();
        
        for (Habit habit : habits) {
            String[] habitArray = habit.toArray();
            csvString.append(String.join(",", habitArray)).append("\n");
        }
        
        ioManager.writeToFile("habits", csvString.toString());
    }

    /**
     * Saves habit records to the storage file.
     */
    private void saveRecords() {
        IOManager ioManager = GlobalManager.getInstance().getIOManager();
        StringBuilder csvString = new StringBuilder();
        
        for (HabitRecord record : records) {
            String[] recordArray = record.toArray();
            csvString.append(String.join(",", recordArray)).append("\n");
        }
        
        ioManager.writeToFile("habit_records", csvString.toString());
    }

    /**
     * Adds a new habit to the tracker.
     *
     * @param name the name of the habit to add
     * @return the newly created Habit object
     */
    public Habit addHabit(String name) {
        int nextId = habits.size() + 1;
        
        // Ensure unique ID
        while (getHabitById(nextId) != null) {
            nextId++;
        }
        
        Habit habit = new Habit(nextId, name);
        habits.add(habit);
        saveHabits();
        return habit;
    }

    /**
     * Removes a habit by its ID.
     *
     * @param habitId the ID of the habit to remove
     * @return true if the habit was removed, false if not found
     */
    public boolean removeHabit(int habitId) {
        Habit habit = getHabitById(habitId);
        if (habit != null) {
            habits.remove(habit);
            // Also remove all records for this habit
            records.removeIf(record -> record.getHabitId() == habitId);
            saveHabits();
            saveRecords();
            return true;
        }
        return false;
    }

    /**
     * Logs completion of a habit for today.
     *
     * @param habitId the ID of the habit to log
     * @return true if logged successfully, false if already logged today or habit not found
     */
    public boolean logHabitCompletion(int habitId) {
        Habit habit = getHabitById(habitId);
        if (habit == null) {
            return false;
        }

        Timestamp today = new Timestamp(System.currentTimeMillis());
        
        // Check if already logged today
        if (isHabitCompletedToday(habitId)) {
            return false;
        }

        HabitRecord record = new HabitRecord(habitId, today);
        records.add(record);
        saveRecords();
        return true;
    }

    /**
     * Gets all habits.
     *
     * @return the list of all habits
     */
    public ArrayList<Habit> getHabits() {
        return habits;
    }

    /**
     * Gets all habit records.
     *
     * @return the list of all habit records
     */
    public ArrayList<HabitRecord> getRecords() {
        return records;
    }

    /**
     * Gets a habit by its ID.
     *
     * @param habitId the ID of the habit to find
     * @return the Habit object if found, null otherwise
     */
    public Habit getHabitById(int habitId) {
        return habits.stream()
                .filter(habit -> habit.getId() == habitId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks if a habit was completed today.
     *
     * @param habitId the ID of the habit to check
     * @return true if the habit was completed today, false otherwise
     */
    public boolean isHabitCompletedToday(int habitId) {
        Timestamp today = new Timestamp(System.currentTimeMillis());
        return records.stream().anyMatch(record -> 
            record.getHabitId() == habitId &&
            record.getTimestamp().getYear() == today.getYear() &&
            record.getTimestamp().getMonth() == today.getMonth() &&
            record.getTimestamp().getDay() == today.getDay()
        );
    }

    /**
     * Gets all records for a specific habit.
     *
     * @param habitId the ID of the habit
     * @return an array of HabitRecord objects for the specified habit
     */
    public HabitRecord[] getRecordsByHabitId(int habitId) {
        return records.stream()
                .filter(record -> record.getHabitId() == habitId)
                .toArray(HabitRecord[]::new);
    }

    /**
     * Clears all habit records (for consistency with other managers).
     * This method is provided for interface consistency but should be used carefully
     * as habit data is user-specific.
     */
    public void clearRecords() {
        records.clear();
    }
}
