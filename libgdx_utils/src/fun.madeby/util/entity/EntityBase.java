package fun.madeby.util.entity;
import com.badlogic.gdx.math.Shape2D;

/**
 * Common behaviour all will have position, size and bounding box for collision detection
 */
public abstract class EntityBase {
    protected float x;
    protected float y;

    // world units
    protected float widthOrRadius = 1;

    public EntityBase(){
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateBoundsForCollisionDetection();
    }

    protected abstract void updateBoundsForCollisionDetection();

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        updateBoundsForCollisionDetection();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        updateBoundsForCollisionDetection();
    }

    public float getWidthOrRadius() {
        return widthOrRadius;
    }



    public abstract Shape2D getBoundsThatAreUsedForCollisionDetection();

}
