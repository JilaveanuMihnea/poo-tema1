package main.Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class PolarAir extends Air {
    private double iceCrystalConcentration;

    public PolarAir(final AirInput airInput) {
        super(airInput);
        this.iceCrystalConcentration = airInput.getIceCrystalConcentration();
        this.setAirQuality(computeAirQuality(0));
        setMaxScore(142);
        setDamageChance(Math.max(0, computeDamageChance()));
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode polarAirNode = super.toNode();
        polarAirNode.put("type", "PolarAir");
        polarAirNode.put("iceCrystalConcentration", iceCrystalConcentration);
        return polarAirNode;
    }

    @Override
    protected double computeAirQuality(double weatherModifier) {
        double quality =  getOxygenLevel() * 2 + (100 - Math.abs(getTemperature()))
                          - iceCrystalConcentration * 0.05 + weatherModifier;
        return airQualityNormalize(quality);
    }

    @Override
    protected double computeWeatherModifier(Object weatherCondition) {
        return -((double) weatherCondition * 0.2);
    }
}