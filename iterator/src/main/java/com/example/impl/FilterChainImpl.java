package com.example.impl;

import com.example.inter.Filter;
import com.example.inter.FilterChain;
import com.example.inter.RequestBody;
import com.example.inter.ResponseBody;

import java.util.List;

/**
 * @author jinrun.xie
 * @date 2019/7/19
 **/
public class FilterChainImpl implements FilterChain {
    private int pos = 0;
    private ApplicationFilterConfig applicationFilterConfig;

    @Override
    public void doFilter(RequestBody requestBody, ResponseBody responseBody) {
        List<Filter> filterList = applicationFilterConfig.getFilterList();
        if (filterList != null && filterList.size() > 0 && pos < filterList.size()) {
            Filter f = filterList.get(pos);
            pos++;
            f.doFilter(requestBody, responseBody, this);
        }
    }

    public FilterChainImpl(ApplicationFilterConfig applicationFilterConfig) {
        this.applicationFilterConfig = applicationFilterConfig;
    }
}
