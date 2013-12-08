/******************************************************************************
 * This work is applicable to the conditions of the MIT License,              *
 * which can be found in the LICENSE file, or at                              *
 * https://github.com/philippwinter/pacman/blob/master/LICENSE                *
 *                                                                            *
 * Copyright (c) 2013 Philipp Winter, Jonas Heidecke & Niklas Kaddatz         *
 ******************************************************************************/

package model;

import model.exception.ObjectAlreadyInListException;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Philipp Winter
 * @author Jonas Heidecke
 * @author Niklas Kaddatz
 */
public class MapObjectContainer implements Container<MapObject> {

    private ArrayList<MapObject> mapObjects;

    public MapObjectContainer() {
        this.mapObjects = new ArrayList<>();
    }

    public MapObject get(int i) {
        return this.mapObjects.get(i);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<MapObject> getAll() {
        return (ArrayList<MapObject>) this.mapObjects.clone();
    }

    public void add(MapObject el) {
        if (!this.mapObjects.contains(el)) {
            this.mapObjects.add(el);
        } else {
            throw new ObjectAlreadyInListException(el.getClass().getCanonicalName());
        }
    }

    public Iterator<MapObject> iterator() {
        return mapObjects.iterator();
    }

}