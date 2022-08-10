package com.example.kdoushen.douyin.util;

public class SystemUtil {
    /**
     * 判断是否是windows系统，是返回true
     * @return
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").toUpperCase().indexOf("WINDOWS")>=0?true:false;
    }
}
