package main.Entities.Soil;

import fileio.SoilInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class TundraSoil extends Soil {
    public TundraSoil(SoilInput soilInput) {
        super(soilInput);
        this.setWildcardAttribute(soilInput.getPermafrostDepth());
        this.setSoilQuality(computeSoilQuality());
        this.setBlockChance(Math.max(0, Math.min(computeBlockChance(), 100)));
    }

    @Override
    protected long computeSoilQuality() {
        double quality;
        quality = (this.getNitrogen() * 0.7) + (this.getOrganicMatter() * 0.5)
                - (this.getWildcardAttribute() * 1.5);
        return soilQualityNormalize(quality);
    }

    @Override
    protected double computeBlockChance() {
        return (50 - this.getWildcardAttribute()) / 50 * 100;
    }
}