package main.Entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class ForestSoil extends Soil {
    private double leafLitter;
    public ForestSoil(SoilInput soilInput) {
        super(soilInput);
        this.leafLitter = soilInput.getLeafLitter();
        this.setSoilQuality(computeSoilQuality());
        this.setBlockChance(Math.max(0, Math.min(computeBlockChance(), 100)));
    }

    @Override
    protected double computeSoilQuality() {
        double quality;
        quality = (this.getNitrogen() * 1.2) + (this.getOrganicMatter() * 2)
                + (this.getWaterRetention() * 1.5) + (leafLitter * 0.3);
        return soilQualityNormalize(quality);
    }

    @Override
    protected double computeBlockChance() {
        return Math.max(0, (this.getWaterRetention() * 0.6 + leafLitter * 0.4) / 80 * 100);
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode forestSoilNode = super.toNode();
        forestSoilNode.put("type", "ForestSoil");
        forestSoilNode.put("leafLitter", leafLitter);
        return forestSoilNode;
    }
}