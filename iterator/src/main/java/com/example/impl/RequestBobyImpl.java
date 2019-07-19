package com.example.impl;

import com.example.inter.RequestBody;

/**
 * @author jinrun.xie
 * @date 2019/7/19
 **/
public class RequestBobyImpl implements RequestBody {

    private String str;

    @Override
    public String getRequestStr() {
        return str;
    }

    @Override
    public void setRequestStr(String str) {
        this.str = str;
    }
}
