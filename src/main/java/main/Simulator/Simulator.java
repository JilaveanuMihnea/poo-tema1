package main.Simulator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.*;
import main.MapUtility.TerraMap;
import main.TerraBot.TerraBot;

import java.util.*;

public class Simulator {
    private boolean isStarted = false;
    private TerraMap terraMap = new TerraMap();
    private TerraBot terraBot;
    private PriorityQueue<Map.Entry<Integer, String>> weatherQueue =
            new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getKey));


    public void simulate(InputLoader inputLoader, ArrayNode output, ObjectMapper MAPPER) {
        List<SimulationInput> simulations = inputLoader.getSimulations();
        for (SimulationInput simulation : simulations) {
            terraMap.mapBuilder(simulation);
            terraBot = new TerraBot(simulation.getEnergyPoints(), terraMap);
        }

        List<CommandInput> commands = inputLoader.getCommands();

        for (CommandInput command : commands) {
            updateSimulation(command.getTimestamp());
            ObjectNode node = MAPPER.createObjectNode();
            switch (command.getCommand()) {
                case "startSimulation":
                    output.add(startSimulation(command));
                    break;
                case "endSimulation":
                    output.add(endSimulation(command));
                    break;
                case "printEnvConditions":
                    output.add(printEnvConditions(command));
                    break;
                case "printMap":
                    output.add(printMap(command));
                    break;
                case "moveRobot":
                    output.add(moveRobot(command));
                    break;
                case "getEnergyStatus":
                    output.add(getEnergyStatus(command));
                    break;
                case "rechargeBattery":
                    output.add(rechargeBattery(command));
                    break;
                case "changeWeatherConditions":
                    output.add(changeWeatherConditions(command));
                    break;
                case "scanObject":
                    // Not implemented
                    break;
            }
        }
    }

    private void updateSimulation(int currentTimestamp) {
        if (isStarted && terraBot.isCharging()) {
            if (currentTimestamp >= terraBot.getBattery().getRechargingStopTime()) {
                terraBot.getBattery().setCharging(false);
            }
        }
        if(!weatherQueue.isEmpty()){
            if(currentTimestamp == weatherQueue.peek().getKey()){
                String weatherType = weatherQueue.poll().getValue();
                terraMap.resetWeather(weatherType);
                weatherQueue.poll();
            }
        }

    }

    private ObjectNode startSimulation(CommandInput command) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("command", "startSimulation");
        if (!isStarted) {
            isStarted = true;
            node.put("message", "Simulation has started.");
        } else {
            node.put("message", "ERROR: Simulation already started. Cannot perform action");
        }
        node.put("timestamp", command.getTimestamp());
        return node;
    }

    private ObjectNode endSimulation(CommandInput command) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("command", "endSimulation");
        if (isStarted) {
            isStarted = false;
            node.put("message", "Simulation has ended.");
        } else {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
        }
        node.put("timestamp", command.getTimestamp());
        return node;
    }

    private ObjectNode printEnvConditions(CommandInput command) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("command", "printEnvConditions");
        if (!isStarted) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else if (terraBot.isCharging()) {
            node.put("message", "ERROR: Robot still charging. Cannot perform action");
        } else {
            node.put("output", terraMap.getCell(terraBot.getPosition().getX(), terraBot.getPosition().getY()).toNodeEnvConditions());
        }

        node.put("timestamp", command.getTimestamp());
        return node;
    }

    private ObjectNode printMap(CommandInput command) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("command", "printMap");
        if (!isStarted) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else {
            node.put("output", terraMap.printMap());
        }
        node.put("timestamp", command.getTimestamp());
        return node;
    }

    private ObjectNode moveRobot(CommandInput command) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("command", "moveRobot");
        if (!isStarted) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else if (terraBot.isCharging()) {
            node.put("message", "ERROR: Robot still charging. Cannot perform action");
        } else {
            terraBot.move();
            if (terraBot.isHasEnergyForMove()) {
                node.put("message", "The robot has successfully moved to position (" +
                        terraBot.getPosition().getX() + ", " +
                        terraBot.getPosition().getY() + ").");
            } else {
                node.put("message", "ERROR: Not enough battery left. Cannot perform action");
            }
        }
        node.put("timestamp", command.getTimestamp());
        return node;
    }

    private ObjectNode getEnergyStatus(CommandInput command) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("command", "getEnergyStatus");
        if (!isStarted) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else if (terraBot.isCharging()) {
            node.put("message", "ERROR: Robot still charging. Cannot perform action");
        } else {
            node.put("message", "TerraBot has " + terraBot.getBattery().getCurrentCharge() + " energy points left.");
        }
        node.put("timestamp", command.getTimestamp());
        return node;
    }

    private ObjectNode rechargeBattery(CommandInput command) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("command", "rechargeBattery");
        if (!isStarted) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else if (terraBot.isCharging()) {
            node.put("message", "ERROR: Robot still charging. Cannot perform action");
        } else {
            terraBot.recharge(command.getTimeToCharge(), command.getTimestamp());
            node.put("message", "Robot battery is charging.");
        }
        node.put("timestamp", command.getTimestamp());
        return node;
    }

    private ObjectNode changeWeatherConditions(CommandInput command) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("command", "changeWeatherConditions");
        if (!isStarted) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else {
            if (terraMap.changeWeather(command)) {
                node.put("message", "The weather has changed.");
                weatherQueue.add(new AbstractMap.SimpleEntry<>(
                        command.getTimestamp() + 2,
                        command.getType()
                ));
            } else {
                node.put("message", "ERROR: The weather change does not affect the environment. Cannot perform action");
            }
        }
        node.put("timestamp", command.getTimestamp());
        return node;
    }

    private ObjectNode scanObject(CommandInput command) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("command", "scanObject");

        node.put("timestamp", command.getTimestamp());
        return node;
    }
}
