package main.Entities.Plants;

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
        OLD
    }


    public Plant(PlantInput plantInput) {
        super(plantInput.getName(), plantInput.getMass());
        plantAge = PlantAge.YOUNG;
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

    public float getOxygenProduction() {
        float ageModifier;
        switch(plantAge) {
            case YOUNG:
                ageModifier = 0.2f;
                break;
            case MATURE:
                ageModifier = 0.7f;
                break;
            case OLD:
                ageModifier = 0.4f;
                break;
            default:
                ageModifier = 0.0f;
        }
        return plantType.getBaseOxygen() + ageModifier;
    }

}