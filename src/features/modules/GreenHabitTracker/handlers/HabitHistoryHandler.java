package features.modules.GreenHabitTracker.handlers;

import core.cli.commands.CommandInstance;
import core.instances.Timestamp;
import core.manager.GlobalManager;
import core.terminal.OutputUtils;
import features.modules.GreenHabitTracker.data.HabitManager;
import features.modules.GreenHabitTracker.instances.Habit;
import features.modules.GreenHabitTracker.instances.HabitRecord;

import java.util.ArrayList;
import java.util.Scanner;

public class HabitHistoryHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        HabitManager habitManager = GlobalManager.getInstance().getHabitManager();
        ArrayList<Habit> habits = habitManager.getHabits();

        if (habits.isEmpty()) {
            OutputUtils.printInfo("No habits have been added yet. Use 'habit add' to create your first habit!");
            return;
        }

        System.out.println("Select a habit to view its history:");
        
        for (Habit habit : habits) {
            System.out.printf("%d. %s%n", habit.getId(), habit.getName());
        }

        System.out.print("> ");

        Scanner scanner = new Scanner(System.in);
        
        try {
            int habitId = Integer.parseInt(scanner.nextLine().trim());
            
            Habit habit = habitManager.getHabitById(habitId);
            if (habit == null) {
                OutputUtils.printError("Invalid habit selection. Please select a valid habit ID.");
                return;
            }

            HabitRecord[] records = habitManager.getRecordsByHabitId(habitId);
            
            OutputUtils.printSectionHeader("📅", "History for: " + habit.getName());
            
            if (records.length == 0) {
                OutputUtils.printInfo("No completion records found for this habit.");
                return;
            }

            // Sort records by timestamp (most recent first)
            java.util.Arrays.sort(records, (a, b) -> 
                Long.compare(b.getTimestamp().getTimestamp(), a.getTimestamp().getTimestamp()));

            for (HabitRecord record : records) {
                Timestamp timestamp = record.getTimestamp();
                String dateStr = String.format("%04d-%02d-%02d", 
                    timestamp.getYear(), timestamp.getMonth(), timestamp.getDay());
                System.out.println("✅ " + dateStr);
            }

        } catch (NumberFormatException e) {
            OutputUtils.printError("Invalid input. Please enter a valid habit number.");
        }
    }
}
