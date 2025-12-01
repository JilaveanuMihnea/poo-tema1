package main.MapUtility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;
import main.Entities.Soil.*;
import main.Entities.Air.*;
import main.Entities.Animals.*;
import main.Entities.Plants.*;
import main.Entities.Water.*;

import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
public final class TerraMap {
    private Cell[][] grid;
    private int rows, cols;
    private boolean activeDesertStorm = false;
    private boolean activeRainfall = false;
    private boolean activePolarWind = false;
    private boolean activeHikers = false;
    private boolean activeSeason = false;
    private boolean changeFoundFlag = false;
    private LinkedList<Animal> scannedAnimals = new LinkedList<>();

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return null;
        }
        return grid[row][col];
    }

    public void mapBuilder(SimulationInput simulationInput) {
        String dim = simulationInput.getTerritoryDim();
        String[] dimensions = dim.split("x");
        int rows = Integer.parseInt(dimensions[0]);
        int cols = Integer.parseInt(dimensions[1]);
        grid = new Cell[rows][cols];
        this.rows = rows;
        this.cols = cols;
        List<SoilInput> soils = simulationInput.getTerritorySectionParams().getSoil();
        List<PlantInput> plants = simulationInput.getTerritorySectionParams().getPlants();
        List<AnimalInput> animals = simulationInput.getTerritorySectionParams().getAnimals();
        List<WaterInput> waters = simulationInput.getTerritorySectionParams().getWater();
        List<AirInput> airs = simulationInput.getTerritorySectionParams().getAir();

        for (SoilInput soil : soils) {
            for (PairInput pair : soil.getSections()) {
                int r = pair.getX();
                int c = pair.getY();
                if (grid[r][c] == null) {
                    grid[r][c] = new Cell();
                }
                switch (soil.getType()) {
                    case "ForestSoil" -> grid[r][c].setSoil(new ForestSoil(soil));
                    case "SwampSoil" -> grid[r][c].setSoil(new SwampSoil(soil));
                    case "DesertSoil" -> grid[r][c].setSoil(new DesertSoil(soil));
                    case "GrasslandSoil" -> grid[r][c].setSoil(new GrasslandSoil(soil));
                    case "TundraSoil" -> grid[r][c].setSoil(new TundraSoil(soil));
                }
            }
        }

        for (PlantInput plant : plants) {
            for (PairInput pair : plant.getSections()) {
                int r = pair.getX();
                int c = pair.getY();
                if (grid[r][c] == null) {
                    grid[r][c] = new Cell();
                }
                grid[r][c].setPlant(new Plant(plant));
            }
        }

        for (AnimalInput animal : animals) {
            for (PairInput pair : animal.getSections()) {
                int r = pair.getX();
                int c = pair.getY();
                if (grid[r][c] == null) {
                    grid[r][c] = new Cell();
                }
                grid[r][c].setAnimal(new Animal(animal, pair, this));
            }
        }

        for (WaterInput water : waters) {
            for (PairInput pair : water.getSections()) {
                int r = pair.getX();
                int c = pair.getY();
                if (grid[r][c] == null) {
                    grid[r][c] = new Cell();
                }
                grid[r][c].setWater(new Water(water));
            }
        }

        for (AirInput air : airs) {
            for (PairInput pair : air.getSections()) {
                int r = pair.getX();
                int c = pair.getY();
                if (grid[r][c] == null) {
                    grid[r][c] = new Cell();
                }
                switch (air.getType()) {
                    case "DesertAir" -> grid[r][c].setAir(new DesertAir(air));
                    case "MountainAir" -> grid[r][c].setAir(new MountainAir(air));
                    case "PolarAir" -> grid[r][c].setAir(new PolarAir(air));
                    case "TropicalAir" -> grid[r][c].setAir(new TropicalAir(air));
                    case "TemperateAir" -> grid[r][c].setAir(new TemperateAir(air));
                }
            }
        }

    }

    public void addScannedAnimal(Animal animal) {
        scannedAnimals.add(animal);
    }

    public ArrayNode printMap() {
        ArrayNode mapNodes = new ObjectMapper().createArrayNode();
        int objectNum;
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                ObjectNode cellNode = new ObjectMapper().createObjectNode();
                ArrayNode section = new ObjectMapper().createArrayNode();
                section.add(r);
                section.add(c);
                cellNode.put("section", section);

                Cell cell = grid[r][c];

                cellNode.put("soilQuality", cell.getSoil().getInterpretedSoilQuality());
                cellNode.put("airQuality", cell.getAir().getInterpretedAirQuality());

                objectNum = 0;
                if (cell.getWater() != null) {
                    objectNum++;
                }
                if (cell.getPlant() != null) {
                    objectNum++;
                }
                if (cell.getAnimal() != null) {
                    objectNum++;
                }
                cellNode.put("totalNrOfObjects",objectNum);
                mapNodes.add(cellNode);
            }
        }

        return mapNodes;
    }

    private void mapWeatherUpdate(String type, Object weatherCondition) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                if(cell.getAir().getType().equalsIgnoreCase(type)) {
                    cell.getAir().updateAirQuality(weatherCondition);
                    changeFoundFlag = true;
                }
            }
        }
    }

    public void resetWeather(String type){
        switch (type) {
            case "desertStorm" -> {
                activeDesertStorm = false;
                mapWeatherUpdate("DesertAir", false);
            }
            case "rainfall" -> {
                activeRainfall = false;
                mapWeatherUpdate("TropicalAir", 0);
            }
            case "polarStorm" -> {
                activePolarWind = false;
                mapWeatherUpdate("PolarAir", 0);
            }
            case "peopleHiking" -> {
                activeHikers = false;
                mapWeatherUpdate("MountainAir", 0);
            }
            case "newSeason" -> {
                activeSeason = false;
                mapWeatherUpdate("TemperateAir", "");
            }
        }
    }

    public boolean changeWeather(CommandInput command) {
        switch (command.getType().strip()) {
            case "desertStorm" -> {
                if (activeDesertStorm) {
                    return false;
                } else {
                    activeDesertStorm = true;
                    mapWeatherUpdate("DesertAir", command.isDesertStorm());
                    if (!changeFoundFlag) {
                        return false;
                    }
                    changeFoundFlag = false;
                    return true;
                }
            }
            case "rainfall" -> {
                if (activeRainfall) {
                    return false;
                } else {
                    activeRainfall = true;
                    mapWeatherUpdate("TropicalAir", command.getRainfall());
                    if (!changeFoundFlag) {
                        return false;
                    }
                    changeFoundFlag = false;
                    return true;
                }
            }
            case "polarStorm" -> {
                if (activePolarWind) {
                    return false;
                } else {
                    activePolarWind = true;
                    mapWeatherUpdate("PolarAir", command.getWindSpeed());
                    if (!changeFoundFlag) {
                        return false;
                    }
                    changeFoundFlag = false;
                    return true;
                }
            }
            case "peopleHiking" -> {
                if (activeHikers) {
                    return false;
                } else {
                    activeHikers = true;
                    mapWeatherUpdate("MountainAir", command.getNumberOfHikers());
                    if (!changeFoundFlag) {
                        return false;
                    }
                    changeFoundFlag = false;
                    return true;
                }
            }
            case "newSeason" -> {
                if (activeSeason) {
                    return false;
                } else {
                    activeSeason = true;
                    mapWeatherUpdate("TemperateAir", command.getSeason());
                    if (!changeFoundFlag) {
                        return false;
                    }
                    changeFoundFlag = false;
                    return true;
                }
            }
            default ->  {
                throw new IllegalStateException("Unexpected weather value: " + command.getType() + ".");
            }
        }
    }

   public void interactionAirAnimal() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                Air air = cell.getAir();
                Animal animal = cell.getAnimal();
                if (animal == null || !animal.isScanned()) {
                    continue;
                }
                if (air.isAirToxic()) {
                    animal.setHungerState(Animal.HungerState.SICK);
                } else {
                    if (animal.getHungerState() == Animal.HungerState.SICK) {
                        animal.setHungerState(Animal.HungerState.HUNGRY);
                    }
                }
            }
        }
   }

    public void interactionSoilPlant() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                Plant plant = cell.getPlant();
                if (plant == null || !plant.isScanned()) {
                    continue;
                }
                if (!plant.updatePlantAge()) {
                    cell.setPlant(null);
                }
            }
        }
    }

    public void interactionWaterAir(int currentTimestamp){
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                Water water = cell.getWater();
                Air air = cell.getAir();
                if (water == null || !water.isScanned()) {
                    continue;
                }
                if (currentTimestamp % 2 != water.getScannedAt() % 2) {
                    return;
                }
                air.updateHumidity(0.1);
            }
        }
    }

    public void interactionWaterSoil(int currentTimestamp) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                Water water = cell.getWater();
                Soil soil = cell.getSoil();
                if (water == null || !water.isScanned()) {
                    continue;
                }
                if (currentTimestamp % 2 != water.getScannedAt() % 2) {
                    return;
                }
                soil.updateWaterRetention(0.1);
            }
        }
    }

    public void interactionWaterPlant() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                Water water = cell.getWater();
                Plant plant = cell.getPlant();
                if (water == null || !water.isScanned()
                    || plant == null || !plant.isScanned()) {
                    continue;
                }
                if (!plant.updatePlantAge()) {
                    cell.setPlant(null);
                }
            }
        }
    }

    public void interactionPlantAir() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                Plant plant = cell.getPlant();
                Air air = cell.getAir();
                if (plant != null && plant.isScanned()) {
                    double oxygenProduced = plant.getOxygenProduction();
                    air.updateOxygenLevel(oxygenProduced);
                }
            }
        }
    }



    public void interactionAnimalSoil() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                Animal animal = cell.getAnimal();
                Soil soil = cell.getSoil();
                if (animal == null || !animal.isScanned()) {
                    continue;
                }
                if (animal.getHungerState() == Animal.HungerState.WELL_FED) {
                    soil.updateOrganicMatter(animal.getOrganicMatterProduction());
                    animal.setOrganicMatterProduction(0.0);
                    animal.setHungerState(Animal.HungerState.HUNGRY);
                }
            }
        }
    }

    public void interactionAnimalFood(int timestamp) {
        for (Animal animal : scannedAnimals) {
            if (animal.getScannedAt() % 2 != timestamp % 2) {
                return;
            }
            animal.move();
        }
    }
}