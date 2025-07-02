package features.modules.CarbonFootprintAnalyzer.handlers;

import core.cli.commands.CommandInstance;
import core.instances.Timestamp;
import core.manager.GlobalManager;
import core.terminal.OutputUtils;
import features.modules.CarbonFootprintAnalyzer.data.FootprintFactors;
import features.modules.CarbonFootprintAnalyzer.data.FootprintManager;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintFactor;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintRecord;

public class FootprintLogHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        String activity = argsMap.get("activity");
        String amountStr = argsMap.get("amount");

        // No need to catch exception, as the ArgumentDataType will ensure it's a valid enum
        FootprintFactor factor = FootprintFactors.getFactorByAbbreviation(activity);

        // No need to catch exception, as the ArgumentDataType will ensure it's a valid float
        float amount = Float.parseFloat(amountStr);

        if (amount <= 0) {
            OutputUtils.printError("The amount must be a positive non-zero number.");
            return;
        }

        FootprintManager footprintManager = GlobalManager.getInstance().getFootprintManager();

        FootprintRecord record = new FootprintRecord(
                footprintManager.getRecords().toArray().length,
                factor,
                amount,
                new Timestamp(System.currentTimeMillis())
        );

        footprintManager.addRecord(record);

        printResult(record);
    }

    private void printResult(FootprintRecord record) {
        OutputUtils.printSectionHeader("✅", "Carbon Footprint Entry Logged");
        
        OutputUtils.printDataRow("Activity", record.getFactor().getName());
        OutputUtils.printDataRow("Amount", String.format("%.2f %s", record.getAmount(), record.getFactor().getPerUnit()));
        OutputUtils.printDataRow("Time", record.getTimestamp().toString());
        
        double footprint = record.getFactor().getEstimatedFootprint(record.getAmount());
        OutputUtils.printStatistic("🔥", "Estimated carbon footprint", String.format("%.6f kg CO2e", footprint), "blue");
        
        System.out.println();
        record.getFactor().printTips();
    }
}
