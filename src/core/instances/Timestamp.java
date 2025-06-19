package core.instances;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Timestamp {
    private final long timestamp;

    public Timestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    public Timestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    private LocalDateTime getLocalDateTime() {
        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public int getYear() {
        return getLocalDateTime().getYear();
    }

    public int getMonth() {
        return getLocalDateTime().getMonthValue(); // 1 to 12
    }

    public int getDay() {
        return getLocalDateTime().getDayOfMonth();
    }

    public int getHour() {
        return getLocalDateTime().getHour();
    }

    public int getMinute() {
        return getLocalDateTime().getMinute();
    }

    public int getSecond() {
        return getLocalDateTime().getSecond();
    }

    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d %02d:%02d:%02d",
                getYear(), getMonth(), getDay(),
                getHour(), getMinute(), getSecond());
    }
}