package main.Entities.Plants;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.PlantInput;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Entities.Entity;

@Data
@NoArgsConstructor
public class Plant extends Entity {
    private PlantTypes plantType;
    private PlantAge plantAge;

    enum PlantAge {
        YOUNG,
        MATURE,
        OLD,
        DEAD;

        private double growthLevel = 0.0;

        public PlantAge updateGrowth() {
            PlantAge[] values = PlantAge.values();
            growthLevel += 0.2;
            growthLevel = Math.round(growthLevel * 100.0) / 100.0;
            if (growthLevel >= 1.0) {
                growthLevel = 0.0;
                return values[this.ordinal() + 1];
            }
            return this;
        }

        public double getGrowthLevel() {
            return growthLevel;
        }

        public void setGrowthLevel(double growthLevel){
            this.growthLevel = growthLevel;
        }
    }

    public Plant(PlantInput plantInput) {
        super(plantInput.getName(), plantInput.getMass());
        plantAge = PlantAge.YOUNG;
        plantAge.setGrowthLevel(0.0);
        switch(plantInput.getType()) {
            case "FloweringPlants":
                this.plantType = PlantTypes.FLOWERING;
                break;
            case "GymnospermsPlants":
                this.plantType = PlantTypes.GYMNOSPERM;
                break;
            case "Ferns":
                this.plantType = PlantTypes.FERN;
                break;
            case "Mosses":
                this.plantType = PlantTypes.MOSS;
                break;
            case "Algae":
                this.plantType = PlantTypes.ALGA;
                break;
            default:
                throw new IllegalArgumentException("Invalid plant type: " + plantType);
        }
    }

    public double getOxygenProduction() {
        double ageModifier;
        switch(plantAge) {
            case YOUNG:
                ageModifier = 0.2;
                break;
            case MATURE:
                ageModifier = 0.7;
                break;
            case OLD:
                ageModifier = 0.4;
                break;
            default:
                ageModifier = 0.0;
        }
        return plantType.getBaseOxygen() + ageModifier;
    }

    public double getTangleChance() {
        return plantType.getTangleChance();
    }

    public boolean updatePlantAge() {
        this.plantAge = this.plantAge.updateGrowth();
        if (this.plantAge == PlantAge.DEAD) {
            this.setScanned(false);
            return false;
        }
        return true;
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode plantNode = super.toNode();
        plantNode.put("type", plantType.getTypeName());
        return plantNode;
    }

    public String getTypeName () {
        return plantType.getTypeName();
    }

}