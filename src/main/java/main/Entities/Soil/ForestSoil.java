package main.Entities.Soil;

import fileio.SoilInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class ForestSoil extends Soil {
    public ForestSoil(SoilInput soilInput) {
        super(soilInput);
        this.setWildcardAttribute(soilInput.getLeafLitter());
        this.setSoilQuality(computeSoilQuality());
        this.setBlockChance(Math.max(0, Math.min(computeBlockChance(), 100)));
    }

    @Override
    protected long computeSoilQuality() {
        double quality;
        quality = (this.getNitrogen() * 1.2) + (this.getOrganicMatter() * 2)
                + (this.getWaterRetention() * 1.5) + (this.getWildcardAttribute() * 0.3);
        return soilQualityNormalize(quality);
    }

    @Override
    protected double computeBlockChance() {
        return (this.getWaterRetention() * 0.6 + this.getWildcardAttribute() * 0.4) / 80 * 100;
    }
}