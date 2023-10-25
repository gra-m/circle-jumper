package fun.madeby.jumper.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import fun.madeby.jumper.config.GameConfig;
import fun.madeby.jumper.entity.Coin;
import fun.madeby.jumper.entity.Planet;
import fun.madeby.jumper.entity.Monster;

public class GameController {
    private static final Logger LOG = new Logger(GameController.class.getName(), Logger.DEBUG);
    private final Array<Coin> coins = new Array<>();
    private final Pool<Coin> coinPool = Pools.get(Coin.class, 10);
    private float coinTimer;

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
        if (jump()) {
            monster.makeStateJumping();
        }
        monster.updating(delta);
        spawnCoins(delta);


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
}
