package main.Entities.Soil;

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
    private double wildcardAttribute;
    private long soilQuality;
    private double blockChance;

    public Soil(SoilInput soilInput) {
        super(soilInput.getName(), soilInput.getMass());
        this.nitrogen = soilInput.getNitrogen();
        this.waterRetention = soilInput.getWaterRetention();
        this.soilpH = soilInput.getSoilpH();
        this.organicMatter = soilInput.getOrganicMatter();
        this.type = soilInput.getType();
    }

    protected long soilQualityNormalize(double quality) {
        quality = Math.max(0, Math.min(quality, 100));
        return Math.round(quality);
    }

    protected abstract long computeSoilQuality();
    protected abstract double computeBlockChance();


}