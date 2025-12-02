package main.Entities.Plants;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.PlantInput;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Constants;
import main.Entities.Entity;

@Data
@NoArgsConstructor
public final class Plant extends Entity {
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
            growthLevel += Constants.PLANT_GROWTH_RATE;
            growthLevel = Math.round(growthLevel * Constants.ROUNDING_FACTOR)
                          / Constants.ROUNDING_FACTOR;
            if (growthLevel >= 1.0) {
                growthLevel = 0.0;
                return values[this.ordinal() + 1];
            }
            return this;
        }

        public void setGrowthLevel(final double growthLevel) {
            this.growthLevel = growthLevel;
        }
    }

    public Plant(final PlantInput plantInput) {
        super(plantInput.getName(), plantInput.getMass());
        plantAge = PlantAge.YOUNG;
        plantAge.setGrowthLevel(0.0);
        switch (plantInput.getType()) {
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

    /**
     * Calculates the oxygen production of the plant based on its type and age.
     * @return the oxygen production value
     */
    public double getOxygenProduction() {
        double ageModifier = switch (plantAge) {
            case YOUNG -> Constants.PLANT_YOUNG_PRODUCTION;
            case MATURE -> Constants.PLANT_MATURE_PRODUCTION;
            case OLD -> Constants.PLANT_OLD_PRODUCTION;
            default -> 0.0;
        };
        return plantType.getBaseOxygen() + ageModifier;
    }

    /**
     * Retrieves the chance of the plant causing tangling based on its type.
     * @return the tangle chance value
     */
    public double getTangleChance() {
        return plantType.getTangleChance();
    }

    /**
     * Updates the plant's age and checks if it has died.
     * @return true if the plant is still alive, false if it has died
     */
    public boolean updatePlantAge() {
        this.plantAge = this.plantAge.updateGrowth();
        if (this.plantAge == PlantAge.DEAD) {
            this.setScanned(false);
            return false;
        }
        return true;
    }

    /**
     * Converts the plant object to a JSON node representation.
     * @return the JSON node representing the plant
     */
    @Override
    public ObjectNode toNode() {
        ObjectNode plantNode = super.toNode();
        plantNode.put("type", plantType.getTypeName());
        return plantNode;
    }

    /**
     * Retrieves the type name of the plant.
     * @return the plant type name
     */
    public String getTypeName() {
        return plantType.getTypeName();
    }

}
