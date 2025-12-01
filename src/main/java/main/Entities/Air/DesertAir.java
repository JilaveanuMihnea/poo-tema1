package main.Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class DesertAir extends Air {
    private double dustParticles;

    public DesertAir(final AirInput airInput) {
        super(airInput);
        this.dustParticles = airInput.getDustParticles();
        this.setAirQuality(computeAirQuality(0));
        setMaxScore(65);
        setDamageChance(Math.max(0, computeDamageChance()));
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode desertAirNode = super.toNode();
        desertAirNode.put("type", "DesertAir");
        desertAirNode.put("desertStorm", isOngoingWeatherEvent());


        return desertAirNode;
    }

    @Override
    protected double computeAirQuality(double weatherModifier) {
        double quality = getOxygenLevel() * 2 - getTemperature() * 0.3
                         - dustParticles * 0.2 + weatherModifier;
        return airQualityNormalize(quality);
    }

    @Override
    protected double computeWeatherModifier(Object weatherCondition) {
        double modifier = 0;
        if ((boolean) weatherCondition) { // Desert Storm active
            modifier -= 30;
        }
        return modifier;
    }
}