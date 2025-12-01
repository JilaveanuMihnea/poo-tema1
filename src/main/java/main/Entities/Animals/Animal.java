package main.Entities.Animals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AnimalInput;
import fileio.PairInput;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Behaviours.Movable;
import main.Entities.Entity;
import main.Entities.Water.Water;
import main.MapUtility.Cell;
import main.MapUtility.TerraMap;

import java.util.LinkedList;

@Data
@NoArgsConstructor
public class Animal extends Entity implements Movable {
    private AnimalTypes animalType;
    private PairInput[] directions = {
            new PairInput(0, 1),   // Up
            new PairInput(1, 0),   // Right
            new PairInput(0, -1),  // Down
            new PairInput(-1, 0)   // Left
    };
    private PairInput position;
    private TerraMap terraMap;
    private HungerState hungerState;
    private double organicMatterProduction = 0.0;
    private int scannedAt;

    public enum HungerState {
        HUNGRY,
        WELL_FED,
        SICK;
    }

    public Animal(AnimalInput animalInput, PairInput position, TerraMap terraMap) {
        super(animalInput.getName(), animalInput.getMass());
        this.position = position;
        this.terraMap = terraMap;
        this.hungerState = HungerState.HUNGRY;
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

    public double getAttackChance() {
        return animalType.getAttackChance();
    }

    public void feed() {
        Cell newCell = terraMap.getCell(this.position.getX(), this.position.getY());
        if (newCell.getAnimal() != null) {
            if (animalType == AnimalTypes.CARNIVORE || animalType == AnimalTypes.PARASITE) {
                this.setMass(this.getMass() + newCell.getAnimal().getMass());
                newCell.setAnimal(null);
                this.hungerState = HungerState.WELL_FED;
                this.organicMatterProduction = 0.5;
            } else {
                throw new IllegalArgumentException("Sper ca nu exista cazul asta");
            }
        } else {
            double extraMass = 0;
            boolean atePlant = false;
            boolean ateWater = false;
            if (newCell.getPlant() != null && newCell.getPlant().isScanned()) {
                extraMass = newCell.getPlant().getMass();
                newCell.setPlant(null);
                atePlant = true;
            }
            if (newCell.getWater() != null) {
                Water water = newCell.getWater();
                extraMass += Math.min(this.getMass() * 0.08, water.getMass());
                water.setMass(water.getMass() - Math.min(this.getMass() * 0.08, water.getMass()));
                ateWater = true;
            }
            this.setMass(this.getMass() + extraMass);
            this.hungerState = HungerState.WELL_FED;
            if (atePlant && ateWater) {
                this.organicMatterProduction = 0.8;
            } else if (atePlant || ateWater) {
                this.organicMatterProduction = 0.5;
            } else {
                this.organicMatterProduction = 0.0;
            }
        }
    }

    @Override
    public void move() {
        int choiceIndex = -1;
        double bestQuality = -1;
        boolean plantFound = false;
        boolean waterFound = false;
        boolean plantSet = false;
        boolean plantAndWaterFound = false;
        for (PairInput direction : directions) {
            PairInput newPosition = new PairInput(
                    position.getX() + direction.getX(),
                    position.getY() + direction.getY()
            );
            Cell nextCell = terraMap.getCell(newPosition.getX(), newPosition.getY());
            plantFound = false;
            waterFound = false;
            if (nextCell != null) {
                if (nextCell.getPlant() != null && nextCell.getPlant().isScanned()) {
                    plantFound = true;
                    if (!plantAndWaterFound) {
                        plantSet = true;
                        choiceIndex = direction == directions[0] ? 0 :
                                      direction == directions[1] ? 1 :
                                      direction == directions[2] ? 2 : 3;
                    }
                }

                if (nextCell.getWater() != null && nextCell.getWater().isScanned()) {
                    waterFound = true;
                    if (bestQuality < nextCell.getWater().getWaterQuality() && !plantAndWaterFound && !plantSet) {
                        bestQuality = nextCell.getWater().getWaterQuality();
                        choiceIndex = direction == directions[0] ? 0 :
                                      direction == directions[1] ? 1 :
                                      direction == directions[2] ? 2 : 3;
                    }
                }

                if (plantFound && waterFound) {
                    if (bestQuality < nextCell.getWater().getWaterQuality()) {
                        bestQuality = nextCell.getWater().getWaterQuality();
                        choiceIndex = direction == directions[0] ? 0 :
                                direction == directions[1] ? 1 :
                                        direction == directions[2] ? 2 : 3;
                    }
                    if (!plantAndWaterFound) {
                        plantAndWaterFound = true;
                    }
                }
            }
        }

        if (choiceIndex == -1) {
            for (PairInput direction : directions) {
                PairInput newPosition = new PairInput(
                        position.getX() + direction.getX(),
                        position.getY() + direction.getY()
                );
                Cell nextCell = terraMap.getCell(newPosition.getX(), newPosition.getY());
                if (nextCell != null) {
                    choiceIndex = direction == directions[0] ? 0 :
                                  direction == directions[1] ? 1 :
                                  direction == directions[2] ? 2 : 3;
                    break;
                }
            }
        }

        Cell oldCell = terraMap.getCell(position.getX(), position.getY());

        this.position.setX(position.getX() + directions[choiceIndex].getX());
        this.position.setY(position.getY() + directions[choiceIndex].getY());

        feed();
        Cell newCell = terraMap.getCell(position.getX(), position.getY());

        newCell.setAnimal(this);
        oldCell.setAnimal(null);
    }

    public String getTypeName () {
        return animalType.getTypeName();
    }
}