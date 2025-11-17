package main.Entities.Air;

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
    private double wildcardAttribute;
    private double airQuality;
    private String type;

    public Air(final AirInput airInput) {
        super(airInput.getName(), airInput.getMass());
        this.humidity = airInput.getHumidity();
        this.temperature = airInput.getTemperature();
        this.oxygenLevel = airInput.getOxygenLevel();
        this.type = airInput.getType();
    }

//    protected abstract double calculateAirQuality();
}