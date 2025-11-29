package main.Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MountainAir extends Air {
    private final static int maxScore = 82;
    private double altitude;

    public MountainAir(final AirInput airInput) {
        super(airInput);
        this.altitude = (airInput.getAltitude());
        this.setAirQuality(computeAirQuality());
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode mountainAirNode = super.toNode();
        mountainAirNode.put("type", "MountainAir");
        mountainAirNode.put("altitude", altitude);
        return mountainAirNode;
    }

    @Override
    protected double computeAirQuality() {
        double quality = getOxygenLevel() * 2 - altitude / 1000
                         + getHumidity() * 0.6;
        return airQualityNormalize(quality);
    }
}