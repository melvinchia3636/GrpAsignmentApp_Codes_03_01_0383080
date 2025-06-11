package features.modules.CarbonFootprintAnalyzer.commands.stats;

import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.handlers.stats.FootprintStatsBreakdownHandler;

public class FootprintStatsBreakdownCommand extends CommandInstance {
    public FootprintStatsBreakdownCommand() {
        super(
                "breakdown",
                "Analyze the breakdown of your carbon footprint by category",
                "",
                new FootprintStatsBreakdownHandler()
        );
    }
}
