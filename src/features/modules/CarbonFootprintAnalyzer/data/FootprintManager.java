package features.modules.CarbonFootprintAnalyzer.data;

import core.instances.Timestamp;
import core.io.CSVParser;
import core.io.IOManager;
import core.manager.GlobalManager;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintFactor;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintRecord;

import java.util.ArrayList;
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
        ArrayList<String[]> csvData = CSVParser.parseCSVString(csvString);
        records = new ArrayList<>();

        for (String[] row : csvData) {
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

            FootprintRecord record = new FootprintRecord(factor, amount, timestamp);
            records.add(record);
        }
    }

    public void addRecord(FootprintRecord record) {
        records.add(record);

        IOManager ioManager = GlobalManager.getInstance().getIOManager();
        ArrayList<String[]> csvData = new ArrayList<>();
        for (FootprintRecord rec : records) {
            csvData.add(rec.toArray());
        }

        String csvString = CSVParser.toCSVString(csvData);
        ioManager.writeToFile("footprint_records", csvString);
    }

    public ArrayList<FootprintRecord> getRecords() {
        return records;
    }

    public FootprintRecord[] getRecordsForLastXDays(int days) {
        long startTimestamp = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L);

        return records.stream()
                .filter(record -> record.timestamp.getTimestamp() >= startTimestamp)
                .toArray(FootprintRecord[]::new);
    }

    public FootprintRecord[] getRecordsForFactor(FootprintFactor factor, int days) {
        long startTimestamp = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L);

        return records.stream()
                .filter(record -> record.factor.equals(factor) && record.timestamp.getTimestamp() >= startTimestamp)
                .toArray(FootprintRecord[]::new);
    }

    public void clearRecords() {
        records.clear();
    }
}
