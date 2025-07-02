package features.modules.GreenHabitTracker.commands;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.PositionalArgument;
import core.cli.commands.CommandInstance;
import features.modules.GreenHabitTracker.handlers.HabitRemoveHandler;

public class HabitRemoveCommand extends CommandInstance {
      public HabitRemoveCommand() {
          super(
                  "remove",
                  "Remove a habit from tracking",
                  "1",
                  new ArgumentList(
                          new PositionalArgument(
                              "habit_id", 
                              "The ID of the habit to remove", 
                              ArgumentDataType.INTEGER, 
                              "Enter the ID of the habit to remove:"
                          )
                  ),
                  new HabitRemoveHandler()
          );
      }
}
