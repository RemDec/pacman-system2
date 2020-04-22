/******************************************************************************
 * This work is applicable to the conditions of the MIT License,              *
 * which can be found in the LICENSE file, or at                              *
 * https://github.com/philippwinter/pacman/blob/master/LICENSE                *
 *                                                                            *
 * Copyright (c) 2013 Philipp Winter, Jonas Heidecke & Niklas Kaddatz         *
 ******************************************************************************/

package model.mapobject;

import model.Position;

/**
 * Placeholder concrete class for a MapObject, linked to a specific {@link Position}.
 *
 * @author Philipp Winter
 */
public class Placeholder extends MapObject {

    public Placeholder(Position pos) {
        this.setPosition(pos);
    }

}
