package model.mapobject;

import model.Position;

public class Bridge extends MapObject {

    public Bridge(Position position) {
        this.setPosition(position);
    }

    public String toString() {
        return "Bridge [" + position + "]";
    }
}
