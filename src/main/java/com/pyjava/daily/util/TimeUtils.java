package com.pyjava.daily.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/6/17 23:52
 */
public class TimeUtils {
    private static final SimpleDateFormat SDF = new SimpleDateFormat(
            "yyyyMMddHHmmssSSS");

    public static String getTimeString(Date date){
        return SDF.format(date);
    }
    
}
