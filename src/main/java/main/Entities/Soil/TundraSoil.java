package main.Entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Constants;

@Data
@NoArgsConstructor
public final class TundraSoil extends Soil {
    private double permafrostDepth;
    public TundraSoil(final SoilInput soilInput) {
        super(soilInput);
        this.permafrostDepth = soilInput.getPermafrostDepth();
        this.setSoilQuality(computeSoilQuality());
        this.setBlockChance(Math.max(0, Math.min(computeBlockChance(), Constants.CLAMP_MAX)));
    }

    @Override
    protected double computeSoilQuality() {
        double quality;
        quality = (this.getNitrogen() * Constants.TUNDRASOIL_Q_NITROGEN_MULT)
                + (this.getOrganicMatter() * Constants.TUNDRASOIL_Q_ORGANIC_MULT)
                - (permafrostDepth * Constants.TUNDRASOIL_Q_FROST_MULT);
        return soilQualityNormalize(quality);
    }

    @Override
    protected double computeBlockChance() {
        return Math.max(0, (Constants.TUNDRASOIL_B_BASE - permafrostDepth)
                        / Constants.TUNDRASOIL_B_DIVIDER
                        * Constants.TUNDRASOIL_B_MULTIPLIER);
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode node = super.toNode();
        node.put("type", "TundraSoil");
        node.put("permafrostDepth", permafrostDepth);
        return node;
    }
}
