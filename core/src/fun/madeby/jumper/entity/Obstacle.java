package fun.madeby.jumper.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

import fun.madeby.jumper.config.GameConfig;
import fun.madeby.util.entity.RectangularBase;

public class Obstacle extends RectangularBase implements Pool.Poolable {
    private float degreeOfAngle;
    private Rectangle jumpSuccessSensor;
    private float sensorDegreeOfAngle;
    private float radius = GameConfig.PLANET_RADIUS - GameConfig.OBSTACLE_SIZE;


    public void update(float delta) {
        if (radius < GameConfig.PLANET_RADIUS) {
            radius+= delta/2;
            float originX = GameConfig.WORLD_CENTER_X;
            float originY = GameConfig.WORLD_CENTER_Y;

            // Obstacle
            float newX = originX + MathUtils.cosDeg(-degreeOfAngle) * radius;
            float newY = originY + MathUtils.sinDeg(-degreeOfAngle) * radius;
            setPosition(newX, newY);

            // Sensor
            float sensorX = originX + MathUtils.cosDeg(-sensorDegreeOfAngle) * radius;
            float sensorY = originY + MathUtils.sinDeg(-sensorDegreeOfAngle) * radius;

            jumpSuccessSensor.set(sensorX, sensorY, getWidthOrRadius(), getHeight());
        }
    }

    public void setAngleToDegree(float angle) {
        degreeOfAngle = angle % 360;
        this.jumpSuccessSensor = new Rectangle();

        sensorDegreeOfAngle = degreeOfAngle + 20f;


    }

    public float getDegreeOfAngle() {
        return degreeOfAngle;
    }

    public float getSensorDegreeOfAngle() {
        return sensorDegreeOfAngle;
    }

    public Rectangle getJumpSuccessSensor() {
        return jumpSuccessSensor;
    }

    @Override
    public void reset() {
        radius = GameConfig.PLANET_RADIUS - GameConfig.OBSTACLE_SIZE;
    }
}
