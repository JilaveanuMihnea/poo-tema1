package main.Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DesertAir extends Air {
    private final static int maxScore = 82;
    private double dustParticles;

    public DesertAir(final AirInput airInput) {
        super(airInput);
        this.dustParticles = airInput.getDustParticles();
        this.setAirQuality(computeAirQuality());
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode desertAirNode = super.toNode();
        desertAirNode.put("type", "DesertAir");
        desertAirNode.put("dustParticles", dustParticles);
        return desertAirNode;
    }

    @Override
    protected double computeAirQuality() {
        double quality = getOxygenLevel() * 2 - getTemperature() * 0.3
                         - dustParticles * 0.2;
        return airQualityNormalize(quality);
    }
}