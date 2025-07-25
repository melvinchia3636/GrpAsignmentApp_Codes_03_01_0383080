package features.modules.CarbonFootprintAnalyzer.handlers.stats;

import core.cli.commands.CommandInstance;
import core.instances.ListOfKVs;
import core.manager.GlobalManager;
import core.terminal.OutputUtils;
import features.modules.CarbonFootprintAnalyzer.data.FootprintFactors;
import features.modules.CarbonFootprintAnalyzer.data.FootprintManager;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintFactor;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintRecord;

import java.util.Arrays;

public class FootprintStatsBreakdownHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        String lastXDaysString = argsMap.get("last");
        int lastXDays = lastXDaysString != null ? Integer.parseInt(lastXDaysString) : 7;

        FootprintManager footprintManager = GlobalManager.getInstance().getFootprintManager();

        if (footprintManager.getRecords().isEmpty()) {
            OutputUtils.printError("No carbon footprint records found.", false);
            OutputUtils.printTip("Log your first activity using the 'footprint log' command.");
            return;
        }

        FootprintRecord[] filteredRecords = footprintManager.getRecordsForLastXDays(lastXDays);

        if (filteredRecords.length == 0) {
            OutputUtils.printError("No records found for the specified period.", false);
            return;
        }

        double totalAmount = Arrays.stream(filteredRecords)
                .mapToDouble(FootprintRecord::getEstimatedFootprint)
                .sum();

        OutputUtils.printSectionHeader("Emission Breakdown by Activity Type for the Last " + lastXDays + " Days");

        ListOfKVs<FootprintFactor, Double> activityBreakdown = new ListOfKVs<>();

        for (FootprintFactor factor : FootprintFactors.FACTORS) {
            FootprintRecord[] recordsForFactor = footprintManager.getRecordsForFactor(factor, lastXDays);

            double totalAmountForFactor = Arrays.stream(recordsForFactor)
                    .mapToDouble(FootprintRecord::getEstimatedFootprint)
                    .sum();

            activityBreakdown.put(factor, totalAmountForFactor);
        }

        activityBreakdown.sortByValue();
        activityBreakdown.reverse();

        for (ListOfKVs.Entry<FootprintFactor, Double> activity : activityBreakdown.entries()) {
            String activityName = activity.getKey().getName();
            double amount = activity.getValue();
            double percentage = (amount / totalAmount) * 100;

            OutputUtils.printDataRow(activityName, 
                String.format("%.2f kg CO2e (%.2f%%)", amount, percentage));
        }

        System.out.println();
        if (!activityBreakdown.entries().isEmpty()) {
            activityBreakdown.entries().get(0).getKey().printTips();
        }
    }
}
