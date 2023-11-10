package fun.madeby.jumper.entity;

import fun.madeby.util.entity.RectangularBase;

public class Spawnable1 extends RectangularBase {
    /**
     * gsp is nothing to do with time, a lower number represents a higher priority for spawning order,
     * that is, more likelihood to be in the way of future different spawnables.
     */
    public int geographicSpawnPriority = 1;
    public boolean isActive;
}
