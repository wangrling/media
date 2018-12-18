package com.android.mm.algorithms.structures.stacks;

import java.util.ArrayList;

/**
 * This is an ArrayList Implementation of stack, Where size is not
 * a problem we can extend the stack as much as we want.
 */
public class ListStack {
    ArrayList<Integer> stackList;

    public ListStack() {
        stackList = new ArrayList<>();
    }

    /**
     * Adds value to the end of list which
     * is the top for stack
     *
     * @param value value to be added
     */
    public void push(int value) {
        stackList.add(value);
    }

    public int pop() {
        if (!isEmpty()) {
            int popValue = stackList.get(stackList.size() - 1);
            stackList.remove(stackList.size() - 1);
            return popValue;
        } else {
            System.out.print("The stack is already empty  ");
            return -1;
        }
    }

    public boolean isEmpty() {
        if (stackList.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public int peek() {
        return stackList.get(stackList.size() - 1);
    }
}
