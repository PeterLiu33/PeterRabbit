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

    /**
     * 将字节数组换成成16进制的字符串
     *
     * @param byteArray
     * @return
     */
    public static String byteArrayToHex2(byte[] byteArray) {
        String output = new String();
        for (int i = 0; i < byteArray.length; i++) {
            int b = (0xFF & byteArray[i]);
            if (b <= 0xF) {
                output += "0";
            }
            output += Integer.toHexString(b);
        }
        output = output.toUpperCase();
        return output;
    }

    /**
     * 将字节数组换成成16进制的字符串
     *
     * @param byteArray
     * @return
     */
    public static String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }
}
