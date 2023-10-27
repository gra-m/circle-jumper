package fun.madeby.jumper.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import fun.madeby.jumper.common.GameManager;
import fun.madeby.jumper.config.GameConfig;
import fun.madeby.jumper.entity.Coin;
import fun.madeby.jumper.entity.Obstacle;
import fun.madeby.jumper.entity.Planet;
import fun.madeby.jumper.entity.Monster;

public class GameController {
    private static final Logger LOG = new Logger(GameController.class.getName(), Logger.DEBUG);
    private final Array<Coin> coins = new Array<>();
    private final Array<Obstacle> obstacles = new Array<>();
    private final Pool<Obstacle> obstaclePool = Pools.get(Obstacle.class, 14);
    private final Pool<Coin> coinPool = Pools.get(Coin.class, 10);
    private float coinTimer;
    private float obstacleTimer;
    private float waitBetweenGames = GameConfig.PAUSE_BEFORE_RESTART;
    private int testLives = 5;
    private int testLivesLost;


    private Planet planet;

    private Monster monster;
    private float monsterStartX;
    private float monsterStartY;

    public GameController() {
        init();
    }
    private void init() {
        planet = new Planet();
        planet.setPosition(GameConfig.WORLD_CENTER_X,GameConfig.WORLD_CENTER_Y);
        planet.setSize(GameConfig.PLANET_RADIUS);

        monster = new Monster();
        monsterStartX = GameConfig.WORLD_CENTER_X - GameConfig.MONSTER_HALF_SIZE;
        monsterStartY = GameConfig.WORLD_CENTER_Y + GameConfig.PLANET_RADIUS;

        monster.setPosition(monsterStartX,
                monsterStartY);

    }

    public void update(float delta) {
        if (waitBetweenGames > 0) {
            waitBetweenGames -= delta;
            return;
        }

        GameManager.getInstance().updateDisplayScores(delta);

        if (jump()) {
            monster.makeStateJumping();
        }
        monster.updating(delta);
        spawnCoins(delta);
        spawnObstacles(delta);
        collisionDetection();
    }

    private void spawnObstacles(float delta) {
        obstacleTimer += delta;

        if(obstacleMaxReached()) {
            obstacleTimer = 0;
            return;
        }

        if (timeToSpawnObstacle()) {
           obstacleTimer = 0;
           Obstacle obstacle = obstaclePool.obtain();
           obstacle.setAngleToDegree(getRandom(360));
           obstacles.add(obstacle);
        }
    }

    private boolean timeToSpawnObstacle() {
        return obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME;
    }


    private void spawnCoins(float delta) {
        coinTimer += delta;

        if (coinMaxReached()) {
            coinTimer = 0;
            return;
        }

        if (timeToSpawnCoin()) {
            coinTimer = 0;
            Coin coin = coinPool.obtain();
            coin.setAngleToDegree(getRandom(360));
            coins.add(coin);


        }
    }



    private boolean jump() {
        // todo shrink planet and have flying monster? == get rid of monster.currentlyWalking();
        return Gdx.input.isKeyPressed(Input.Keys.SPACE) && monster.currentlyWalking();
    }

    public Planet getPlanet(){
        return planet;
    }

    public Monster getMonster() {
        return monster;
    }

    public Array<Coin> getCoins() {
        return coins;
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }

    public float getWaitBetweenGames() {
        return waitBetweenGames;
    }

    // Janet and John's
    private boolean timeToSpawnCoin() {
        return coinTimer >= GameConfig.COIN_SPAWN_TIME;
    }

    private float getRandom(float betweenZeroAnd) {
        return MathUtils.random(betweenZeroAnd);
    }

    private boolean coinMaxReached() {
        return coins.size >= GameConfig.MAX_COINS;
    }

    private boolean obstacleMaxReached() {
        return obstacles.size >= GameConfig.MAX_OBSTACLES;
    }

    private void collisionDetection() {

        for (int i = 0; i < coins.size; i++) {
            if(collided(coins.get(i).getBoundsThatAreUsedForCollisionDetection()))
                coinScore(i);
        }

        for (int i = 0; i < obstacles.size; i++) {
            if(collided(obstacles.get(i).getBoundsThatAreUsedForCollisionDetection()))
                lifeLost(i);
        }

        for (int i = 0; i < obstacles.size; i++) {
            if(collided(obstacles.get(i).getJumpSuccessSensor()))
                jumpSuccessScore(i);
        }
    }


    private void coinScore(int coinCollected) {
        Coin collected = coins.removeIndex(coinCollected);
        coinPool.free(collected);
        GameManager.getInstance().incrementScore(GameConfig.COIN_SCORE);
    }

    private void lifeLost(int bashedObstacle) {
        Obstacle bashed = obstacles.removeIndex(bashedObstacle);
        obstaclePool.free(bashed);
        decrementLives();
    }

    /** todo
     * Poss implement here or in GameManager but for now calling reset after test-lives are lost
     */
    private void decrementLives() {
        if (testLivesLost++ >= testLives)
            reset();
    }

    private void reset() {
        coinPool.freeAll(coins);
        coinPool.clear();

        obstaclePool.freeAll(obstacles);
        obstacles.clear();

        GameManager.getInstance().reset();

        monster.reset();
        monster.setPosition(monsterStartX, monsterStartY);

        waitBetweenGames = GameConfig.PAUSE_BEFORE_RESTART;

    }

    private void jumpSuccessScore(int jumpedObstacle) {
        Obstacle jumped = obstacles.removeIndex(jumpedObstacle);
        obstaclePool.free(jumped);
        GameManager.getInstance().incrementScore(GameConfig.JUMP_SCORE);
    }

    private boolean collided(Rectangle itemBounds) {
        return Intersector.overlaps(monster.getBoundsThatAreUsedForCollisionDetection(), itemBounds);
    }
}
