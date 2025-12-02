package main.Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;

import main.Constants;
import main.Entities.Entity;

@Data
@NoArgsConstructor
public abstract class Air extends Entity {
    private int maxScore;
    private double humidity;
    private double temperature;
    private double oxygenLevel;
    private double airQuality;
    private double damageChance;
    private double weatherModifier = 0;
    private boolean ongoingWeatherEvent = false;
    private String type;

    public Air(final AirInput airInput) {
        super(airInput.getName(), airInput.getMass());
        this.humidity = airInput.getHumidity();
        this.temperature = airInput.getTemperature();
        this.oxygenLevel = airInput.getOxygenLevel();
        this.type = airInput.getType();
    }

    /**
     * Convert Air object to JSON node
     * @return ObjectNode representing the Air object
     */
    @Override
    public ObjectNode toNode() {
        ObjectNode airNode = super.toNode();
        airNode.put("humidity", humidity);
        airNode.put("temperature", temperature);
        airNode.put("oxygenLevel", oxygenLevel);
        airNode.put("airQuality", airQuality);
        return airNode;
    }

    protected abstract double computeAirQuality(double weatherMod);
    protected abstract double computeWeatherModifier(Object weatherCondition);

    /**
     * Add value to current humidity and update air quality
     * @param value value to add to humidity
     */
    public void updateHumidity(final double value) {
        this.humidity += value;
        this.humidity = Math.round(this.humidity * Constants.ROUNDING_FACTOR)
                                   / Constants.ROUNDING_FACTOR;
        updateAirQuality();
    }

    /**
     * Add oxygen production to current oxygen level and update air quality
     * @param oxygenProduction oxygen level to add
     */
    public void updateOxygenLevel(final double oxygenProduction) {
        this.oxygenLevel += oxygenProduction;
        this.oxygenLevel = Math.round(oxygenLevel * Constants.ROUNDING_FACTOR)
                                      / Constants.ROUNDING_FACTOR;
        updateAirQuality();
    }

    private void updateAirQuality() {
        this.airQuality = computeAirQuality(this.weatherModifier);
        this.damageChance = Math.max(0, computeDamageChance());
    }

    /**
     * Update air quality based on weather condition
     * @param weatherCondition current weather condition
     */
    public void updateAirQuality(final Object weatherCondition) {
        this.weatherModifier = computeWeatherModifier(weatherCondition);
        if (weatherModifier != 0) {
            this.ongoingWeatherEvent = true;
        } else {
            this.ongoingWeatherEvent = false;
        }
        this.airQuality = computeAirQuality(this.weatherModifier);
        this.damageChance = Math.max(0, computeDamageChance());
    }

    /**
     * Normalize and round air quality to be within 0-100 range
     * @param quality raw air quality score
     * @return normalized air quality score
     */
    protected double airQualityNormalize(final double quality) {
        double qualityClamped = Math.max(0, Math.min(quality, Constants.CLAMP_MAX));
        return Math.round(qualityClamped * Constants.ROUNDING_FACTOR)
                / Constants.ROUNDING_FACTOR;
    }

    /**
     * Interpret air quality score into descriptive category
     * @return String representing air quality category
     */
    public String getInterpretedAirQuality() {
        if (airQuality < Constants.AIR_POOR_QUALITY_THRESHOLD) {
            return "poor";
        } else if (airQuality < Constants.AIR_MODERATE_QUALITY_THRESHOLD) {
            return "moderate";
        } else {
            return "good";
        }
    }

    /**
     * Check if air is toxic based on damage chance
     * @return true if air is toxic, false otherwise
     */
    public boolean isAirToxic() {
        return damageChance > (Constants.AIR_TOXIC_THRESHOLD_MULT * maxScore);
    }

    /**
     * Compute damage chance based on air quality
     * @return damage chance value
     */
    protected double computeDamageChance() {
        return Math.max(0, Math.round(Constants.AIR_DMG_BASE * (1 - airQuality / maxScore)
                        * Constants.ROUNDING_FACTOR)
                        / Constants.ROUNDING_FACTOR);
    }
}
