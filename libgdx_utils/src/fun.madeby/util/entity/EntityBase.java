package fun.madeby.util.entity;

import com.badlogic.gdx.math.Rectangle;

/**
 * Common behaviour all will have position, size and bounding box for collision detection
 */
public abstract class EntityBase {
    protected float x;
    protected float y;

    // world units
    protected float width = 1;
    protected float height = 1;

    // any change to size or position of an entity with a texture needs the bounds to follow
    protected Rectangle boundsForCollisionDetection;

    public EntityBase(){
        boundsForCollisionDetection = new Rectangle(x, y, width, height);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateBoundsForCollisionDetection();
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        updateBoundsForCollisionDetection();
    }

    public void updateBoundsForCollisionDetection() {
        boundsForCollisionDetection.set(getX(), getY(), getWidth(), getHeight());
    }

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

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Rectangle getBoundsThatAreUsedForCollisionDetection() {
        return boundsForCollisionDetection;
    }
}
