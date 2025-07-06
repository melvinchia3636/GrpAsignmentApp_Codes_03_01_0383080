package features.modules.CarbonFootprintAnalyzer.handlers.stats;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.OutputUtils;
import features.modules.CarbonFootprintAnalyzer.data.FootprintManager;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintRecord;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;

public class FootprintStatsSummaryHandler extends CommandInstance.Handler {
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

        double[] amounts = Arrays.stream(filteredRecords)
                .mapToDouble(FootprintRecord::getEstimatedFootprint)
                .toArray();

        DoubleSummaryStatistics statistics = Arrays.stream(amounts).summaryStatistics();

        String[][] summaryStats = getSummaryStats(statistics, lastXDays);

        OutputUtils.printSummaryBox("Carbon Footprint Summary for the Last " + lastXDays + " Days", summaryStats);
        OutputUtils.printTip("Setting a daily goal helps you stay consistent!");
    }

    private static String[][] getSummaryStats(DoubleSummaryStatistics statistics, int lastXDays) {
        double totalAmount = statistics.getSum();
        double averageAmount = totalAmount / lastXDays;
        double highestAmount = statistics.getMax();
        double lowestAmount = statistics.getMin();

        return new String[][]{
            {"Total Amount", String.format("%.4f kg CO2e", totalAmount), "red"},
            {"Average Amount", String.format("%.4f kg CO2e", averageAmount), "blue"},
            {"Highest Amount", String.format("%.4f kg CO2e", highestAmount), "yellow"},
            {"Lowest Amount", String.format("%.4f kg CO2e", lowestAmount), "green"}
        };
    }
}
