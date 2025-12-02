package main.Entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Constants;

@Data
@NoArgsConstructor
public final class GrasslandSoil extends Soil {
    private double rootDensity;
    public GrasslandSoil(final SoilInput soilInput) {
        super(soilInput);
        this.rootDensity = soilInput.getRootDensity();
        this.setSoilQuality(computeSoilQuality());
        this.setBlockChance(Math.max(0, Math.min(computeBlockChance(), Constants.CLAMP_MAX)));
    }

    @Override
    protected double computeSoilQuality() {
        double quality;
        quality = (this.getNitrogen() * Constants.GRASSLANDSOIL_Q_NITROGEN_MULT)
                + (this.getOrganicMatter() * Constants.GRASSLANDSOIL_Q_ORGANIC_MULT)
                + (rootDensity * Constants.GRASSLANDSOIL_Q_ROOT_MULT);
        return soilQualityNormalize(quality);
    }

    @Override
    protected double computeBlockChance() {
        return Math.max(0, (Constants.GRASSLANDSOIL_B_BASE - rootDensity
                            + this.getWaterRetention() * Constants.GRASSLANDSOIL_B_RETENTION_MULT)
                            / Constants.GRASSLANDSOIL_B_DIVIDER
                            * Constants.GRASSLANDSOIL_B_MULTIPLIER);
    }
    @Override
    public ObjectNode toNode() {
        ObjectNode grasslandSoilNode = super.toNode();
        grasslandSoilNode.put("type", "GrasslandSoil");
        grasslandSoilNode.put("rootDensity", rootDensity);
        return grasslandSoilNode;
    }
}
