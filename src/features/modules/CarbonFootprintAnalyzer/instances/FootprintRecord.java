package features.modules.CarbonFootprintAnalyzer.instances;

import core.instances.Timestamp;

public class FootprintRecord {
    public final FootprintFactor factor;
    public final double amount;
    public final Timestamp timestamp;

    public FootprintRecord(FootprintFactor factor, double amount, Timestamp timestamp) {
        this.factor = factor;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "FootprintRecord{" +
                "activity='" + factor + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }

    public String[] toArray() {
        return new String[]{factor.abbreviation, String.valueOf(amount), String.valueOf(timestamp.getTimestamp())};
    }
}
