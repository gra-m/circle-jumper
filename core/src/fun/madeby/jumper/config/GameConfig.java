package fun.madeby.jumper.config;

public class GameConfig {

    public static final float WIDTH = 600f; // pixels
    public static final float HEIGHT = 800f; // pixels


    // The rest are in WorldUnits, including hud which mimics Gen Width and Height in its granularity
    public static final float WORLD_WIDTH = 16f;
    public static final float WORLD_HEIGHT = 24f;
    public static final float HUD_WIDTH = 600f;
    public static final float HUD_HEIGHT = 800f;
    public static final float HUD_SCORE_PADDING = 20f;

    public static final float WORLD_CENTER_X = WORLD_WIDTH/2f;
    public static final float WORLD_CENTER_Y = WORLD_HEIGHT/2f;
    public static final int CELL_SIZE = 1;

    public static final float PLANET_DIAMETER = 9f;
    public static final float PLANET_RADIUS = PLANET_DIAMETER / 2f;

    public static final float MONSTER_SIZE = 1f;
    public static final float MONSTER_HALF_SIZE = MONSTER_SIZE / 2f;
    public static final float MONSTER_START_ANGULAR_SPEED = 45f;
    public static final float START_ANGLE = -90f;

    public static final float MONSTER_MAX_SPEED = 2f;
    public static final float MONSTER_START_ACCELERATION = 4f;

    public static final float OBSTACLE_SIZE = 1f;
    public static final float OBSTACLE_HALF_SIZE = MONSTER_SIZE / 2f;
    public static final float OBSTACLE_SPAWN_TIME =  2.0f;

    public static final float COIN_SIZE = 1f;
    public static final float COIN_HALF_SIZE = COIN_SIZE / 2f;
    public static final float COIN_SPAWN_TIME =  1.25f;


    public static final int ACTUAL_MAX_OBSTACLES = 3;
    public static final int ACTUAL_MAX_COINS = 2;
    public static final float MIN_SEPARATION_OBJECTS = 60;
    public static final float MIN_SEPARATION_COINS = 60;

    public static final int COIN_SCORE = 20;
    public static final int JUMP_SCORE = 10;

    public static final float PAUSE_BEFORE_RESTART = 3;

    private GameConfig(){}
}
