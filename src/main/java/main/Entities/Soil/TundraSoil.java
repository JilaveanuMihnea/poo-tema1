package main.Entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class TundraSoil extends Soil {
    private double permafrostDepth;
    public TundraSoil(SoilInput soilInput) {
        super(soilInput);
        this.permafrostDepth = soilInput.getPermafrostDepth();
        this.setSoilQuality(computeSoilQuality());
        this.setBlockChance(Math.max(0, Math.min(computeBlockChance(), 100)));
    }

    @Override
    protected double computeSoilQuality() {
        double quality;
        quality = (this.getNitrogen() * 0.7) + (this.getOrganicMatter() * 0.5)
                - (permafrostDepth * 1.5);
        return soilQualityNormalize(quality);
    }

    @Override
    protected double computeBlockChance() {
        return Math.max(0, (50 - permafrostDepth) / 50 * 100);
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode node = super.toNode();
        node.put("type", "TundraSoil");
        node.put("permafrostDepth", permafrostDepth);
        return node;
    }
}