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
                grid[r][c].setAnimal(new Animal(animal));
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
        switch (command.getType()) {
            case "desertStorm" -> {
                if (activeDesertStorm) {
                    return false;
                } else {
                    activeDesertStorm = true;
                    mapWeatherUpdate("DesertAir", command.isDesertStorm());
                    return true;
                }
            }
            case "rainfall" -> {
                if (activeRainfall) {
                    return false;
                } else {
                    activeRainfall = true;
                    mapWeatherUpdate("TropicalAir", command.getRainfall());
                    return true;
                }
            }
            case "polarStorm" -> {
                if (activePolarWind) {
                    return false;
                } else {
                    activePolarWind = true;
                    mapWeatherUpdate("PolarAir", command.getWindSpeed());
                    return true;
                }
            }
            case "peopleHiking" -> {
                if (activeHikers) {
                    return false;
                } else {
                    activeHikers = true;
                    mapWeatherUpdate("MountainAir", command.getNumberOfHikers());
                    return true;
                }
            }
            case "newSeason" -> {
                if (activeSeason) {
                    return false;
                } else {
                    activeSeason = true;
                    mapWeatherUpdate("TemperateAir", command.getSeason());
                    return true;
                }
            }
            default ->  {
                throw new IllegalStateException("Unexpected weather value: " + command.getType());
            }
        }
    }
}