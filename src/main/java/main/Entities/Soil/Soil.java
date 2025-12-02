package main.Entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Constants;
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

    public Soil(final SoilInput soilInput) {
        super(soilInput.getName(), soilInput.getMass());
        this.nitrogen = soilInput.getNitrogen();
        this.waterRetention = soilInput.getWaterRetention();
        this.soilpH = soilInput.getSoilpH();
        this.organicMatter = soilInput.getOrganicMatter();
        this.type = soilInput.getType();
    }

    /**
     * Normalizes soil quality to be within 0 and CLAMP_MAX and rounds to 2 decimal places
     * @param quality the soil quality to normalize
     * @return the normalized soil quality
     */
    protected double soilQualityNormalize(final double quality) {
        double qualityClamped = Math.max(0, Math.min(quality, Constants.CLAMP_MAX));
        return Math.round(qualityClamped * Constants.ROUNDING_FACTOR) / Constants.ROUNDING_FACTOR;
    }

    /**
     * Converts the soil to a JSON object node
     * @return the JSON object node representing the soil
     */
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

    /**
     * Updates water retention by adding the given value and updates soil quality
     * @param value the value to add to water retention
     */
    public void updateWaterRetention(final double value) {
        this.waterRetention = Math.round((this.waterRetention + value)
                                         * Constants.ROUNDING_FACTOR)
                                         / Constants.ROUNDING_FACTOR;
        updateSoilQuality();
    }

    /**
     * Updates nitrogen by adding the given value and updates soil quality
     * @param value the value to add to nitrogen
     */
    public void updateOrganicMatter(final double value) {
        this.organicMatter = Math.round((this.organicMatter + value)
                                        * Constants.ROUNDING_FACTOR)
                                        / Constants.ROUNDING_FACTOR;
        updateSoilQuality();
    }

    private void updateSoilQuality() {
        this.soilQuality = computeSoilQuality();
        this.blockChance = computeBlockChance();
    }

    /**
     * Interprets soil quality into qualitative terms
     * @return the interpreted soil quality
     */
    public String getInterpretedSoilQuality() {
        if (soilQuality < Constants.SOIL_POOR_QUALITY_THRESHOLD) {
            return "poor";
        } else if (soilQuality < Constants.SOIL_MODERATE_QUALITY_THRESHOLD) {
            return "moderate";
        } else {
            return "good";
        }
    }
}
