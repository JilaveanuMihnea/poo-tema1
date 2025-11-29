package main.Entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class DesertSoil extends Soil {
    private double salinity;
    public DesertSoil(SoilInput soilInput) {
        super(soilInput);
        this.salinity = soilInput.getSalinity();
        this.setSoilQuality(computeSoilQuality());
        this.setBlockChance(Math.max(0, Math.min(computeBlockChance(), 100)));
    }

    @Override
    protected double computeSoilQuality() {
        double quality;
        quality = (this.getNitrogen() * 0.5) + (this.getWaterRetention() * 0.3)
                - (this.salinity * 2);
        return soilQualityNormalize(quality);
    }

    @Override
    protected double computeBlockChance() {
        return (100 - getWaterRetention() + salinity) / 100 * 100;
    }
    @Override
    public ObjectNode toNode() {
        ObjectNode desertSoilNode = super.toNode();
        desertSoilNode.put("type", "DesertSoil");
        desertSoilNode.put("salinity", salinity);
        return desertSoilNode;
    }
}