package fun.madeby.util.entity;

import com.badlogic.gdx.math.Rectangle;

public abstract class RectangularBase extends EntityBase{

    private static final  float REQUIRED_TO_SET_MONSTER_TRIGONOMETRY = -90f;
    protected float circumferencePositionInDegrees = REQUIRED_TO_SET_MONSTER_TRIGONOMETRY;
    protected float height = 1;

    // any change to size or position of an entity with a texture needs the bounds to follow
    protected Rectangle boundsForCollisionDetection;

    public RectangularBase(){
        boundsForCollisionDetection = new Rectangle(x, y, widthOrRadius, height);
    }

    public float getCircumferencePositionInDegrees() {
        return circumferencePositionInDegrees;
    }

    protected void updateBoundsForCollisionDetection() {
        boundsForCollisionDetection.set(getX(), getY(), getWidthOrRadius(), getHeight());
    }

    public float getHeight() {
        return height;
    }


    public Rectangle getBoundsThatAreUsedForCollisionDetection() {
        return boundsForCollisionDetection;
    }

    public void setSize(float width, float height) {
        this.widthOrRadius = width;
        this.height = height;
        updateBoundsForCollisionDetection();
    }
}
