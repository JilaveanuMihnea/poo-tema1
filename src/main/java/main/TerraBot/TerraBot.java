package main.TerraBot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.PairInput;
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
import main.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public final class TerraBot implements Movable {
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

    /**
     * Sets TerraBot to recharging state and updates its battery charge and recharging stop time
     * @param extraCharge the amount of battery recharge
     * @param currentTimestamp timestamp when recharging starts (to calculate the
     *                         recharging stop time)
     */
    public void recharge(final int extraCharge, final int currentTimestamp) {
        battery.setCurrentCharge(battery.getCurrentCharge() + extraCharge);
        battery.setRechargingStopTime(currentTimestamp + extraCharge);
        battery.setCharging(true);
    }

    public boolean isCharging() {
        return battery.isCharging();
    }

    /**
     * Checks if TerraBot has enough energy to perform an action that consumes energy
     * @param requiredEnergy the amount of energy required to perform the action
     * @return true if TerraBot has enough energy, false otherwise
     */
    public boolean canPerformEnergyConsumingAction(final int requiredEnergy) {
        return battery.getCurrentCharge() >= requiredEnergy;
    }

    /**
     * Gets an inventory item by its name
     * @param componentName the name of the inventory item
     * @return the InventoryEntry object if found, null otherwise
     */
    private InventoryEntry getInventoryItemByName(final String componentName) {
        for (InventoryEntry entry : inventory) {
            if (entry.getComoponentName().equals(componentName)) {
                return entry;
            }
        }
        return null;

    }

    /**
     * Scans an object in the current cell based on the provided attributes
     * @param color the color attribute of the object
     * @param smell the smell attribute of the object
     * @param sound the sound attribute of the object
     * @param timestamp the current timestamp
     * @return a string indicating the type of object scanned, or null if object is not found
     */
    public String scanObject(final String color, final String smell, final String sound,
                             final int timestamp) {
        Cell currentCell = getCurrentCell();
        if (color.equals("none")) {
            Water water = currentCell.getWater();
            if (water == null) {
                return null;
            } else {
                battery.consumeCharge(Constants.TERRABOT_SCAN_ENERGY);
                water.setScanned(true);
                water.setScannedAt(timestamp);
                if (getInventoryItemByName(water.getName()) == null) {
                    this.inventory.add(new InventoryEntry(water.getName(), water.getMass(),
                                                          water.getType()));
                }
                return "water";
            }
        } else if (sound.equals("none")) {
            Plant plant = currentCell.getPlant();
            if (plant == null) {
                return null;
            } else {
                battery.consumeCharge(Constants.TERRABOT_SCAN_ENERGY);
                plant.setScanned(true);
                if (getInventoryItemByName(plant.getName()) == null) {
                    this.inventory.add(new InventoryEntry(plant.getName(), plant.getMass(),
                                                          plant.getTypeName()));
                }
                return "a plant";
            }
        } else {
            Animal animal = currentCell.getAnimal();
            if (animal == null) {
                return null;
            } else {
                battery.consumeCharge(Constants.TERRABOT_SCAN_ENERGY);
                terraMap.addScannedAnimal(animal);
                animal.setScanned(true);
                animal.setScannedAt(timestamp);
                if (getInventoryItemByName(animal.getName())  == null) {
                    this.inventory.add(new InventoryEntry(animal.getName(), animal.getMass(),
                                                          animal.getTypeName()));
                }
                return "an animal";
            }
        }
    }

    /**
     * Learns a fact about a component and updates the inventory
     * @param componentName the name of the component
     * @param subject the fact to be learned
     * @return true if the fact was learned successfully, false if item not in inventory
     */
    public boolean learnFact(final String componentName, final String subject) {

        InventoryEntry entry = getInventoryItemByName(componentName);
        if (entry == null) {
            return false;
        }
        battery.consumeCharge(2);
        entry.setFact(subject);
        return true;
    }

    /**
     * Gets the knowledge base of TerraBot as a JSON array
     * @return an ArrayNode representing the knowledge base
     */
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

    /**
     * Improves the environment based on the improvement type and component used
     * @param improvementType the improvement action to be performed
     * @param componentName the name of the component used for improvement
     * @return
     */
    public String improveEnvironment(final String improvementType, final String componentName) {
        InventoryEntry entry = getInventoryItemByName(componentName);
        if (entry == null) {
            return "ERROR: Subject not yet saved. Cannot perform action";
        }

        switch (improvementType) {
            case "plantVegetation" -> {
                if (entry.isPlantMethod()) {
                    battery.consumeCharge(Constants.TERRABOT_IMPROVE_ENERGY);
                    Air air = getCurrentCell().getAir();
                    air.updateOxygenLevel(Constants.IMPROVE_PLANT_VALUE);
                    return "The " + componentName + " was planted successfully.";
                } else {
                    return "ERROR: Fact not yet saved. Cannot perform action";
                }
            }
            case "fertilizeSoil" -> {
                if (entry.isFertilizeMethod()) {
                    battery.consumeCharge(Constants.TERRABOT_IMPROVE_ENERGY);
                    Soil soil = getCurrentCell().getSoil();
                    soil.updateOrganicMatter(Constants.IMPROVE_FERTILIZE_VALUE);
                    return "The soil was succesfully fertilized using " + componentName + ".";
                } else {
                    return "ERROR: Fact not yet saved. Cannot perform action";
                }
            }
            case "increaseHumidity" -> {
                if (entry.isHumidityMethod()) {
                    battery.consumeCharge(Constants.TERRABOT_IMPROVE_ENERGY);
                    Air air = getCurrentCell().getAir();
                    air.updateHumidity(Constants.IMPROVE_HUMIDITY_VALUE);
                    return "The humidity was successfully increased using " + componentName + ".";
                } else {
                    return "ERROR: Fact not yet saved. Cannot perform action";
                }
            }
            case "increaseMoisture" -> {
                if (entry.isMoistureMethod()) {
                    battery.consumeCharge(Constants.TERRABOT_IMPROVE_ENERGY);
                    Soil soil = getCurrentCell().getSoil();
                    soil.updateWaterRetention(Constants.IMPROVE_MOISTURE_VALUE);
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
