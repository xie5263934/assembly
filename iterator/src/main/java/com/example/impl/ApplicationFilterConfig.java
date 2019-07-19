package com.example.impl;

import com.example.inter.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jinrun.xie
 * @date 2019/7/19
 **/
public class ApplicationFilterConfig {
    private List<Filter> filterList = new ArrayList<>();

    public List<Filter> getFilterList() {
        return this.filterList;
    }

    public void setFilter(Filter filter) {
        this.filterList.add(filter);
    }
}
