package features.modules.CarbonFootprintAnalyzer.commands.data;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.KeywordArgument;
import core.cli.arguments.PositionalArgument;
import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.data.FootprintFactors;
import features.modules.CarbonFootprintAnalyzer.handlers.data.FootprintDataEditHandler;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintFactor;

import java.util.Arrays;

public class FootprintDataEditCommand extends CommandInstance {
    public FootprintDataEditCommand() {
        super(
                "edit",
                "Edit a specific carbon footprint record",
                "87 --activity car --amount 8.7",
                new ArgumentList(
                        new PositionalArgument("index", "The index of the record to delete", ArgumentDataType.INTEGER),
                        new KeywordArgument("activity", "a", "The activity type (e.g., car, flight, etc.)", new ArgumentDataType("enum",
                                Arrays.stream(FootprintFactors.FACTORS)
                                        .map(FootprintFactor::getAbbreviation)
                                        .toArray(String[]::new)
                        ), false),
                        new KeywordArgument("amount", "m", "The amount of activity performed in corresponding units", ArgumentDataType.FLOAT, false)
                ),
                new FootprintDataEditHandler()
        );
    }
}
