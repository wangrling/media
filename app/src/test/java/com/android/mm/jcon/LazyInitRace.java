package com.android.mm.jcon;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class LazyInitRace {
    private ExpensiveObject instance = null;

    private class ExpensiveObject {
    }

    public ExpensiveObject getInstance() {
        if (instance == null) {
            instance = new ExpensiveObject();
        }

        return instance;
    }
}
