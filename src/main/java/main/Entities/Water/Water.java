package main.Entities.Water;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.WaterInput;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Constants;
import main.Entities.Entity;

import static java.lang.Math.abs;

@Data
@NoArgsConstructor
public final class Water extends Entity {
    private double salinity;
    private double pH;
    private double purity;
    private double turbidity;
    private double contaminantIndex;
    private boolean isFrozen;
    private String type;
    private double waterQuality;
    private int scannedAt;

    public Water(final WaterInput waterInput) {
        super(waterInput.getName(), waterInput.getMass());
        this.salinity = waterInput.getSalinity();
        this.pH = waterInput.getPH();
        this.purity = waterInput.getPurity();
        this.turbidity = waterInput.getTurbidity();
        this.contaminantIndex = waterInput.getContaminantIndex();
        this.isFrozen = waterInput.isFrozen();
        this.waterQuality = computeWaterQuality();
        this.type = waterInput.getType();
    }

    private double computeWaterQuality() {
        double quality;
        quality = Constants.WATER_Q_PURITY_MULT * (purity / Constants.WATER_Q_PURITY_DIVIDER)
                + Constants.WATER_Q_PH_MULT * (1 - abs(pH - Constants.WATER_Q_PH_FACTOR)
                / Constants.WATER_Q_PH_FACTOR)
                + Constants.WATER_Q_SALINITY_MULT * (1 - salinity
                / Constants.WATER_Q_SALINITY_DIVIDER)
                + Constants.WATER_Q_TURBIDITY_MULT * (1 - turbidity
                / Constants.WATER_Q_TURBIDITY_DIVIDER)
                + Constants.WATER_Q_CONTAMINANT_MULT * (1 - contaminantIndex
                / Constants.WATER_Q_CONTAMINANT_DIVIDER)
                + Constants.WATER_Q_FROZEN_MULT * (isFrozen ? 0 : 1); // frozen score
        quality *= Constants.WATER_Q_TOTAL_MULTIPLIER;
        return quality;
    }
    @Override
    public ObjectNode toNode() {
        ObjectNode waterNode = super.toNode();
        waterNode.put("type", type);
        return waterNode;
    }
}
