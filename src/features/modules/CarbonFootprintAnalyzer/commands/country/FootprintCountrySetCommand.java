package features.modules.CarbonFootprintAnalyzer.commands.country;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.PositionalArgument;
import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import features.modules.CarbonFootprintAnalyzer.handlers.country.FootprintCountrySetHandler;

public class FootprintCountrySetCommand extends CommandInstance {
    public FootprintCountrySetCommand() {
        super(
                "set",
                "Set the country for carbon footprint comparison.",
                "Malaysia",
                new ArgumentList(
                        new PositionalArgument(
                                "country",
                                "The country to set for carbon footprint comparison",
                                new ArgumentDataType("enum",
                                        GlobalManager.getInstance().getGlobalEmissionsPerCapita().getEntities())
                        )
                ),
                new FootprintCountrySetHandler()
        );
    }
}
