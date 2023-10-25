package fun.madeby.jumper.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;

import fun.madeby.jumper.config.GameConfig;
import fun.madeby.util.entity.RectangularBase;

public class Coin extends RectangularBase implements Pool.Poolable {
    private float degreeOfAngle;


    public void setAngleToDegree(float angle) {
        degreeOfAngle = angle % 360;

        float radius = GameConfig.PLANET_RADIUS;
        float originX = GameConfig.WORLD_CENTER_X;
        float originY = GameConfig.WORLD_CENTER_Y;

        float newX = originX + MathUtils.cosDeg(-degreeOfAngle) * radius;
        float newY = originY + MathUtils.sinDeg(-degreeOfAngle) * radius;

        setPosition(newX, newY);
    }

    public float getDegreeOfAngle() {
        return degreeOfAngle;
    }

    @Override
    public void reset() {

    }
}
