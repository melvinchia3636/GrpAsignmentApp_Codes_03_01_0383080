package features.modules.CarbonFootprintAnalyzer.commands.stats;

import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.handlers.stats.FootprintStatsSummaryHandler;

public class FootprintStatsSummaryCommand extends CommandInstance {
    public FootprintStatsSummaryCommand() {
        super(
                "summary",
                "Displays a summary of your carbon footprint analysis.",
                "",
                new FootprintStatsSummaryHandler()
        );
    }
}
