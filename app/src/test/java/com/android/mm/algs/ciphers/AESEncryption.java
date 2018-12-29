package com.android.mm.algs.ciphers;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class AESEncryption {

    /**
     * Gets the AES encryption key.
     *
     * @return  (Secret key that we encrypt using it).
     * @throws NoSuchAlgorithmException
     */
    public static SecretKey getSecretEncryptionKey() throws NoSuchAlgorithmException {
        KeyGenerator aesKeyGenerator = KeyGenerator.getInstance("AES");
        aesKeyGenerator.init(128);  // The AES key size in number of bits.
        SecretKey secKey = aesKeyGenerator.generateKey();
        return secKey;
    }

    /**
     * Encrypts plainText in AES using the secret key
     *
     * @param plainText
     * @param secKey
     * @return byteCipherText (The encrypted text)
     * @throws NoSuchPaddingException
     *             (from Cipher)
     * @throws NoSuchAlgorithmException
     *             (from Cipher)
     * @throws InvalidKeyException
     *             (from Cipher)
     * @throws BadPaddingException
     *             (from Cipher)
     * @throws IllegalBlockSizeException
     *             (from Cipher)
     */
    public static byte[] encryptText(String plainText, SecretKey secKey) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        return byteCipherText;
    }

    /**
     * Decrypts encrypted byte array using the key used for encryption.
     *
     * @param byteCipherText
     * @param secKey
     * @return plainText
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);
    }
}
