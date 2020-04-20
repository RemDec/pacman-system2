package model.container;

import model.Point;

public class PointContainer extends ObjectContainer<Point> {

    public void removeAll() {
        for (Point p : this) {
            p.deSpawn();
        }
        super.elmts.clear();
    }
}