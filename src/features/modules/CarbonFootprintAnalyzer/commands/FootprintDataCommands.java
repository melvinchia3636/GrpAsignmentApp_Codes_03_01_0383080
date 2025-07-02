package features.modules.CarbonFootprintAnalyzer.commands;

import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.commands.data.*;

public class FootprintDataCommands extends CommandInstance {
    public FootprintDataCommands() {
        super(
                "data",
                "Manage your carbon footprint data",
                new CommandInstance[]{
                        new FootprintDataHistoryCommand(),
                        new FootprintDataEditCommand(),
                        new FootprintDataDeleteCommand(),
                        new FootprintDataExportCommand(),
                        new FootprintDataClearCommand()
                }
        );
    }
}
