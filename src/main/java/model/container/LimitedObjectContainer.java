package model.container;

import model.exception.ListFullException;
import model.exception.ObjectAlreadyInListException;

import java.util.Vector;

public class LimitedObjectContainer<E> extends ObjectContainer<E> {

    public final int max;

    public LimitedObjectContainer(int max){
        if (max < 0)
            throw new IllegalArgumentException("A Container cannot have a negative size");
        this.max = max;
        this.elmts = new Vector<E>(max);
    }

    @Override
    public void add(E el) {
        if (!this.elmts.contains(el)) {
            if (this.elmts.size() < this.max) {
                this.elmts.add(el);
            } else {
                throw new ListFullException(elmts.getClass().getCanonicalName());
            }
        } else {
            throw new ObjectAlreadyInListException(elmts.getClass().getCanonicalName());
        }
    }

    public int getMax(){
        return this.max;
    }

}
