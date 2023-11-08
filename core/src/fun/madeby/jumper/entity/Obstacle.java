package fun.madeby.jumper.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

import fun.madeby.jumper.config.GameConfig;
import fun.madeby.util.entity.RectangularBase;

public class Obstacle extends RectangularBase implements Pool.Poolable {
    private Rectangle jumpSuccessSensor;
    private float sensorDegreeOfAngle;

    public Obstacle(int height) {
        super(height);
    }

    public Obstacle() {
        super(1);
    }


    public void setAngleToDegree(float angle) {
        super.circumferencePositionInDegrees = angle % 360;
        this.jumpSuccessSensor = new Rectangle();

        sensorDegreeOfAngle = circumferencePositionInDegrees + 20f;

        float radius = GameConfig.PLANET_RADIUS;
        float originX = GameConfig.WORLD_CENTER_X;
        float originY = GameConfig.WORLD_CENTER_Y;

        // Obstacle
        float newX = originX + MathUtils.cosDeg(-circumferencePositionInDegrees) * radius;
        float newY = originY + MathUtils.sinDeg(-circumferencePositionInDegrees) * radius;
        setPosition(newX, newY);

        // Sensor
        float sensorX = originX + MathUtils.cosDeg(-sensorDegreeOfAngle) * radius;
        float sensorY = originY + MathUtils.sinDeg(-sensorDegreeOfAngle) * radius;

        jumpSuccessSensor.set(sensorX, sensorY, getWidthOrRadius(), getHeight());
    }

    public float getCircumferencePositionInDegrees() {
        return super.circumferencePositionInDegrees;
    }

    public float getSensorDegreeOfAngle() {
        return sensorDegreeOfAngle;
    }

    public Rectangle getJumpSuccessSensor() {
        return jumpSuccessSensor;
    }

    @Override
    public void reset() {

    }
}
