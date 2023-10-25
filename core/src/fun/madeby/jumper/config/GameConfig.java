package fun.madeby.jumper.config;

public class GameConfig {

    public static final float WIDTH = 480f; // pixels
    public static final float HEIGHT = 800f; // pixels


    // The rest are in WorldUnits
    public static final float WORLD_WIDTH = 16f;
    public static final float WORLD_HEIGHT = 24f;


    public static final float WORLD_CENTER_X = WORLD_WIDTH/2f;
    public static final float WORLD_CENTER_Y = WORLD_HEIGHT/2f;
    public static final int CELL_SIZE = 1;

    public static final float PLANET_DIAMETER = 9f;
    public static final float PLANET_RADIUS = PLANET_DIAMETER / 2f;

    public static final float MONSTER_SIZE = 1f;
    public static final float MONSTER_HALF_SIZE = MONSTER_SIZE / 2f;
    public static final float MONSTER_START_ANGULAR_SPEED = 45f;
    public static final float MONSTER_START_ANGLE = -90f;

    public static final float MONSTER_MAX_SPEED = 2f;
    public static final float MONSTER_START_ACCELERATION = 4f;


    private GameConfig(){}
}
