package main.Entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Constants;

@Data
@NoArgsConstructor
public final class DesertSoil extends Soil {
    private double salinity;
    public DesertSoil(final SoilInput soilInput) {
        super(soilInput);
        this.salinity = soilInput.getSalinity();
        this.setSoilQuality(computeSoilQuality());
        this.setBlockChance(Math.max(0, Math.min(computeBlockChance(), Constants.CLAMP_MAX)));
    }

    @Override
    protected double computeSoilQuality() {
        double quality;
        quality = (this.getNitrogen() * Constants.DESERTSOIL_Q_NITROGEN_MULT)
                + (this.getWaterRetention() * Constants.DESERTSOIL_Q_RETENTION_MULT)
                - (this.salinity * Constants.DESERTSOIL_Q_SALINITY_MULT);
        return soilQualityNormalize(quality);
    }

    @Override
    protected double computeBlockChance() {
        return Math.max(0, (Constants.DESERTSOIL_B_BASE - getWaterRetention() + salinity)
                            / Constants.DESERTSOIL_B_DIVIDER
                            * Constants.DESERTSOIL_B_MULTIPLIER);
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode desertSoilNode = super.toNode();
        desertSoilNode.put("type", "DesertSoil");
        desertSoilNode.put("salinity", salinity);
        return desertSoilNode;
    }
}
