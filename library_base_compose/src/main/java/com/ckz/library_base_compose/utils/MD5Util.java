package com.ckz.library_base_compose.utils;

import java.security.MessageDigest;

/** 
 * 采用MD5加密
 * @author Xingxing,Xie
 * @datetime 2014-5-31 
 */
public class MD5Util {
    /*** 
     * MD5加密 生成32位md5�??
     * @param inStr 待加密字符串
     * @return 返回32位md5�??
     */
    public static String md5Encode(String inStr)  {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = inStr.getBytes("UTF-8");
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }


    }

}