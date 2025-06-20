package features.modules.CarbonFootprintAnalyzer.instances;

import core.instances.Timestamp;

public class FootprintRecord {
    private final FootprintFactor factor;
    private final double amount;
    private final Timestamp timestamp;

    public FootprintRecord(FootprintFactor factor, double amount, Timestamp timestamp) {
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

    public FootprintFactor getFactor() {
        return factor;
    }

    public double getAmount() {
        return amount;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
