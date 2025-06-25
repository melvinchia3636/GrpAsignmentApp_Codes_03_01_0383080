package features.modules.CarbonFootprintAnalyzer.commands;

import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.commands.data.*;

public class FootprintDataCommand extends CommandInstance {
    public FootprintDataCommand() {
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
