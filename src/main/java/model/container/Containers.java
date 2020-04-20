package model.container;

import model.Coin;
import model.MapObject;
import model.Pacman;
import model.Point;


public class Containers {

    public static IndexedContainer getPositionContainer(int width, int height){
        return new IndexedContainer(width, height);
    }

    public static ObjectContainer<MapObject> getMapObjectsContainer(){
        return new ObjectContainer<MapObject>();
    }

    public static PointContainer getPointContainer(){
        return new PointContainer();
    }

    public static LimitedObjectContainer<Coin> getCoinContainer() {
        return new LimitedObjectContainer<Coin>(4);
    }

    public static LimitedObjectContainer<Pacman> getPacmanContainer(){
        return new LimitedObjectContainer<Pacman>(2);
    }

    public static GhostContainer getGhostContainer(){
        return new GhostContainer(4);
    }

}
