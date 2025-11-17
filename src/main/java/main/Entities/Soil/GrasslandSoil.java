package main.Entities.Soil;

import fileio.SoilInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class GrasslandSoil extends Soil {
    public GrasslandSoil(SoilInput soilInput) {
        super(soilInput);
        this.setWildcardAttribute(soilInput.getRootDensity());
        this.setSoilQuality(computeSoilQuality());
        this.setBlockChance(Math.max(0, Math.min(computeBlockChance(), 100)));
    }

    @Override
    protected long computeSoilQuality() {
        double quality;
        quality = (this.getNitrogen() * 1.3) + (this.getOrganicMatter() * 1.5)
                + (this.getWildcardAttribute() * 0.8);
        return soilQualityNormalize(quality);
    }

    @Override
    protected double computeBlockChance() {
        return (50 - this.getWildcardAttribute() + this.getWaterRetention() * 0.5) / 75 * 100;
    }
}