package features.modules.CarbonFootprintAnalyzer.commands.goal;

import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.handlers.goal.FootprintGoalViewHandler;

public class FootprintGoalViewCommand extends CommandInstance {
    public FootprintGoalViewCommand() {
        super("view",
                "View your current carbon footprint goal",
                "",
                new FootprintGoalViewHandler()
        );
    }
}
