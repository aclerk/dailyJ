package com.pyjava.daily.util;

import java.util.UUID;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/6/17 23:50
 */
public class UuidUtils {
    public static String getUuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
