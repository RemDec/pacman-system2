package model.mapobject;

import model.Game;
import model.Position;
import model.Scorable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * If Pacman eats it, he becomes still for a certain period of time.
 */
public class Fish extends StaticTarget implements Scorable {

    private int stillTime = 3000;

    public Fish(Position pos) {
        this.state = State.AVAILABLE;
        this.setPosition(pos);
    }

    @Override
    public void changeState(State s) {
        if (s == null) {
            throw new IllegalArgumentException("A null state is not allowed.");
        } else if (state == s) {
            throw new IllegalArgumentException("The new state must differ from the old one.");
        }

            if (s == State.EATEN) {
            setVisible(false);
        } else if (s == State.AVAILABLE) {
            setVisible(true);
        }
        this.state = s;
    }

    @Override
    public void gotEaten() {
        this.changeState(State.EATEN);
        for (Pacman p : Game.getInstance().getPacmanContainer()) {
            if (p.position == this.position){
                this.stopPacman(p);
            }
        }
    }

    /**
     * Make the pacman that is on the fish's position still for a period of time
     */
    private void stopPacman(final Pacman pacman){
        pacman.changeState(DynamicTarget.State.WAITING);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                pacman.changeState(DynamicTarget.State.HUNTED);
            }
        }, stillTime);
    }

    @Override
    public int getScore() {
        return 0;
    }
}
