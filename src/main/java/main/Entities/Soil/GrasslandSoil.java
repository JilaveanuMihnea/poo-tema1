package main.Entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class GrasslandSoil extends Soil {
    private double rootDensity;
    public GrasslandSoil(SoilInput soilInput) {
        super(soilInput);
        this.rootDensity = soilInput.getRootDensity();
        this.setSoilQuality(computeSoilQuality());
        this.setBlockChance(Math.max(0, Math.min(computeBlockChance(), 100)));
    }

    @Override
    protected double computeSoilQuality() {
        double quality;
        quality = (this.getNitrogen() * 1.3) + (this.getOrganicMatter() * 1.5)
                + (rootDensity * 0.8);
        return soilQualityNormalize(quality);
    }

    @Override
    protected double computeBlockChance() {
        return Math.max(0, (50 - rootDensity + this.getWaterRetention() * 0.5) / 75 * 100);
    }
    @Override
    public ObjectNode toNode() {
        ObjectNode grasslandSoilNode = super.toNode();
        grasslandSoilNode.put("type", "GrasslandSoil");
        grasslandSoilNode.put("rootDensity", rootDensity);
        return grasslandSoilNode;
    }
}