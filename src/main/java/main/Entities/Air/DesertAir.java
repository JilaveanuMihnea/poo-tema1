package main.Entities.Air;

import fileio.AirInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DesertAir extends Air {
    private final static int maxScore = 82;

    public DesertAir(final AirInput airInput) {
        super(airInput);
        this.setWildcardAttribute(airInput.getDustParticles());
    }
}