package model.container;

import model.exception.ObjectAlreadyInListException;

import java.util.Iterator;
import java.util.Vector;

public class ObjectContainer<E> implements Container<E> {

    protected Vector<E> elmts;

    public ObjectContainer(){
        this.elmts = new Vector<E>();
    }

    @Override
    public E get(int i) {
        return this.elmts.get(i);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> getAll() {
        return (Vector<E>) this.elmts.clone();
    }

    @Override
    public void add(E el) {
        if (!this.elmts.contains(el)) {
            this.elmts.add(el);
        } else {
            throw new ObjectAlreadyInListException(elmts.getClass().getCanonicalName());
        }
    }

    @Override
    public void add(Container<E> container) {
        for (E elmt : container){
            this.add(elmt);
        }
    }

    @Override
    public void remove(E el) {
        this.elmts.remove(el);
    }

    @Override
    public void removeAll(){
        this.elmts.clear();
    }

    @Override
    public boolean contains(E o) {
        return this.elmts.contains(o);
    }

    @Override
    public int size() {
        return this.elmts.size();
    }

    @Override
    public Iterator<E> iterator() {
        return this.elmts.iterator();
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o != null) {
            if (o instanceof ObjectContainer) {
                return this.getAll().equals(((ObjectContainer<E>) o).getAll());
            }
        }
        return false;
    }
}
