package main.Entities.Animals;

public enum AnimalTypes {
    HERBIVORE(85, "Herbivores"),
    CARNIVORE(30, "Carnivores"),
    OMNIVORE(60, "Omnivores"),
    DETRITIVORE(90, "Detritivores"),
    PARASITE(10, "Parasites");

    private float attackChance;
    private String typeName;

    AnimalTypes(int attackPossibility, String typeName) {
        this.attackChance = (100 - attackPossibility) / 10.0f;
        this.typeName = typeName;
    }

    public float getAttackChance() {
        return attackChance;
    }

    public String getTypeName() {
        return typeName;
    }
}
