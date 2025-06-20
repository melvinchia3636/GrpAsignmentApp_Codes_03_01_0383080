package features.modules.CarbonFootprintAnalyzer.commands.data;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.KeywordArgument;
import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.handlers.data.FootprintDataHistoryHandler;

public class FootprintDataHistoryCommand extends CommandInstance {
    public FootprintDataHistoryCommand() {
        super(
                "history",
                "View your carbon footprint history as a table",
                "",
                new ArgumentList(
                        new KeywordArgument("last", "l", "Display history for the last N days", ArgumentDataType.INTEGER, false)
                ),
                new FootprintDataHistoryHandler()
        );
    }
}
