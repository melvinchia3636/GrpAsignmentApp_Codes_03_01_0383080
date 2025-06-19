package features.modules.CarbonFootprintAnalyzer.handlers;

import core.cli.commands.CommandInstance;
import core.instances.Timestamp;
import core.manager.GlobalManager;
import core.terminal.Chalk;
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

        FootprintRecord record = new FootprintRecord(
                factor,
                amount,
                new Timestamp(System.currentTimeMillis())
        );

        FootprintManager footprintManager = GlobalManager.getInstance().getFootprintManager();
        footprintManager.addRecord(record);

        printResult(record);
    }

    private void printResult(FootprintRecord record) {
        System.out.println();
        OutputUtils.printSuccess("Successfully logged the carbon footprint entry:");
        System.out.printf("  - Activity: %s%n", record.factor.activity);
        System.out.printf("  - Amount: %s %s%n", record.amount, record.factor.perUnit);
        System.out.printf("  - Time: %s%n", record.timestamp);
        System.out.println();
        System.out.println("🔥Estimated carbon footprint: " + new Chalk(
                String.format("%6f kg CO2e", record.factor.getEstimatedFootprint(record.amount))
        ).blue().bold());
        System.out.println();
        record.factor.printTips();
    }
}
