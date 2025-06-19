package features.modules.CarbonFootprintAnalyzer.handlers.data;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import features.modules.CarbonFootprintAnalyzer.data.FootprintManager;

public class FootprintDataHistoryHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        FootprintManager footprintManager = GlobalManager.getInstance().getFootprintManager();

        if (footprintManager.getRecords().isEmpty()) {
            System.out.println("No carbon footprint records found.");
            System.out.println(new Chalk("Log your first activity using the 'footprint log' command.").yellow().bold());
            return;
        }

        String separator = "╔════════════════════╦════════════╦════════╦══════════════════════╗";
        String header    = "║ Activity           ║ Amount     ║ Unit   ║ Time                 ║";
        String divider   = "╠════════════════════╬════════════╬════════╬══════════════════════╣";
        String footer    = "╚════════════════════╩════════════╩════════╩══════════════════════╝";

        System.out.println();
        System.out.println(new Chalk("🌿 Your Carbon Footprint History: \n").bold());
        System.out.println(separator);
        System.out.println(header);
        System.out.println(divider);

        footprintManager.getRecords().forEach(record -> {
            System.out.printf(
                    "║ %-18s ║ %-10.2f ║ %-6s ║ %-20s ║%n",
                    record.factor.activity,
                    record.amount,
                    record.factor.perUnit,
                    record.timestamp
            );
        });

        System.out.println(footer);
    }
}
