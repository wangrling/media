package com.android.live.concurrency.sharingobjects;

import java.util.HashSet;
import java.util.Set;

/**
 * Immutable objects are always thread-safe.
 *
 * An object is immutable if:
 * 1) Its state cannot be modified after construction.
 * 2) Al it fields are fina,; and it is properly constructed (the this reference
 * doesn't escape during construction).
 *
 * Just as it is a good practice to make all fields private unless they need greater
 * visibility, it is a good practice to make all fields final unless they need to
 * be mutable.
 */

public class ThreeStooges {
    private final Set<String> stooges = new HashSet<String>();

    public ThreeStooges() {
        stooges.add("Moe");
        stooges.add("Larry");
        stooges.add("Curly");
    }

    public boolean isStooge(String name) {
        return stooges.contains(name);
    }
}
