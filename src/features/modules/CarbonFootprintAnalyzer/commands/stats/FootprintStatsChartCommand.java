package features.modules.CarbonFootprintAnalyzer.commands.stats;

import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.handlers.stats.FootprintStatsChartHandler;

public class FootprintStatsChartCommand extends CommandInstance {
    public FootprintStatsChartCommand() {
        super(
                "chart",
                "Generates a chart of your carbon footprint over time.",
                "",
                new FootprintStatsChartHandler()
        );
    }
}
