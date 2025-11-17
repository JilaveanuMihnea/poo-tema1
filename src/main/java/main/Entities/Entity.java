package main.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class Entity {
    private String name;
    private double mass;

    public Entity(String name, double mass) {
        this.name = name;
        this.mass = mass;
    }
}
