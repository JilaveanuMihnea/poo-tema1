package main.Entities.Air;

import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MountainAir extends Air {
    private final static int maxScore = 82;

    public MountainAir(final AirInput airInput) {
        super(airInput);
        this.setWildcardAttribute(airInput.getAltitude());
    }
}