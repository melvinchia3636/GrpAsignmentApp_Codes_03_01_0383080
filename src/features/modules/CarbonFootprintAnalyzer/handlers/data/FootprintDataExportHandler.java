package features.modules.CarbonFootprintAnalyzer.handlers.data;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import features.modules.CarbonFootprintAnalyzer.data.FootprintManager;

public class FootprintDataExportHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        FootprintManager footprintManager = GlobalManager.getInstance().getFootprintManager();
        footprintManager.exportRecords();
    }
}
