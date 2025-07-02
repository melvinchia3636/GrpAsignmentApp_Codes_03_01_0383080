package core.instances;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * A wrapper class for timestamp operations that provides convenient methods
 * for working with timestamps and converting them to date/time components.
 */
public class Timestamp {
    private final long timestamp;

    /**
     * Creates a new Timestamp with the current system time.
     */
    public Timestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Creates a new Timestamp with the specified timestamp value.
     *
     * @param timestamp the timestamp in milliseconds since epoch
     */
    public Timestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the timestamp value in milliseconds since epoch.
     *
     * @return the timestamp value
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Converts the timestamp to a LocalDateTime in the system's default timezone.
     *
     * @return the LocalDateTime representation of this timestamp
     */
    private LocalDateTime getLocalDateTime() {
        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * Gets the year component of this timestamp.
     *
     * @return the year
     */
    public int getYear() {
        return getLocalDateTime().getYear();
    }

    /**
     * Gets the month component of this timestamp.
     *
     * @return the month (1-12)
     */
    public int getMonth() {
        return getLocalDateTime().getMonthValue(); // 1 to 12
    }

    /**
     * Gets the day component of this timestamp.
     *
     * @return the day of the month
     */
    public int getDay() {
        return getLocalDateTime().getDayOfMonth();
    }

    /**
     * Gets the hour component of this timestamp.
     *
     * @return the hour (0-23)
     */
    public int getHour() {
        return getLocalDateTime().getHour();
    }

    /**
     * Gets the minute component of this timestamp.
     *
     * @return the minute (0-59)
     */
    public int getMinute() {
        return getLocalDateTime().getMinute();
    }

    /**
     * Gets the second component of this timestamp.
     *
     * @return the second (0-59)
     */
    public int getSecond() {
        return getLocalDateTime().getSecond();
    }

    /**
     * Returns a string representation of this timestamp in the format "YYYY-MM-DD HH:MM:SS".
     *
     * @return a formatted string representation of the timestamp
     */
    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d %02d:%02d:%02d",
                getYear(), getMonth(), getDay(),
                getHour(), getMinute(), getSecond());
    }
}