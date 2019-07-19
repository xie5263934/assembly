package com.example.impl;

import com.example.TUserService;
import com.example.entity.TUser;
import com.example.mapper.TUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jinrun.xie
 * @date 2019/7/18
 **/
@Service
public class TUserServiceImpl implements TUserService {

    @Autowired
    private TUserMapper userMapper;

    @Override
    public TUser getUserByName(String name) {
        return userMapper.getUserByName(name);
    }
}
