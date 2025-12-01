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

    public Entity(String name, double mass) {
        this.name = name;
        this.mass = mass;
    }

    public ObjectNode toNode() {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("name", name);
        node.put("mass", mass);
        return node;
    }
}
