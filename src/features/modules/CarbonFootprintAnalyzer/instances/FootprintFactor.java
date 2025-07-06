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

    /**
     * Constructs a new FootprintFactor with the specified parameters.
     *
     * @param name         the name of the activity
     * @param abbreviation a short abbreviation for the activity
     * @param factor       the emission factor (kg CO2e per unit)
     * @param perUnit      the unit of measurement for this activity
     * @param tips         an array of tips for reducing emissions from this activity
     */
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

    /**
     * Prints a random tip for reducing the carbon footprint of this activity.
     * If no tips are available, prints a message indicating so.
     */
    public void printTips() {
        if (getTips().length == 0) {
            System.out.println("No tips available for this activity.");
            return;
        }

        Random random = new Random();
        int tipIndex = random.nextInt(getTips().length);
        String tip = getTips()[tipIndex];

        System.out.println("[TIP] " + new Chalk(tip).bold());
    }

    /**
     * Gets the name of this activity.
     *
     * @return the activity name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the abbreviation of this activity.
     *
     * @return the activity abbreviation
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Gets the emission factor for this activity.
     *
     * @return the emission factor in kg CO2e per unit
     */
    public double getFactor() {
        return factor;
    }

    /**
     * Gets the unit of measurement for this activity.
     *
     * @return the unit description
     */
    public String getPerUnit() {
        return perUnit;
    }

    /**
     * Gets the array of tips for reducing emissions from this activity.
     *
     * @return an array of tip strings
     */
    public String[] getTips() {
        return tips;
    }
}
