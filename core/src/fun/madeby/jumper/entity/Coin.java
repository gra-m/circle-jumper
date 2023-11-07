package fun.madeby.jumper.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;

import fun.madeby.jumper.config.GameConfig;
import fun.madeby.util.entity.RectangularBase;

public class Coin extends RectangularBase implements Pool.Poolable {
    private static final Logger LOG = new Logger(Coin.class.getName(), Logger.DEBUG);
    private boolean spawnBodyHeightAbovePlanet;




    public void setAngleToDegree(float angle) {
        circumferencePositionInDegrees = angle % 360;

        float radius = GameConfig.PLANET_RADIUS;

        if(spawnBodyHeightAbovePlanet) {
            radius+= GameConfig.COIN_SIZE;
        }

        float originX = GameConfig.WORLD_CENTER_X;
        float originY = GameConfig.WORLD_CENTER_Y;

        float newX = originX + (MathUtils.cosDeg(-circumferencePositionInDegrees) * radius);
        float newY = originY + (MathUtils.sinDeg(-circumferencePositionInDegrees) * radius);

        setPosition(newX, newY);
    }

    public float getCircumferencePositionInDegrees() {
        return super.circumferencePositionInDegrees;
    }

    @Override
    public void reset() {
        spawnBodyHeightAbovePlanet = false;

    }

    public void spawnBodyHeightAbovePlanet() {
        // todo have seen that this is called
        LOG.debug("A COIN'S SPAWN RADIUS HAS BEEN ADJUSTED.");
        spawnBodyHeightAbovePlanet = true;
    }

    public void spawnOnPlanet() {
        spawnBodyHeightAbovePlanet = false;
    }
}
