package main.MapUtility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import fileio.SimulationInput;
import fileio.PairInput;
import fileio.AnimalInput;
import fileio.PlantInput;
import fileio.SoilInput;
import fileio.WaterInput;
import fileio.AirInput;
import main.Constants;
import main.Entities.Soil.Soil;
import main.Entities.Soil.ForestSoil;
import main.Entities.Soil.SwampSoil;
import main.Entities.Soil.DesertSoil;
import main.Entities.Soil.GrasslandSoil;
import main.Entities.Soil.TundraSoil;
import main.Entities.Air.Air;
import main.Entities.Air.DesertAir;
import main.Entities.Air.MountainAir;
import main.Entities.Air.PolarAir;
import main.Entities.Air.TropicalAir;
import main.Entities.Air.TemperateAir;
import main.Entities.Animals.Animal;
import main.Entities.Plants.Plant;
import main.Entities.Water.Water;

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

    /**
     * Gets the cell at the specified row and column.
     *
     * @param row the row index
     * @param col the column index
     * @return the Cell object at the specified position, or null if out of bounds
     */
    public Cell getCell(final int row, final int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return null;
        }
        return grid[row][col];
    }

    /**
     * Builds the map based on the provided simulation input.
     *
     * @param simulationInput the input data for the simulation
     */
    public void mapBuilder(final SimulationInput simulationInput) {
        String dim = simulationInput.getTerritoryDim();
        String[] dimensions = dim.split("x");
        int r = Integer.parseInt(dimensions[0]);
        int c = Integer.parseInt(dimensions[1]);
        grid = new Cell[r][c];
        this.rows = r;
        this.cols = c;
        List<SoilInput> soils = simulationInput.getTerritorySectionParams().getSoil();
        List<PlantInput> plants = simulationInput.getTerritorySectionParams().getPlants();
        List<AnimalInput> animals = simulationInput.getTerritorySectionParams().getAnimals();
        List<WaterInput> waters = simulationInput.getTerritorySectionParams().getWater();
        List<AirInput> airs = simulationInput.getTerritorySectionParams().getAir();

        for (SoilInput soil : soils) {
            for (PairInput pair : soil.getSections()) {
                r = pair.getX();
                c = pair.getY();
                if (grid[r][c] == null) {
                    grid[r][c] = new Cell();
                }
                switch (soil.getType()) {
                    case "ForestSoil" -> grid[r][c].setSoil(new ForestSoil(soil));
                    case "SwampSoil" -> grid[r][c].setSoil(new SwampSoil(soil));
                    case "DesertSoil" -> grid[r][c].setSoil(new DesertSoil(soil));
                    case "GrasslandSoil" -> grid[r][c].setSoil(new GrasslandSoil(soil));
                    case "TundraSoil" -> grid[r][c].setSoil(new TundraSoil(soil));
                    default -> grid[r][c].setSoil(null);
                }
            }
        }

        for (PlantInput plant : plants) {
            for (PairInput pair : plant.getSections()) {
                r = pair.getX();
                c = pair.getY();
                if (grid[r][c] == null) {
                    grid[r][c] = new Cell();
                }
                grid[r][c].setPlant(new Plant(plant));
            }
        }

        for (AnimalInput animal : animals) {
            for (PairInput pair : animal.getSections()) {
                r = pair.getX();
                c = pair.getY();
                if (grid[r][c] == null) {
                    grid[r][c] = new Cell();
                }
                grid[r][c].setAnimal(new Animal(animal, pair, this));
            }
        }

        for (WaterInput water : waters) {
            for (PairInput pair : water.getSections()) {
                r = pair.getX();
                c = pair.getY();
                if (grid[r][c] == null) {
                    grid[r][c] = new Cell();
                }
                grid[r][c].setWater(new Water(water));
            }
        }

        for (AirInput air : airs) {
            for (PairInput pair : air.getSections()) {
                r = pair.getX();
                c = pair.getY();
                if (grid[r][c] == null) {
                    grid[r][c] = new Cell();
                }
                switch (air.getType()) {
                    case "DesertAir" -> grid[r][c].setAir(new DesertAir(air));
                    case "MountainAir" -> grid[r][c].setAir(new MountainAir(air));
                    case "PolarAir" -> grid[r][c].setAir(new PolarAir(air));
                    case "TropicalAir" -> grid[r][c].setAir(new TropicalAir(air));
                    case "TemperateAir" -> grid[r][c].setAir(new TemperateAir(air));
                    default ->  grid[r][c].setAir(null);
                }
            }
        }

    }

    /**
     * Adds a scanned animal to the list of scanned animals.
     *
     * @param animal the Animal object to be added
     */
    public void addScannedAnimal(final Animal animal) {
        scannedAnimals.add(animal);
    }

    /**
     * Prints the current state of the map as a JSON array.
     *
     * @return an ArrayNode representing the map state
     */
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
                cellNode.put("totalNrOfObjects", objectNum);
                mapNodes.add(cellNode);
            }
        }

        return mapNodes;
    }

    private void mapWeatherUpdate(final String type, final Object weatherCondition) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                if (cell.getAir().getType().equalsIgnoreCase(type)) {
                    cell.getAir().updateAirQuality(weatherCondition);
                    changeFoundFlag = true;
                }
            }
        }
    }

    /**
     * Resets the weather condition of the specified type.
     *
     * @param type the type of weather condition to reset
     */
    public void resetWeather(final String type) {
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
            default -> {
                throw new IllegalStateException("Unexpected weather value: " + type + ".");
            }
        }
    }

    /**
     * Changes the weather condition based on the provided command.
     *
     * @param command the CommandInput object containing weather change information
     * @return true if the weather was able to be changed, false otherwise
     */
    public boolean changeWeather(final CommandInput command) {
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
                throw new IllegalStateException("Unexpected weather value: "
                            + command.getType() + ".");
            }
        }
    }

    /**
     * Handles the interaction between soil and air in the map.
     */
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

    /**
     * Handles the interaction between water and air in the map.
     *
     * @param currentTimestamp the current timestamp of the simulation
     */
    public void interactionWaterAir(final int currentTimestamp) {
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
                air.updateHumidity(Constants.WATER_AIR_HUMIDITY);
            }
        }
    }

    /**
     * Handles the interaction between water and soil in the map.
     *
     * @param currentTimestamp the current timestamp of the simulation
     */
    public void interactionWaterSoil(final int currentTimestamp) {
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
                soil.updateWaterRetention(Constants.WATER_SOIL_RETENTION);
            }
        }
    }

    /**
     * Handles the interaction between water and plants in the map.
     */
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

    /**
     * Handles the interaction between water and plants in the map.
     */
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

    /**
     * Handles the interaction between animals and soil in the map.
     */
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

    /**
     * Handles the interaction between animals and food in the map.
     *
     * @param timestamp the current timestamp of the simulation
     */
    public void interactionAnimalFood(final int timestamp) {
        for (Animal animal : scannedAnimals) {
            if (animal.getScannedAt() % 2 != timestamp % 2) {
                return;
            }
            animal.move();
        }
    }
}
