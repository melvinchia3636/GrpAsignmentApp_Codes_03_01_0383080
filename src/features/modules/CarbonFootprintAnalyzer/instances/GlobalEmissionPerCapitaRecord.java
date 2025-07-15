package features.modules.CarbonFootprintAnalyzer.instances;

public class GlobalEmissionPerCapitaRecord {
    private final String entity;
    private final int year;
    private final double co2ePerCapita;

    /**
     * Constructs a new GlobalEmissionPerCapitaRecord with the specified entity, year, and CO2e per capita.
     *
     * @param entity        the name of the entity
     * @param year           the year of the record
     * @param co2ePerCapita the CO2 equivalent emissions per capita for that year (in tonnes CO2e)
     */
    public GlobalEmissionPerCapitaRecord(String entity, int year, double co2ePerCapita) {
        this.entity = entity;
        this.year = year;
        this.co2ePerCapita = co2ePerCapita;
    }

    public String getEntity() {
        return entity;
    }

    public int getYear() {
        return year;
    }

    public double getCo2ePerCapita() {
        return co2ePerCapita;
    }
}
