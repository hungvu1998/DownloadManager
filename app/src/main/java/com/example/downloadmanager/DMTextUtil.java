package com.example.downloadmanager;

import android.util.Patterns;
import android.webkit.URLUtil;

public class DMTextUtil {
    public static boolean isValidUrl(CharSequence input){
        if(Patterns.WEB_URL.matcher(input).matches()){
            return URLUtil.isHttpUrl(input.toString()) || URLUtil.isHttpsUrl(input.toString());
        }
        return false;
    }

    private static String toSizePretty(long size){
        String suffix = " bytes";
        if (size >= 1024) {
            suffix = "Kb";
            size /= 1024;
            if (size >= 1024) {
                suffix = "Mb";
                size /= 1024;
                if (size >= 1024) {
                    suffix = "Gb";
                    size /= 1024;
                }
            }
        }
        return size + suffix;
    }

    public static String getSizeProgressString(int current, int total){
        return toSizePretty(current) + " of " + toSizePretty(total);
    }

    public static int getPercentProgress(int current, int total){
        return (int) ( (float) current / (float) total * 100);
    }

    public static String getPercentProgressString(int current, int total){
        return getPercentProgress(current,total) + "%";
    }
}
