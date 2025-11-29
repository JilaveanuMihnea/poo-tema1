package main.Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PolarAir extends Air {
    private final static int maxScore = 82;
    private double iceCrystalConcentration;

    public PolarAir(final AirInput airInput) {
        super(airInput);
        this.iceCrystalConcentration = airInput.getIceCrystalConcentration();
        this.setAirQuality(computeAirQuality());
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode polarAirNode = super.toNode();
        polarAirNode.put("type", "PolarAir");
        polarAirNode.put("iceCrystalConcentration", iceCrystalConcentration);
        return polarAirNode;
    }

    @Override
    protected double computeAirQuality() {
        double quality =  getOxygenLevel() * 2 + (100 - Math.abs(getTemperature()))
                          - iceCrystalConcentration * 0.05;
        return airQualityNormalize(quality);
    }
}