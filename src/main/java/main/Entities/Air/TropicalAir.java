package main.Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class TropicalAir extends Air {
    private double co2Level;

    public TropicalAir(final AirInput airInput) {
        super(airInput);
        this.co2Level = Math.round(airInput.getCo2Level() * 100.0) / 100.0;
        this.setAirQuality(computeAirQuality(0));
        setMaxScore(82);
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
    protected double computeAirQuality(double weatherModifier) {
        double quality = getOxygenLevel() * 2 + getHumidity() * 0.5
                         - co2Level * 0.01 + weatherModifier;
        return airQualityNormalize(quality);
    }

    @Override
    protected double computeWeatherModifier(Object weatherCondition) {
        return (Double.parseDouble(weatherCondition.toString()) * 0.3);
    }
}