package features.modules.GreenHabitTracker.commands;

import core.cli.commands.CommandInstance;
import features.modules.GreenHabitTracker.handlers.HabitListHandler;

public class HabitListCommand extends CommandInstance {
      public HabitListCommand() {
          super(
                    "list",
                    "List all green habits",
                    "'List all your green habits'",
                    new HabitListHandler()
          );
      }
}
