package com.android.mm.algs.ciphers;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA {

    public RSA(int bits) {
        generateKeys(bits);
    }

    private BigInteger modulus, privateKey, publicKey;

    /**
     *
     * @param message
     * @return encrypted message
     */
    public synchronized String encrypt(String message) {
        return (new BigInteger(message.getBytes())).modPow(publicKey, modulus).toString();
    }

    /**
     *
     * @param message
     * @return encrypted message as big integer
     */
    public synchronized BigInteger encrypt(BigInteger message) {
        return message.modPow(publicKey, modulus);
    }

    /**
     *
     * @param encryptedMessage
     * @return plain message
     */
    public synchronized String decrypt(String encryptedMessage) {
        return new String((new BigInteger(encryptedMessage)).modPow(privateKey, modulus).toByteArray());
    }

    /**
     *
     * @param encryptedMessage
     * @return plain message as big integer
     */
    public synchronized BigInteger decrypt(BigInteger encryptedMessage) {
        return encryptedMessage.modPow(privateKey, modulus);
    }

    /**
     * Generate a new public and private key set.
     *
     * @param bits
     */
    public synchronized void generateKeys(int bits) {
        SecureRandom r = new SecureRandom();
        BigInteger p = new BigInteger(bits / 2, 100, r);
        BigInteger q = new BigInteger(bits / 2, 100, r);

        modulus = p.multiply(q);

        BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        publicKey = new BigInteger("3");

        while (m.gcd(publicKey).intValue() > 1) {
            publicKey = publicKey.add(new BigInteger("2"));
        }

        privateKey = publicKey.modInverse(m);
    }
}