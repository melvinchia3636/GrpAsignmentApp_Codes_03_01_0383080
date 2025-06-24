package features.modules.CarbonFootprintAnalyzer.commands.data;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.PositionalArgument;
import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.handlers.data.FootprintDataDeleteHandler;

public class FootprintDataDeleteCommand extends CommandInstance {
    public FootprintDataDeleteCommand() {
        super(
                "delete",
                "Delete a specific carbon footprint record",
                "87",
                new ArgumentList(
                        new PositionalArgument("index", "The index of the record to delete", ArgumentDataType.INTEGER)
                ),
                new FootprintDataDeleteHandler()
        );
    }
}
