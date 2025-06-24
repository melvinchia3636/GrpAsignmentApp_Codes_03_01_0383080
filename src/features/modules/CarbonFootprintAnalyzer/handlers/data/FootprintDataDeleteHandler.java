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

        OutputUtils.printSuccess("Found carbon footprint record at index " + index + ":");
        System.out.printf("  - Activity: %s%n", record.getFactor().getActivity());
        System.out.printf("  - Amount: %2f %s%n", record.getAmount(), record.getFactor().getPerUnit());
        System.out.printf("  - Time: %s%n", record.getTimestamp());

        Scanner sc = new Scanner(System.in);
        System.out.print("\nAre you sure you want to delete this carbon footprint record? This action cannot be undone (yes/no): ");
        String confirmation = sc.nextLine().trim().toLowerCase();

        if ("yes".equals(confirmation)) {
            footprintManager.removeRecord(record);
            System.out.println(new Chalk("Carbon footprint record at index " + index + " has been deleted.").yellow());
        } else {
            OutputUtils.printError("Operation cancelled. No data was deleted.");
        }
    }
}
