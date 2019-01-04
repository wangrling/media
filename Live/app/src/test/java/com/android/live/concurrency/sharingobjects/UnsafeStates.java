package com.android.live.concurrency.sharingobjects;

/**
 * Publishing states in this way is problematic because any caller can modify its contents.
 *
 */

public class UnsafeStates {

    private String[] states = new String[] {
            "AK", "AL"
    };

    public String[] getStates() {
        return states;
    }
}
