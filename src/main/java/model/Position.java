/******************************************************************************
 * This work is applicable to the conditions of the MIT License,              *
 * which can be found in the LICENSE file, or at                              *
 * https://github.com/philippwinter/pacman/blob/master/LICENSE                *
 *                                                                            *
 * Copyright (c) 2013 Philipp Winter, Jonas Heidecke & Niklas Kaddatz         *
 ******************************************************************************/

package model;

/**
 * The position class represents a point on the map (x, y). IT SHOULD NOT BE CONSTRUCTED OUTSIDE THE {@link Map} CLASS.
 *
 * @author Philipp Winter
 * @author Jonas Heidecke
 * @author Niklas Kaddatz
 */
public class Position {

    private final int x;

    private final int y;

    private MapObjectContainer onPosition;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.onPosition = new MapObjectContainer();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Get MapObjects located at this position
     * @return Container of {@link MapObject}s located at this position
     */
    public MapObjectContainer getOnPosition() {
        return onPosition;
    }

    /**
     * Add a {@link MapObject} as located at this position
     * @param mapObject the object to add
     */
    public void add(MapObject mapObject) {
        assert mapObject.getPosition() != null;
        this.onPosition.add(mapObject);
    }

    /**
     * Remove an object from this position by reference
     * @param mapObject the object to remove from the container associated with this position
     */
    public void remove(MapObject mapObject) {
        this.onPosition.remove(mapObject);
    }

    /**
     * Test whether there is a {@link Wall} among {@link MapObject}s associated with this position
     * @return true if no {@link Wall} at this position
     */
    public boolean isMoveableTo() {
        for (MapObject mO : this.onPosition) {
            if (mO instanceof Wall) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof Position) {
                Position p = (Position) obj;
                return p.getX() == this.getX()
                        && p.getY() == this.getY();
            }
        }

        return false;
    }

    public String toString() {
        return x + "|" + y;
    }

}
