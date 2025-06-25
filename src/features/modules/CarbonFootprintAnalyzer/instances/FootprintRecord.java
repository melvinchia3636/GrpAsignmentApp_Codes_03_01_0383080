package features.modules.CarbonFootprintAnalyzer.instances;

import core.instances.Timestamp;

public class FootprintRecord {
    private final int index;
    private final FootprintFactor factor;
    private final double amount;
    private final Timestamp timestamp;

    public FootprintRecord(int index, FootprintFactor factor, double amount, Timestamp timestamp) {
        this.index = index;
        this.factor = factor;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "FootprintRecord{" +
                "activity='" + getFactor() + '\'' +
                ", amount=" + getAmount() +
                ", timestamp=" + getTimestamp() +
                '}';
    }

    public String[] toArray() {
        return new String[]{getFactor().getAbbreviation(), String.valueOf(getAmount()), String.valueOf(getTimestamp().getTimestamp())};
    }

    public int getIndex() {
        return index;
    }

    public FootprintFactor getFactor() {
        return factor;
    }

    public double getAmount() {
        return amount;
    }

    public double getEstimatedFootprint() {
        return factor.getEstimatedFootprint(amount);
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
