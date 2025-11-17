package main.Entities.Soil;

import fileio.SoilInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class DesertSoil extends Soil {
    public DesertSoil(SoilInput soilInput) {
        super(soilInput);
        this.setWildcardAttribute(soilInput.getSalinity());
        this.setSoilQuality(computeSoilQuality());
        this.setBlockChance(Math.max(0, Math.min(computeBlockChance(), 100)));
    }

    @Override
    protected long computeSoilQuality() {
        double quality;
        quality = (this.getNitrogen() * 0.5) + (this.getWaterRetention() * 0.3)
                - (this.getWildcardAttribute() * 2);
        return soilQualityNormalize(quality);
    }

    @Override
    protected double computeBlockChance() {
        return (100 - getWaterRetention() + getWildcardAttribute()) / 100 * 100;
    }
}