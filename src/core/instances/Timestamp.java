package core.instances;

/**
 * Represents a timestamp with millisecond precision, supporting breakdown into date and time components,
 * manipulation by various time units, and conversion to and from epoch milliseconds.
 * The timestamp is internally stored as milliseconds since the Unix epoch (1970-01-01 00:00:00 UTC).
 * All calculations are adjusted for the Malaysia time zone (UTC+8).
 */
public class Timestamp {
    private long timestamp;

    private static final int[] DAYS_IN_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final long TZ_OFFSET_MS = 8 * 3600 * 1000L;

    /**
     * Constructs a Timestamp representing the current system time.
     */
    public Timestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Constructs a Timestamp from the specified epoch milliseconds.
     *
     * @param timestamp the epoch milliseconds
     */
    public Timestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Returns the internal timestamp value in epoch milliseconds.
     *
     * @return the timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the internal timestamp value in epoch milliseconds.
     *
     * @param timestamp the timestamp in milliseconds
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Determines if the specified year is a leap year.
     *
     * @param year the year to check
     * @return true if leap year, false otherwise
     */
    private static boolean isLeap(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * Breaks down the timestamp into its date and time components, adjusted for Malaysia time zone.
     *
     * @return an array containing year, month, day, hour, minute, second, millisecond
     */
    private int[] breakDown() {
        long ms = timestamp + TZ_OFFSET_MS;
        long totalSeconds = ms / 1000;
        int milli = (int) (ms % 1000);

        long days = totalSeconds / 86400;
        int secOfDay = (int) (totalSeconds % 86400);

        int year = 1970;
        while (true) {
            int daysInYear = isLeap(year) ? 366 : 365;
            if (days >= daysInYear) {
                days -= daysInYear;
                year++;
            } else {
                break;
            }
        }

        int month = 1;
        for (int i = 0; i < 12; i++) {
            int dim = DAYS_IN_MONTH[i];
            if (i == 1 && isLeap(year)) dim++;
            if (days >= dim) {
                days -= dim;
                month++;
            } else {
                break;
            }
        }

        int day = (int) days + 1;
        int hour = secOfDay / 3600;
        int min = (secOfDay % 3600) / 60;
        int sec = (secOfDay % 60);

        return new int[]{year, month, day, hour, min, sec, milli};
    }

    /**
     * Gets the year component of the timestamp.
     *
     * @return the year
     */
    public int getYear() {
        return breakDown()[0];
    }

    /**
     * Gets the month component of the timestamp (1-12).
     *
     * @return the month
     */
    public int getMonth() {
        return breakDown()[1];
    }

    /**
     * Gets the day component of the timestamp (1-31).
     *
     * @return the day
     */
    public int getDay() {
        return breakDown()[2];
    }

    /**
     * Gets the day of the week for the timestamp.
     *
     * @return the day of the week (1=Monday, ..., 7=Sunday)
     */
    public int getDayOfWeek() {
        long ms = timestamp + TZ_OFFSET_MS;
        long totalDays = ms / (86400L * 1000L);

        int dayOfWeek = (int) ((totalDays + 4) % 7);
        if (dayOfWeek == 0) return 7;
        return dayOfWeek;
    }

    /**
     * Gets the hour component of the timestamp (0-23).
     *
     * @return the hour
     */
    public int getHour() {
        return breakDown()[3];
    }

    /**
     * Gets the minute component of the timestamp (0-59).
     *
     * @return the minute
     */
    public int getMinute() {
        return breakDown()[4];
    }

    /**
     * Gets the second component of the timestamp (0-59).
     *
     * @return the second
     */
    public int getSecond() {
        return breakDown()[5];
    }

    /**
     * Gets the millisecond component of the timestamp (0-999).
     *
     * @return the millisecond
     */
    public int getMillisecond() {
        return breakDown()[6];
    }

    /**
     * Sets the year component of the timestamp.
     *
     * @param year the year to set
     */
    public void setYear(int year) {
        setDate(year, getMonth(), getDay(), getHour(), getMinute(), getSecond(), getMillisecond());
    }

    /**
     * Sets the month component of the timestamp.
     *
     * @param month the month to set (1-12)
     */
    public void setMonth(int month) {
        setDate(getYear(), month, getDay(), getHour(), getMinute(), getSecond(), getMillisecond());
    }

    /**
     * Sets the day component of the timestamp.
     *
     * @param day the day to set (1-31)
     */
    public void setDay(int day) {
        setDate(getYear(), getMonth(), day, getHour(), getMinute(), getSecond(), getMillisecond());
    }

    /**
     * Sets the hour component of the timestamp.
     *
     * @param hour the hour to set (0-23)
     */
    public void setHour(int hour) {
        setDate(getYear(), getMonth(), getDay(), hour, getMinute(), getSecond(), getMillisecond());
    }

    /**
     * Sets the minute component of the timestamp.
     *
     * @param min the minute to set (0-59)
     */
    public void setMinute(int min) {
        setDate(getYear(), getMonth(), getDay(), getHour(), min, getSecond(), getMillisecond());
    }

    /**
     * Sets the second component of the timestamp.
     *
     * @param sec the second to set (0-59)
     */
    public void setSecond(int sec) {
        setDate(getYear(), getMonth(), getDay(), getHour(), getMinute(), sec, getMillisecond());
    }

    /**
     * Sets the millisecond component of the timestamp.
     *
     * @param ms the millisecond to set (0-999)
     */
    public void setMillisecond(int ms) {
        setDate(getYear(), getMonth(), getDay(), getHour(), getMinute(), getSecond(), ms);
    }

    /**
     * Sets the date and time components of the timestamp.
     *
     * @param year  the year
     * @param month the month (1-12)
     * @param day   the day (1-31)
     * @param hour  the hour (0-23)
     * @param min   the minute (0-59)
     * @param sec   the second (0-59)
     * @param ms    the millisecond (0-999)
     */
    private void setDate(int year, int month, int day, int hour, int min, int sec, int ms) {
        int days = 0;
        for (int y = 1970; y < year; y++) {
            days += isLeap(y) ? 366 : 365;
        }
        for (int m = 1; m < month; m++) {
            days += DAYS_IN_MONTH[m - 1];
            if (m == 2 && isLeap(year)) days += 1;
        }
        days += day - 1;

        long totalSeconds = days * 86400L + hour * 3600L + min * 60L + sec;
        this.timestamp = totalSeconds * 1000L + ms - TZ_OFFSET_MS;
    }

    /**
     * Adds the specified amount of time to the timestamp in the given unit.
     * Supported units: year, month, day, hour, minute, second, millisecond.
     *
     * @param unit   the unit of time to add
     * @param amount the amount to add (can be negative)
     * @throws IllegalArgumentException if the unit is invalid
     */
    public void add(String unit, int amount) {
        switch (unit.toLowerCase()) {
            case "year":
                setYear(getYear() + amount);
                break;
            case "month": {
                int y = getYear();
                int m = getMonth() + amount;
                while (m > 12) {
                    m -= 12;
                    y++;
                }
                while (m < 1) {
                    m += 12;
                    y--;
                }
                setYear(y);
                setMonth(m);
            }
            break;
            case "day":
                this.timestamp += amount * 86400L * 1000L;
                break;
            case "hour":
                this.timestamp += amount * 3600L * 1000L;
                break;
            case "minute":
                this.timestamp += amount * 60L * 1000L;
                break;
            case "second":
                this.timestamp += amount * 1000L;
                break;
            case "millisecond":
                this.timestamp += amount;
                break;
            default:
                throw new IllegalArgumentException("Invalid unit: " + unit);
        }
    }

    /**
     * Subtracts the specified amount of time from the timestamp in the given unit.
     * Supported units: year, month, day, hour, minute, second, millisecond.
     *
     * @param unit   the unit of time to subtract
     * @param amount the amount to subtract (can be negative)
     * @throws IllegalArgumentException if the unit is invalid
     */
    public void subtract(String unit, int amount) {
        add(unit, -amount);
    }

    /**
     * Returns a string representation of the timestamp in the format yyyy-MM-dd HH:mm:ss.
     *
     * @return the formatted date-time string
     */
    @Override
    public String toString() {
        int[] v = breakDown();
        return String.format("%04d-%02d-%02d %02d:%02d:%02d", v[0], v[1], v[2], v[3], v[4], v[5]);
    }
}
