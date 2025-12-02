package main.Entities.Soil;

import main.Constants;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class ForestSoil extends Soil {
    private double leafLitter;
    public ForestSoil(final SoilInput soilInput) {
        super(soilInput);
        this.leafLitter = soilInput.getLeafLitter();
        this.setSoilQuality(computeSoilQuality());
        this.setBlockChance(Math.max(0, Math.min(computeBlockChance(), Constants.CLAMP_MAX)));
    }

    @Override
    protected double computeSoilQuality() {
        double quality;
        quality = (this.getNitrogen() * Constants.FORESTSOIL_Q_NITROGEN_MULT)
                + (this.getOrganicMatter() * Constants.FORESTSOIL_Q_ORGANIC_MULT)
                + (this.getWaterRetention() * Constants.FORESTSOIL_Q_RETENTION_MULT)
                + (leafLitter * Constants.FORESTSOIL_Q_LITTER_MULT);
        return soilQualityNormalize(quality);
    }

    @Override
    protected double computeBlockChance() {
        return Math.max(0, (this.getWaterRetention() * Constants.FORESTSOIL_B_RETENTION_MULT
                            + leafLitter * Constants.FORESTSOIL_B_LITTER_MULT)
                            / Constants.FORESTSOIL_B_DIVIDER
                            * Constants.FORESTSOIL_B_MULTIPLIER);
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode forestSoilNode = super.toNode();
        forestSoilNode.put("type", "ForestSoil");
        forestSoilNode.put("leafLitter", leafLitter);
        return forestSoilNode;
    }
}
