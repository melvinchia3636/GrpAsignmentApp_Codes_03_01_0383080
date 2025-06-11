package features.modules.CarbonFootprintAnalyzer.commands.stats;

import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.handlers.stats.FootprintStatsStreakHandler;

public class FootprintStatsStreakCommand extends CommandInstance {
    public FootprintStatsStreakCommand() {
        super(
                "streak",
                "Analyze your carbon footprint streaks",
                "Show your longest streak of days with reduced carbon footprint",
                new FootprintStatsStreakHandler()
        );
    }
}
