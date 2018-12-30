package com.android.mm.algs.structures;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;

/**
 * Collection which does not allow removing elements (only collect and iterate).
 *
 * @param <E>   - the generic type of an element in this bag.
 */

public class Bag<E> implements Iterable<E> {
    private Node<E> firstElement;   // first element of the bag.
    private int size;       // size of bag

    private static class Node<E> {
        private E content;
        private Node<E> nextElement;
    }

    /**
     * @return  an iterator that iterates over the elements in this bag in arbitrary order.
     */
    @NonNull
    @Override
    public Iterator<E> iterator() {
        return new ListIterator<>(firstElement);
    }


    private class ListIterator<E> implements Iterator<E> {

        private Node<E> currentElement;

        public ListIterator(Node<E> firstElement) {
            currentElement = firstElement;
        }

        @Override
        public boolean hasNext() {
            return currentElement != null;
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new NoSuchElementException();

            E element = currentElement.content;
            currentElement = currentElement.nextElement;
            return element;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Create an empty bag.
     */
    public Bag() {
        firstElement = null;
        size = 0;
    }

    /**
     * @return true if this bag is empty, false otherwise.
     */
    public boolean isEmpty() {
        return firstElement == null;
    }

    /**
     * @return  the number of elements.
     */
    public int size() {
        return size;
    }

    /**
     * @param element   - the element to add.
     * firstElement总是后加入的, 3 -> 2 -> 1.
     */
    public void add(E element) {
        Node<E> oldFirst = firstElement;
        firstElement = new Node<>();
        firstElement.content = element;
        firstElement.nextElement = oldFirst;
        size++;
    }

    /**
     * Checks if the bag contains a specific element.
     * @param element   which you want to look for.
     * @return  true if bag contains element, otherwise false.
     */
    public boolean contains(E element) {
        Iterator<E> iterator = this.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(element)) {
                return true;
            }
        }
        return false;
    }
}
