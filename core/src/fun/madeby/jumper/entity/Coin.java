package fun.madeby.jumper.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;

import fun.madeby.jumper.config.GameConfig;
import fun.madeby.util.entity.RectangularBase;

public class Coin extends RectangularBase implements Pool.Poolable {

    @Override
    public void reset() {

    }

    public void setAngleToDegree(float angle) {
        float radius = GameConfig.PLANET_RADIUS;
        float originX = GameConfig.WORLD_CENTER_X;
        float originY = GameConfig.WORLD_CENTER_Y;

        float newX = originX + MathUtils.cosDeg(-angle) * radius;
        float newY = originY + MathUtils.sinDeg(-angle) * radius;

        setPosition(newX, newY);
    }
}
