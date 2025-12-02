package main.Entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class Entity {
    private String name;
    private double mass;
    private boolean scanned = false;

    public Entity(final String name, final double mass) {
        this.name = name;
        this.mass = mass;
    }

    /**
     * Converts the entity to a JSON object node
     * @return the JSON object node representing the entity
     */
    public ObjectNode toNode() {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("name", name);
        node.put("mass", mass);
        return node;
    }
}
