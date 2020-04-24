package model.mapobject;

import model.Position;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A Trap makes the {@link Target} stuck for a period of time, just on person can be stuck at a time.
 *
 */
public class Trap extends Boxes {

    private int trapTime = 5000;
    private int someoneOn = 0;

    public Trap(Position position) {
        this.setPosition(position);
    }

    public String toString() {
        return "Trap [" + position + "]";
    }

    @Override
    public void action(final Pacman pacman) {
        // Pacman on have 3 values (2,1,0) because performCollisions is done before leaving a case
        // So a True/False would not work properly if the timer was doing pacmanOn = False
        if (someoneOn == 0) {
            someoneOn = 2;
            pacman.changeState(DynamicTarget.State.WAITING);
            System.out.println("a");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    System.out.println("aa");
                    pacman.changeState(DynamicTarget.State.HUNTED);
                    someoneOn -= 1;
                }
            }, trapTime);
        } else if (someoneOn == 1) {
            someoneOn -= 1;
        }
    }

    @Override
    public void action(final Ghost ghost) {
        if (someoneOn == 0) {
            someoneOn = 2;
            if (ghost.state == DynamicTarget.State.HUNTED)
                ghost.changeState(DynamicTarget.State.HUNTED_STOP);
            else if (ghost.state == DynamicTarget.State.HUNTER)
                ghost.changeState(DynamicTarget.State.HUNTER_STOP);
            System.out.println(ghost.state);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    ghost.changeState(DynamicTarget.State.HUNTER);
                    someoneOn -= 1;
                }
            }, trapTime);
        } else if (someoneOn == 1) {
            someoneOn = 0;
        }
    }
}
