package main.Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Constants;

@Data
@NoArgsConstructor
public final class TropicalAir extends Air {
    private double co2Level;

    public TropicalAir(final AirInput airInput) {
        super(airInput);
        this.co2Level = Math.round(airInput.getCo2Level() * Constants.ROUNDING_FACTOR)
                        / Constants.ROUNDING_FACTOR;
        this.setAirQuality(computeAirQuality(0));
        setMaxScore(Constants.TROPICALAIR_MAX_SCORE);
        setDamageChance(Math.max(0, computeDamageChance()));
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode tropicalAirNode = super.toNode();
        tropicalAirNode.put("type", "TropicalAir");
        tropicalAirNode.put("co2Level", co2Level);
        return tropicalAirNode;
    }

    @Override
    protected double computeAirQuality(final double weatherMod) {
        double quality = getOxygenLevel() * Constants.TROPICALAIR_OXYGENLEVEL_MULT
                         + getHumidity() * Constants.TROPICALAIR_HUMIDITY_MULT
                         - co2Level * Constants.TROPICALAIR_CO2LEVEL_MULT
                         + weatherMod;
        return airQualityNormalize(quality);
    }

    @Override
    protected double computeWeatherModifier(final Object weatherCondition) {
        return (Double.parseDouble(weatherCondition.toString())
                * Constants.TROPICALAIR_WEATHER_MODIFIER_MULT);
    }
}
