package features.modules.GreenHabitTracker.handlers;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.OutputUtils;
import features.modules.GreenHabitTracker.data.HabitManager;
import features.modules.GreenHabitTracker.instances.Habit;

public class HabitRemoveHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        String habitIdStr = argsMap.get("habit_id");
        
        int habitId = Integer.parseInt(habitIdStr);
        
        HabitManager habitManager = GlobalManager.getInstance().getHabitManager();
        Habit habit = habitManager.getHabitById(habitId);
        
        if (habit == null) {
            OutputUtils.printError("Habit with ID " + habitId + " not found.");
            return;
        }
        
        String habitName = habit.getName();
        boolean success = habitManager.removeHabit(habitId);
        
        if (success) {
            OutputUtils.printSuccess("Habit '" + habitName + "' has been removed successfully.");
        } else {
            OutputUtils.printError("Failed to remove habit.");
        }
    }
}
