package features.modules.CarbonFootprintAnalyzer.data;

import core.instances.Timestamp;
import core.io.CSVParser;
import core.io.IOManager;
import core.manager.GlobalManager;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintFactor;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintRecord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

/**
 * Manages carbon footprint records for the logged-in user.
 * Handles creation, storage, retrieval, and analysis of footprint data.
 */
public class FootprintManager {
    private ArrayList<FootprintRecord> records;

    /**
     * Initializes the FootprintManager by loading existing records from storage.
     * If no records file exists, creates a new empty records list.
     */
    public void init() {
        IOManager ioManager = GlobalManager.getInstance().getIOManager();

        if (!ioManager.existsFile("footprint_records")) {
            records = new ArrayList<>();
            ioManager.writeToFile("footprint_records", "");

            return;
        }

        String csvString = ioManager.parseStringFromFile("footprint_records");
        Object[] csvData = CSVParser.parseCSVString(csvString).stream().sorted(
                Comparator.comparingLong(a -> Long.parseLong(a[2]))
        ).toArray();
        records = new ArrayList<>();

        for (int i = 0; i < csvData.length; i++) {
            String[] row = (String[]) csvData[i];
            if (row.length < 3) continue; // Skip invalid rows

            String activity = row[0];
            float amount;
            Timestamp timestamp;

            FootprintFactor factor = FootprintFactors.getFactorByAbbreviation(activity);


            try {
                amount = Float.parseFloat(row[1]);
                timestamp = new Timestamp(Long.parseLong(row[2]));
            } catch (NumberFormatException e) {
                continue; // Skip rows with invalid number formats
            }

            FootprintRecord record = new FootprintRecord(i, factor, amount, timestamp);
            records.add(record);
        }
    }

    /**
     * Retrieves a footprint record by its index.
     *
     * @param index the index of the record to retrieve
     * @return the FootprintRecord with the specified index, or null if not found
     */
    public FootprintRecord getRecordByIndex(int index) {
        for (FootprintRecord record : records) {
            if (record.getIndex() == index) {
                return record;
            }
        }

        return null;
    }

    /**
     * Gets all footprint records.
     *
     * @return the list of all footprint records
     */
    public ArrayList<FootprintRecord> getRecords() {
        return records;
    }

    /**
     * Retrieves all records that were logged on a specific date.
     *
     * @param date the date to filter records by
     * @return an array of FootprintRecord objects from the specified date
     */
    public FootprintRecord[] getRecordsByDate(Timestamp date) {
        return records.stream()
                .filter(record -> record.getTimestamp().getYear() == date.getYear() &&
                        record.getTimestamp().getMonth() == date.getMonth() &&
                        record.getTimestamp().getDay() == date.getDay())
                .toArray(FootprintRecord[]::new);
    }

    /**
     * Retrieves all records from the last X days.
     *
     * @param days the number of days to look back
     * @return an array of FootprintRecord objects from the last X days
     */
    public FootprintRecord[] getRecordsForLastXDays(int days) {
        long startTimestamp = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L);

        return records.stream()
                .filter(record -> record.getTimestamp().getTimestamp() >= startTimestamp)
                .toArray(FootprintRecord[]::new);
    }
    
    /**
     * Groups all records by day of the week for the last 7 days.
     *
     * @return a 2D array where each sub-array contains records for a specific day of the week
     */
    public FootprintRecord[][] getRecordsGroupedByWeekDay() {
        FootprintRecord[][] allRecords = new FootprintRecord[7][];

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            c.add(Calendar.DATE, -1);
        }
        for (int i = 0; i < 7; i++) {
            Timestamp startOfDay = new Timestamp(c.getTimeInMillis());
            c.add(Calendar.DATE, 1);
            Timestamp endOfDay = new Timestamp(c.getTimeInMillis());
            FootprintRecord[] records = getRecordsForPeriod(startOfDay, endOfDay);
            allRecords[i] = records;
        }
        
        return allRecords;
    }

    /**
     * Gets records for a specific footprint factor within the last X days.
     *
     * @param factor the footprint factor to filter by
     * @param days   the number of days to look back
     * @return an array of FootprintRecord objects for the specified factor and time period
     */
    public FootprintRecord[] getRecordsForFactor(FootprintFactor factor, int days) {
        long startTimestamp = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L);

        return records.stream()
                .filter(record -> record.getFactor().equals(factor) && record.getTimestamp().getTimestamp() >= startTimestamp)
                .toArray(FootprintRecord[]::new);
    }

    /**
     * Adds a new footprint record and saves it to storage.
     *
     * @param record the FootprintRecord to add
     */
    public void addRecord(FootprintRecord record) {
        records.add(record);
        writeRecordsToFile();
    }

    /**
     * Updates an existing footprint record with new factor and amount values.
     *
     * @param record the record to update
     * @param factor the new footprint factor
     * @param amount the new amount value
     * @throws IllegalArgumentException if the record is not found in the list
     */
    public void updateRecord(FootprintRecord record, FootprintFactor factor, double amount) {
        int index = records.indexOf(record);
        if (index == -1) {
            throw new IllegalArgumentException("Record not found in the list.");
        }
        records.set(index, new FootprintRecord(record.getIndex(), factor, amount, record.getTimestamp()));
        writeRecordsToFile();
    }

    /**
     * Removes a footprint record and updates storage.
     *
     * @param record the FootprintRecord to remove
     */
    public void removeRecord(FootprintRecord record) {
        records.remove(record);
        writeRecordsToFile();
    }

    /**
     * Clears all footprint records.
     *
     * @param wipeData if true, also removes data from storage
     */
    public void clearRecords(boolean wipeData) {
        records.clear();
        if (wipeData) writeRecordsToFile();
    }

    /**
     * Clears all footprint records from memory only (does not affect storage).
     */
    public void clearRecords() {
        records.clear();
    }

    /**
     * Exports all footprint records to a CSV file.
     */
    public void exportRecords() {
        String csvString = toCSVString();
        IOManager ioManager = GlobalManager.getInstance().getIOManager();
        ioManager.exportToFile("footprint_records_export.csv", csvString);
    }

    /**
     * Writes all records to the storage file.
     */
    private void writeRecordsToFile() {
        IOManager ioManager = GlobalManager.getInstance().getIOManager();

        String csvString = toCSVString();
        ioManager.writeToFile("footprint_records", csvString);
    }

    /**
     * Converts all records to a CSV string format.
     *
     * @return a CSV string representation of all records
     */
    private String toCSVString() {
        ArrayList<String[]> csvData = new ArrayList<>();
        for (FootprintRecord record : records) {
            csvData.add(record.toArray());
        }

        return CSVParser.toCSVString(csvData);
    }
    
    /**
     * Gets records within a specific time period.
     *
     * @param start the start timestamp
     * @param end   the end timestamp
     * @return an array of FootprintRecord objects within the specified period
     */
    private FootprintRecord[] getRecordsForPeriod(Timestamp start, Timestamp end) {
        return records.stream()
                .filter(record ->
                        record.getTimestamp().getTimestamp() >= start.getTimestamp()
                                && record.getTimestamp().getTimestamp() <= end.getTimestamp())
                .toArray(FootprintRecord[]::new);
    }
}
