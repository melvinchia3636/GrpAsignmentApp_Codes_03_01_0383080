package features.modules.CarbonFootprintAnalyzer.handlers.data;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
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
            System.out.println(new Chalk("Log your first activity using the 'footprint log' command.").yellow());
            return;
        }

        FootprintRecord[] filteredRecords = footprintManager.getRecordsForLastXDays(lastXDays);

        if (filteredRecords.length == 0) {
            OutputUtils.printError("No records found for the specified period.", false);
            return;
        }

        String separator = "╔═══════╦═══════════════════════════╦════════════╦════════╦══════════════════════╗";
        String header    = "║ Index ║ Activity                  ║ Amount     ║ Unit   ║ Time                 ║";
        String divider   = "╠═══════╬═══════════════════════════╬════════════╬════════╬══════════════════════╣";
        String footer    = "╚═══════╩═══════════════════════════╩════════════╩════════╩══════════════════════╝";

        System.out.println();
        System.out.printf(new Chalk("🌿 Your Carbon Footprint History for the Last %d Days: \n").bold().toString(), lastXDays);
        System.out.println(separator);
        System.out.println(header);
        System.out.println(divider);

        FootprintRecord[] sortedRecords = Arrays.stream(filteredRecords)
                .sorted((r1, r2) -> Long.compare(r2.getTimestamp().getTimestamp(), r1.getTimestamp().getTimestamp()))
                .toArray(FootprintRecord[]::new);

        for (FootprintRecord record : sortedRecords) {
            System.out.printf(
                    "║ %-5s ║ %-25s ║ %-10.2f ║ %-6s ║ %-20s ║%n",
                    record.getIndex(),
                    record.getFactor().getActivity(),
                    record.getAmount(),
                    record.getFactor().getPerUnit(),
                    record.getTimestamp()
            );
        }

        System.out.println(footer);
    }
}
