package features.modules.CarbonFootprintAnalyzer.instances;

import core.instances.Timestamp;

/**
 * Represents a record of a carbon footprint activity logged by a user.
 * Contains information about the activity type, amount, timestamp, and calculated emissions.
 */
public class FootprintRecord {
    private final int index;
    private final FootprintFactor factor;
    private final double amount;
    private final Timestamp timestamp;

    /**
     * Constructs a new FootprintRecord with the specified parameters.
     *
     * @param index     the unique index of this record
     * @param factor    the footprint factor defining the activity type and emission calculation
     * @param amount    the amount of activity performed
     * @param timestamp the timestamp when this activity was recorded
     */
    public FootprintRecord(int index, FootprintFactor factor, double amount, Timestamp timestamp) {
        this.index = index;
        this.factor = factor;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    /**
     * Returns a string representation of this footprint record.
     *
     * @return a string containing the activity, amount, and timestamp
     */
    @Override
    public String toString() {
        return "FootprintRecord{" +
                "activity='" + getFactor() + '\'' +
                ", amount=" + getAmount() +
                ", timestamp=" + getTimestamp() +
                '}';
    }

    /**
     * Converts this record to a string array for CSV export or other purposes.
     *
     * @return an array containing the factor abbreviation, amount, and timestamp
     */
    public String[] toArray() {
        return new String[]{getFactor().getAbbreviation(), String.valueOf(getAmount()), String.valueOf(getTimestamp().getTimestamp())};
    }

    /**
     * Gets the index of this record.
     *
     * @return the record index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the footprint factor associated with this record.
     *
     * @return the footprint factor
     */
    public FootprintFactor getFactor() {
        return factor;
    }

    /**
     * Gets the amount of activity performed.
     *
     * @return the activity amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Calculates the estimated carbon footprint for this record.
     *
     * @return the estimated footprint in kg CO2e
     */
    public double getEstimatedFootprint() {
        return factor.getEstimatedFootprint(amount);
    }

    /**
     * Gets the timestamp when this activity was recorded.
     *
     * @return the timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        FootprintRecord that = (FootprintRecord) obj;
        return index == that.index &&
                Double.compare(that.amount, amount) == 0 &&
                factor.equals(that.factor) &&
                timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        int result = index;
        result = 31 * result + factor.hashCode();
        result = 31 * result + Double.hashCode(amount);
        result = 31 * result + timestamp.hashCode();
        return result;
    }
}
