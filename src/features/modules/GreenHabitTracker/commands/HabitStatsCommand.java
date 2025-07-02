package features.modules.GreenHabitTracker.commands;

import core.cli.commands.CommandInstance;
import features.modules.GreenHabitTracker.handlers.HabitHistoryHandler;

public class HabitStatsCommand extends CommandInstance {
      public HabitStatsCommand() {
          super(
                    "stats",
                    "View the statistics of a habit",
                    "",
                    new HabitHistoryHandler()
          );
      }
}
