package com.example.wegomarket;

import com.example.wegomarket.model.User;
import com.example.wegomarket.service.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
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

    public static String md5Encode(String input) {
        MessageDigest md;
        String output = "";
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


    public static double cos(int val1, int val2) {
        int[] x = new int[32];
        int[] y = new int[32];

        //初始化
        for (int i = 0; i < 32; i++) {
            x[i] = (val1 & 1 << i) == 0 ? 0 : 1;
            y[i] = (val2 & 1 << i) == 0 ? 0 : 1;
        }

        //检查
//        System.out.println(Arrays.toString(x));
//        System.out.println(Arrays.toString(y));

        int sumXY = 0;
        int sumX = 0;
        int sumY = 0;

        for (int i = 0; i < 32; i++) {
            sumXY += x[i] * y[i];
            sumX += x[i] * x[i];
            sumY += y[i] * y[i];
        }

        //检查
//        System.out.println("sumX = " + sumX);
//        System.out.println("sumY = " + sumY);
//        System.out.println("sumXY = " + sumXY);

        return ((double) sumXY / (Math.sqrt(sumX) * Math.sqrt(sumY)));
    }


    public static void refreshRecommend(UserService userService, long userId) {
        User user = userService.findUserById(userId);

        //获得所有相似度最高的用户id
        List<User> users = userService.getUserList();
        double similarity = 0;
        List<User> similarUsers = new ArrayList<>();
        for (User cur : users) {
            //保证不和自己对比，永远为1，为最高。
            if (cur.getId() != userId) {
                double temp = Util.cos(cur.getBuy(), user.getBuy());
                if (temp > similarity) {
                    similarUsers.clear();
                    similarUsers.add(cur);
                } else if (temp == similarity) {
                    similarUsers.add(cur);
                }
            }
        }

        int toBuy = 0;
        for (User cur : similarUsers) {
            toBuy |= cur.getBuy();
        }

        //保存到数据库
        user.setToBuy(toBuy);
        userService.save(user);
    }

    public static void main(String[] args) {
//        System.out.println(md5Encode("123"));

//        System.out.println(cos(4, 12));
//        System.out.println(cos(8, 12));
//        System.out.println(cos(12, 15));
//        System.out.println(cos(4, 4));


        System.out.println(4 | 8);//12
        System.out.println(4 | 12);//12
        System.out.println(9 | 3);//11
    }
}
