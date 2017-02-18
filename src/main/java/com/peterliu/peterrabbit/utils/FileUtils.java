package com.peterliu.peterrabbit.utils;

import java.util.regex.Matcher;

/**
 * Created by bavatinolab on 17/2/7.
 */
public abstract class FileUtils {

    public static final void main(String[] args){
        String a = Matcher.quoteReplacement("a$");
        System.out.println(a);
        "a".replaceAll(a, "$");
    }

    /**
     * 判断当前文件是否合法
     *
     * @param fileType
     * @param legalSuffix
     * @return
     */
    public static boolean isIllegalFile(String fileType, String[] legalSuffix) {
        for (String suffix : legalSuffix) {
            if (suffix.equalsIgnoreCase(fileType)) {
                return true;
            }
        }
        return false;
    }
}
