package com.android.mm;

import android.content.Context;

import com.android.mm.algorithms.structures.Bag;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.android.mm", appContext.getPackageName());
    }

    @Test
    public void testBag() {
        Bag<String> bag = new Bag<>();

        bag.add("1");
        bag.add("1");
        bag.add("2");

        // 可以加入重复值。
        assertEquals(bag.size(), 3);
        assertFalse(bag.contains(null));
        assertTrue(bag.contains("1"));
        assertFalse(bag.contains("3"));
    }
}
