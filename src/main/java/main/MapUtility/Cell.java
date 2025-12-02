package main.MapUtility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Entities.Soil.Soil;
import main.Entities.Air.Air;
import main.Entities.Water.Water;
import main.Entities.Plants.Plant;
import main.Entities.Animals.Animal;

@Data
@NoArgsConstructor
public final class Cell {
    private Soil soil;
    private Air air;
    private Water water;
    private Plant plant;
    private Animal animal;

    public Cell(final Soil soil, final Air air, final Water water,
                final Plant plant, final Animal animal) {
        this.soil = soil;
        this.air = air;
        this.water = water;
        this.plant = plant;
        this.animal = animal;
    }

    /**
     * Converts the cell's environmental conditions to a JSON ObjectNode
     *
     * @return ObjectNode representing the cell's environmental conditions
     */
    public ObjectNode toNodeEnvConditions() {
        ObjectNode cellNode = new ObjectMapper().createObjectNode();
        if (soil != null) {
            cellNode.put("soil", soil.toNode());
        }
        if (air != null) {
            cellNode.put("air", air.toNode());
        }
        if (water != null) {
            cellNode.put("water", water.toNode());
        }
        if (plant != null) {
            cellNode.put("plants", plant.toNode());
        }
        if (animal != null) {
            cellNode.put("animals", animal.toNode());
        }
        return cellNode;
    }

    /**
     * Computes the energy required for the robot to move on this cell
     *
     * @return the computed energy consumption as an integer
     */
    public int computeConsumption() {
        int count = 2;
        double sum = soil.getBlockChance();
        sum += air.getDamageChance();
        if (animal != null) {
            sum += animal.getAttackChance();
            count++;
        }
        if (plant != null) {
            sum += plant.getTangleChance();
            count++;
        }
        sum = Math.abs(sum / count);

        return (int) Math.round(sum);
    }
}
