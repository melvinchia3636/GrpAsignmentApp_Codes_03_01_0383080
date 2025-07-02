package features.modules.GreenHabitTracker.handlers;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.OutputUtils;
import features.modules.GreenHabitTracker.data.HabitManager;
import features.modules.GreenHabitTracker.instances.Habit;

import java.util.ArrayList;

public class HabitTodayHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        HabitManager habitManager = GlobalManager.getInstance().getHabitManager();
        ArrayList<Habit> habits = habitManager.getHabits();

        if (habits.isEmpty()) {
            OutputUtils.printInfo("No habits have been added yet. Use 'habit add' to create your first habit!");
            return;
        }

        System.out.println("Today's Habits:");
        
        for (Habit habit : habits) {
            boolean completed = habitManager.isHabitCompletedToday(habit.getId());
            String status = completed ? "✓" : " ";
            System.out.printf("[%s] %s%n", status, habit.getName());
        }
    }
}
