/******************************************************************************
 * This work is applicable to the conditions of the MIT License,              *
 * which can be found in the LICENSE file, or at                              *
 * https://github.com/philippwinter/pacman/blob/master/LICENSE                *
 *                                                                            *
 * Copyright (c) 2013 Philipp Winter, Jonas Heidecke & Niklas Kaddatz         *
 ******************************************************************************/

package model;

import model.mapobject.DynamicTarget;
import model.mapobject.Ghost;
import model.mapobject.Pacman;

/**
 * A Level coupled with a Game instance determines the number of the current level and the difficulty parameters
 * associated with. As the level increases, difficulty does it as well through a modification of these parameters.
 * The transition between two levels and the evolution of parameters is handled here.
 *
 * @author Philipp Winter
 * @author Jonas Heidecke
 * @author Niklas Kaddatz
 */
public class Level {

    private static Level instance;

    /**
     * The current level (correlated with difficulty)
     */
    private int level = 1;

    private double secondsPerCoin = 7;

    private static final double TIME_PER_COIN_FACTOR = 0.85;

    private static final double PROBA_NEW_LIFE = 0.3;

    private Level() {

    }

    public static Level getInstance() {
        if (Level.instance == null) {
            Level.instance = new Level();
        }

        return Level.instance;
    }

    public void nextLevel() {
        // Reduce the amount of time the user has to munch a ghost once Coin eaten
        this.secondsPerCoin *= TIME_PER_COIN_FACTOR;

        this.level++;

        // Pacman has a chance to get a new life
        if(Math.random() <= PROBA_NEW_LIFE) {
            Game.getInstance().increasePlayerLifes();
        }

        // Change the refresh rate = How fast is the pacman moving
        Game.getInstance().changeRefreshRate(this);

        for (Ghost g : Game.getInstance().getGhostContainer()) {
            g.changeState(DynamicTarget.State.HUNTER);
        }

        for (Pacman p : Game.getInstance().getPacmanContainer()) {
            p.changeState(DynamicTarget.State.HUNTED);
        }

        Map.getInstance().onNextLevel();
        Game.getInstance().getEventHandlerManager().restartExecution();
    }

    public boolean equals(Object o) {
        if (o != null) {
            if (o instanceof Level) {
                return this.getLevel() == ((Level) o).getLevel()
                        && this.getSecondsPerCoin() == ((Level) o).getSecondsPerCoin();
            }
        }
        return false;
    }

    public static void reset() {
        Level.instance = new Level();
    }

    public int getLevel() {
        return this.level;
    }

    public double getSecondsPerCoin() {
        return secondsPerCoin;
    }

}
