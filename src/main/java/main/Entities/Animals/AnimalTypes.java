package main.Entities.Animals;

import main.Constants;

public enum AnimalTypes {
    HERBIVORE(85, "Herbivores"),
    CARNIVORE(30, "Carnivores"),
    OMNIVORE(60, "Omnivores"),
    DETRITIVORE(90, "Detritivores"),
    PARASITE(10, "Parasites");

    private double attackChance;
    private String typeName;

    AnimalTypes(final int attackPossibility, final String typeName) {
        this.attackChance = (Constants.ANIMAL_ATTACK_BASE - attackPossibility)
                            / Constants.ANIMAL_ATTACK_DIVIDER;
        this.typeName = typeName;
    }

    public double getAttackChance() {
        return attackChance;
    }

    public String getTypeName() {
        return typeName;
    }
}
