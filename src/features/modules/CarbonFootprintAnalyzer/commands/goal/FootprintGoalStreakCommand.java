package features.modules.CarbonFootprintAnalyzer.commands.goal;

import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.handlers.goal.FootprintGoalStreakHandler;

public class FootprintGoalStreakCommand extends CommandInstance {
    public FootprintGoalStreakCommand() {
        super(
                "streak",
                "Analyze your carbon footprint streaks",
                "Show your longest streak of days with reduced carbon footprint",
                new FootprintGoalStreakHandler()
        );
    }
}
