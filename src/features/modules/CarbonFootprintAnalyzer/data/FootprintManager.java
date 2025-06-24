package features.modules.CarbonFootprintAnalyzer.data;

import core.instances.Timestamp;
import core.io.CSVParser;
import core.io.IOManager;
import core.manager.GlobalManager;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintFactor;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintRecord;

import java.util.ArrayList;
import java.util.Comparator;

public class FootprintManager {
    private ArrayList<FootprintRecord> records;

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

    public FootprintRecord getRecordByIndex(int index) {
        for (FootprintRecord record : records) {
            if (record.getIndex() == index) {
                return record;
            }
        }

        return null;
    }

    public ArrayList<FootprintRecord> getRecords() {
        return records;
    }

    public FootprintRecord[] getRecordsForLastXDays(int days) {
        long startTimestamp = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L);

        return records.stream()
                .filter(record -> record.getTimestamp().getTimestamp() >= startTimestamp)
                .toArray(FootprintRecord[]::new);
    }

    public FootprintRecord[] getRecordsForFactor(FootprintFactor factor, int days) {
        long startTimestamp = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L);

        return records.stream()
                .filter(record -> record.getFactor().equals(factor) && record.getTimestamp().getTimestamp() >= startTimestamp)
                .toArray(FootprintRecord[]::new);
    }

    public void addRecord(FootprintRecord record) {
        records.add(record);
        writeRecordsToFile();
    }

    public void removeRecord(FootprintRecord record) {
        records.remove(record);
        writeRecordsToFile();
    }

    public void clearRecords(boolean wipeData) {
        records.clear();
        if (wipeData) writeRecordsToFile();
    }

    public void clearRecords() {
        records.clear();
    }

    public void exportRecords() {
        String csvString = toCSVString();
        IOManager ioManager = GlobalManager.getInstance().getIOManager();
        ioManager.exportToFile("footprint_records_export.csv", csvString);
    }

    private void writeRecordsToFile() {
        IOManager ioManager = GlobalManager.getInstance().getIOManager();

        String csvString = toCSVString();
        ioManager.writeToFile("footprint_records", csvString);
    }

    private String toCSVString() {
        ArrayList<String[]> csvData = new ArrayList<>();
        for (FootprintRecord record : records) {
            csvData.add(record.toArray());
        }

        return CSVParser.toCSVString(csvData);
    }
}
