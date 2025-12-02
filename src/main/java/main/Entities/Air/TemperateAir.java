package main.Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Constants;

@Data
@NoArgsConstructor
public final class TemperateAir extends Air {
    private double pollenLevel;

    public TemperateAir(final AirInput airInput) {
        super(airInput);
        this.pollenLevel = airInput.getPollenLevel();
        this.setAirQuality(computeAirQuality(0));
        setMaxScore(Constants.TEMPERATEAIR_MAX_SCORE);
        setDamageChance(Math.max(0, computeDamageChance()));
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode temperateAirNode = super.toNode();
        temperateAirNode.put("type", "TemperateAir");
        temperateAirNode.put("pollenLevel", pollenLevel);
        return temperateAirNode;
    }

    @Override
    protected double computeAirQuality(final double weatherMod) {
        double quality = getOxygenLevel() * Constants.TEMPERATEAIR_OXYGENLEVEL_MULT
                         + getHumidity() * Constants.TEMPERATEAIR_HUMIDITY_MULT
                         - pollenLevel * Constants.TEMPERATEAIR_POLLENLEVEL_MULT
                         + weatherMod;
        return airQualityNormalize(quality);
    }

    @Override
    protected double computeWeatherModifier(final Object weatherCondition) {
        return (((String) weatherCondition).equals("Spring") ? Constants.SPRING_MODIFIER : 0);
    }
}
