package features.modules.CarbonFootprintAnalyzer.handlers.stats;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.modules.CarbonFootprintAnalyzer.data.FootprintManager;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintRecord;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Stream;

public class FootprintStatsSummaryHandler extends CommandInstance.Handler {
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

        double[] amounts = Stream.of(filteredRecords)
                .mapToDouble(record -> record.amount)
                .toArray();

        DoubleSummaryStatistics statistics = Arrays.stream(amounts).summaryStatistics();

        double totalAmount = statistics.getSum();
        double averageAmount = totalAmount / lastXDays;
        double highestAmount = statistics.getMax();
        double lowestAmount = statistics.getMin();

        System.out.println();
        System.out.println(new Chalk("📊 Carbon Footprint Summary for the Last " + lastXDays + " Days:").bold());
        System.out.println();
        System.out.printf("Total Amount: %.4f kg CO2e%n", totalAmount);
        System.out.printf("Average Amount: %.4f kg CO2e%n", averageAmount);
        System.out.printf("Highest Amount: %.4f kg CO2e%n", highestAmount);
        System.out.printf("Lowest Amount: %.4f kg CO2e%n", lowestAmount);
        System.out.println();
        System.out.println("🧠Tip: Setting a daily goal helps you stay consistent!");
    }
}
