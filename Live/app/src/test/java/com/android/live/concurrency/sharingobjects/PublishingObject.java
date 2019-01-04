package com.android.live.concurrency.sharingobjects;

import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;

public class PublishingObject {
    public static Set<SecretKey> knownSecrets;

    public void initialize() {
        knownSecrets = new HashSet<>();
    }
}
