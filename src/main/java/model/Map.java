/******************************************************************************
 * This work is applicable to the conditions of the MIT License,              *
 * which can be found in the LICENSE file, or at                              *
 * https://github.com/philippwinter/pacman/blob/master/LICENSE                *
 *                                                                            *
 * Copyright (c) 2013 Philipp Winter, Jonas Heidecke & Niklas Kaddatz         *
 ******************************************************************************/

package model;

import model.container.*;
import model.mapobject.*;

/**
 * A Map holds all available {@link Position}s in the stage, every {@link Position} references a {@link ObjectContainer<MapObject>}
 * containing all {@link MapObject} placed on it. Semantically, {@link Wall}s are {@link MapObject} where no
 *
 * @author Philipp Winter
 * @author Jonas Heidecke
 * @author Niklas Kaddatz
 */
public class Map {

    public static final PositionContainer positionsToRender = Containers.getPositionContainer(Map.getInstance().width, Map.getInstance().height);

    private static Map instance;

    private PositionContainer positionContainer;

    public final int width;

    public final int height;

    private boolean objectsPlaced = false;

    private static final int DFLT_MAP_WIDTH = 20;
    private static final int DFLT_MAP_HEIGHT = 10;

    public static Map getInstance() {
        if (Map.instance == null) {
            Map.instance = new Map();
        }

        return Map.instance;
    }

    public static void reset() {
        Map.instance = new Map();
    }

    private Map() {
        this(DFLT_MAP_WIDTH, DFLT_MAP_HEIGHT);
    }

    private Map(int width, int height) {
        this.width = width;
        this.height = height;

        this.positionContainer = Containers.getPositionContainer(width, height);

        // Create all position instances for this map
        for (int actX = 0; actX < width; actX++) {
            for (int actY = 0; actY < height; actY++) {
                this.positionContainer.add(new Position(actX, actY));
            }
        }
    }

    /**
     * Place all objects in {@link Game}'s containers and mark them for rendering
     */
    public void placeObjects() {
        MapPlacer.placeDynamicObjects();
        MapPlacer.placeStaticObjects(this.positionContainer);
        MapPlacer.spawnStaticTargets(this.positionContainer);
        this.objectsPlaced = true;
        this.markAllForRendering();
    }

    /**
     * Replace all dynamic objects to restart a new level, mark all as to render
     */
    public void onNextLevel() {
        MapPlacer.replaceDynamicObjects();

        for(Coin c : Game.getInstance().getCoinContainer()){
            if(c.getState() == StaticTarget.State.EATEN) {
                c.changeState(StaticTarget.State.AVAILABLE);
            }
        }
        for(Point p : Game.getInstance().getPointContainer()){
            if(p.getState() == StaticTarget.State.EATEN){
                p.changeState(StaticTarget.State.AVAILABLE);
            }
        }
        this.markAllForRendering();
    }

    public void onPacmanGotEaten() {
        MapPlacer.replaceDynamicObjects();
    }

    public void markAllForRendering() {
        positionsToRender.add(positionContainer);
    }

    public boolean isObjectsPlaced() {
        return objectsPlaced;
    }

    public PositionContainer getPositionContainer() {
        return this.positionContainer;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null) {
            if (o instanceof Map) {
                boolean samePosContainer = this.getPositionContainer().equals(((Map) o).getPositionContainer());
                boolean samePlacedObjects = this.objectsPlaced == ((Map) o).isObjectsPlaced();
                return samePosContainer && samePlacedObjects;
            }
        }
        return false;
    }

    public String toString(){
        String s = "Map " + width + "x" + height + "  " + super.toString() + "\n" + positionContainer;
        return s;
    }

    /**
     * Get the number of free adjacent positions from a given one, meaning whether it is movable to.
     * @param pos The position to look for neighbouring free positions (so max. 4)
     * @return The number of adjacent free positions
     */
    public static int freeNeighbourFields(Position pos) {
        int count = 0;
        for (Direction d : Direction.values()) {
            if (getPositionByDirectionIfMovableTo(pos, d) != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * From an actual previous {@link Position} and a {@link Direction} to move to, return the resulting
     * {@link Position} considered a one-step movement.
     *
     * @param prevPos The starting position
     * @param movingTo The movement direction
     * @return The resulting {@link Position} considering the movement or null if there is no way to move to (wall)
     */
    public static Position getPositionByDirectionIfMovableTo(Position prevPos, Direction movingTo) {
        Position p = null;
        if (prevPos == null) {
            throw new IllegalArgumentException("prevPos cannot be null.");
        }
        try {
            if (movingTo == Direction.NORTH) {
                p = Map.getInstance().getPositionContainer().get(prevPos.getX(), prevPos.getY() - 1);
            } else if (movingTo == Direction.EAST) {
                p = Map.getInstance().getPositionContainer().get(prevPos.getX() + 1, prevPos.getY());
            } else if (movingTo == Direction.WEST) {
                p = Map.getInstance().getPositionContainer().get(prevPos.getX() - 1, prevPos.getY());
            } else if (movingTo == Direction.SOUTH) {
                p = Map.getInstance().getPositionContainer().get(prevPos.getX(), prevPos.getY() + 1);
            }
            if (p != null && p.isMoveableTo()) {
                return p;
            } else {
                return null;
            }
        } catch (IllegalArgumentException ex) {
            // Just return null to signalize, that the point doesn't exist
            return null;
        }
    }

    public Position getStartingPos(Position pos){
        return MapPlacer.getActualStartingPosition(this.positionContainer, pos);
    }


    public enum Direction {

        NORTH, WEST, EAST, SOUTH;

        /**
         * Guess a random Direction to go among available considered current {@link Position} of m0
         * @param mO the Object to guess a {@link Direction} for
         * @return an valid {@link Direction} to move to
         */
        public static Direction guessDirection(MapObject mO) {
            Direction[] directions = Direction.values();
            Position guessedPosition = null;
            Direction guessedDirection = null;

            Helper.shuffle(directions);

            for (Direction direction : directions) {
                guessedPosition = Map.getPositionByDirectionIfMovableTo(mO.getPosition(), direction);
                if (guessedPosition != null) {
                    guessedDirection = direction;
                    break;
                }
            }
            if (guessedPosition == null) {
                throw new RuntimeException("Couldn't find any free position, blocked object :" + mO.toString());
            } else {
                return guessedDirection;
            }
        }
    }

}
