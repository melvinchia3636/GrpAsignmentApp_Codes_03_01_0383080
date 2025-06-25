package features.modules.CarbonFootprintAnalyzer.commands.stats;

import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.handlers.stats.FootprintStatsChartHandler;

public class FootprintStatsChartCommand extends CommandInstance {
    public FootprintStatsChartCommand() {
        super(
                "chart",
                "View a chart of your weekly carbon footprint emissions",
                "",
                new FootprintStatsChartHandler()
        );
    }
}
