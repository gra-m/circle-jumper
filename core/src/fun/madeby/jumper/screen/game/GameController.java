package fun.madeby.jumper.screen.game;

import com.badlogic.gdx.utils.Logger;

import fun.madeby.jumper.config.GameConfig;
import fun.madeby.jumper.entity.Planet;
import fun.madeby.jumper.entity.Monster;

public class GameController {
    private static final Logger LOG = new Logger(GameController.class.getName(), Logger.DEBUG);

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

    }

    public Planet getPlanet(){
        return planet;
    }

    public Monster getMonster() {
        return monster;
    }
}
