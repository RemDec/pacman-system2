package model;

import model.container.Containers;
import model.container.LimitedObjectContainer;
import model.container.PointContainer;
import model.container.PositionContainer;
import model.mapobject.*;

public class MapPlacer {

    public static void placeDynamicObjects() {
        Game g = Game.getInstance();
        Map.StartingPosition startingPositions = Map.startingPositions;

        // --------- PACMANS ---------
        LimitedObjectContainer<Pacman> pacC = g.getPacmanContainer();

        pacC.add(new Pacman(startingPositions.PACMAN_MALE, Pacman.Sex.MALE));

        if (Settings.getInstance().getGameMode() == Game.Mode.MULTIPLAYER) {
            pacC.add(new Pacman(startingPositions.PACMAN_FEMALE, Pacman.Sex.FEMALE));
        }

        // --------- GHOSTS ---------
        LimitedObjectContainer<Ghost> gC = g.getGhostContainer();
        gC.add(new Ghost(startingPositions.GHOST_BLUE, Ghost.Colour.BLUE));
        gC.add(new Ghost(startingPositions.GHOST_ORANGE, Ghost.Colour.ORANGE));
        gC.add(new Ghost(startingPositions.GHOST_PINK, Ghost.Colour.PINK));
        gC.add(new Ghost(startingPositions.GHOST_RED, Ghost.Colour.RED));
    }


    public static void placeStaticObjects(PositionContainer positionContainer) {
        // Origin is leftmost upper point
        // --------- WALLS ---------
        int width = positionContainer.getWidth();
        int height = positionContainer.getHeight();

        PositionContainer wallPositions = Containers.getPositionContainer(width, height);
        // Top border
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(0, 0),
                positionContainer.get(19, 0)
        ));
        // Left border
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(0, 1),
                positionContainer.get(0, 9)
        ));
        // Bottom border
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(1, 9),
                positionContainer.get(19, 9)
        ));
        // Right border
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(19, 1),
                positionContainer.get(19, 8)
        ));

        // Left Side
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(2, 2),
                positionContainer.get(2, 5)
        ));
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(3, 2),
                positionContainer.get(5, 2)
        ));
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(5, 3),
                positionContainer.get(5, 5)
        ));
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(3, 5),
                positionContainer.get(4, 5)
        ));
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(2, 7),
                positionContainer.get(5, 7)
        ));

        // Right Side
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(14, 2),
                positionContainer.get(14, 5)
        ));
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(15, 2),
                positionContainer.get(17, 2)
        ));
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(17, 3),
                positionContainer.get(17, 5)
        ));
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(15, 5),
                positionContainer.get(16, 5)
        ));
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(14, 7),
                positionContainer.get(17, 7)
        ));

        // Center Top
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(7, 2),
                positionContainer.get(7, 4)
        ));
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(8, 4),
                positionContainer.get(12, 4)
        ));
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(12, 2),
                positionContainer.get(12, 3)
        ));

        // Center Bottom
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(7, 6),
                positionContainer.get(7, 8)
        ));
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(8, 6),
                positionContainer.get(12, 6)
        ));
        wallPositions.add(positionContainer.getRange(
                positionContainer.get(12, 7),
                positionContainer.get(12, 8)
        ));

        for (Position p : wallPositions) {
            new Wall(p);
        }

        // ------- PLACEHOLDER -------

        PositionContainer placeholderPositions = Containers.getPositionContainer(width, height);

        // LEFT
        placeholderPositions.add(
                positionContainer.getRange(
                        positionContainer.get(3, 3),
                        positionContainer.get(3, 4)
                )
        );

        placeholderPositions.add(
                positionContainer.getRange(
                        positionContainer.get(4, 3),
                        positionContainer.get(4, 4)
                )
        );

        // RIGHT
        placeholderPositions.add(
                positionContainer.getRange(
                        positionContainer.get(15, 3),
                        positionContainer.get(15, 4)
                )
        );

        placeholderPositions.add(
                positionContainer.getRange(
                        positionContainer.get(16, 3),
                        positionContainer.get(16, 4)
                )
        );

        // TOP
        placeholderPositions.add(
                positionContainer.getRange(
                        positionContainer.get(8, 2),
                        positionContainer.get(11, 2)
                )
        );
        placeholderPositions.add(
                positionContainer.getRange(
                        positionContainer.get(8, 3),
                        positionContainer.get(11, 3)
                )
        );

        // BOTTOM
        placeholderPositions.add(
                positionContainer.getRange(
                        positionContainer.get(8, 7),
                        positionContainer.get(11, 7)
                )
        );

        placeholderPositions.add(
                positionContainer.getRange(
                        positionContainer.get(8, 8),
                        positionContainer.get(11, 8)
                )
        );

        for (Position p : placeholderPositions) {
            new Placeholder(p);
        }


    }

    public static void spawnStaticTargets(PositionContainer positionContainer) {
        // Origin is leftmost upper point
        // --------- COINS ---------
        LimitedObjectContainer<Coin> cC = Game.getInstance().getCoinContainer();
        PointContainer pC = Game.getInstance().getPointContainer();

        cC.removeAll();
        pC.removeAll();

        cC.add(new Coin(positionContainer.get(1, 1)));
        cC.add(new Coin(positionContainer.get(1, 8)));
        cC.add(new Coin(positionContainer.get(18, 1)));
        cC.add(new Coin(positionContainer.get(18, 8)));

        // --------- POINTS ---------
        for (Position p : positionContainer) {
            if (p.getOnPosition().size() == 0) {
                pC.add(new Point(p));

            }
        }
    }

    public static void replaceDynamicObjects() {
        Map.StartingPosition startingPositions = Map.startingPositions;
        LimitedObjectContainer<Ghost> gC = Game.getInstance().getGhostContainer();
        for(Ghost g : gC) {
            switch(g.getColour()) {
                case RED: g.move(startingPositions.GHOST_RED);
                    break;
                case PINK: g.move(startingPositions.GHOST_PINK);
                    break;
                case BLUE: g.move(startingPositions.GHOST_BLUE);
                    break;
                case ORANGE: g.move(startingPositions.GHOST_ORANGE);
                    break;
                default:
                    throw new RuntimeException("Unknown ghost color");
            }
        }

        LimitedObjectContainer<Pacman> pC = Game.getInstance().getPacmanContainer();
        for(Pacman p : pC) {
            switch(p.getSex()) {
                case MALE:
                    p.move(startingPositions.PACMAN_MALE);
                    break;
                case FEMALE:
                    p.move(startingPositions.PACMAN_FEMALE);
                    break;
            }

        }
    }

}
