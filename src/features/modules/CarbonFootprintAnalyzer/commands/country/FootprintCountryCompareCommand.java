package features.modules.CarbonFootprintAnalyzer.commands.country;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.KeywordArgument;
import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import features.modules.CarbonFootprintAnalyzer.handlers.country.FootprintCountryCompareHandler;

public class FootprintCountryCompareCommand extends CommandInstance {
    public FootprintCountryCompareCommand() {
        super(
                "compare",
                "Compare your carbon footprint with the average footprint of a country",
                "-c Singapore",
                new ArgumentList(
                        new KeywordArgument(
                                "country",
                                "c",
                                "The country to compare with",
                                new ArgumentDataType("enum",
                                        GlobalManager.getInstance().getGlobalEmissionsPerCapita().getEntities()),
                                false
                        )
                ),
                new FootprintCountryCompareHandler()
        );
    }
}
