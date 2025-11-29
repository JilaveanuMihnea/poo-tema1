package main.Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TropicalAir extends Air {
    private final static int maxScore = 82;
    private double co2Level;

    public TropicalAir(final AirInput airInput) {
        super(airInput);
        this.co2Level = airInput.getCo2Level();
        this.setAirQuality(computeAirQuality());
    }
    @Override
    public ObjectNode toNode() {
        ObjectNode tropicalAirNode = super.toNode();
        tropicalAirNode.put("type", "TropicalAir");
        tropicalAirNode.put("co2Level", co2Level);
        return tropicalAirNode;
    }

    @Override
    protected double computeAirQuality() {
        double quality = getOxygenLevel() * 2 + getHumidity() * 0.5 - co2Level * 0.01;
        return airQualityNormalize(quality);
    }
}