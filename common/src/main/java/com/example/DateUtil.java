package com.example;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jinrun.xie
 * @date 2019/6/20
 **/
public class DateUtil {
    public static String format(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
