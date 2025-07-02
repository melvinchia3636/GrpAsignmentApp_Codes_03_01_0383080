package features.modules.GreenHabitTracker.commands;

import core.cli.commands.CommandInstance;
import features.modules.GreenHabitTracker.handlers.HabitHistoryHandler;

public class HabitHistoryCommand extends CommandInstance {
      public HabitHistoryCommand() {
          super(
                    "history",
                    "View the history of a habit",
                    "",
                    new HabitHistoryHandler()
          );
      }
}
