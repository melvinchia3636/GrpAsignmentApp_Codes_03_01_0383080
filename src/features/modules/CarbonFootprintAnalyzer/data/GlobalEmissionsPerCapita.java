package features.modules.CarbonFootprintAnalyzer.data;

import core.io.CSVParser;
import core.terminal.OutputUtils;
import features.modules.CarbonFootprintAnalyzer.instances.GlobalEmissionPerCapitaRecord;

import java.util.ArrayList;

public class GlobalEmissionsPerCapita {
    private final ArrayList<GlobalEmissionPerCapitaRecord> records;

    /**
     * Constructs a GlobalEmissionsPerCapita object by parsing the CSV dataset file.
     * The dataset should contain global CO₂ emissions per capita data.
     *
     * @param datasetPath The path to the CSV file containing the emissions data.
     * @throws IllegalArgumentException If the CSV format is invalid or if there are parsing errors.
     */
    public GlobalEmissionsPerCapita(String datasetPath) {
        ArrayList<String[]> csvData = CSVParser.parseCSVFile(datasetPath);
        records = new ArrayList<>();

        String[] headers = csvData.get(0);
        if (headers.length < 3 ||
                !headers[0].equals("Entity") ||
                !headers[1].equals("Year") ||
                !headers[2].equals("Annual CO₂ emissions (per capita)")) {
            throw new IllegalArgumentException("Invalid CSV format for global emissions dataset. Please make sure " +
                    "the dataset is downloaded from https://ourworldindata.org/co2-and-greenhouse-gas-emissions#explore-data-on-co2-and-greenhouse-gas-emissions " +
                    "without any modifications.");
        }

        for (int i = 1; i < csvData.size(); i++) {
            try {
                String[] row = csvData.get(i);
                if (row.length < 3) continue; // Skip invalid rows

                String activity = row[0];
                int year;
                double amount;

                year = Integer.parseInt(row[1]);
                amount = Double.parseDouble(row[2]);

                GlobalEmissionPerCapitaRecord record = new GlobalEmissionPerCapitaRecord(
                        activity, year, amount
                );

                records.add(record);
            } catch (Exception e) {
                OutputUtils.printError("Error parsing row " + i + " of the CO2e dataset: " + e.getMessage(), false);
                System.exit(1);
            }
        }
    }

    /**
     * Returns the list of records containing global emissions per capita data.
     *
     * @return An ArrayList of GlobalEmissionPerCapitaRecord objects.
     */
    public ArrayList<GlobalEmissionPerCapitaRecord> getRecords() {
        return records;
    }

    /**
     * Returns an array of unique entities (countries or regions) present in the dataset.
     *
     * @return An array of strings representing unique entities.
     */
    public String[] getEntities() {
        ArrayList<String> entities = new ArrayList<>();
        for (GlobalEmissionPerCapitaRecord record : records) {
            String entity = record.getEntity();
            if (!entities.contains(entity)) {
                entities.add(entity);
            }
        }

        return entities.toArray(new String[0]);
    }

    /**
     * Filters the records for a specific entity (country or region).
     *
     * @param entity The name of the entity to filter by.
     * @return An array of GlobalEmissionPerCapitaRecord objects for the specified entity.
     */
    public GlobalEmissionPerCapitaRecord[] filterEntityRecords(String entity) {
        ArrayList<GlobalEmissionPerCapitaRecord> filteredRecords = new ArrayList<>();
        for (GlobalEmissionPerCapitaRecord record : records) {
            if (record.getEntity().equalsIgnoreCase(entity)) {
                filteredRecords.add(record);
            }
        }
        return filteredRecords.toArray(new GlobalEmissionPerCapitaRecord[0]);
    }
}
