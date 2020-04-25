package model.container;

import model.mapobject.Point;

/**
 * Specific container for {@link Point}s
 *
 * @author Rémy Decocq (built from original implementation)
 *
 */
public class PointContainer extends ObjectContainer<Point> {

    public void removeAll() {
        for (Point p : this) {
            p.deSpawn();
        }
        super.elmts.clear();
    }
}