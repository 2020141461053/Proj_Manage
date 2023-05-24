package com.example.eback.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class test {
    public static void main(String[] args) {

        Date date = new Date();
        //将时间格式化成yyyy-MM-dd HH:mm:ss的格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //创建Calendar实例
        Calendar cal = Calendar.getInstance();
        //设置当前时间
        cal.setTime(date);
        //在当前时间基础上减一年
        cal.add(Calendar.YEAR, -7);
        System.out.println(format.format(cal.getTime()));
        //在当前时间基础上减一月
        cal.add(Calendar.MONTH,-1);
        System.out.println(format.format(cal.getTime()));
        //同理增加一天的方法：
        cal.add(Calendar.DATE, 1);
        System.out.println(format.format(cal.getTime()));

    }
}
