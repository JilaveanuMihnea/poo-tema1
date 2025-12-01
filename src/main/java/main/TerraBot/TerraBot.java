package main.TerraBot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.Behaviours.Movable;
import main.Entities.Air.Air;
import main.Entities.Animals.Animal;
import main.Entities.Plants.Plant;
import main.Entities.Soil.Soil;
import main.Entities.Water.Water;
import main.MapUtility.Cell;
import main.MapUtility.TerraMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TerraBot implements Movable {
    private Battery battery;
    private PairInput position;
    private boolean hasEnergyForMove = true;
    private List<InventoryEntry> inventory = new ArrayList<>();

    private PairInput[] directions = {
            new PairInput(0, 1),   // Up
            new PairInput(1, 0),   // Right
            new PairInput(0, -1),  // Down
            new PairInput(-1, 0)   // Left
    };
    private TerraMap terraMap;

    public TerraBot(final int initialCharge, final TerraMap terraMap) {
        position = new PairInput(0, 0);
        this.battery = new Battery(initialCharge);
        this.terraMap = terraMap;
    }

    public void recharge(int extraCharge, int currentTimestamp) {
        battery.setCurrentCharge(battery.getCurrentCharge() + extraCharge);
        battery.setRechargingStopTime(currentTimestamp + extraCharge);
        battery.setCharging(true);
    }

    public boolean isCharging() {
        return battery.isCharging();
    }

    public boolean canPerformEnergyConsumingAction(int requiredEnergy) {
        return battery.getCurrentCharge() >= requiredEnergy;
    }

    private InventoryEntry getInventoryItemByName(String componentName) {
        for (InventoryEntry entry : inventory) {
            if (entry.getComoponentName().equals(componentName)) {
                return entry;
            }
        }
        return null;

    }

    public String scanObject(String color, String smell, String sound, int timestamp) {
        Cell currentCell = getCurrentCell();
        if (color.equals("none")) {
            Water water = currentCell.getWater();
            if (water == null) {
                return null;
            } else {
                battery.consumeCharge(7);
                water.setScanned(true);
                water.setScannedAt(timestamp);
                if (getInventoryItemByName(water.getName()) == null) {
                    this.inventory.add(new InventoryEntry(water.getName(), water.getMass(), water.getType()));
                }
                return "water";
            }
        } else if (sound.equals("none")) {
            Plant plant = currentCell.getPlant();
            if (plant == null) {
                return null;
            } else {
                battery.consumeCharge(7);
                plant.setScanned(true);
                if (getInventoryItemByName(plant.getName()) == null) {
                    this.inventory.add(new InventoryEntry(plant.getName(), plant.getMass(), plant.getTypeName()));
                }
                return "a plant";
            }
        } else {
            Animal animal = currentCell.getAnimal();
            if (animal == null) {
                return null;
            } else {
                battery.consumeCharge(7);
                terraMap.addScannedAnimal(animal);
                animal.setScanned(true);
                animal.setScannedAt(timestamp);
                if (getInventoryItemByName(animal.getName())  == null) {
                    this.inventory.add(new InventoryEntry(animal.getName(), animal.getMass(), animal.getTypeName()));
                }
                return "an animal";
            }
        }
    }

    public boolean learnFact(String componentName, String subject) {

        InventoryEntry entry = getInventoryItemByName(componentName);
        if (entry == null) {
            return false;
        }
        battery.consumeCharge(2);
        entry.setFact(subject);
        return true;
    }

    public ArrayNode printKnowledgeBase() {
        ArrayNode arrayNode = new ObjectMapper().createArrayNode();
        for (InventoryEntry entry : inventory) {
            if (entry.getFacts().isEmpty()) {
                continue;
            }
            ObjectNode entryNode = new ObjectMapper().createObjectNode();
            entryNode.put("topic", entry.getComoponentName());
            entryNode.put("facts", entry.getFactsNode());
            arrayNode.add(entryNode);
        }
        return arrayNode;
    }

    public String improveEnvironment(String improvementType, String componentName, String componentType) {
        InventoryEntry entry = getInventoryItemByName(componentName);
        if (entry == null) {
            return "ERROR: Subject not yet saved. Cannot perform action";
        }

        switch (improvementType) {
            case "plantVegetation" -> {
                if (entry.isPlantMethod()) {
                    battery.consumeCharge(10);
                    Air air = getCurrentCell().getAir();
                    air.updateOxygenLevel(0.3);
                    PlantInput plantInput = new PlantInput();
                    plantInput.setName(componentName);
                    plantInput.setMass(entry.getMass());
                    plantInput.setType(componentType);
                    return "The " + componentName + " was planted successfully.";
                } else {
                    return "ERROR: Fact not yet saved. Cannot perform action";
                }
            }
            case "fertilizeSoil" -> {
                if (entry.isFertilizeMethod()) {
                    battery.consumeCharge(10);
                    Soil soil = getCurrentCell().getSoil();
                    soil.updateOrganicMatter(0.3);
                    return "The soil was succesfully fertilized using " + componentName + ".";
                } else {
                    return "ERROR: Fact not yet saved. Cannot perform action";
                }
            }
            case "increaseHumidity" -> {
                if (entry.isHumidityMethod()) {
                    battery.consumeCharge(10);
                    Air air = getCurrentCell().getAir();
                    air.updateHumidity(0.3);
                    return "The humidity was successfully increased using " + componentName + ".";
                } else {
                    return "ERROR: Fact not yet saved. Cannot perform action";
                }
            }
            case "increaseMoisture" -> {
                if (entry.isMoistureMethod()) {
                    battery.consumeCharge(10);
                    Soil soil = getCurrentCell().getSoil();
                    soil.updateWaterRetention(0.2);
                    return "The moisture was successfully increased using " + componentName;
                } else {
                    return "ERROR: Fact not yet saved. Cannot perform action";
                }
            }
            default -> {
                return "improvement_type_not_found";
            }
        }

    }

    @Override
    public void move() {
        int bestConsumption = Integer.MAX_VALUE;
        int choiceIndex = -1;
        for (PairInput direction : directions) {
            int newX = position.getX() + direction.getX();
            int newY = position.getY() + direction.getY();
            Cell nextCell = terraMap.getCell(newX, newY);

            if (nextCell != null) {
                if (nextCell.computeConsumption() < bestConsumption) {
                    bestConsumption = nextCell.computeConsumption();
                    choiceIndex = Arrays.asList(directions).indexOf(direction);
                }
            }
        }

        if (battery.consumeCharge(bestConsumption)) {
            hasEnergyForMove = true;
            position.setX(position.getX() + directions[choiceIndex].getX());
            position.setY(position.getY() + directions[choiceIndex].getY());
        } else {
            hasEnergyForMove = false;
        }
    }

    private Cell getCurrentCell() {
        return terraMap.getCell(position.getX(), position.getY());
    }
}