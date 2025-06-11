package features.modules.CarbonFootprintAnalyzer.commands.data;

import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.handlers.data.FootprintDataClearHandler;

public class FootprintDataClearCommand extends CommandInstance {
    public FootprintDataClearCommand() {
        super(
                "clear",
                "Clears the data of your carbon footprint analysis.",
                "",
                new FootprintDataClearHandler()
        );
    }
}
