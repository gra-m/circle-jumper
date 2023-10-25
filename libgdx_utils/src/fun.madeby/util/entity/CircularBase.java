package fun.madeby.util.entity;

import com.badlogic.gdx.math.Circle;

public abstract class CircularBase extends EntityBase{

    // any change to size or position of an entity with a texture needs the bounds to follow
    protected Circle boundsForCollisionDetection;

    public CircularBase(){
        boundsForCollisionDetection = new Circle(x, y, widthOrRadius);
    }

    protected void updateBoundsForCollisionDetection() {
        boundsForCollisionDetection.set(getX(), getY(), getWidthOrRadius());
    }


    public Circle getBoundsThatAreUsedForCollisionDetection() {
        return boundsForCollisionDetection;
    }

    public void setSize(float radius) {
        this.widthOrRadius = radius;
        updateBoundsForCollisionDetection();
    }

}
