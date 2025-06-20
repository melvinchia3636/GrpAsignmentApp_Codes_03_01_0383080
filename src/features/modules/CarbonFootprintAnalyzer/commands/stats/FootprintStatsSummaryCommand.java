package features.modules.CarbonFootprintAnalyzer.commands.stats;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.KeywordArgument;
import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.handlers.stats.FootprintStatsSummaryHandler;

public class FootprintStatsSummaryCommand extends CommandInstance {
    public FootprintStatsSummaryCommand() {
        super(
                "summary",
                "Displays a summary of your carbon footprint analysis.",
                "",
                new ArgumentList(
                        new KeywordArgument("last", "l", "Display summary for the last N days", ArgumentDataType.INTEGER, false)
                ),
                new FootprintStatsSummaryHandler()
        );
    }
}
