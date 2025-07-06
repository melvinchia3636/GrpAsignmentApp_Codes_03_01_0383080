package features.modules.CarbonFootprintAnalyzer.handlers.data;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.modules.CarbonFootprintAnalyzer.data.FootprintManager;

import java.util.Scanner;

public class FootprintDataClearHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        OutputUtils.printSectionHeader("Clear All Carbon Footprint Data");
        OutputUtils.printWarning("This will permanently delete all your carbon footprint records!");
        
        Scanner sc = new Scanner(System.in);
        System.out.print(new Chalk("Are you sure you want to clear all carbon footprint data? This action cannot be undone (yes/no): ").red());
        String confirmation = sc.nextLine().trim().toLowerCase();

        if ("yes".equals(confirmation)) {
            FootprintManager footprintManager = GlobalManager.getInstance().getFootprintManager();
            footprintManager.clearRecords(true);
            OutputUtils.printSuccess("All carbon footprint data has been cleared.");
            OutputUtils.printTip("You can start fresh by logging new activities with 'footprint log'.");
        } else {
            OutputUtils.printInfo("Operation cancelled. No data was cleared.");
        }
        
        sc.close();
    }
}
