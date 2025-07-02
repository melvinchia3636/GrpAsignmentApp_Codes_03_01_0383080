package features.modules.GreenHabitTracker.commands;

import core.cli.commands.CommandInstance;
import features.modules.GreenHabitTracker.handlers.HabitLogHandler;

public class HabitLogCommand extends CommandInstance {
      public HabitLogCommand() {
          super(
                    "log",
                    "Log a habit for today",
                    "",
                    new HabitLogHandler()
          );
      }
}
