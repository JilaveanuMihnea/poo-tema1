package main.Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TemperateAir extends Air {
    private final static int maxScore = 82;
    private double pollenLevel;

    public TemperateAir(final AirInput airInput) {
        super(airInput);
        this.pollenLevel = airInput.getPollenLevel();
        this.setAirQuality(computeAirQuality());
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode temperateAirNode = super.toNode();
        temperateAirNode.put("type", "TemperateAir");
        temperateAirNode.put("pollenLevel", pollenLevel);
        return temperateAirNode;
    }

    @Override
    protected double computeAirQuality() {
        double quality = getOxygenLevel() * 2 + getHumidity() * 0.7
                         - pollenLevel * 0.1;
        return airQualityNormalize(quality);
    }
}