package com.android.live.concurrency.composeobject;

import android.app.Person;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.concurrent.GuardedBy;

/**
 * This example makes no assumptions about the thread-safety of Person, but if it is
 * mutable, additional synchronization will be needed when accessing a Person retrieved
 * from a PersonSet.
 *
 * Confinement makes it easier to build thread-safe class because a class that confines
 * its state can be analyzed from thread safety without having to examine the whole
 * program.
 */

public class PersonSet {

    @GuardedBy("this")
    private final Set<Person> mySet = new HashSet<Person>();

    public synchronized void addPerson(Person p) {
        mySet.add(p);
    }

    public synchronized boolean containsPerson(Person p) {
        return mySet.contains(p);
    }
}
