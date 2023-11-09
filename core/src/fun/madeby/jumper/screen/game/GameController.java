package fun.madeby.jumper.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;

import fun.madeby.jumper.common.GameManager;
import fun.madeby.jumper.common.GameState;
import fun.madeby.jumper.config.GameConfig;
import fun.madeby.jumper.entity.Coin;
import fun.madeby.jumper.entity.Obstacle;
import fun.madeby.jumper.entity.Planet;
import fun.madeby.jumper.entity.Monster;
import fun.madeby.jumper.screen.menu.OverlayCallback;
import fun.madeby.util.GdxUtils;
import fun.madeby.util.entity.RectangularBase;
import fun.madeby.util.utilclasses.BooleanTIntegerVHolder;

public class GameController {
    private static final Logger LOG = new Logger(GameController.class.getName(), Logger.DEBUG);

    private final SpawnController spawnController;

    private float animationTime;
    private float waitBetweenGames = GameConfig.PAUSE_BEFORE_RESTART;
    private int testLives = 1;
    private int testLivesLost;
    private boolean logging = false;


    private GameState gameState = GameState.MENU;
    private Planet planet;

    private Monster monster;
    private float monsterStartX;
    private float monsterStartY;
    private OverlayCallback callback;


    public GameController() {
        this.spawnController = new SpawnController();
        init();
    }
    private void init() {
        if (logging) {
            LOG.debug("init Called");
        }

        planet = new Planet();
        planet.setPosition(GameConfig.WORLD_CENTER_X,GameConfig.WORLD_CENTER_Y);
        planet.setSize(GameConfig.PLANET_RADIUS);

        monster = new Monster();
        monsterStartX = GameConfig.WORLD_CENTER_X - GameConfig.MONSTER_HALF_SIZE;
        monsterStartY = GameConfig.WORLD_CENTER_Y + GameConfig.PLANET_RADIUS;

        monster.setPosition(monsterStartX,
                monsterStartY);

        callback =  new OverlayCallback() {
            @Override
            public void home() {
                gameState = GameState.MENU;

            }

            @Override
            public void ready() {
                restart();
                gameState = GameState.READY;

            }
        };

    }

    // GameController class provide what is required:
    public void update(float delta) {

        if (gameState.isReady() && waitBetweenGames > 0) {
            waitBetweenGames -= delta;
            if (waitBetweenGames <= 0) {
                gameState = GameState.PLAYING;
            }
        }

        if(!gameState.isPlaying()) {
            return;
        }

        animationTime += delta;

        GameManager.getInstance().updateDisplayScores(delta);

        if (jump()) {
            monster.makeStateJumping();
        }
        monster.updating(delta);
        for (Obstacle ob : obstacles) {
            ob.update(delta);
        }

        for (Coin coin : coins) {
            coin.update(delta);
        }

        spawnCoins(delta);
        spawnObstacles(delta);
        collisionDetection();
    }

    private void spawnObstacles(float delta) {
        obstacleTimer += delta;
        int maxToSpawn;

        if(!okToSpawnObstacle().getT()) {
            return;
        } else {
            maxToSpawn = okToSpawnObstacle().getV();
        }

        if (logging) {
            LOG.debug("okToSpawnObstacle == true");
        }

            addZeroToMaxObstacles(maxToSpawn);
            obstacleTimer = 0;
    }

    private void addZeroToMaxObstacles(int maxSpawnAmount) {
        int obstaclesToSpawn = (int) getRandom(0, maxSpawnAmount + 1);
        Array<Obstacle>  newObstacles = new Array<>(obstaclesToSpawn);
        int attempts = 1;

        if (logging) {
            LOG.debug("addZeroToMaxObstacles trying to spawn " + obstaclesToSpawn +" obstacles");
        }


        // fixme obstacles are being called so quikly afterward the previously spawned is blocking the next. Attempts just stops crash
        // spawns only one.
        for (int i = 1; i <= obstaclesToSpawn;) {
            float plannedSpawnAngle = monster.getCircumferencePositionInDegrees() - i * GameConfig.MIN_SEPARATION_OBJECTS - MathUtils.random(60, 80);
            if (plannedSpawnAngle < 0)
                plannedSpawnAngle = 360 + plannedSpawnAngle;
            if (attempts % 3 == 0) {
                LOG.debug("Broke tryin'");
                break;
            }
            if (logging) {
            LOG.debug("obstacle " + i + " is trying to get spawned @ " + plannedSpawnAngle);
            }
                if (noNonPlayerGameObjectAlreadyAt(plannedSpawnAngle)) {
                    Obstacle obstacle = obstaclePool.obtain();
                    obstacle.setAngleToDegree(plannedSpawnAngle);
                    newObstacles.add(obstacle);
                    i++;
                    attempts = 0;
                } else {
                    attempts++;

                }
        }
        for (Obstacle ob: newObstacles) {
            obstacles.add(ob);
        }

    }


    private BooleanTIntegerVHolder okToSpawnObstacle() {
        boolean ok =  obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME &&
                obstacles.size < GameConfig.ACTUAL_MAX_OBSTACLES;
        BooleanTIntegerVHolder returnValues = new BooleanTIntegerVHolder(ok,
                GameConfig.ACTUAL_MAX_OBSTACLES - obstacles.size);

        if (logging) {
            LOG.debug("timeToSpawnObstacles = " + ok);
        }
        return returnValues;
    }


    private void spawnCoins(float delta) {
        coinTimer += delta;

        if (!timeToSpawnCoin()) {
            return;
        }
        addZeroToTwoCoins();
        }

    private void addZeroToTwoCoins() {
        int coinsToSpawn = (int) getRandom(0, GameConfig.ACTUAL_MAX_COINS + 1);

        for (int i = 0; i <= coinsToSpawn;) {
            float plannedSpawnAngle = MathUtils.random(360);

            if (noPlayerOrExistingCollectableAt(plannedSpawnAngle)) {
                Coin coin = coinPool.obtain();
                coin.setAngleToDegree(plannedSpawnAngle);
                if(trueThatAnObjectClashedWith(plannedSpawnAngle, GameConfig.MIN_SEPARATION_COINS)) {
                    coin.spawnBodyHeightAbovePlanet(); // fixme have seen this called but coin above still failing 234
                }
                coins.add(coin);
                i++;
            }
        }
    }

    private boolean noNonPlayerGameObjectAlreadyAt(float plannedSpawnAngle){
        Array<RectangularBase> arrayOfCoins = new Array<RectangularBase>(coins);

            boolean noObstaclesInWay = GdxUtils.reverseBooleanToCorrectContext(trueThatAnObjectClashedWith(plannedSpawnAngle, GameConfig.MIN_SEPARATION_COINS));
            boolean noCoinsInWay = trueIfArrayOfGameObjectsDoesNotClashWith(plannedSpawnAngle, arrayOfCoins,
                           GameConfig.MIN_SEPARATION_OBJECTS);

        if(logging) {
            LOG.debug("No coins in way = " + noCoinsInWay + " No obstacles in way = " + noObstaclesInWay);
        }

        return (noObstaclesInWay && noCoinsInWay);

    }


    private boolean trueThatAnObjectClashedWith(float plannedSpawnAngle, float tolerance) {
        Array<RectangularBase> arrayOfObstacles = new Array<RectangularBase>(obstacles);
        return !trueIfArrayOfGameObjectsDoesNotClashWith(plannedSpawnAngle, arrayOfObstacles,
                tolerance );

    }

    private boolean noPlayerOrExistingCollectableAt(float plannedSpawnAngle) {
        boolean noPlayerInWay = checkAngle(plannedSpawnAngle,
                monster.getCircumferencePositionInDegrees(),
                GameConfig.MIN_SEPARATION_COINS);

        Array<RectangularBase> arrayOfCoins = new Array<RectangularBase>(coins);
        boolean noCoinInWay = trueIfArrayOfGameObjectsDoesNotClashWith(plannedSpawnAngle, arrayOfCoins,
                GameConfig.MIN_SEPARATION_COINS);


        monster.getCircumferencePositionInDegrees();


        return (noPlayerInWay && noCoinInWay);

    }


    private boolean trueIfArrayOfGameObjectsDoesNotClashWith(float plannedSpawnAngle, Array<RectangularBase> array,
                                                             float tolerance) {
        Boolean nothingInWay = true;
        for (RectangularBase item : array) {
            boolean currentNotInWay;
            currentNotInWay = checkAngle(plannedSpawnAngle,
                    item.getCircumferencePositionInDegrees(),
                    tolerance);

            if (!currentNotInWay) {
              nothingInWay = false;
                break;
            }
        }
        return nothingInWay;
    }

    private boolean checkAngle(float angle1, float angle2, float tolerance ) {
        float difference = Math.abs(Math.abs(angle1) - Math.abs(angle2));
        return difference > tolerance;
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

    public float getAnimationTime(){return animationTime;}

    public float getWaitBetweenGames() {
        return waitBetweenGames;
    }

    // Janet and John's
    private boolean timeToSpawnCoin() {
        return coinTimer >= GameConfig.COIN_SPAWN_TIME && coins.size == 0;
    }

    private float getRandom(float min, float oneAboveRequiredMax) {
        return MathUtils.random(min, oneAboveRequiredMax);
    }

    private void collisionDetection() {

        for (int i = 0; i < spawnController.getCoins().size; i++) {
            if(collided(spawnController.getCoins().get(i).getBoundsThatAreUsedForCollisionDetection()))
                spawnController.removeCoin(i);
        }

        for (int i = 0; i < obstacles.size; i++) {
            if(collided(obstacles.get(i).getBoundsThatAreUsedForCollisionDetection())) {
                lifeLost(i);
                //reset();
            }
        }

        for (int i = 0; i < obstacles.size; i++) {
            if(collided(obstacles.get(i).getJumpSuccessSensor()))
                jumpSuccessScore(i);
        }
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
            gameState = GameState.GAME_OVER;
    }

    public void restart() {
        gameState = GameState.READY;
        animationTime = 0f;

        spawnController.resetCoinSpawning();
        spawnController.resetObstacleSpawning();


        monster.reset();
        monster.setPosition(monsterStartX, monsterStartY);


        GameManager.getInstance().updateHighScore();
        GameManager.getInstance().reset();


        waitBetweenGames = GameConfig.PAUSE_BEFORE_RESTART;

    }

    private void jumpSuccessScore(int jumpedObstacle) {
        if (logging) {
            LOG.debug("jumpSuccessScore");
        }
        Obstacle jumped = obstacles.removeIndex(jumpedObstacle);
        obstaclePool.free(jumped);
        GameManager.getInstance().incrementScore(GameConfig.JUMP_SCORE);
    }

    private boolean collided(Rectangle itemBounds) {
        return Intersector.overlaps(monster.getBoundsThatAreUsedForCollisionDetection(), itemBounds);
    }

    public OverlayCallback getOverlayCallback() {
        return callback;
    }


    public GameState getGameState() {
        return gameState;
    }
}
