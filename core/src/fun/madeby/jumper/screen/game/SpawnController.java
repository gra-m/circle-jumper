package fun.madeby.jumper.screen.game;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import fun.madeby.jumper.common.GameManager;
import fun.madeby.jumper.config.GameConfig;
import fun.madeby.jumper.entity.Coin;
import fun.madeby.jumper.entity.Obstacle;

public class SpawnController {
    private final Array<Coin> coins = new Array<>();
    private final Array<Obstacle> obstacles = new Array<>();
    private final Pool<Obstacle> obstaclePool = Pools.get(Obstacle.class, 14);
    private final Pool<Coin> coinPool = Pools.get(Coin.class, 10);
    private float obstacleTimer;
    private float coinTimer;


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
