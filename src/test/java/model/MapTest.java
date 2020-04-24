/******************************************************************************
 * This work is applicable to the conditions of the MIT License,              *
 * which can be found in the LICENSE file, or at                              *
 * https://github.com/philippwinter/pacman/blob/master/LICENSE                *
 *                                                                            *
 * Copyright (c) 2013 Philipp Winter, Jonas Heidecke & Niklas Kaddatz         *
 ******************************************************************************/

package model;

import controller.MainController;
import model.Map.Direction;
import model.container.ObjectContainer;
import model.mapobject.Ghost;
import model.mapobject.MapObject;
import model.mapobject.Wall;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * MapTest
 *
 * @author Philipp Winter
 * @author Jonas Heidecke
 * @author Niklas Kaddatz
 */
public class MapTest {

    private Map instance;

    @Before
    public void setUp() {
        MainController.reset();
        this.instance = Map.getInstance();
    }

    @Test
    public void testGetInstance() {
        assertNotNull(Map.getInstance());
    }

    @Test
    public void testReset() {
        Map oldO = Map.getInstance();
        Map.reset();
        Map newO = Map.getInstance();
        assertNotSame(oldO, newO);
    }

    @Test
    public void testGetPositionContainer() {
        assertNotNull(instance.getPositionContainer());
    }

    @Test
    public void testPlaceAllObjects(){
        Map m = Map.getInstance();
        for (Position pos: m.getPositionContainer()){
            assertEquals(0, pos.getOnPosition().size());
        }
        m.placeObjects();
        assertTrue(m.isObjectsPlaced());
        ArrayList<ObjectContainer<? extends MapObject>> conts = Game.getInstance().getAllMapobjecContainers();
        for (ObjectContainer<? extends MapObject> container: conts) {
            for (MapObject mo : container) {
                Position pos = mo.getPosition();
                assertNotNull(pos);
                assertTrue(m.getPositionContainer().contains(pos));
            }
        }
    }

    @Test
    public void testGetPositionByDirectionIfMoveableTo() {
        for(Direction d: Direction.values()) {
            //Still nothing placed, being in the middle ..
            assertNotNull(Map.getPositionByDirectionIfMovableTo(Map.getInstance().getPositionContainer().get(5, 5), d));
        }
        //On border
        assertNull(Map.getPositionByDirectionIfMovableTo(Map.getInstance().getPositionContainer().get(0, 0), Direction.NORTH));
        assertNotNull(Map.getPositionByDirectionIfMovableTo(Map.getInstance().getPositionContainer().get(0, 0), Direction.EAST));
        //Placing a Wall on previous free pos
        new Wall(Map.getInstance().getPositionContainer().get(1,0));
        assertNull(Map.getPositionByDirectionIfMovableTo(Map.getInstance().getPositionContainer().get(0, 0), Direction.EAST));
    }
}
