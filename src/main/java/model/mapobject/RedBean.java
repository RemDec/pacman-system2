package model.mapobject;

/**
 * If Pacman eats it, it will launch a projectile in its current direction once per second for a certain period of time.
 * A ghost on the path of a projectile will be killed instantaneously
 */
public class RedBean extends StaticTarget{

    @Override
    public void changeState(State state) {

    }

    @Override
    public void gotEaten() {

    }
}