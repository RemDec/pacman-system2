/******************************************************************************
 * This work is applicable to the conditions of the MIT License,              *
 * which can be found in the LICENSE file, or at                              *
 * https://github.com/philippwinter/pacman/blob/master/LICENSE                *
 *                                                                            *
 * Copyright (c) 2013 Philipp Winter, Jonas Heidecke & Niklas Kaddatz         *
 ******************************************************************************/

package model;

import model.exception.ListFullException;
import model.exception.ObjectAlreadyInListException;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Philipp Winter
 * @author Jonas Heidecke
 * @author Niklas Kaddatz
 */
public class PacmanContainer implements Container<Pacman> {

    /**
     * All pacman instances, for example Pacman and Mrs. Pacman
     */
    private ArrayList<Pacman> pacmans;

    public final int max = 2;

    public PacmanContainer() {
        this.pacmans = new ArrayList<>(max);
    }

    public void add(Pacman pacman) {
        if (!this.pacmans.contains(pacman)) {
            if (this.pacmans.size() < this.max) {
                this.pacmans.add(pacman);
            } else {
                throw new ListFullException(pacman.getClass().getCanonicalName());
            }
        } else {
            throw new ObjectAlreadyInListException(pacman.getClass().getCanonicalName());
        }
    }

    public Pacman get(int i) {
        return this.pacmans.get(i);
    }

    public ArrayList<Pacman> get(Position pos) {
        ArrayList<Pacman> pacmansOnPosition = new ArrayList<>(2);

        for (Pacman p : this.pacmans) {
            if (p.isOnPosition(pos)) {
                pacmansOnPosition.add(p);
            }
        }

        return pacmansOnPosition;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Pacman> getAll() {
        return (ArrayList<Pacman>) this.pacmans.clone();
    }

    public Iterator<Pacman> iterator() {
        return pacmans.iterator();
    }

}