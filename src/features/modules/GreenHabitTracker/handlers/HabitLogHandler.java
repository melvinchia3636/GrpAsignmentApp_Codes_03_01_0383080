package features.modules.GreenHabitTracker.handlers;

import core.cli.commands.CommandInstance;
import core.instances.Timestamp;
import core.manager.GlobalManager;
import core.terminal.OutputUtils;
import features.modules.GreenHabitTracker.data.HabitManager;
import features.modules.GreenHabitTracker.instances.Habit;

import java.util.ArrayList;
import java.util.Scanner;

public class HabitLogHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        HabitManager habitManager = GlobalManager.getInstance().getHabitManager();
        ArrayList<Habit> habits = habitManager.getHabits();

        if (habits.isEmpty()) {
            OutputUtils.printError("No habits have been added yet. Use 'habit add' to create your first habit!");
            return;
        }

        System.out.println("Which habit did you complete today?");

        for (Habit habit : habits) {
            System.out.printf("%d. %s%n", habit.getId(), habit.getName());
        }

        System.out.print("> ");
        
        try (Scanner scanner = new Scanner(System.in)) {
            int habitId = Integer.parseInt(scanner.nextLine().trim());
            
            Habit habit = habitManager.getHabitById(habitId);
            if (habit == null) {
                OutputUtils.printError("Invalid habit selection. Please select a valid habit ID.");
                return;
            }

            if (habitManager.isHabitCompletedToday(habitId)) {
                OutputUtils.printWarning("You have already logged this habit for today!");
                return;
            }

            boolean success = habitManager.logHabitCompletion(habitId);
            if (success) {
                Timestamp today = new Timestamp(System.currentTimeMillis());
                System.out.printf("✅ Habit '%s' marked as completed for today (%s)%n", 
                    habit.getName(), 
                    String.format("%04d-%02d-%02d", today.getYear(), today.getMonth(), today.getDay()));
            } else {
                OutputUtils.printError("Failed to log habit completion.");
            }

        } catch (NumberFormatException e) {
            OutputUtils.printError("Invalid input. Please enter a valid habit number.");
        }
    }
}
