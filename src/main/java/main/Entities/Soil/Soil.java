package main.Entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Entities.Entity;

@Data
@NoArgsConstructor
public abstract class Soil extends Entity {
    private double nitrogen;
    private double waterRetention;
    private double soilpH;
    private double organicMatter;
    private String type;
    private double soilQuality;
    private double blockChance;

    public Soil(SoilInput soilInput) {
        super(soilInput.getName(), soilInput.getMass());
        this.nitrogen = soilInput.getNitrogen();
        this.waterRetention = soilInput.getWaterRetention();
        this.soilpH = soilInput.getSoilpH();
        this.organicMatter = soilInput.getOrganicMatter();
        this.type = soilInput.getType();
    }

    protected double soilQualityNormalize(double quality) {
        quality = Math.max(0, Math.min(quality, 100));
        return Math.round(quality * 100.0) / 100.0;
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode soilNode = super.toNode();
        soilNode.put("type", type);
        soilNode.put("nitrogen", nitrogen);
        soilNode.put("waterRetention", waterRetention);
        soilNode.put("soilpH", soilpH);
        soilNode.put("organicMatter", organicMatter);
        soilNode.put("soilQuality", soilQuality);
        return soilNode;
    }

    protected abstract double computeSoilQuality();
    protected abstract double computeBlockChance();

    public void updateWaterRetention(double value){
        this.waterRetention = Math.round((this.waterRetention + value) * 100.0) / 100.0;
        updateSoilQuality();
    }

    public void updateOrganicMatter(double value){
        this.organicMatter = Math.round((this.organicMatter + value) * 100.0) / 100.0;
        updateSoilQuality();
    }

    private void updateSoilQuality() {
        this.soilQuality = computeSoilQuality();
        this.blockChance = computeBlockChance();
    }

    public String getInterpretedSoilQuality() {
        if (soilQuality < 40) {
            return "poor";
        } else if (soilQuality < 70) {
            return "moderate";
        } else {
            return "good";
        }
    }

}