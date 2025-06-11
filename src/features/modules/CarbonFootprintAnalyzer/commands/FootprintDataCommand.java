package features.modules.CarbonFootprintAnalyzer.commands;

import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.commands.data.FootprintDataClearCommand;
import features.modules.CarbonFootprintAnalyzer.commands.data.FootprintDataExportCommand;
import features.modules.CarbonFootprintAnalyzer.commands.data.FootprintDataHistoryCommand;

public class FootprintDataCommand extends CommandInstance {
    public FootprintDataCommand() {
        super(
                "data",
                "Manage your carbon footprint data",
                new CommandInstance[]{
                        new FootprintDataHistoryCommand(),
                        new FootprintDataExportCommand(),
                        new FootprintDataClearCommand()
                }
        );
    }
}
