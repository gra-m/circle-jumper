package fun.madeby.jumper.screen.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import fun.madeby.jumper.common.GameManager;
import fun.madeby.jumper.config.GameConfig;
import fun.madeby.jumper.entity.Coin;
import fun.madeby.jumper.entity.Monster;
import fun.madeby.jumper.entity.Obstacle;
import fun.madeby.util.GdxUtils;
import fun.madeby.util.entity.RectangularBase;
import fun.madeby.util.utilclasses.BooleanTIntegerVHolder;

public class SpawnController {
    private boolean logging = true;
    private static final Logger LOG = new Logger(SpawnController.class.getName(), Logger.DEBUG);
    private final Monster monster;

    private final Array<Coin> coins = new Array<>();
    private final Array<Obstacle> obstacles = new Array<>();
    private final Pool<Obstacle> obstaclePool = Pools.get(Obstacle.class, 14);
    private final Pool<Coin> coinPool = Pools.get(Coin.class, 10);
    private float obstacleTimer;
    private float coinTimer;

    public SpawnController(Monster monster) {
        this.monster = monster;
    }
    // External Calls

    public void spawnCoins(float delta) {
        coinTimer += delta;

        if (!timeToSpawnCoin()) {
            return;
        }
        addZeroToTwoCoins();
    }

    public void spawnObstacles(float delta) {
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


    // Coin spawning logic
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

    // Obstacle spawning logic
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
                LOG.debug("Broke tryin' to spawn object == 3 attempts");
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


    // Helper Methods
    private boolean timeToSpawnCoin() {
        return coinTimer >= GameConfig.COIN_SPAWN_TIME && coins.size == 0;
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

    private float getRandom(float min, float oneAboveRequiredMax) {
        return MathUtils.random(min, oneAboveRequiredMax);
    }

    private boolean checkAngle(float angle1, float angle2, float tolerance ) {
        float difference = Math.abs(Math.abs(angle1) - Math.abs(angle2));
        return difference > tolerance;
    }

    private boolean trueThatAnObjectClashedWith(float plannedSpawnAngle, float tolerance) {
        Array<RectangularBase> arrayOfObstacles = new Array<RectangularBase>(obstacles);
        return !trueIfArrayOfGameObjectsDoesNotClashWith(plannedSpawnAngle, arrayOfObstacles,
                tolerance );

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


    // reset remove and get
    public void resetCoinSpawning(){
       coinPool.freeAll(coins);
       coins.clear(); // this was coinPool.clear() fixme in master
       coinTimer = 0.0f;
    }

    public void resetObstacleSpawning() {
        obstaclePool.freeAll(obstacles);
        obstacles.clear();
        obstacleTimer = 0.0f;
    }

    public void removeCoin(int index) {
        Coin collected = coins.removeIndex(index);
        coinPool.free(collected);
        GameManager.getInstance().incrementScore(GameConfig.COIN_SCORE);
        coinTimer = 0.0f; //fixme is this necessary here puts back when coins spawn again.
    }

    public void removeObstacle(int index) {
        Obstacle cleared = obstacles.removeIndex(index);
        obstaclePool.free(cleared);
        GameManager.getInstance().incrementScore(GameConfig.JUMP_SCORE);
        obstacleTimer = 0.0f;
    }

    public Array<Coin> getCoins() {
        return coins;
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }

}
