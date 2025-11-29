package main.MapUtility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.Entities.Soil.*;
import main.Entities.Air.*;
import main.Entities.Water.*;
import main.Entities.Plants.*;
import main.Entities.Animals.*;

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

}