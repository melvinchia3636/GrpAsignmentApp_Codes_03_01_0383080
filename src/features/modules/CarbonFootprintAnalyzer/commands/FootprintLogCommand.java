package features.modules.CarbonFootprintAnalyzer.commands;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.PositionalArgument;
import core.cli.commands.CommandInstance;
import core.terminal.Chalk;
import features.modules.CarbonFootprintAnalyzer.data.FootprintFactors;
import features.modules.CarbonFootprintAnalyzer.handlers.FootprintLogHandler;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintFactor;

import java.util.Arrays;

public class FootprintLogCommand extends CommandInstance {
    public FootprintLogCommand() {
        super(
                "log",
                "Log a new carbon footprint entry.",
                "driving 8.7",
                new ArgumentList(
                        new PositionalArgument(
                                "activity",
                                "The activity for which you are logging the carbon footprint.",
                                new ArgumentDataType("enum",
                                        Arrays.stream(FootprintFactors.FACTORS)
                                                .map(FootprintFactor::getAbbreviation)
                                                .toArray(String[]::new)
                                ),
                                String.format("Please enter the %s of the activity you performed:\n%s\n\n>",
                                        new Chalk("[code]").blue().bold(),
                                        String.join("\n", Arrays.stream(FootprintFactors.FACTORS)
                                                .map(e ->
                                                        String.format(
                                                                "  - %s %s (per %s)",
                                                                new Chalk("[" + e.getAbbreviation() + "]").blue().bold(),
                                                                e.getName(),
                                                                e.getPerUnit()
                                                        )
                                                )
                                                .toArray(String[]::new)
                                        )
                                )
                        ),
                        new PositionalArgument(
                                "amount",
                                "The amount of activity performed in corresponding units.",
                                ArgumentDataType.FLOAT,
                                "Please enter the amount of activity performed:"
                        )
                ),
                new FootprintLogHandler()
        );
    }
}
