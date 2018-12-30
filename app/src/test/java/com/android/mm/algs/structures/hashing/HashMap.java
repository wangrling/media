package com.android.mm.algs.structures.hashing;

public class HashMap {

    private int hSize;
    private LinkedList[] buckets;

    public HashMap(int hSize) {
        buckets = new LinkedList[hSize];
        for (int i = 0; i < hSize; i++) {
            buckets[i] =  new LinkedList();
            // Java requires explicit initialisaton of each object
        }
        this.hSize = hSize;
    }

    // 通过求余计算Hash值，定位到哪个数组。
    public int hashing(int key) {
        int hash = key % hSize;
        if (hash <= 0) {
            hash += hSize;
        }
        return hash;
    }

    public void insertHash(int key) {
        int hash = hashing(key);
        buckets[hash].insert(key);
    }

    // 删除表中的某个值。
    public void deleteHash(int key) {
        int hash = hashing(key);

        buckets[hash].delete(key);
    }

    public void displayHashTable() {
        for (int i = 0;i < hSize ; i++) {
            System.out.printf("Bucket %d :",i);
            buckets[i].display();
        }
    }
}
