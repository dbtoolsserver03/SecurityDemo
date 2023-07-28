package com.baizhi;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPasswordEncoder {


    public static void main(String[] args) {

        //1.加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(16);
        String encode = bCryptPasswordEncoder.encode("123");
        System.out.println(encode);

    }
}
