package fun.madeby.jumper.entity;

import com.badlogic.gdx.math.MathUtils;

import fun.madeby.jumper.config.GameConfig;
import fun.madeby.util.entity.RectangularBase;

public class Monster extends RectangularBase {
    private float angleDegrees = GameConfig.MONSTER_START_ANGLE;
    private float angleDegreeSpeed = GameConfig.MONSTER_START_ANGULAR_SPEED;

    /**
     * Speed and angle used to calculate and update position every frame
     */
    public void update(float delta) {
        // degrees traversed in time since last frame:
        angleDegrees += angleDegreeSpeed * delta;
        // limit degrees to those in circle
        angleDegrees = angleDegrees % 360;

        // radius of circle being traversed by monster
        // center of circle being traversed by monster
        float originX = GameConfig.WORLD_CENTER_X;
        float originY = GameConfig.WORLD_CENTER_Y;

        // - angleDegrees == clockwise movement + angleDegrees = anti-clockwise
        float newX = originX + MathUtils.cosDeg(-angleDegrees) * GameConfig.PLANET_RADIUS;
        float newY = originY + MathUtils.sinDeg(-angleDegrees) * GameConfig.PLANET_RADIUS;

        setPosition(newX, newY);



    }

}
