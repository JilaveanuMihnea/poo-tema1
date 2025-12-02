package main.Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Constants;

@Data
@NoArgsConstructor
public final class
MountainAir extends Air {
    private double altitude;

    public MountainAir(final AirInput airInput) {
        super(airInput);
        this.altitude = (airInput.getAltitude());
        this.setAirQuality(computeAirQuality(0));
        setMaxScore(Constants.MOUNTAINAIR_MAX_SCORE);
        setDamageChance(Math.max(0, computeDamageChance()));
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode mountainAirNode = super.toNode();
        mountainAirNode.put("type", "MountainAir");
        mountainAirNode.put("altitude", altitude);
        return mountainAirNode;
    }

    @Override
    protected double computeAirQuality(final double weatherMod) {
        double quality = getOxygenLevel() * Constants.MOUNTAINAIR_OXYGENLEVEL_MULT
                         - altitude / Constants.MOUNTAINAIR_ALTITUDE_DIVIDER
                         + getHumidity() * Constants.MOUNTAINAIR_HUMIDITY_MULT
                         + weatherMod;
        return airQualityNormalize(quality);
    }

    @Override
    protected double computeWeatherModifier(final Object weatherCondition) {
        return Double.parseDouble(weatherCondition.toString())
                * Constants.MOUNTAINAIR_WEATHER_MODIFIER_MULT;
    }
}
