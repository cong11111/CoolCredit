package com.tiny.cash.loan.card.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author rainking
 */
public class MD5Util {

    /**
     * 16进制字符
     */
    static String[] chars = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};

    /**
     * RandomAccessFile 获取文件的MD5值
     *
     * @param file 文件路径
     * @return md5
     */
    public static String getFileMd5(File file) {
        MessageDigest messageDigest;
        RandomAccessFile randomAccessFile = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            if (file == null) {
                return "";
            }
            if (!file.exists()) {
                return "";
            }
            randomAccessFile = new RandomAccessFile(file, "r");
            byte[] bytes = new byte[1024 * 1024 * 10];
            int len = 0;
            while ((len = randomAccessFile.read(bytes)) != -1) {
                messageDigest.update(bytes, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, messageDigest.digest());
            StringBuilder md5 = new StringBuilder(bigInt.toString(16));
            while (md5.length() < 32) {
                md5.insert(0, "0");
            }
            return md5.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                    randomAccessFile = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * md5加密 32位 小写
     * @param plainText
     * @return
     */
    public static String encryption(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }
    /**
     * 转化字符串为MD5加密值
     * @param str
     * @return
     */
    public static String StringInMd5(String str) {

        // 消息签名（摘要）
        MessageDigest md5 = null;
        try {
            // 参数代表的是算法名称
            md5 = MessageDigest.getInstance("md5");
            byte[] result = md5.digest(str.getBytes());

            StringBuilder sb = new StringBuilder(32);
            // 将结果转为16进制字符  0~9 A~F
            for (int i = 0; i < result.length; i++) {
                // 一个字节对应两个字符
                byte x = result[i];
                // 取得高位
                int h = 0x0f & (x >>> 4);
                // 取得低位
                int l = 0x0f & x;
                sb.append(chars[h]).append(chars[l]);
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


}