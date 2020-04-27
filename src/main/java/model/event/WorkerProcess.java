/******************************************************************************
 * This work is applicable to the conditions of the MIT License,              *
 * which can be found in the LICENSE file, or at                              *
 * https://github.com/philippwinter/pacman/blob/master/LICENSE                *
 *                                                                            *
 * Copyright (c) 2013 Philipp Winter, Jonas Heidecke & Niklas Kaddatz         *
 ******************************************************************************/

package model.event;

import controller.MainController;
import model.*;
import model.Map.Direction;
import model.container.*;
import model.mapobject.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Winter
 * @author Jonas Heidecke
 * @author Niklas Kaddatz
 * @author Rémy Decocq (modifications)
 */
public class WorkerProcess implements Process {

    private PointContainer points;

    private LimitedObjectContainer<Coin> coins;

    private LimitedObjectContainer<Ghost> ghosts;

    private LimitedObjectContainer<Pacman> pacmans;

    private ObjectContainer<DynamicTarget> dynamicTargets;

    private Map map;

    private final int TIME_SEC_FACTOR = 1000;

    private boolean checkCoinSeconds = false;

    @Override
    public long getTiming() {
        //return (long) (TIME_SEC_FACTOR / Game.getInstance().getRefreshRate());
        return 100;
    }

    @Override
    public long getStartupDelay() {
        return 0;
    }

    @Override
    public void onLoad() {
        this.points = Game.getInstance().getPointContainer();
        this.coins = Game.getInstance().getCoinContainer();
        this.ghosts = Game.getInstance().getGhostContainer();
        this.pacmans = Game.getInstance().getPacmanContainer();
        this.dynamicTargets = Game.getInstance().getSpecialDynamicTargetContainer();
        this.map = Game.getInstance().getMap();
    }

    @Override
    public void run() {
        try {
            if (this.check()) {
                this.markDynamicObjectsForRendering();

                this.handleCoins();
                this.performCollisions();
                this.handlePacmans();
                this.handleDynamicTargets();
                this.performCollisions(); // Must be done two times to prevent two objects moving through each other
                this.handleGhosts();

                this.markDynamicObjectsForRendering();
            }
        } catch (Throwable t) {
            MainController.uncaughtExceptionHandler.uncaught(t);
        }
    }

    private void markDynamicObjectsForRendering() {
        for(Pacman p : Game.getInstance().getPacmanContainer()){
            Map.positionsToRender.add(p.getPosition());
        }
        for(Ghost g : Game.getInstance().getGhostContainer()){
            Map.positionsToRender.add(g.getPosition());
        }
    }

    private boolean check() {
        boolean performFurtherActions;

        // Check whether level is completed
        PointContainer pC = Game.getInstance().getPointContainer();
        int pointsEaten = 0;

        for (Point p : pC) {
            if (p.getState() == StaticTarget.State.EATEN) {
                pointsEaten++;
            }
        }

        int size = Game.getInstance().getPointContainer().size();

        performFurtherActions = (pointsEaten != size) && (!Game.getInstance().isGameOver());
        if (pointsEaten == size) {
            // All points were eaten, go next level
            Level.getInstance().nextLevel();
        }

        return performFurtherActions;
    }

    private void handlePacmans() {
        LimitedObjectContainer<Pacman> pacmans = Game.getInstance().getPacmanContainer();

        for (Pacman p : pacmans) {
            this.handlePacman(p);
        }
    }

    /**
     * Handle dynamics targets of the dynamic targets container
     */
    private void handleDynamicTargets() {
        ObjectContainer<DynamicTarget> dynamicTargets = Game.getInstance().getSpecialDynamicTargetContainer();
        List<Fireball> fireballs = new ArrayList<>();

        for (DynamicTarget t : dynamicTargets) {
            if (t instanceof Fireball){
                fireballs.add((Fireball) t);
            }
        }
        // Because destroying a fireball while
        for (Fireball fireball : fireballs) {
            this.handleFireball(fireball);
        }
    }

    /**
     * Handle a fireball, a fireball moves and is destroyed if going in a wall
     * @param f the fireball
     */
    private void handleFireball(Fireball f) {
        ObjectContainer<DynamicTarget> dynamicTargets = Game.getInstance().getSpecialDynamicTargetContainer();

        Position newPosition = Map.getPositionByDirectionIfMovableTo(f.getPosition(), f.getHeadingTo());
        if (newPosition != null) {
            f.tryMoving(newPosition);
        } else {
            //Remove the fireball from the containers
            dynamicTargets.remove(f);
            Map.getInstance().getPositionContainer().get(f.getPosition().getX(), f.getPosition().getY()).remove(f);
        }
    }

    public void handleCoins() {
        double activeSeconds = Coin.getActiveSeconds();

        if (activeSeconds != Coin.PACMAN_AINT_EATER) {
            Coin.reduceActiveSeconds(1 / Game.getInstance().getRefreshRate());
        }

        if (checkCoinSeconds && Coin.getActiveSeconds() == Coin.PACMAN_AINT_EATER) {
            for (Ghost g : Game.getInstance().getGhostContainer()) {
                if (g.getState() == DynamicTarget.State.HUNTED) {
                    g.changeState(DynamicTarget.State.HUNTER);
                }
            }

            for (Pacman p : Game.getInstance().getPacmanContainer()) {
                p.changeState(DynamicTarget.State.HUNTED);
            }

            checkCoinSeconds = false;
        }
    }

    private void handleGhosts() {
        for (Ghost g : this.ghosts) {
            this.handleGhost(g);
        }
    }

    private void handlePacman(Pacman pac) {
        if (pac.getState() != DynamicTarget.State.MUNCHED && pac.getState() != DynamicTarget.State.WAITING) {
            List allowedDirections = pac.getAllowedDirections();
            if (allowedDirections.contains(pac.getHeadingTo())){
                Position newPosition = Map.getPositionByDirectionIfMovableTo(pac.getPosition(), pac.getHeadingTo());
                if (newPosition != null) {
                    pac.tryMoving(newPosition);
                }
            }
        }
        Map.positionsToRender.add(pac.getPosition());
    }

    private void performCollisions() {
        for (Pacman p : pacmans) {
            performCollision(p);
        }
        for (Ghost g : ghosts) {
            performCollision(g);
        }
        for (DynamicTarget dt : dynamicTargets) {
            performCollision(dt);
        }
    }

    private void performCollision(Pacman pac) {
        ObjectContainer<MapObject> mapObjectsOnPos = pac.getPosition().getOnPosition();

        for (MapObject mO : mapObjectsOnPos.getAll()) {
            if (mO instanceof StaticTarget) {
                StaticTarget t = (StaticTarget) mO;
                // An already eaten thing hasn't to be eaten again
                if (t.getState() != StaticTarget.State.EATEN) {
                    if (t instanceof Coin) {
                        checkCoinSeconds = true;
                    }
                    if (pac.canTouch(t)) {
                        pac.eat(t);
                    }
                }
            } else if (mO instanceof Ghost) {
                Ghost g = (Ghost) mO;
                if (g.getState() == DynamicTarget.State.HUNTED) {
                    if (pac.canTouch(g)) {
                        pac.eat(g);
                    }
                } else if ((g.getState() == DynamicTarget.State.HUNTER || g.getState() == DynamicTarget.State.HUNTER_STOP) && pac.getState() != DynamicTarget.State.INVINSIBLE) {
                    if (g.canTouch(pac)) {
                        g.eat(pac);
                    }
                }
            } else if (mO instanceof Boxes) {
                Boxes b = (Boxes) mO;
                b.action(pac);
            }
        }
    }

    private void performCollision(Ghost g) {
        ObjectContainer<MapObject> mapObjectsOnPos = g.getPosition().getOnPosition();

        for (MapObject mO : mapObjectsOnPos.getAll()) {
            if (mO instanceof Boxes) {
                Boxes b = (Boxes) mO;
                b.action(g);
            }
        }
    }

    private void performCollision(DynamicTarget dt) {
        if (dt instanceof Fireball){
            performCollision((Fireball)dt);
        }
    }

    private void performCollision(Fireball f) {
        ObjectContainer<MapObject> mapObjectsOnPos = f.getPosition().getOnPosition();

        for (MapObject mO : mapObjectsOnPos.getAll()) {
            if (mO instanceof Ghost) {
                Ghost g = (Ghost) mO;
                g.changeState(DynamicTarget.State.KILLED);
            }
        }
    }

    private void handleGhost(Ghost g) {
        Position newPosition;
        newPosition = Map.getPositionByDirectionIfMovableTo(g.getPosition(), g.getHeadingTo());

        // If the Ghost stands in front of a wall OR it could take another way (0.5 probability)
        if (newPosition == null || (Map.freeNeighbourFields(g.getPosition()) > 1 && Math.round(Math.random()) == 1)) {
            Direction guessedDirection = Map.Direction.guessDirection(g);
            g.setHeadingTo(guessedDirection);
            newPosition = Map.getPositionByDirectionIfMovableTo(g.getPosition(), guessedDirection);
        }
        if (newPosition == null)
            throw new RuntimeException("A Ghost is blocked :" + g);

        if (g.getState() == DynamicTarget.State.MUNCHED || g.getState() == DynamicTarget.State.KILLED) {
            g.changeState(DynamicTarget.State.WAITING);
            MapPlacer.replaceDynamicObject(g);
        } else if (g.getState() == DynamicTarget.State.WAITING) {
            if (g.getWaitingSeconds() > 0) {
                g.reduceWaitingSeconds(1 / Game.getInstance().getRefreshRate());
            } else if (g.getWaitingSeconds() == 0) {
                g.changeState(DynamicTarget.State.HUNTER);
            }
        } else {
            List allowedDirections = g.getAllowedDirections();
            if (allowedDirections.contains(g.getHeadingTo())) {
                if (newPosition != null) {
                    g.tryMoving(newPosition);
                }
            }
        }
    }
}
