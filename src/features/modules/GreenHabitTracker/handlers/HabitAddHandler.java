package features.modules.GreenHabitTracker.handlers;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.OutputUtils;
import features.modules.GreenHabitTracker.data.HabitManager;
import features.modules.GreenHabitTracker.instances.Habit;

public class HabitAddHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        String habitName = argsMap.get("habit");

        if (habitName == null || habitName.trim().isEmpty()) {
            OutputUtils.printError("Habit name cannot be empty.");
            return;
        }

        HabitManager habitManager = GlobalManager.getInstance().getHabitManager();
        Habit habit = habitManager.addHabit(habitName.trim());

        OutputUtils.printSectionHeader("Add a New Habit");
        OutputUtils.printDataRow("Name", habit.getName());
        OutputUtils.printSuccess("Added successfully!");
    }
}
