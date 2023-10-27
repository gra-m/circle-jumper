package fun.madeby.jumper.entity;

import com.badlogic.gdx.math.MathUtils;

import fun.madeby.jumper.config.GameConfig;
import fun.madeby.util.entity.RectangularBase;

public class Monster extends RectangularBase {
    private float angleDegrees = GameConfig.START_ANGLE;
    private float walkingAngularSpeed = GameConfig.MONSTER_START_ANGULAR_SPEED;
    private float speed = 0;
    private float acceleration = GameConfig.MONSTER_START_ACCELERATION;
    private MonsterState monstersState = MonsterState.WALKING;


    /**
     * Speed and angle used to calculate and update position every frame
     */
    public void updating(float delta) {

        adjustForState(delta);



        // degrees traversed in time since last frame:
        angleDegrees += walkingAngularSpeed * delta;
        // limit degrees to those in circle
        angleDegrees = angleDegrees % 360;

        // radius of circle being traversed by monster
        // center of circle being traversed by monster
        float radiusAdjustedWithSpeed = GameConfig.PLANET_RADIUS + speed;
        float originX = GameConfig.WORLD_CENTER_X;
        float originY = GameConfig.WORLD_CENTER_Y;

        // - angleDegrees == clockwise movement + angleDegrees = anti-clockwise
        float newX = originX + MathUtils.cosDeg(-angleDegrees) * radiusAdjustedWithSpeed;
        float newY = originY + MathUtils.sinDeg(-angleDegrees) * radiusAdjustedWithSpeed;

        setPosition(newX, newY);
    }

    public void reset() {
        angleDegrees = GameConfig.START_ANGLE;
    }

    private void adjustForState(float delta) {
        if (monstersState.isJumping()) {
            speed += acceleration * delta;
            if (maxSpeedReached()) {
               this.makeStateFalling();
            }
        }
        else if (monstersState.isFalling()) {
            speed -= acceleration * delta;
            if (speedZeroOrLess()) {
                speed = 0;
                this.makeStateWalking();
            }

        }
    }

    private boolean speedZeroOrLess() {
        return speed <= 0;
    }

    private boolean maxSpeedReached() {
        return speed >= GameConfig.MONSTER_MAX_SPEED;
    }

    public float getAngleDegrees() {
        return angleDegrees;
    }

    public void makeStateWalking() {
        monstersState = MonsterState.WALKING;
    }

    public boolean currentlyWalking() {
        return monstersState.isWalking();
    }

    public void makeStateJumping() {
        monstersState = MonsterState.JUMPING;
    }

    public boolean currentlyJumping() {
        return monstersState.isJumping();
    }

    public void makeStateFalling() {
        monstersState = MonsterState.FALLING;
    }

    public boolean currentlyFalling() {
        return monstersState.isFalling();
    }


}
