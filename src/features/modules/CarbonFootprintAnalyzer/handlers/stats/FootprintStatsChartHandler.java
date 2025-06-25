package features.modules.CarbonFootprintAnalyzer.handlers.stats;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.modules.CarbonFootprintAnalyzer.data.FootprintManager;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintRecord;

import java.util.Arrays;

public class FootprintStatsChartHandler extends CommandInstance.Handler {
    private static final String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    private static final int CHART_WIDTH = 50;

    @Override
    public void run() {
        FootprintManager footprintManager = GlobalManager.getInstance().getFootprintManager();

        if (footprintManager.getRecords().isEmpty()) {
            OutputUtils.printError("No carbon footprint records found.", false);
            System.out.println(new Chalk("Log your first activity using the 'footprint log' command.").yellow());
            return;
        }

        FootprintRecord[][] groupedRecords = footprintManager.getRecordsGroupedByWeekDay();
        double[] totalOfEachDay = Arrays.stream(groupedRecords).mapToDouble(
                records -> Arrays.stream(records)
                        .mapToDouble(FootprintRecord::getEstimatedFootprint)
                        .sum()
        ).toArray();
        double maxAmount = Arrays.stream(totalOfEachDay).max().orElse(0.0);

        if (maxAmount == 0) {
            OutputUtils.printError("No carbon footprint records found for the current week.", false);
            return;
        }

        System.out.println();
        System.out.println(new Chalk("📊Carbon Footprint Chart for the Current Week:").bold());

        for (int i = 0; i < daysOfWeek.length; i++) {
            String day = daysOfWeek[i];
            double total = totalOfEachDay[i];
            int barLength = (int) Math.round((total / maxAmount) * CHART_WIDTH);

            StringBuilder bar = new StringBuilder();
            for (int j = 0; j < barLength; j++) {
                bar.append("█");
            }
            for (int j = barLength; j < CHART_WIDTH; j++) {
                bar.append(" ");
            }

            System.out.printf("%s | %s %.2f kg%n",
                    day,
                    bar,
                    total
            );
        }
    }
}
