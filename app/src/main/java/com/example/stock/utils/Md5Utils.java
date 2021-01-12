package com.example.stock.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {

    /**
     * MD5机密，带有分隔符的加密
     *
     * @param original 要加密的内容
     * @return 加密后的内容
     */
    public static String toMd5(String original) {

        if (TextUtils.isEmpty(original)) return null;
        else {
            try {
                byte[] bytes = original.getBytes();
                MessageDigest algorithm = MessageDigest.getInstance("MD5");
                algorithm.reset();
                algorithm.update(bytes);
                StringBuilder strBuilder = new StringBuilder();
                for (byte b : algorithm.digest()) {
                    strBuilder.append(String.format("%02x", 0xFF & b));
                }
                return strBuilder.toString();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //加密、解密MD5（一次调用加密，再次调用处理加密后的内容是解密）
    public static String convertMD5(String inStr) {

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        return new String(a);
    }

    /**
     * 计算文件的MD5值
     *
     * @param file 要计算的文件
     * @return 返回计算的文件MD5结果
     */
    static public String calcMD5(File file) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream input = new FileInputStream(file);
            byte[] buf = new byte[1024];
            while (input.available() > 0) {
                int res = input.read(buf, 0, buf.length);
                md.update(buf, 0, res);
            }
            input.close();
            byte[] md5 = md.digest();
            return bytesToHexString(md5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "" + file.length();
    }

    /**
     * Convert an array of bytes to a string of hexadecimal numbers
     */
    private static String bytesToHexString(byte[] array) {

        StringBuilder res = new StringBuilder();
        for (byte anArray : array) {
            int val = anArray + 256;
            String b = "00" + Integer.toHexString(val);
            int len = b.length();
            String sub = b.substring(len - 2);
            res.append(sub);
        }
        return res.toString();
    }
}
