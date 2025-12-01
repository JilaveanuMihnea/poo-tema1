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
public class InventoryEntry {
    private List<String> facts;
    private String comoponentName;
    private double mass;
    private String componentType;
    private boolean plantMethod = false;
    private boolean fertilizeMethod = false;
    private boolean humidityMethod = false;
    private boolean moistureMethod = false;

    public InventoryEntry(String comoponentName, double mass, String componentType) {
        this.comoponentName = comoponentName;
        this.mass = mass;
        this.componentType = componentType;
        facts = new ArrayList<>();
    }

    public void setFact (String subject) {
        facts.add(subject);
        //get third word of string method
        String relevantWord = subject.split(" ")[2];
        switch (relevantWord) {
            case "plant" -> this.plantMethod = true;
            case "fertilize" -> this.fertilizeMethod = true;
            case "increase" -> this.humidityMethod = true; //humidity
            case "increaseMoisture" -> this.moistureMethod = true;
        }
    }

    public ArrayNode getFactsNode() {
        ArrayNode factsNode = new ObjectMapper().createArrayNode();
        for (String fact : facts) {
            factsNode.add(fact);
        }
        return factsNode;
    }

}
