package main.Entities.Water;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.WaterInput;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Entities.Entity;

import static java.lang.Math.abs;

@Data
@NoArgsConstructor
public class Water extends Entity {
    private double salinity;
    private double pH;
    private double purity;
    private double turbidity;
    private double contaminantIndex;
    private boolean isFrozen;
    private String type;
    private double waterQuality;
    private int scannedAt;

    public Water(WaterInput waterInput) {
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
        quality = 0.3 * (purity / 100) // purity score
                + 0.2 * (1 - abs(pH - 7.5) / 7.5) // ph score
                + 0.15 * (1 - salinity / 350) // salinity score
                + 0.1 * (1 - turbidity / 100) // turbidity score
                + 0.15 * (1 - contaminantIndex / 100) // contaminant index score
                + 0.2 * (isFrozen ? 0 : 1); // frozen score
        quality *= 100;
        return quality;
    }
    @Override
    public ObjectNode toNode() {
        ObjectNode waterNode = super.toNode();
        waterNode.put("type", type);
//        waterNode.put("salinity", salinity);
//        waterNode.put("pH", pH);
//        waterNode.put("purity", purity);
//        waterNode.put("turbidity", turbidity);
//        waterNode.put("contaminantIndex", contaminantIndex);
//        waterNode.put("isFrozen", isFrozen);
//        waterNode.put("waterQuality", waterQuality);
        return waterNode;
    }
}