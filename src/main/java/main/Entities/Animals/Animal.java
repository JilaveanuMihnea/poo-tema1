package main.Entities.Animals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
            case "Herbivores":
                this.animalType = AnimalTypes.HERBIVORE;
                break;
            case "Carnivores":
                this.animalType = AnimalTypes.CARNIVORE;
                break;
            case "Omnivores":
                this.animalType = AnimalTypes.OMNIVORE;
                break;
            case "Detritivores":
                this.animalType = AnimalTypes.DETRITIVORE;
                break;
            case "Parasites":
                this.animalType = AnimalTypes.PARASITE;
                break;
            default:
                throw new IllegalArgumentException("Invalid animal type: " + animalType);
        }
    }

    @Override
    public ObjectNode toNode() {
        ObjectNode animalNode = super.toNode();
        animalNode.put("type", animalType.getTypeName());
        return animalNode;
    }
}