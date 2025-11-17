package main.Entities.Air;

import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TemperateAir extends Air {
    private final static int maxScore = 82;

    public TemperateAir(final AirInput airInput) {
        super(airInput);
        this.setWildcardAttribute(airInput.getPollenLevel());
    }
}