package features.modules.GreenHabitTracker.commands;

import core.cli.commands.CommandInstance;

public class HabitCommands extends CommandInstance {
    public HabitCommands() {
        super("habit", "Manage your green habits", new CommandInstance[] {
            new HabitAddCommand(),
            new HabitListCommand(),
            new HabitRemoveCommand(),
            new HabitLogCommand(),
            new HabitTodayCommand(),
            new HabitStatsCommand(),
            new HabitHistoryCommand()
        });
    }
}
