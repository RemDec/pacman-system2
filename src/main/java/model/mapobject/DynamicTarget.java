/******************************************************************************
 * This work is applicable to the conditions of the MIT License,              *
 * which can be found in the LICENSE file, or at                              *
 * https://github.com/philippwinter/pacman/blob/master/LICENSE                *
 *                                                                            *
 * Copyright (c) 2013 Philipp Winter, Jonas Heidecke & Niklas Kaddatz         *
 ******************************************************************************/

package model.mapobject;

import model.Map.Direction;
import model.Position;

/**
 * The parent class of all MapObjects that can move by themselves.
 *
 * @author Philipp Winter
 * @author Jonas Heidecke
 * @author Niklas Kaddatz
 * @see Ghost
 * @see Pacman
 */

public abstract class DynamicTarget extends Target {

    protected State state = State.WAITING;

    /**
     * The direction the object is heading to, e.g. moving to.
     */
    private Direction headingTo = Direction.NORTH;

    /**
     * Move the object to the new position.
     *
     * @param pos The new position of this object.
     */
    public void move(Position pos) {
        boolean wallOnPosition = false;
        boolean placeHolderOnPosition = false;
        for(MapObject m : pos.getOnPosition()){
            if (m instanceof Wall) {
                wallOnPosition = true; // this position allow a dynamic target to be located on
                break;
            } else if(m instanceof Placeholder){
                placeHolderOnPosition = true;
            }
        }
        if(!wallOnPosition && !placeHolderOnPosition){
            this.setPosition(pos);
        } else if (placeHolderOnPosition && isHeadingTo(Direction.NORTH) && this instanceof Ghost){
            this.setPosition(pos); // Ghosts allowed to go on PlaceHolder in order to leave their spawn
        }
    }

    /**
     * Let the object eat a subclass of Target.
     *
     * @param target The object to be eaten.
     */
    protected abstract void eat(Target target);

    /**
     *  This dynamic target got eaten, should implement behaviour from its point of view (changing current State)
     */
    public abstract void gotEaten();

    /**
     * Change the state and perform necessary actions in order to do this.
     *
     * @param state The new state.
     */
    public abstract void changeState(State state);

    /**
     * Return the direction this object is heading to.
     *
     * @return The direction.
     */
    public Direction getHeadingTo() {
        return headingTo;
    }

    /**
     * Check, if this object is heading to <i>direction</i>.
     * Similar to {@code obj.getHeadingTo() == direction}.
     *
     * @param direction The direction to be checked against.
     *
     * @return <code>True</code> if the position is equal, <code>false</code> if not.
     */
    public boolean isHeadingTo(Direction direction) {
        return this.headingTo == direction;
    }

    /**
     * Change the direction this object is heading to.
     *
     * @param direction The new direction.
     */
    public void setHeadingTo(Direction direction) {
        this.headingTo = direction;
    }

    public State getState() {
        return this.state;
    }

    public boolean equals(Object o) {
        if (o != null) {
            if (o instanceof DynamicTarget) {
                boolean sameHeading = this.getHeadingTo().equals(((DynamicTarget) o).getHeadingTo());
                boolean sameState = this.getState().equals(((DynamicTarget) o).getState());
                boolean samePos = this.getPosition().equals(((DynamicTarget) o).getPosition());
                return sameHeading && sameState && samePos;
            }
        }
        return false;
    }

    public enum State {

        /**
         * The object is on the hunt.
         */
        HUNTER,

        /**
         * The object is getting hunted.
         */
        HUNTED,

        /**
         * The object was munched / eaten.
         */
        MUNCHED,

        /**
         * The object is waiting, for example to respawn.
         */
        WAITING

    }
}
