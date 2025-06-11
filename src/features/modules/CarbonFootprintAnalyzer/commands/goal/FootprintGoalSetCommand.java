package features.modules.CarbonFootprintAnalyzer.commands.goal;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.PositionalArgument;
import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.handlers.goal.FootprintGoalSetHandler;

public class FootprintGoalSetCommand extends CommandInstance {
    public FootprintGoalSetCommand() {
        super("set", "Set your everyday carbon footprint goal",
                "4.5",
                new ArgumentList(
                        new PositionalArgument[]{
                                new PositionalArgument(
                                        "goal",
                                        "The target carbon footprint per day in kg CO2e",
                                        ArgumentDataType.FLOAT
                                )
                        }
                ),
                new FootprintGoalSetHandler()
        );
    }
}
