package features.modules.CarbonFootprintAnalyzer.instances;

import core.terminal.Chalk;

import java.util.Random;

/**
 * Represents a carbon footprint factor for a specific activity.
 * This class is used to store the details of a carbon footprint factor,
 * including the activity name, abbreviation, factor value, unit of measurement,
 * and tips for reducing the carbon footprint associated with that activity.
 */
public class FootprintFactor {
    private final String name;
    private final String abbreviation;
    private final double factor;
    private final String perUnit;
    private final String[] tips;

    public FootprintFactor(String name, String abbreviation, double factor, String perUnit, String[] tips) {
        this.name = name;
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
        return getFactor() * amount;
    }

    public void printTips() {
        if (getTips().length == 0) {
            System.out.println("No tips available for this activity.");
            return;
        }

        Random random = new Random();
        int tipIndex = random.nextInt(getTips().length);
        String tip = getTips()[tipIndex];

        System.out.println("🌿Tip: " + new Chalk(tip).bold());
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public double getFactor() {
        return factor;
    }

    public String getPerUnit() {
        return perUnit;
    }

    public String[] getTips() {
        return tips;
    }
}
