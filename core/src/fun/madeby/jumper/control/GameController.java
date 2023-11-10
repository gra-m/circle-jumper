package fun.madeby.jumper.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;

import fun.madeby.jumper.common.GameManager;
import fun.madeby.jumper.common.GameState;
import fun.madeby.jumper.config.GameConfig;
import fun.madeby.jumper.entity.Coin;
import fun.madeby.jumper.entity.Monster;
import fun.madeby.jumper.entity.Obstacle;
import fun.madeby.jumper.entity.Planet;
import fun.madeby.jumper.screen.menu.OverlayCallback;

public class GameController {
    private static final Logger LOG = new Logger(GameController.class.getName(), Logger.DEBUG);
    private final boolean logging = true;
    private final int testLives = 1;
    private SpawnController spawnController;
    private float animationTime;
    private float waitBetweenGames = GameConfig.PAUSE_BEFORE_RESTART;
    private int testLivesLost;

    private GameState gameState = GameState.MENU;
    private Planet planet;
    private Monster monster;
    private float monsterStartX;
    private float monsterStartY;
    private OverlayCallback callback;


    public GameController()
    {
        init();
    }

    private void init()
    {
        if (logging)
        {
            LOG.debug("init Called");
        }

        planet = new Planet();
        planet.setPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);
        planet.setSize(GameConfig.PLANET_RADIUS);

        monster = new Monster();
        monsterStartX = GameConfig.WORLD_CENTER_X - GameConfig.MONSTER_HALF_SIZE;
        monsterStartY = GameConfig.WORLD_CENTER_Y + GameConfig.PLANET_RADIUS;

        monster.setPosition(monsterStartX,
                monsterStartY);

        initialiseSpawning();

        callback = new OverlayCallback() {
            @Override
            public void home()
            {
                gameState = GameState.MENU;

            }

            @Override
            public void ready()
            {
                restart();
                gameState = GameState.READY;
            }
        };
    }

    private void initialiseSpawning()
    {
        this.spawnController = new SpawnController(monster);
    }

    // GameController class provide what is required:
    public void update(float delta)
    {

        if (gameState.isReady() && waitBetweenGames > 0)
        {
            waitBetweenGames -= delta;
            if (waitBetweenGames <= 0)
            {
                gameState = GameState.PLAYING;
            }
        }

        if (!gameState.isPlaying())
        {
            return;
        }

        animationTime += delta;

        GameManager.getInstance().updateDisplayScores(delta);

        if (jump())
        {
            monster.makeStateJumping();
        }
        monster.updating(delta);
        for (Obstacle ob : spawnController.getObstacles())
        {
            ob.update(delta);
        }

        for (Coin coin : spawnController.getCoins())
        {
            coin.update(delta);
        }

        spawnController.spawnCoins(delta);
        spawnController.spawnObstacles(delta);
        collisionDetection();
    }

    private boolean jump()
    {
        return Gdx.input.isKeyPressed(Input.Keys.SPACE) && monster.currentlyWalking();
    }

    public Planet getPlanet()
    {
        return planet;
    }

    public Monster getMonster()
    {
        return monster;
    }


    public float getAnimationTime()
    {
        return animationTime;
    }

    public float getWaitBetweenGames()
    {
        return waitBetweenGames;
    }


    private void collisionDetection()
    {

        for (int i = 0; i < spawnController.getCoins().size; i++)
        {
            if (collided(spawnController.getCoins().get(i).getBoundsThatAreUsedForCollisionDetection()))
                spawnController.removeCoin(i);
        }

        for (int i = 0; i < spawnController.getObstacles().size; i++)
        {
            if (collided(spawnController.getObstacles().get(i).getBoundsThatAreUsedForCollisionDetection()))
            {
                lifeLost(i);
                //reset();
            }
        }

        for (int i = 0; i < spawnController.getObstacles().size; i++)
        {
            if (collided(spawnController.getObstacles().get(i).getJumpSuccessSensor()))
                jumpSuccessScore(i);
        }
    }

    private void lifeLost(int bashedObstacle)
    {
        spawnController.removeObstacle(bashedObstacle);
        decrementLives();
    }

    private void decrementLives()
    {
        if (testLivesLost++ >= testLives)
            gameState = GameState.GAME_OVER;
    }

    public void restart()
    {
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

    private void jumpSuccessScore(int jumpedObstacle)
    {
        if (logging)
        {
            LOG.debug("jumpSuccessScore");
        }
        spawnController.removeObstacle(jumpedObstacle);
    }

    private boolean collided(Rectangle itemBounds)
    {
        return Intersector.overlaps(monster.getBoundsThatAreUsedForCollisionDetection(), itemBounds);
    }

    public OverlayCallback getOverlayCallback()
    {
        return callback;
    }


    public GameState getGameState()
    {
        return gameState;
    }

    public Array<Obstacle> getObstacles()
    {
        return spawnController.getObstacles();
    }

    public Array<Coin> getCoins()
    {
        return spawnController.getCoins();
    }
}
