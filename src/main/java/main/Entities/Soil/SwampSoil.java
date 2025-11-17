package main.Entities.Soil;

import fileio.SoilInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class SwampSoil extends Soil {
    public SwampSoil(SoilInput soilInput) {
        super(soilInput);
        this.setWildcardAttribute(soilInput.getWaterLogging());
        this.setSoilQuality(computeSoilQuality());
        this.setBlockChance(Math.max(0, Math.min(computeBlockChance(), 100)));
    }

    @Override
    protected long computeSoilQuality() {
        double quality;
        quality = (this.getNitrogen() * 1.1) + (this.getOrganicMatter() * 2.2)
                - (this.getWildcardAttribute() * 5);
        return soilQualityNormalize(quality);
    }

    @Override
    protected double computeBlockChance() {
        return (this.getWildcardAttribute() * 10);
    }
}