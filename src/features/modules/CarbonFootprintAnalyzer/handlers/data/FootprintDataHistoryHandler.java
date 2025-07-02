package features.modules.CarbonFootprintAnalyzer.handlers.data;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.OutputUtils;
import features.modules.CarbonFootprintAnalyzer.data.FootprintManager;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintRecord;

import java.util.Arrays;

public class FootprintDataHistoryHandler extends CommandInstance.Handler {
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

        OutputUtils.printSectionHeader("🌿", "Your Carbon Footprint History for the Last " + lastXDays + " Days");
        
        String separator = "╔═══════╦═══════════════════════════╦════════════╦════════╦══════════════════════╗";
        String header    = "║ Index ║ Activity                  ║ Amount     ║ Unit   ║ Time                 ║";
        String divider   = "╠═══════╬═══════════════════════════╬════════════╬════════╬══════════════════════╣";
        String footer    = "╚═══════╩═══════════════════════════╩════════════╩════════╩══════════════════════╝";

        System.out.println(separator);
        System.out.println(header);
        System.out.println(divider);

        for (FootprintRecord record : filteredRecords) {
            System.out.printf(
                    "║ %-5s ║ %-25s ║ %-10.2f ║ %-6s ║ %-20s ║%n",
                    record.getIndex(),
                    record.getFactor().getName(),
                    record.getAmount(),
                    record.getFactor().getPerUnit(),
                    record.getTimestamp()
            );
        }

        System.out.println(footer);

        double totalFootprint = Arrays.stream(filteredRecords).mapToDouble(FootprintRecord::getEstimatedFootprint).sum();
        
        String[][] summaryStats = {
            {"Total records found", String.valueOf(filteredRecords.length), "blue"},
            {"Total carbon footprint", String.format("%.2f kg CO2e", totalFootprint), "red"}
        };
        
        OutputUtils.printSummaryBox("Summary", summaryStats);
    }
}
