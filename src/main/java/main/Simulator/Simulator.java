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
    private TerraMap terraMap;
    private TerraBot terraBot;
    private boolean nextSimFlag = false;
    private PriorityQueue<Map.Entry<Integer, String>> weatherQueue =
            new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getKey));


    public void simulate(InputLoader inputLoader, ArrayNode output, ObjectMapper MAPPER) {
        int lastTimestamp = 0;
        int commandIndex = 0;
        List<SimulationInput> simulations = inputLoader.getSimulations();
        List<CommandInput> commands = inputLoader.getCommands();

        for (SimulationInput simulation : simulations) {
            weatherQueue.clear();
            lastTimestamp = 0;
            terraMap = new TerraMap();
            terraMap.mapBuilder(simulation);
            terraBot = new TerraBot(simulation.getEnergyPoints(), terraMap);


            for (int currentCommandIndex = commandIndex; currentCommandIndex < commands.size(); currentCommandIndex++) {
                CommandInput command = commands.get(currentCommandIndex);
                if (command.getTimestamp() > lastTimestamp + 1) {
                    updateSequence(lastTimestamp, command.getTimestamp() - lastTimestamp - 1);
                }

                updateSimulation(command.getTimestamp());

                commandHandler(command, output);

                lastTimestamp = command.getTimestamp();
                if (nextSimFlag) {
                    nextSimFlag = false;
                    commandIndex = currentCommandIndex + 1;
                    break;
                }
            }
        }

        for (int currentCommandIndex = commandIndex; currentCommandIndex < commands.size(); currentCommandIndex++) {
            commandHandler(commands.get(currentCommandIndex), output);
        }


    }

    private void commandHandler(CommandInput command, ArrayNode output) {
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
                output.add(scanObject(command));
                break;
            case "learnFact":
                output.add(learnFact(command));
                break;
            case "printKnowledgeBase":
                output.add(printKnowledgeBase(command));
                break;
            case "improveEnvironment":
                output.add(improveEnvironment(command));
                break;
        }
    }

    private void updateSequence(int currentTimestamp, int repeats) {
        for (int i = 0; i < repeats; i++, currentTimestamp++) {
            if(!weatherQueue.isEmpty()){
                if(currentTimestamp == weatherQueue.peek().getKey()){
                    String weatherType = weatherQueue.peek().getValue();
                    terraMap.resetWeather(weatherType);
                    weatherQueue.poll();
                }
            }

            // order: air. soil, water, plant, animal
            terraMap.interactionSoilPlant();

            terraMap.interactionWaterAir(currentTimestamp);
            terraMap.interactionWaterSoil(currentTimestamp);
            terraMap.interactionWaterPlant();

            terraMap.interactionPlantAir();

            terraMap.interactionAnimalSoil();
            terraMap.interactionAnimalFood(currentTimestamp);

        }

    }

    private void updateSimulation(int currentTimestamp) {

        if (isStarted && terraBot.isCharging()) {
            if (currentTimestamp >= terraBot.getBattery().getRechargingStopTime()) {
                terraBot.getBattery().setCharging(false);
            }
        }

        updateSequence(currentTimestamp, 1);

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
            nextSimFlag = true;
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
        if (!isStarted) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else if (terraBot.isCharging()) {
            node.put("message", "ERROR: Robot still charging. Cannot perform action");
        } else if  (!terraBot.canPerformEnergyConsumingAction(7)) {
            node.put("message", "ERROR: Not enough energy to perform action");
        } else {
            String scannedObject = terraBot.scanObject(command.getColor(),
                    command.getSmell(), command.getSound(), command.getTimestamp());
            if (scannedObject != null) {
                node.put("message", "The scanned object is " + scannedObject + ".");
            } else {
                node.put("message", "ERROR: Object not found. Cannot perform action");
            }
        }
        node.put("timestamp", command.getTimestamp());
        return node;
    }

    private ObjectNode learnFact(CommandInput command) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("command", "learnFact");
        if (!isStarted) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else if (terraBot.isCharging()) {
            node.put("message", "ERROR: Robot still charging. Cannot perform action");
        } else if  (!terraBot.canPerformEnergyConsumingAction(2)) {
            node.put("message", "ERROR: Not enough battery left. Cannot perform action");
        } else {
            if (terraBot.learnFact(command.getComponents(), command.getSubject())) {
                node.put("message", "The fact has been successfully saved in the database.");
            } else {
                node.put("message", "ERROR: Subject not yet saved. Cannot perform action");
            }
        }
        node.put("timestamp", command.getTimestamp());
        return node;
    }

    private ObjectNode printKnowledgeBase(CommandInput command) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("command", "printKnowledgeBase");
        if (!isStarted) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else {
            node.put("output", terraBot.printKnowledgeBase());
        }
        node.put("timestamp", command.getTimestamp());
        return node;
    }

    private ObjectNode improveEnvironment(CommandInput command) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("command", "improveEnvironment");
        if (!isStarted) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else if (terraBot.isCharging()) {
            node.put("message", "ERROR: Robot still charging. Cannot perform action");
        } else if  (!terraBot.canPerformEnergyConsumingAction(10)) {
            node.put("message", "ERROR: Not enough battery left. Cannot perform action");
        } else {
            node.put("message", terraBot.improveEnvironment(
                    command.getImprovementType(),
                    command.getName(),
                    command.getType()
            ));
        }
        node.put("timestamp", command.getTimestamp());
        return node;
    }
}
