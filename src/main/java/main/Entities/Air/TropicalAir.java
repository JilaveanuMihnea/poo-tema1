package main.Entities.Air;

import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TropicalAir extends Air {
    private final static int maxScore = 82;

    public TropicalAir(final AirInput airInput) {
        super(airInput);
        this.setWildcardAttribute(airInput.getCo2Level());
    }
}