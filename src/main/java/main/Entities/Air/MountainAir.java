package main.Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class MountainAir extends Air {
    private double altitude;

    public MountainAir(final AirInput airInput) {
        super(airInput);
        this.altitude = (airInput.getAltitude());
        this.setAirQuality(computeAirQuality(0));
        setMaxScore(78);
        setDamageChance(computeDamageChance());
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode mountainAirNode = super.toNode();
        mountainAirNode.put("type", "MountainAir");
        mountainAirNode.put("altitude", altitude);
        return mountainAirNode;
    }

    @Override
    protected double computeAirQuality(double weatherModifier) {
        double quality = getOxygenLevel() * 2 - altitude / 1000
                         + getHumidity() * 0.6 + weatherModifier;
        return airQualityNormalize(quality);
    }

    @Override
    protected double computeWeatherModifier(Object weatherCondition) {
        return -(Double.parseDouble(weatherCondition.toString()) * 0.1);
    }
}