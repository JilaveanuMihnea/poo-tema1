package main.Entities.Animals;

import fileio.AnimalInput;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Entities.Entity;

@Data
@NoArgsConstructor
public class Animal extends Entity {
    private AnimalTypes animalType;

    public Animal(AnimalInput animalInput) {
        super(animalInput.getName(), animalInput.getMass());
        switch(animalInput.getType()) {
            case "Herbivore":
                this.animalType = AnimalTypes.HERBIVORE;
                break;
            case "Carnivore":
                this.animalType = AnimalTypes.CARNIVORE;
                break;
            case "Omnivore":
                this.animalType = AnimalTypes.OMNIVORE;
                break;
            case "Deritrivore":
                this.animalType = AnimalTypes.DETRITIVORE;
                break;
            case "Parasite":
                this.animalType = AnimalTypes.PARASITE;
                break;
            default:
                throw new IllegalArgumentException("Invalid animal type: " + animalType);
        }
    }
}