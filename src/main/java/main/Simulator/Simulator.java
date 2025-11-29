package main.Simulator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.*;
import main.MapUtility.Map;

import java.util.List;

public class Simulator {
    private boolean isStarted = false;
    private Map map = new Map();

    public void simulate(InputLoader inputLoader, ArrayNode output, ObjectMapper MAPPER) {
        // Simulation logic to be implemented

        List<SimulationInput> simulations = inputLoader.getSimulations();
        for (SimulationInput simulation : simulations) {
            map.mapBuilder(simulation);
        }

        List<CommandInput> commands = inputLoader.getCommands();



        for (CommandInput command : commands) {
            ObjectNode node = MAPPER.createObjectNode();
            switch (command.getCommand()) {
                case "startSimulation":
                    node.put("command", "startSimulation");
                    if (!isStarted) {
                        isStarted = true;
                        node.put("message", "Simulation has started.");
                    } else {
                        node.put("message", "ERROR: Simulation already started. Cannot perform action");
                    }
                    node.put("timestamp", command.getTimestamp());
                    output.add(node);
                    break;
                case "endSimulation":
                    node.put("command", "endSimulation");
                    if (isStarted) {
                        isStarted = false;
                        node.put("message", "Simulation has ended.");
                    } else {
                        node.put("message", "ERROR: Simulation not started. Cannot perform action");
                    }
                    node.put("timestamp", command.getTimestamp());
                    output.add(node);
                    break;
                case "printEnvConditions":
                    node.put("command", "printEnvConditions");
                    if (!isStarted) {
                        node.put("message", "ERROR: Simulation not started. Cannot perform action");
                    } else {
                        //TODO: fix coordinates here
                        node.put("output", map.getCell(0, 0).toNodeEnvConditions());
                    }

                    node.put("timestamp", command.getTimestamp());
                    output.add(node);
                    break;
                case "printMap":
                    node.put("command", "printMap");
                    if (!isStarted) {
                        node.put("message", "ERROR: Simulation not started. Cannot perform action");
                    } else {
                        node.put("output", map.printMap());
                    }
                    node.put("timestamp", command.getTimestamp());
                    output.add(node);
                    break;
            }
        }
    }
}