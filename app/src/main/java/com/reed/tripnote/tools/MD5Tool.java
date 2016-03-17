package com.reed.tripnote.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码md5加密工具类
 * Created by 伟 on 2016/3/17.
 */
public class MD5Tool {

    public static String compute(String inStr) {
        String result = null;
        try {
            byte[] valueByte = inStr.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(valueByte);
            result = toHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }


    // 将传递进来的字节数组转换成十六进制的字符串形式并返回
    private static String toHex(byte[] buffer) {
        StringBuilder sb = new StringBuilder(buffer.length * 2);
        for (byte b : buffer) {
            sb.append(Character.forDigit((b & 0xf0) >> 4, 16));
            sb.append(Character.forDigit(b & 0x0f, 16));
        }
        return sb.toString();
    }
}
