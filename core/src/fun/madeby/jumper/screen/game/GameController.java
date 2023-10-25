package fun.madeby.jumper.screen.game;

import com.badlogic.gdx.utils.Logger;

import fun.madeby.jumper.config.GameConfig;
import fun.madeby.jumper.entity.Planet;

public class GameController {
    private static final Logger LOG = new Logger(GameController.class.getName(), Logger.DEBUG);

    private Planet planet;

    public GameController() {
        init();
    }
    private void init() {
        planet = new Planet();
        planet.setPosition(GameConfig.WORLD_CENTER_X,GameConfig.WORLD_CENTER_Y);

    }

    public void update(float delta) {

    }

    public Planet getPlanet(){
        return planet;
    }
}
