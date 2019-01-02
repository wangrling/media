package com.google.android.libgdx.utils;

import java.util.Iterator;

import androidx.annotation.NonNull;

public class ObjectIntMap<K> implements Iterable<ObjectIntMap.Entry<K>> {
    @NonNull
    @Override
    public Iterator<ObjectIntMap.Entry<K>> iterator() {
        return null;
    }

    public int get(K key, int defaultValue) {

        return -1;
    }

    public void put(K key, int value) {

    }

    public static class Entry<K> {
        public K key;
        public int value;

        public String toString() {
            return key + "=" + value;
        }
    }
}
