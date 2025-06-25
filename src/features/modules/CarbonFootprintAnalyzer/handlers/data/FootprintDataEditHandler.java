package features.modules.CarbonFootprintAnalyzer.handlers.data;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.modules.CarbonFootprintAnalyzer.data.FootprintFactors;
import features.modules.CarbonFootprintAnalyzer.data.FootprintManager;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintRecord;

public class FootprintDataEditHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        String indexStr = argsMap.get("index");
        int index = Integer.parseInt(indexStr);

        String activity = argsMap.get("activity");
        String amountStr = argsMap.get("amount");
        double amount = 0.0;

        if (amountStr != null && !amountStr.isEmpty()) {
            amount = Double.parseDouble(amountStr);
        }

        if ((activity == null || activity.isEmpty()) && amount <= 0) {
            OutputUtils.printError("You must provide either an activity or a positive non-zero amount to edit the record.");
            return;
        }

        FootprintManager footprintManager = GlobalManager.getInstance().getFootprintManager();
        FootprintRecord record = footprintManager.getRecordByIndex(index);

        if (record == null) {
            OutputUtils.printError("No carbon footprint record found at index " + index + ".");
            return;
        }

        footprintManager.updateRecord(
                record,
                activity != null ? FootprintFactors.getFactorByAbbreviation(activity) : record.getFactor(),
                amount > 0 ? amount : record.getAmount()
        );

        OutputUtils.printSuccess("Changes made to the carbon footprint record at index " + index + ":");

        String activityString = String.format(
                "  - Activity: %s%n",
                record.getFactor().getName() +
                        (activity != null ? " -> " + FootprintFactors.getFactorByAbbreviation((activity)).getName() : ""));
        String amountString = String.format(
                "  - Amount: %2f %s%n",
                record.getAmount(),
                record.getFactor().getPerUnit() + (amount > 0 ? " -> " + amount + " " + record.getFactor().getPerUnit() : ""));

        if (activity != null && !activity.isEmpty()) {
            System.out.print(new Chalk(activityString).yellow().bold());
        } else {
            System.out.print(activityString);
        }

        if (amount > 0) {
            System.out.print(new Chalk(amountString).yellow().bold());
        } else {
            System.out.print(amountString);
        }
    }
}
