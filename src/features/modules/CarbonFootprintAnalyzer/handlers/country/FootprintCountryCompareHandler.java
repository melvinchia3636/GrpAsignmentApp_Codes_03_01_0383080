package features.modules.CarbonFootprintAnalyzer.handlers.country;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.modules.CarbonFootprintAnalyzer.data.FootprintManager;
import features.modules.CarbonFootprintAnalyzer.data.GlobalEmissionsPerCapita;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintRecord;
import features.modules.CarbonFootprintAnalyzer.instances.GlobalEmissionPerCapitaRecord;

import java.util.Arrays;
import java.util.Comparator;

public class FootprintCountryCompareHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        GlobalEmissionsPerCapita globalEmissions = GlobalManager.getInstance().getGlobalEmissionsPerCapita();

        String country = validateAndGetCountry();
        if (country == null) return;

        FootprintRecord[] userRecords = getUserRecords();
        if (userRecords == null) return;

        double userAnnualFootprint = getUserAnnualFootprint(userRecords);

        GlobalEmissionPerCapitaRecord latestRecord = getCountryLatestRecord(globalEmissions, country);
        if (latestRecord == null) return;

        double countryAnnualFootprint = latestRecord.getCo2ePerCapita();

        double difference = userAnnualFootprint - countryAnnualFootprint;
        double percentageDifference = (difference / countryAnnualFootprint) * 100;

        outputComparisonResults(
                country,
                latestRecord.getYear(),
                userAnnualFootprint,
                countryAnnualFootprint,
                difference,
                percentageDifference
        );
    }

    /**
     * Retrieves the latest record for the specified country from the global emissions data.
     * If no records are found for the country, it prints an error message and returns null.
     *
     * @param globalEmissions The GlobalEmissionsPerCapita instance containing emissions data.
     * @param country         The country to retrieve records for.
     * @return The latest GlobalEmissionPerCapitaRecord for the specified country, or null if not found.
     */
    private static GlobalEmissionPerCapitaRecord getCountryLatestRecord(GlobalEmissionsPerCapita globalEmissions, String country) {
        GlobalEmissionPerCapitaRecord[] countryRecords = globalEmissions.filterEntityRecords(country);

        if (countryRecords.length == 0) {
            OutputUtils.printError("No data found for country: " + country, false);
            OutputUtils.printTip("Please check the country name and try again.");
            return null;
        }

        return Arrays.stream(countryRecords)
                .max(Comparator.comparingInt(GlobalEmissionPerCapitaRecord::getYear))
                .orElse(null);
    }

    /**
     * Calculates the user's annual carbon footprint based on their records.
     * If the user has less than a full year's worth of data, it scales the annual footprint accordingly.
     *
     * @param userRecords The user's carbon footprint records for the last year.
     * @return The estimated annual carbon footprint in tonnes CO2e.
     */
    private static double getUserAnnualFootprint(FootprintRecord[] userRecords) {
        double userAnnualFootprint = Arrays.stream(userRecords)
                .mapToDouble(FootprintRecord::getEstimatedFootprint)
                .sum() / 1000.0; // Convert kg to tonnes

        long oldestTimestamp = Arrays.stream(userRecords)
                .mapToLong(record -> record.getTimestamp().getTimestamp())
                .min()
                .orElse(System.currentTimeMillis());

        // Calculate the number of days covered by the user's records
        long daysCovered = (System.currentTimeMillis() - oldestTimestamp) / (24 * 60 * 60 * 1000L);

        // If the user has less than a full year's worth of data, scale the annual footprint
        if (daysCovered < 365 && daysCovered > 0) {
            userAnnualFootprint = userAnnualFootprint * (365.0 / daysCovered);
        }
        return userAnnualFootprint;
    }

    /**
     * Validates and retrieves the country for comparison.
     * If no country is specified, it uses the user's default country.
     *
     * @return The country to compare against, or null if no valid country is found.
     */
    private String validateAndGetCountry() {
        String country = argsMap.get("country");
        country = country != null ? country :
                GlobalManager.getInstance().getUserManager().getFootprintCountry();

        if (country == null || country.isEmpty()) {
            OutputUtils.printError("No country specified for comparison.", false);
            OutputUtils.printTip("Either provide a country using the '-c' option or set your " +
                    "default country using 'footprint country set'.");

            return null;
        }

        return country;
    }

    /**
     * Retrieves the user's carbon footprint records for the last year.
     *
     * @return An array of FootprintRecord objects for the last year, or null if no records are found.
     */
    private FootprintRecord[] getUserRecords() {
        FootprintManager footprintManager = GlobalManager.getInstance().getFootprintManager();

        if (footprintManager.getRecords().isEmpty()) {
            OutputUtils.printError("No carbon footprint records found.", false);
            OutputUtils.printTip("Log your first activity using the 'footprint log' command.");
            return null;
        }

        FootprintRecord[] userRecords = footprintManager.getRecordsForLastXDays(365);

        if (userRecords.length == 0) {
            OutputUtils.printError("No records found for the last year. Please log some activities first.", false);
            return null;
        }

        return userRecords;
    }

    /**
     * Outputs the comparison results in a formatted manner.
     *
     * @param country               The country being compared against.
     * @param recordYear            The year of the latest record for the country.
     * @param userAnnualFootprint   The user's annual carbon footprint in tonnes CO2e.
     * @param countryAnnualFootprint The country's average annual carbon footprint in tonnes CO2e.
     * @param difference            The difference between user's and country's footprints in tonnes CO2e.
     * @param percentageDifference  The percentage difference between user's and country's footprints.
     */
    private void outputComparisonResults(
            String country,
            int recordYear,
            double userAnnualFootprint,
            double countryAnnualFootprint,
            double difference,
            double percentageDifference
    ) {
        OutputUtils.printSectionHeader("Carbon Footprint Country Comparison");

        OutputUtils.printDataRow("Your Annual Footprint", String.format("%.2f tonnes CO2e", userAnnualFootprint));
        OutputUtils.printDataRow(country + " Average (" + recordYear + ")",
                String.format("%.2f tonnes CO2e", countryAnnualFootprint));

        String comparisonText;

        if (difference > 0) {
            comparisonText = String.format("%.2f tonnes CO2e higher (%.1f%% above average)",
                    Math.abs(difference), Math.abs(percentageDifference));
        } else {
            comparisonText = String.format("%.2f tonnes CO2e lower (%.1f%% below average)",
                    Math.abs(difference), Math.abs(percentageDifference));
        }

        OutputUtils.printDataRow("Difference",
                difference > 0 ? new Chalk(comparisonText).red() : new Chalk(comparisonText).green());

        System.out.println();
        if (difference > 0) {
            if (percentageDifference > 50) {
                OutputUtils.printTip("Consider setting aggressive reduction goals to bring your footprint closer to the " + country + " average.");
            } else if (percentageDifference > 20) {
                OutputUtils.printTip("You're above the " + country + " average. Small daily changes can make a big difference!");
            } else {
                OutputUtils.printTip("You're slightly above the " + country + " average. Keep tracking to identify reduction opportunities.");
            }
        } else {
            if (Math.abs(percentageDifference) > 20) {
                OutputUtils.printEncouragement("Excellent! You're significantly below the " + country + " average. Keep up the great work!");
            } else {
                OutputUtils.printEncouragement("Good job! You're below the " + country + " average footprint.");
            }
        }

        OutputUtils.printInfo(String.format("This comparison is based on the data from the year %d.",
                recordYear));
    }
}
