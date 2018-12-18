package com.android.mm.algorithms.structures.stacks;

/**
 * This class implements a ArrayStack using two different implementations.
 * ArrayStack is used with a regular array and Stack2 uses an ArrayList.
 *
 * A stack is exactly what it sounds like. An element gets added to the top of
 * the stack and only the element on the top may be removed. This is an example
 * of an array implementation of a ArrayStack. So an element can only be added/removed
 * from the end of the array. In theory stack have no fixed size, but with an
 * array implementation it does.
 */

public class ArrayStack {
    /** The max size of the ArrayStack */
    private int maxSize;
    /** The array representation of the ArrayStack */
    private int[] stackArray;
    /** The top of the stack */
    private int top;

    public ArrayStack(int size) {
        maxSize = size;
        stackArray = new int[maxSize];
        top = -1;
    }

    /**
     * Adds an element to the top of the stack.
     * @param value The element added.
     */
    public void push(int value) {
        if (!isFull()) {    // Checks for a full stack
            top++;
            stackArray[top] = value;
        } else {
            resize(maxSize*2);
            push(value);
        }
    }

    /**
     * Removes the top element of the stack and returns the value you've removed.
     * @return  value popped off the stack.
     */
    public int pop() {
        if (!isEmpty()) {   // Checks for en empty stack
            return stackArray[top--];
        } else{
            System.out.println("The stack is already empty");
            return -1;
        }
    }

    /**
     * Returns the element at the top of the stack.
     * @Return element at the top of the stack.
     */
    public int peek() {
        if (!isEmpty()) {
            return stackArray[top];
        } else{
            System.out.println("The stack is empty, cant peek");
            return -1;
        }
    }

    private void resize(int newSize) {
        //private int[] transferArray = new int[newSize]; we can't put modifires here !
        int[] transferArray = new int[newSize];

        for (int i = 0; i < stackArray.length; i++) {
            transferArray[i] = stackArray[i];
            stackArray = transferArray;
        }
        maxSize = newSize;
    }

    /**
     * @return true if the stack is empty.
     */
    public boolean isEmpty() {
        return top == -1;
    }

    /**
     * @return true if the stack is full.
     */
    public boolean isFull() {
        return top+1 == maxSize;
    }

    /**
     * Deletes everything in the ArrayStack
     *
     * Doesn't delete elements in the array
     * but if you call push method after calling
     * makeEmpty it will overwrite previous
     * values
     */
    public void makeEmpty(){ //Doesn't delete elements in the array but if you call
        top = -1;			 //push method after calling makeEmpty it will overwrite previous values
    }
}
