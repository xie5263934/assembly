package com.example.impl;

import com.example.DateUtil;
import com.example.HelloService;
import com.example.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author jinrun.xie
 * @date 2019/6/20
 **/
@Service
@Slf4j
public class HelloServiceImpl implements HelloService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String hello(String name) {
        String result = "hello:"+name+ DateUtil.format(new Date());
        return result;
    }
}
