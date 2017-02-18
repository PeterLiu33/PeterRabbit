package com.peterliu.peterrabbit.utils;

/**
 * Created by bavatinolab on 17/1/24.
 */
public abstract class StringUtils {

    public static boolean isBlank(String value){
        if(value == null || "".equals(value.trim())){
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String value){
        if(value != null && !"".equals(value.trim())){
            return true;
        }
        return false;
    }
}
