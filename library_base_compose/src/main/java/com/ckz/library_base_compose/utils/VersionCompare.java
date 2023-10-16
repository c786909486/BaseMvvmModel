package com.ckz.library_base_compose.utils;

import android.util.Log;

/**
 * Created by hz-java on 2018/8/9.
 */

public class VersionCompare {

    /**
     * 版本号对比，0代表相等，1代表version1大于version2，-1代表version1小于version2
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
    String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        Log.d("HomePageActivity", "version1Array=="+version1Array.length);
        Log.d("HomePageActivity", "version2Array=="+version2Array.length); int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length); int diff = 0;
        Log.d("HomePageActivity", "verTag2=2222="+version1Array[index]);
        while (index < minLen && (diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index])) == 0) { index++; } if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1; }
            }
            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            } return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }
}
