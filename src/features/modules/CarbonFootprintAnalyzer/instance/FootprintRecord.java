package features.modules.CarbonFootprintAnalyzer.instance;

import core.instances.Timestamp;

public class FootprintRecord {
    public final FootprintFactor factor;
    public final float amount;
    public final Timestamp timestamp;

    public FootprintRecord(FootprintFactor factor, float amount, Timestamp timestamp) {
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
