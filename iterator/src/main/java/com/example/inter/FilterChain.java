package com.example.inter;

/**
 * @author jinrun.xie
 * @date 2019/7/19
 **/
public interface FilterChain {
    void doFilter(RequestBody requestBody, ResponseBody responseBody);
}
