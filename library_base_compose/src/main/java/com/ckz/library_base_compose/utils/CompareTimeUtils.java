package com.ckz.library_base_compose.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CompareTimeUtils {

    private static SimpleDateFormat allFormat = new SimpleDateFormat("MM/dd HH:mm");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public static boolean isSameDay(long day1,long day2){
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String date1 = format.format(new Date(day1));
        String date2 = format.format(new Date(day2));
        return date1.equals(date2);
    }

    public static String resetDate(long day1,long day2){
        String time;
        if (isSameDay(day1,day2)){
            time = dateFormat.format(new Date(day1))+" "+timeFormat.format(new Date(day1))+" - "+timeFormat.format(new Date(day2));
        }else {
            time = allFormat.format(new Date(day1))+" - "+allFormat.format(new Date(day2));
        }

        return time;
    }
}
