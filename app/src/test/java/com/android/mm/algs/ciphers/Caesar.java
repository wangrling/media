package com.android.mm.algs.ciphers;

/**
 * A Java implementation of Caesar Cipher. /It is a type of substitution cipher
 * in which each letter in the plaintext is replaced by a letter some fixed
 * number of positions down the alphabet. /
 */
public class Caesar {

    public static String encode(String message, int shift) {
        String encoded = "";

        while (shift >= 26) { // 26 = number of latin letters
            shift -= 26;
        }

        final int length = message.length();
        for (int i = 0; i < length; i++) {

            char current = message.charAt(i); // Java law : char + int = char

            if (IsCapitalLatinLetter(current)) {

                current += shift;
                encoded += (char) (current > 'Z' ? current - 26 : current); // 26 = number of latin letters

            } else if (IsSmallLatinLetter(current)) {

                current += shift;
                encoded += (char) (current > 'z' ? current - 26 : current); // 26 = number of latin letters

            } else {
                encoded += current;
            }
        }
        return encoded;
    }

    public static String decode(String encryptedMessage, int shift) {
        String decoded = "";

        while (shift >= 26) { // 26 = number of latin letters
            shift -= 26;
        }

        final int length = encryptedMessage.length();
        for (int i = 0; i < length; i++) {
            char current = encryptedMessage.charAt(i);
            if (IsCapitalLatinLetter(current)) {

                current -= shift;
                decoded += (char) (current < 'A' ? current + 26 : current);// 26 = number of latin letters

            } else if (IsSmallLatinLetter(current)) {

                current -= shift;
                decoded += (char) (current < 'a' ? current + 26 : current);// 26 = number of latin letters

            } else {
                decoded += current;
            }
        }
        return decoded;
    }

    /**
     *
     * @param c
     * @return true if character is capital Latin letter or false for others
     */
    private static boolean IsCapitalLatinLetter(char c) {
        return c >= 'A' && c <= 'Z';
    }

    /**
     *
     * @param c
     * @return true if character is small Latin letter or false for others
     */
    private static boolean IsSmallLatinLetter(char c) {
        return c >= 'a' && c <= 'z';
    }

}
