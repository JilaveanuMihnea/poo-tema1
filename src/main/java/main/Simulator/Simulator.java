package main.Simulator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.*;

import java.util.List;

public class Simulator {

    public void simulate(InputLoader inputLoader, ArrayNode output, ObjectMapper MAPPER) {
        // Simulation logic to be implemented

        List<SimulationInput> simulations = inputLoader.getSimulations();
        List<CommandInput> commands = inputLoader.getCommands();



        for (CommandInput command : commands) {
            switch (command.getCommand()) {
                case "startSimulation":
                    ObjectNode simulationNode = MAPPER.createObjectNode();
                    simulationNode.put("command", "startSimulation");
                    simulationNode.put("message", "Simulation started");
                    simulationNode.put("timestamp", command.getTimestamp());
                    output.add(simulationNode);
                    break;
            }
        }
    }
}