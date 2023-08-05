package com.tiny.cash.loan.card.utils;

import android.text.TextUtils;
import android.util.Log;

import com.tiny.cash.loan.card.kudicredit.BuildConfig;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    private static final String password = "3dd6f287c328a018";//"3dd6f287c328a018cb5c93aa48554e8b";

    public static String encryptAES(String content) {

        if (TextUtils.isEmpty(content)) {
            throw new RuntimeException("content can not be empty");
        }

        byte[] encryptResult = encrypt(content, password);
        String encryptResultStr = parseByte2HexStr(encryptResult);
        return encryptResultStr;
    }

    public static String decryptAES(String ciphertext) {
        if (TextUtils.isEmpty(ciphertext)) {
            throw new RuntimeException("ciphertext can not be empty");
        }

        try {
            byte[] decryptFrom = parseHexStr2Byte(ciphertext);
            byte[] decryptResult = decrypt(decryptFrom, password);
            return new String(decryptResult);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static byte[] encrypt(String content, String password) {
        try {
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);
            return result;
        } catch (Exception e) {
            Log.e("encrypt error", e.getMessage());
        }
        return null;
    }

    private static byte[] decrypt(byte[] content, String password) {
        try {
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            Log.e("decrypt error", e.getMessage());
        }
        return null;
    }

    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /*public static void main(String[] args) {
        System.out.println("加密解密测试:");
        String password = "ICREDIT" + "2022-01-01";
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        System.out.println("密码为:" + password);
        String content = "this is update data 加密解密测试，&%……&（（））））¥#@！%……&（+——）";
        System.out.println("原内容为：" + content);
        String encryContent = AESUtil.encryptAES(content, password);
        System.out.println("加密后的内容为：" + encryContent);
        String decryContent = AESUtil.decryptAES(encryContent, password);
        System.out.println("解密后的内容为：" + decryContent);
    }*/

}