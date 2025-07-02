package features.modules.GreenHabitTracker.commands;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.PositionalArgument;
import core.cli.commands.CommandInstance;
import features.modules.GreenHabitTracker.handlers.HabitAddHandler;

public class HabitRemoveCommand extends CommandInstance {
      public HabitRemoveCommand() {
          super(
                  "add",
                  "Add a new green habit",
                  "'Use reusable shopping bag'",
                  new ArgumentList(
                          new PositionalArgument("habit", "The green habit to add", ArgumentDataType.STRING)
                  ),
                  new HabitAddHandler()
          );
      }
}
