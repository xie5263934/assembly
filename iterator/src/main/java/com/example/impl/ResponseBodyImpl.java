package com.example.impl;

import com.example.inter.ResponseBody;

/**
 * @author jinrun.xie
 * @date 2019/7/19
 **/
public class ResponseBodyImpl implements ResponseBody {

    private String str;

    @Override
    public String getResponseStr() {
        return str;
    }

    @Override
    public void setResponseStr(String str) {
        this.str = str;
    }
}
