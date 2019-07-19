package com.example.impl;

import com.example.inter.Filter;
import com.example.inter.FilterChain;
import com.example.inter.RequestBody;
import com.example.inter.ResponseBody;

/**
 * @author jinrun.xie
 * @date 2019/7/19
 **/
public class FilterChainTest {
    public static void main(String[] args) {
        Filter f1 = new CSSFilter();
        Filter f2 = new HTMLFilter();
        Filter f3 = new JSFilter();
        ApplicationFilterConfig config = new ApplicationFilterConfig();
        config.setFilter(f1);
        config.setFilter(f2);
        config.setFilter(f3);
        FilterChain filterChain = new FilterChainImpl(config);
        RequestBody requestBody = new RequestBobyImpl();
        requestBody.setRequestStr("开始servlet模式的责任链计算");
        ResponseBody responseBody = new ResponseBodyImpl();
        filterChain.doFilter(requestBody, responseBody);
        System.out.println(responseBody.getResponseStr());
    }
}
