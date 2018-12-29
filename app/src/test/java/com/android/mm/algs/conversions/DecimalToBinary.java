package com.android.mm.algs.conversions;

public class DecimalToBinary {

    public static int bitwiseConversion(int n) {
        int b = 0, c = 0, d;

        while (n != 0) {
            d = (n & 1);
            b += d * (int) Math.pow(10, c++);
            n >>= 1;
        }

        return b;
    }

    public static int conventionalConversion(int n) {
        int b = 0, c = 0, d;
        while (n != 0) {
            d = n % 2;
            b = b + d * (int) Math.pow(10, c++);
            n /= 2;
        } //converting decimal to binary

        return b;
    }
}
