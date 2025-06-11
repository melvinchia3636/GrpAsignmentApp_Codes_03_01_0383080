package features.modules.CarbonFootprintAnalyzer.commands.data;

import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.handlers.data.FootprintDataHistoryHandler;

public class FootprintDataHistoryCommand extends CommandInstance {
    public FootprintDataHistoryCommand() {
        super(
                "history",
                "View your carbon footprint history as a table",
                "",
                new FootprintDataHistoryHandler()
        );
    }
}
