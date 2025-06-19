package features.modules.CarbonFootprintAnalyzer.handlers.stats;

import core.cli.commands.CommandInstance;
import core.instances.SimpleMap;
import core.manager.GlobalManager;
import core.terminal.Chalk;
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
            System.out.println(new Chalk("Log your first activity using the 'footprint log' command.").yellow());
            return;
        }

        FootprintRecord[] filteredRecords = footprintManager.getRecordsForLastXDays(lastXDays);

        if (filteredRecords.length == 0) {
            OutputUtils.printError("No records found for the specified period.", false);
            return;
        }

        double totalAmount = Arrays.stream(filteredRecords)
                .mapToDouble(record -> record.amount)
                .sum();

        System.out.println();
        System.out.println("\uD83E\uDDFEEmission Breakdown by Activity Type for the Last " + lastXDays + " Days:\n");

        SimpleMap<FootprintFactor, Double> activityBreakdown = new SimpleMap<>();

        for (FootprintFactor factor : FootprintFactors.FACTORS) {
            FootprintRecord[] recordsForFactor = footprintManager.getRecordsForFactor(factor, lastXDays);

            double totalAmountForFactor = Arrays.stream(recordsForFactor)
                    .mapToDouble(record -> record.amount)
                    .sum();

            activityBreakdown.put(factor, totalAmountForFactor);
        }

        activityBreakdown.sortByValue();
        activityBreakdown.reverse();

        for (SimpleMap.Entry<FootprintFactor, Double> activity : activityBreakdown.entries()) {
            String activityName = activity.key.activity;
            double amount = activity.value;

            double percentage = (amount / totalAmount) * 100;

            System.out.printf("  - %-25s:    %.2f kg CO2e    (%.2f%%)\n", activityName, amount, percentage);
        }

        System.out.println();
        activityBreakdown.entries().get(0).key.printTips();
    }
}
