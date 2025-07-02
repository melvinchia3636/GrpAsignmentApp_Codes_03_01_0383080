package features.modules.CarbonFootprintAnalyzer.commands;

import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.commands.goal.FootprintGoalSetCommand;
import features.modules.CarbonFootprintAnalyzer.commands.goal.FootprintGoalStreakCommand;
import features.modules.CarbonFootprintAnalyzer.commands.goal.FootprintGoalViewCommand;

public class FootprintGoalCommands extends CommandInstance {
    public FootprintGoalCommands() {
        super(
                "goal",
                "Set or view your carbon footprint goal",
                new CommandInstance[]{
                        new FootprintGoalViewCommand(),
                        new FootprintGoalStreakCommand(),
                        new FootprintGoalSetCommand(),
                }
        );
    }
}
