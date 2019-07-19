package com.example.impl;

import com.example.inter.Filter;
import com.example.inter.FilterChain;
import com.example.inter.RequestBody;
import com.example.inter.ResponseBody;

/**
 * @author jinrun.xie
 * @date 2019/7/19
 **/
public class JSFilter implements Filter {
    @Override
    public void doFilter(RequestBody requestBody, ResponseBody responseBody, FilterChain filterChain) {
        StringBuilder stringBuilder = new StringBuilder(requestBody.getRequestStr());
        stringBuilder.append("JSFilter处理过了，咯咯");
        requestBody.setRequestStr(stringBuilder.toString());
        responseBody.setResponseStr(stringBuilder.toString());
        filterChain.doFilter(requestBody, responseBody);
    }
}
