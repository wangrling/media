package com.android.mm.algorithms;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;

public class AlgorithmsActivity extends ListActivity {

    private static final List<String> algorithms = Arrays.asList(
            "algs/AlgorithmsTest.java",

            "(1)加密部分",
            "AES(Advanced Encryption Standard)加密解密算法，提供两个函数: encrypt(加密), decrypt(解密)。",
            "AES java jdk实现，方便好用，能快速计算出结果。",
            "凯撒(Caesar)加密，采用偏移量字符替换模式。",
            "Columnar Transposition Cipher (分栏式置换加密)，不用理解它的实现原理。",
            "RSA是非对称算法，加解密使用不同的秘钥。",
            "Vigenere(维吉利亚)加密。",

            "(2)数据压缩(Compress)",

            "(3)转换",
            "任意进制转换",
            "二进制转换成十六进制，每隔四位计算十六进制的值。",
            "十进制转换成二进制，conventionalConversion, bitwiseConversion两种方法。",
            "十进制转换成十六进制，Integer.toHexString已经包括该算法。",

            "Bag: collection which does not allow removing elements (only collect and iterate).",
            "Stack: An element gets added to the top of the stack and only the element on the top may be removed.",
            "CircularBuffer: 环形缓冲区，记录读写位置和剩余数，读写两个线程同步工作。"

    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                algorithms));
    }
}
