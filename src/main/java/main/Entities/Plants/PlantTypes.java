package main.Entities.Plants;

enum PlantTypes {
    FLOWERING(6.0f, 90, "FloweringPlants"),
    GYMNOSPERM(0.0f, 60, "GymnospermsPlants"),
    FERN(0.0f, 30, "Ferns"),
    MOSS(0.8f, 40, "Mosses"),
    ALGA(0.5f, 20, "Algae");

    private float baseOxygen;
    private float tangleChance;
    private String typeName;

    PlantTypes(float baseOxygen, int tanglePossiblity, String typeName) {
        this.baseOxygen = baseOxygen;
        this.tangleChance = (float) tanglePossiblity / 100.0f;
        this.typeName = typeName;
    }

    public float getBaseOxygen() {
        return baseOxygen;
    }

    public float getTangleChance() {
        return tangleChance;
    }

    public String getTypeName() {
        return typeName;
    }
}