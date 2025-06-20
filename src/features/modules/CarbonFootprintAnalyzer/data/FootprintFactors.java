package features.modules.CarbonFootprintAnalyzer.data;

import features.modules.CarbonFootprintAnalyzer.instances.FootprintFactor;

/**
 * Contains predefined carbon footprint factors for various activities.
 * Each factor includes the activity name, abbreviation, emission factor,
 * unit of measurement, and tips for reducing carbon footprint.
 * Can be extended with additional factors as needed.
 */
public class FootprintFactors {
    public static final FootprintFactor[] FACTORS = {
            new FootprintFactor("Driving a car", "car", 0.16984, "km",
                    new String[]{
                            "Use public transportation when possible.",
                            "Carpool with friends or colleagues.",
                            "Maintain your vehicle to improve fuel efficiency.",
                            "Consider switching to an electric or hybrid vehicle."
                    }),
            new FootprintFactor("Flying a plane", "plane", 0.17580, "km",
                    new String[]{
                            "Choose direct flights to reduce emissions.",
                            "Offset your flight emissions through carbon offset programs.",
                            "Travel by train or bus for shorter distances.",
                            "Consider virtual meetings instead of flying."
                    }),
            new FootprintFactor("Electricity consumption", "electricity", 0.20493, "kWh",
                    new String[]{
                            "Switch to renewable energy sources like solar or wind.",
                            "Use energy-efficient appliances and LED lighting.",
                            "Unplug devices when not in use to save energy.",
                            "Reduce heating and cooling by using programmable thermostats."
                    }),
            new FootprintFactor("Water usage", "water", 0.00015311, "liters",
                    new String[]{
                            "Fix leaks in faucets and toilets to conserve water.",
                            "Take shorter showers and turn off the tap while brushing teeth.",
                            "Use water-efficient fixtures and appliances.",
                            "Collect rainwater for gardening and outdoor use."
                    }),
            new FootprintFactor("Plastic usage", "plastic", 3.16478049, "kg",
                    new String[]{
                            "Reduce single-use plastics by using reusable bags, bottles, and containers.",
                            "Recycle plastic products properly to minimize waste.",
                            "Choose products with minimal or no plastic packaging.",
                            "Support companies that use sustainable materials."
                    }),
    };

    /**
     * Retrieves a FootprintFactor by its abbreviation.
     *
     * @param abbreviation The abbreviation of the footprint factor.
     * @return The FootprintFactor corresponding to the abbreviation.
     */
    public static FootprintFactor getFactorByAbbreviation(String abbreviation) {
        for (FootprintFactor factor : FACTORS) {
            if (factor.getAbbreviation().equalsIgnoreCase(abbreviation)) {
                return factor;
            }
        }

        // This should never happen as the input is validated correctly,
        throw new IllegalArgumentException("No footprint factor found for abbreviation: " + abbreviation);
    }
}
