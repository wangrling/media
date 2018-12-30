package com.android.mm;

import com.android.mm.algs.structures.Bag;
import com.android.mm.algs.structures.CircularBuffer;
import com.android.mm.algorithms.structures.stacks.ArrayStack;
import com.android.mm.algorithms.structures.stacks.ListStack;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class AlgorithmsTest {



    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }



    @Test
    public void testStack() {
        ArrayStack arrayStack = new ArrayStack(4);
        arrayStack.push(5);
        arrayStack.push(8);
        arrayStack.push(2);
        arrayStack.push(9);

        assertFalse(arrayStack.isEmpty());
        assertTrue(arrayStack.isFull());
        assertEquals(arrayStack.peek(), 9);
        assertEquals(arrayStack.pop(), 9);
        assertEquals(arrayStack.peek(), 2);

        ListStack listStack = new ListStack();
        listStack.push(5);
        listStack.push(8);
        listStack.push(2);
        listStack.push(9);

        assertFalse(listStack.isEmpty());
        assertEquals(listStack.peek(), 9);
        assertEquals(listStack.pop(), 9);
        assertEquals(listStack.peek(), 2);
        assertEquals(listStack.pop(), 2);
    }


}
