package main.Entities.Plants;

import main.Constants;

enum PlantTypes {
    FLOWERING(6.0, 90, "FloweringPlants"),
    GYMNOSPERM(0.0, 60, "GymnospermsPlants"),
    FERN(0.0, 30, "Ferns"),
    MOSS(0.8, 40, "Mosses"),
    ALGA(0.5, 20, "Algae");

    private double baseOxygen;
    private double tangleChance;
    private String typeName;

    PlantTypes(final double baseOxygen, final int tanglePossiblity, final String typeName) {
        this.baseOxygen = baseOxygen;
        this.tangleChance = tanglePossiblity / Constants.PLANT_TANGLE_DIVIDER;
        this.typeName = typeName;
    }

    public double getBaseOxygen() {
        return baseOxygen;
    }

    public double getTangleChance() {
        return tangleChance;
    }

    public String getTypeName() {
        return typeName;
    }
}
