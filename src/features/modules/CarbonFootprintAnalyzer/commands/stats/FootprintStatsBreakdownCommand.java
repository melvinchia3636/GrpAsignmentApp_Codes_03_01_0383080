package features.modules.CarbonFootprintAnalyzer.commands.stats;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.KeywordArgument;
import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.handlers.stats.FootprintStatsBreakdownHandler;

public class FootprintStatsBreakdownCommand extends CommandInstance {
    public FootprintStatsBreakdownCommand() {
        super(
                "breakdown",
                "Analyze the breakdown of your carbon footprint by category",
                "",
                new ArgumentList(
                        new KeywordArgument("last", "l", "Display breakdown for the last N days", ArgumentDataType.INTEGER, false)
                ),
                new FootprintStatsBreakdownHandler()
        );
    }
}
