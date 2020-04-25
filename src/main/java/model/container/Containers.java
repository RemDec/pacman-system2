package model.container;

import model.mapobject.*;


public class Containers {

    public static PositionContainer getPositionContainer(int width, int height){
        return new PositionContainer(width, height);
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

    public static ObjectContainer<MapObject> getSpecialObjectsContainer() {
        return new ObjectContainer<MapObject>();
    }

    public static LimitedObjectContainer<Pacman> getPacmanContainer(){
        return new LimitedObjectContainer<Pacman>(2);
    }

    public static LimitedObjectContainer<Ghost> getGhostContainer(){
        return new LimitedObjectContainer<Ghost>(4);
    }

}
