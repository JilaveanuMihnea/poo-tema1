package main.Entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class SwampSoil extends Soil {
    private double waterLogging;
    public SwampSoil(SoilInput soilInput) {
        super(soilInput);
        this.waterLogging = soilInput.getWaterLogging();
        this.setSoilQuality(computeSoilQuality());
        this.setBlockChance(Math.max(0, Math.min(computeBlockChance(), 100)));
    }

    @Override
    protected double computeSoilQuality() {
        double quality;
        quality = (this.getNitrogen() * 1.1) + (this.getOrganicMatter() * 2.2)
                - (waterLogging * 5);
        return soilQualityNormalize(quality);
    }

    @Override
    protected double computeBlockChance() {
        return Math.max(0, (waterLogging * 10));
    }
    @Override
    public ObjectNode toNode() {
        ObjectNode swampSoilNode = super.toNode();
        swampSoilNode.put("type", "SwampSoil");
        swampSoilNode.put("waterLogging", waterLogging);
        return swampSoilNode;
    }
}