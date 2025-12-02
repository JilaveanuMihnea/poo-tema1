package main;

public final class Constants {
    private Constants() {
        // Prevent instantiation
    }

    // TERRABOT CONSTANTS
    public static final int TERRABOT_SCAN_ENERGY = 7;
    public static final int TERRABOT_LEARN_ENERGY_COST = 2;
    public static final int TERRABOT_IMPROVE_ENERGY = 10;

    public static final double IMPROVE_PLANT_VALUE = 0.3;
    public static final double IMPROVE_FERTILIZE_VALUE = 0.3;
    public static final double IMPROVE_HUMIDITY_VALUE = 0.2;
    public static final double IMPROVE_MOISTURE_VALUE = 0.2;


    // SOIL CONSTANTS
    public static final int SOIL_POOR_QUALITY_THRESHOLD = 40;
    public static final int SOIL_MODERATE_QUALITY_THRESHOLD = 70;

    public static final double FORESTSOIL_Q_NITROGEN_MULT = 1.2;
    public static final double FORESTSOIL_Q_ORGANIC_MULT = 2.0;
    public static final double FORESTSOIL_Q_LITTER_MULT = 0.3;
    public static final double FORESTSOIL_Q_RETENTION_MULT = 1.5;
    public static final double FORESTSOIL_B_RETENTION_MULT = 0.6;
    public static final double FORESTSOIL_B_LITTER_MULT = 0.4;
    public static final double FORESTSOIL_B_DIVIDER = 80.0;
    public static final double FORESTSOIL_B_MULTIPLIER = 100.0;

    public static final double DESERTSOIL_Q_NITROGEN_MULT = 0.5;
    public static final double DESERTSOIL_Q_SALINITY_MULT = 2.0;
    public static final double DESERTSOIL_Q_RETENTION_MULT = 0.3;
    public static final double DESERTSOIL_B_BASE = 100.0;
    public static final double DESERTSOIL_B_DIVIDER = 100.0;
    public static final double DESERTSOIL_B_MULTIPLIER = 100.0;

    public static final double TUNDRASOIL_Q_NITROGEN_MULT = 0.7;
    public static final double TUNDRASOIL_Q_ORGANIC_MULT = 0.5;
    public static final double TUNDRASOIL_Q_FROST_MULT = 1.5;
    public static final double TUNDRASOIL_B_BASE = 50.0;
    public static final double TUNDRASOIL_B_DIVIDER = 50.0;
    public static final double TUNDRASOIL_B_MULTIPLIER = 100.0;

    public static final double GRASSLANDSOIL_Q_NITROGEN_MULT = 1.3;
    public static final double GRASSLANDSOIL_Q_ORGANIC_MULT = 1.5;
    public static final double GRASSLANDSOIL_Q_ROOT_MULT = 0.8;
    public static final double GRASSLANDSOIL_B_BASE = 50.0;
    public static final double GRASSLANDSOIL_B_RETENTION_MULT = 0.5;
    public static final double GRASSLANDSOIL_B_DIVIDER = 75.0;
    public static final double GRASSLANDSOIL_B_MULTIPLIER = 100.0;

    public static final double SWAMPSOIL_Q_NITROGEN_MULT = 1.1;
    public static final double SWAMPSOIL_Q_ORGANIC_MULT = 2.2;
    public static final double SWAMPSOIL_Q_WATERLOGGING_MULT = 5.0;
    public static final double SWAMPSOIL_B_WATERLOGGING_MULT = 10.0;

    // WATER CONSTANTS
    public static final double WATER_Q_PURITY_MULT = 0.3;
    public static final double WATER_Q_PH_MULT = 0.2;
    public static final double WATER_Q_SALINITY_MULT = 0.15;
    public static final double WATER_Q_TURBIDITY_MULT = 0.1;
    public static final double WATER_Q_CONTAMINANT_MULT = 0.15;
    public static final double WATER_Q_FROZEN_MULT = 0.2;
    public static final double WATER_Q_PURITY_DIVIDER = 100.0;
    public static final double WATER_Q_PH_FACTOR = 7.5;
    public static final double WATER_Q_SALINITY_DIVIDER = 350.0;
    public static final double WATER_Q_TURBIDITY_DIVIDER = 100.0;
    public static final double WATER_Q_CONTAMINANT_DIVIDER = 100.0;
    public static final double WATER_Q_TOTAL_MULTIPLIER = 100.0;

    // ANIMAL CONSTANTS
    public static final double ANIMAL_ATTACK_BASE = 100.0;
    public static final double ANIMAL_ATTACK_DIVIDER = 10.0;
    public static final double ANIMAL_ORGANIC_PROD_1 = 0.5;
    public static final double ANIMAL_ORGANIC_PROD_2 = 0.8;
    public static final double INTAKE_RATE = 0.08;

    // AIR CONSTANTS
    public static final int AIR_POOR_QUALITY_THRESHOLD = 40;
    public static final int AIR_MODERATE_QUALITY_THRESHOLD = 70;
    public static final double AIR_TOXIC_THRESHOLD_MULT = 0.8;
    public static final double AIR_DMG_BASE = 100.0;

    public static final int DESERTAIR_MAX_SCORE = 65;
    public static final double DESERTAIR_DUSTPARTICLES_MULT = 0.2;
    public static final double DESERTAIR_TEMPERATURE_MULT = 0.3;
    public static final double DESERTAIR_OXYGENLEVEL_MULT = 2.0;
    public static final double DESERTSTORM_MODIFIER = -30.0;

    public static final int TEMPERATEAIR_MAX_SCORE = 84;
    public static final double TEMPERATEAIR_POLLENLEVEL_MULT = 0.1;
    public static final double TEMPERATEAIR_OXYGENLEVEL_MULT = 2.0;
    public static final double TEMPERATEAIR_HUMIDITY_MULT = 0.7;
    public static final double SPRING_MODIFIER = -15.0;

    public static final int MOUNTAINAIR_MAX_SCORE = 78;
    public static final double MOUNTAINAIR_ALTITUDE_DIVIDER = 1000.0;
    public static final double MOUNTAINAIR_OXYGENLEVEL_MULT = 2.0;
    public static final double MOUNTAINAIR_HUMIDITY_MULT = 0.6;
    public static final double MOUNTAINAIR_WEATHER_MODIFIER_MULT = -0.1;

    public static final int TROPICALAIR_MAX_SCORE = 82;
    public static final double TROPICALAIR_CO2LEVEL_MULT = 0.01;
    public static final double TROPICALAIR_OXYGENLEVEL_MULT = 2.0;
    public static final double TROPICALAIR_HUMIDITY_MULT = 0.5;
    public static final double TROPICALAIR_WEATHER_MODIFIER_MULT = 0.3;

    public static final int POLARAIR_MAX_SCORE = 142;
    public static final double POLARAIR_ICECRYSTALCONC_MULT = 0.05;
    public static final double POLARAIR_OXYGENLEVEL_MULT = 2.0;
    public static final double POLARAIR_TEMPERATURE_BASE = 100.0;
    public static final double POLARAIR_WEATHER_MODIFIER_MULT = -0.2;

    // PLANTS CONSTANTS
    public static final double PLANT_TANGLE_DIVIDER = 100.0;
    public static final double PLANT_GROWTH_RATE = 0.2;
    public static final double PLANT_YOUNG_PRODUCTION = 0.2;
    public static final double PLANT_MATURE_PRODUCTION = 0.7;
    public static final double PLANT_OLD_PRODUCTION = 0.4;

    // INTERACTION CONSTANTS
    public static final double WATER_AIR_HUMIDITY = 0.1;
    public static final double WATER_SOIL_RETENTION = 0.1;

    // GENERAL CONSTANTS
    public static final int CLAMP_MAX = 100;
    public static final int CLAMP_MIN = 0;
    public static final double ROUNDING_FACTOR = 100.0;
}
