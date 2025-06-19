package features.modules.CarbonFootprintAnalyzer.instance;

/**
 * Represents a carbon footprint factor for a specific activity.
 * This class is used to store the details of a carbon footprint factor,
 * including the activity name, abbreviation, factor value, unit of measurement,
 * and tips for reducing the carbon footprint associated with that activity.
 */
public class FootprintFactor {
    public final String activity;
    public final String abbreviation;
    public final double factor;
    public final String perUnit;
    public final String[] tips;

    public FootprintFactor(String activity, String abbreviation, double factor, String perUnit, String[] tips) {
        this.activity = activity;
        this.abbreviation = abbreviation;
        this.factor = factor;
        this.perUnit = perUnit;
        this.tips = tips;
    }

    /**
     * Calculates the estimated carbon footprint for a given amount of activity.
     *
     * @param amount The amount of activity performed.
     * @return The estimated carbon footprint in kg CO2e.
     */
    public double getEstimatedFootprint(double amount) {
        return factor * amount;
    }
}
