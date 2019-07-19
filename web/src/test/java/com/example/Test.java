package com.example;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author jinrun.xie
 * @date 2019/7/18
 **/
public class Test {
    @org.junit.Test
    public void test() {
        System.out.println(new BCryptPasswordEncoder().encode("admin1"));
    }
}
