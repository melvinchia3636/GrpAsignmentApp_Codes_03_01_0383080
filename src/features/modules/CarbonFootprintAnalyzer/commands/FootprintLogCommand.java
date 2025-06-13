package features.modules.CarbonFootprintAnalyzer.commands;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.PositionalArgument;
import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.handlers.FootprintLogHandler;

public class FootprintLogCommand extends CommandInstance {
    public FootprintLogCommand() {
        super(
                "log",
                "Log a new carbon footprint entry.",
                "driving 8.7",
                new ArgumentList(
                        new PositionalArgument[]{
                                new PositionalArgument(
                                        "activity",
                                        "The activity for which you are logging the carbon footprint.",
                                        ArgumentDataType.STRING
                                ),
                                new PositionalArgument(
                                        "amount",
                                        "The amount of activity performed in corresponding units.",
                                        ArgumentDataType.FLOAT
                                )
                        }
                ),
                new FootprintLogHandler()
        );
    }
}
