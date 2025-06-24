package features.modules.CarbonFootprintAnalyzer.handlers.data;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.OutputUtils;
import features.modules.CarbonFootprintAnalyzer.data.FootprintManager;

import java.util.Scanner;

public class FootprintDataClearHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Are you sure you want to clear all carbon footprint data? This action cannot be undone (yes/no): ");
        String confirmation = sc.nextLine().trim().toLowerCase();

        if ("yes".equals(confirmation)) {
            FootprintManager footprintManager = GlobalManager.getInstance().getFootprintManager();
            footprintManager.clearRecords(true);
            System.out.println("All carbon footprint data has been cleared.");
        } else {
            OutputUtils.printError("Operation cancelled. No data was cleared.");
        }
    }
}
