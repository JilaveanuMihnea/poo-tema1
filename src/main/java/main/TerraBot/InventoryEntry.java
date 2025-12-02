package main.TerraBot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public final class InventoryEntry {
    private List<String> facts;
    private String comoponentName;
    private double mass;
    private String componentType;
    private boolean plantMethod = false;
    private boolean fertilizeMethod = false;
    private boolean humidityMethod = false;
    private boolean moistureMethod = false;

    public InventoryEntry(final String comoponentName, final double mass,
                          final String componentType) {
        this.comoponentName = comoponentName;
        this.mass = mass;
        this.componentType = componentType;
        facts = new ArrayList<>();
    }

    /**
     * Sets a fact and updates method flags based on the fact's content.
     * @param subject The fact to be added.
     */
    public void setFact(final String subject) {
        facts.add(subject);
        // Third word in a "method" fact indicates the type of method
        String relevantWord = subject.split(" ")[2];
        switch (relevantWord) {
            case "plant" -> this.plantMethod = true;
            case "fertilize" -> this.fertilizeMethod = true;
            case "increase" -> this.humidityMethod = true; //humidity
            case "increaseMoisture" -> this.moistureMethod = true;
            default -> {
                return;
            }
        }
    }

    /**
     * Converts the list of facts into a JSON ArrayNode.
     * @return An ArrayNode containing all facts.
     */
    public ArrayNode getFactsNode() {
        ArrayNode factsNode = new ObjectMapper().createArrayNode();
        for (String fact : facts) {
            factsNode.add(fact);
        }
        return factsNode;
    }

}
