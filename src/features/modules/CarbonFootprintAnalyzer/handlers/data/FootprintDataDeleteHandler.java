package features.modules.CarbonFootprintAnalyzer.handlers.data;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.modules.CarbonFootprintAnalyzer.data.FootprintManager;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintRecord;

import java.util.Scanner;

public class FootprintDataDeleteHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        String indexStr = argsMap.get("index");
        int index = Integer.parseInt(indexStr);
        FootprintManager footprintManager = GlobalManager.getInstance().getFootprintManager();
        FootprintRecord record = footprintManager.getRecordByIndex(index);

        if (record == null) {
            OutputUtils.printError("No carbon footprint record found at index " + index + ".");
            return;
        }

        OutputUtils.printSectionHeader("Delete Carbon Footprint Record");
        OutputUtils.printInfo("Found carbon footprint record at index " + index + ":");
        
        OutputUtils.printDataRow("Activity", record.getFactor().getName());
        OutputUtils.printDataRow("Amount", String.format("%.2f %s", record.getAmount(), record.getFactor().getPerUnit()));
        OutputUtils.printDataRow("Time", record.getTimestamp().toString());

        Scanner sc = new Scanner(System.in);
        System.out.print(new Chalk("\nAre you sure you want to delete this carbon footprint record? This action cannot be undone (yes/no): ").red());
        String confirmation = sc.nextLine().trim().toLowerCase();

        if ("yes".equals(confirmation)) {
            footprintManager.removeRecord(record);
            OutputUtils.printSuccess("Carbon footprint record at index " + index + " has been deleted.");
        } else {
            OutputUtils.printError("Operation cancelled. No data was deleted.", false);
        }
        
        sc.close();
    }
}
