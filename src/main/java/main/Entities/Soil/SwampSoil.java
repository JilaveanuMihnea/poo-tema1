package main.Entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Constants;

@Data
@NoArgsConstructor
public final class SwampSoil extends Soil {
    private double waterLogging;
    public SwampSoil(final SoilInput soilInput) {
        super(soilInput);
        this.waterLogging = soilInput.getWaterLogging();
        this.setSoilQuality(computeSoilQuality());
        this.setBlockChance(Math.max(0, Math.min(computeBlockChance(), Constants.CLAMP_MAX)));
    }

    @Override
    protected double computeSoilQuality() {
        double quality;
        quality = (this.getNitrogen() * Constants.SWAMPSOIL_Q_NITROGEN_MULT)
                + (this.getOrganicMatter() * Constants.SWAMPSOIL_Q_ORGANIC_MULT)
                - (waterLogging * Constants.SWAMPSOIL_Q_WATERLOGGING_MULT);
        return soilQualityNormalize(quality);
    }

    @Override
    protected double computeBlockChance() {
        return Math.max(0, (waterLogging * Constants.SWAMPSOIL_B_WATERLOGGING_MULT));
    }
    @Override
    public ObjectNode toNode() {
        ObjectNode swampSoilNode = super.toNode();
        swampSoilNode.put("type", "SwampSoil");
        swampSoilNode.put("waterLogging", waterLogging);
        return swampSoilNode;
    }
}
