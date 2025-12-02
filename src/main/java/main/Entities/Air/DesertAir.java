package main.Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Constants;

@Data
@NoArgsConstructor
public final class DesertAir extends Air {
    private double dustParticles;

    public DesertAir(final AirInput airInput) {
        super(airInput);
        this.dustParticles = airInput.getDustParticles();
        this.setAirQuality(computeAirQuality(0));
        setMaxScore(Constants.DESERTAIR_MAX_SCORE);
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
    protected double computeAirQuality(final double weatherMod) {
        double quality = getOxygenLevel() * Constants.DESERTAIR_OXYGENLEVEL_MULT
                         - getTemperature() * Constants.DESERTAIR_TEMPERATURE_MULT
                         - dustParticles * Constants.DESERTAIR_DUSTPARTICLES_MULT
                         + weatherMod;
        return airQualityNormalize(quality);
    }

    @Override
    protected double computeWeatherModifier(final Object weatherCondition) {
        double modifier = 0;
        if ((boolean) weatherCondition) { // Desert Storm active
            modifier += Constants.DESERTSTORM_MODIFIER;
        }
        return modifier;
    }
}
