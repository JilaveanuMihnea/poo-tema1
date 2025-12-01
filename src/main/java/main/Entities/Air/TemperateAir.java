package main.Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class TemperateAir extends Air {
    private double pollenLevel;

    public TemperateAir(final AirInput airInput) {
        super(airInput);
        this.pollenLevel = airInput.getPollenLevel();
        this.setAirQuality(computeAirQuality(0));
        setMaxScore(84);
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
    protected double computeAirQuality(double weatherModifier) {
        double quality = getOxygenLevel() * 2 + getHumidity() * 0.7
                         - pollenLevel * 0.1 + weatherModifier;
        return airQualityNormalize(quality);
    }

    @Override
    protected double computeWeatherModifier(Object weatherCondition) {
        return -(((String) weatherCondition).equals("Spring") ? 15 : 0);
    }
}