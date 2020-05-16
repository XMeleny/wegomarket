package com.example.wegomarket;

import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public class Util {
    public static String makeCheckCode() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String md5Encode(String input){
        MessageDigest md;
        String output="";
        try {
            md = MessageDigest.getInstance("MD5");
            //面向字节处理，所以可以处理字节的东西，如图片，压缩包。。
            byte[] in = input.getBytes();
            byte[] out = md.digest(in);
            //将md5处理后的output结果利用Base64转成原有的字符串,不会乱码
            output = Base64.encodeBase64String(out);
            return output;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("密码加密失败");
            return input;
        }
    }

    public static void main(String[] args) {
        System.out.println(md5Encode("123"));
    }
}
