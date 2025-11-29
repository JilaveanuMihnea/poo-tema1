package main.Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;

import main.Entities.Entity;

@Data
@NoArgsConstructor
public abstract class Air extends Entity {
    private double humidity;
    private double temperature;
    private double oxygenLevel;
    private double airQuality;
    private String type;

    public Air(final AirInput airInput) {
        super(airInput.getName(), airInput.getMass());
        this.humidity = airInput.getHumidity();
        this.temperature = airInput.getTemperature();
        this.oxygenLevel = airInput.getOxygenLevel();
        this.type = airInput.getType();
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode airNode = super.toNode();
        airNode.put("humidity", humidity);
        airNode.put("temperature", temperature);
        airNode.put("oxygenLevel", oxygenLevel);
        airNode.put("airQuality", airQuality);
        return airNode;
        }

    protected abstract double computeAirQuality();
    protected double airQualityNormalize(double quality) {
        quality = Math.max(0, Math.min(quality, 100));
        return Math.round(quality * 100.0) / 100.0;
    }

    public String getInterpretedAirQuality() {
        if (airQuality < 40) {
            return "poor";
        } else if (airQuality < 70) {
            return "moderate";
        } else {
            return "good";
        }
    }
}