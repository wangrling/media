package com.android.mm.algs;

import com.android.mm.algs.ciphers.AESEncryption;
import com.android.mm.algs.ciphers.Caesar;
import com.android.mm.algs.ciphers.ColumnarTranspositionCipher;
import com.android.mm.algs.ciphers.RSA;
import com.android.mm.algs.ciphers.Vigenere;
import com.android.mm.algs.compression.HEncoder;
import com.android.mm.algs.conversions.AnyBaseToAnyBase;
import com.android.mm.algs.conversions.BinaryToHexadecimal;
import com.android.mm.algs.conversions.DecimalToBinary;
import com.android.mm.algs.conversions.DecimalToHexadecimal;

import org.junit.Test;

import java.math.BigInteger;

import javax.crypto.SecretKey;

import static org.junit.Assert.*;

public class AlgorithmsTest {

    @Test
    public void testDecimalToHexadecimal() {
        int dec = 305445566;

        String libraryDecToHex = Integer.toHexString(dec);
        String decToHex = DecimalToHexadecimal.decToHex(dec);

        assertEquals(libraryDecToHex, decToHex);
    }

    @Test
    public void testDecimalToBinary() {
        assertEquals(DecimalToBinary.bitwiseConversion(10), 1010);
        assertEquals(DecimalToBinary.conventionalConversion(10), 1010);
    }

    @Test
    public void testBinaryToHexadecimal() {
        int binary = 1110;
        assertEquals(BinaryToHexadecimal.binToHex(binary), "E");
    }

    @Test
    public void testAnyBaseToAnyBase() {
        String s1 = "12";
        int b1 = 10;
        String s2 = "14";
        int b2 = 8;

        assertTrue(AnyBaseToAnyBase.validForBase(s1, b1));

        assertEquals(AnyBaseToAnyBase.base2base(s1, b1, b2), s2);
    }

    @Test
    public void testCompress() {
        HEncoder h = new HEncoder("aaaabbbcccccccccccdddd");
        System.out.println(h.compress("aabccd"));
        System.out.println(h.decompress("101011000111"));
    }

    @Test
    public void testVigenere() {
        String text = "Hello World";
        String key = "itsakey";

        assertEquals(text, Vigenere.decrypt(Vigenere.encrypt(text, key), key));
    }

    @Test
    public void testRSA() {
        RSA rsa = new RSA(1024);

        String text = "Hello World";

        String cipherText = rsa.encrypt(text);
        assertEquals(text, rsa.decrypt(cipherText));
    }

    @Test
    public void testColumnarTransposition() {
        String keywordForExample = "asd215";
        String wordBeingEncrypted = "This is a test of the Columnar Transposition Cipher";

        System.out.println("Word encrypted ->>> " + ColumnarTranspositionCipher
                .encrpyter(wordBeingEncrypted, keywordForExample));

        assertEquals(wordBeingEncrypted, ColumnarTranspositionCipher.decrypter());
    }


    @Test
    public void testCaesar() {
        String message = "Hello World";
        int shift = 3;

        System.out.println("ENCODED MESSAGE IS \n" +
                Caesar.encode(message, shift)); //send our function to handle

        String decryptionText = Caesar.decode(Caesar.encode(message, shift), shift);
        System.out.println("DECODED MESSAGE IS \n" + decryptionText);

        assertEquals(message, decryptionText);
    }

    @Test
    public void testAESEncryption() throws Exception {
        String plainText = "Hello World";
        SecretKey secKey = AESEncryption.getSecretEncryptionKey();
        byte[] cipherText = AESEncryption.encryptText(plainText, secKey);
        String decryptedText = AESEncryption.decryptText(cipherText, secKey);

        System.out.println("Original text: " + plainText);
        System.out.println("AES key: " + secKey.getEncoded());
        System.out.println("Encrypted text: " + cipherText);
        System.out.println("Descrypted text: " + decryptedText);

        assertEquals(plainText, decryptedText);
    }


    // encrpyt of decrypt
    // 运算量太大，笔记本跑不出来这个算法。
    @Test
    public void testAES() {
        String in;
        // 加密
        // a plaintext block (128-Bit Integer in base 16)
        in = "12";
        BigInteger plainText = new BigInteger(in, 16);
        // a Key (128-Bit Integer in base 16)
        in = "12";
        BigInteger encryptionKey = new BigInteger(in, 16);
        // System.out.println("The encrypted message is: \n" + AES.encrypt(plainText, encryptionKey).toString(16));

        // 解密
        //  ciphertext block (128-Bit Integer in base 16)
        in = "12";
        BigInteger cipherText = new BigInteger(in, 16);
        // a Key (128-Bit Integer in base 16)
        in = "12";
        BigInteger decryptionKey = new BigInteger(in, 16);
        // System.out.println("The deciphered message is:\n" + AES.decrypt(cipherText, decryptionKey).toString(16));

    }
}
