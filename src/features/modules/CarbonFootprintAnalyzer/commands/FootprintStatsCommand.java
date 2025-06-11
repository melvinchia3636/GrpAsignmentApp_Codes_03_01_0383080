package features.modules.CarbonFootprintAnalyzer.commands;

import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.commands.stats.FootprintStatsBreakdownCommand;
import features.modules.CarbonFootprintAnalyzer.commands.stats.FootprintStatsChartCommand;
import features.modules.CarbonFootprintAnalyzer.commands.stats.FootprintStatsStreakCommand;
import features.modules.CarbonFootprintAnalyzer.commands.stats.FootprintStatsSummaryCommand;

public class FootprintStatsCommand extends CommandInstance {
    public FootprintStatsCommand() {
        super(
                "stats",
                "View your carbon footprint statistics",
                new CommandInstance[]{
                        new FootprintStatsSummaryCommand(),
                        new FootprintStatsBreakdownCommand(),
                        new FootprintStatsChartCommand(),
                        new FootprintStatsStreakCommand()
                }
        );
    }
}
