package com.axun.ccrcserviceapp.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

/**
 *@packageName com.axun.ccrcserviceapp.utils
 *@author kzcai
 *@date 2020-02-19
 */
object TimeSetUtils {

    private val sameDateFormat = SimpleDateFormat("HH:mm")
    private val dateFormat = SimpleDateFormat("yyyy年MM月dd日")
    private val weekFormat = SimpleDateFormat("EEEE")

    private val dateTimeFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm")
    private val weekTimeFormat = SimpleDateFormat("EEEE HH:mm")

    fun setDateToString(time:Long):String{
        val now = System.currentTimeMillis()
        if (isSameDate(time,now)){
            return sameDateFormat.format(Date(time))
        } else if (isYestoday(time,now)){
            return "昨天"
        } else if (isSameWeek(time,now)){
            return weekFormat.format(Date(time))
        } else{
            return dateFormat.format(Date(time))
        }
    }

    fun setTimeToString(time:Long):String{
        val now = System.currentTimeMillis()
        if (isSameDate(time,now)){
            return sameDateFormat.format(Date(time))
        } else if (isYestoday(time,now)){
            return "昨天 ${sameDateFormat.format(Date(time))}"
        } else if (isSameWeek(time,now)){
            return weekTimeFormat.format(Date(time))
        } else{
            return dateTimeFormat.format(Date(time))
        }
    }


    fun isSameDate(time1:Long,time2:Long):Boolean{
        val format = SimpleDateFormat("yyyy-MM-dd")
        val date1 = format.format(Date(time1))
        val date2 = format.format(Date(time2))
        return date1==date2
    }

    fun isYestoday(time1:Long,time2:Long):Boolean{
        val instance = Calendar.getInstance()
        instance.timeInMillis = time1
        val d1 = instance.get(Calendar.DAY_OF_YEAR)
        instance.timeInMillis = time2
        val d2 = instance.get(Calendar.DAY_OF_YEAR)
        return d1 - d2 == 1 || d2 - d1 == 1

    }

    fun isSameWeek(time1:Long,time2:Long):Boolean{
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = Date(time1)
        cal2.time = Date(time2)
        val subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)
        if (subYear == 0)
        // subYear==0,说明是同一年
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true
        } else if (subYear == 1 && cal2.get(Calendar.MONTH) == 11)
        //subYear==1,说明cal比cal2大一年;java的一月用"0"标识，那么12月用"11"
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true
        } else if (subYear == -1 && cal1.get(Calendar.MONTH) == 11)
        //subYear==-1,说明cal比cal2小一年
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true
        }
        return false
    }
}