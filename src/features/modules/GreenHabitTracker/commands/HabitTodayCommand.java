package features.modules.GreenHabitTracker.commands;

import core.cli.commands.CommandInstance;
import features.modules.GreenHabitTracker.handlers.HabitTodayHandler;

public class HabitTodayCommand extends CommandInstance {
      public HabitTodayCommand() {
          super(
                    "today",
                    "See the completion status of your habits for today",
                    "",
                    new HabitTodayHandler()
          );
      }
}
