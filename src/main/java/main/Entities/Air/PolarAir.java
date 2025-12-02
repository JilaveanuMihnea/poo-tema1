package main.Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Constants;

@Data
@NoArgsConstructor
public final class PolarAir extends Air {
    private double iceCrystalConcentration;

    public PolarAir(final AirInput airInput) {
        super(airInput);
        this.iceCrystalConcentration = airInput.getIceCrystalConcentration();
        this.setAirQuality(computeAirQuality(0));
        setMaxScore(Constants.POLARAIR_MAX_SCORE);
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
    protected double computeAirQuality(final double weatherMod) {
        double quality =  getOxygenLevel() * Constants.POLARAIR_OXYGENLEVEL_MULT
                          + (Constants.POLARAIR_TEMPERATURE_BASE - Math.abs(getTemperature()))
                          - iceCrystalConcentration * Constants.POLARAIR_ICECRYSTALCONC_MULT
                          + weatherMod;
        return airQualityNormalize(quality);
    }

    @Override
    protected double computeWeatherModifier(final Object weatherCondition) {
        return ((double) weatherCondition * Constants.POLARAIR_WEATHER_MODIFIER_MULT);
    }
}
