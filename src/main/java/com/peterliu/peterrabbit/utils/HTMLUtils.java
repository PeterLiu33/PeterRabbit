package com.peterliu.peterrabbit.utils;

import java.util.regex.Matcher;

/**
 * Created by bavatinolab on 17/2/8.
 */
public abstract class HTMLUtils {

    /**
     * 将$符号转义
     *
     * @param input
     * @return
     */
    public static String escapeDollar(String input) {
        if (StringUtils.isNotBlank(input)) {
            return input.replaceAll(Matcher.quoteReplacement("$"), "&#36");
        }
        return "";
    }
}
