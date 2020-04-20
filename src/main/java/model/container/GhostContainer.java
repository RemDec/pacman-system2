package model.container;

import model.Ghost;
import model.Position;

import java.util.Vector;

public class GhostContainer extends LimitedObjectContainer<Ghost> {

    public GhostContainer(int max) {
        super(max);
    }

    /**
     * Gets all ghosts on the submitted position.
     *
     * @param pos The position to look for.
     *
     * @return All ghosts on <i>pos</i>.
     */
    public Vector<Ghost> get(Position pos) {
        Vector<Ghost> onPosition = new Vector<>();
        for (Ghost g : super.elmts) {
            if (g.isOnPosition(pos)) {
                onPosition.add(g);
            }
        }
        return onPosition;
    }

}
