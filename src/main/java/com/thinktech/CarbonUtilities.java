package com.thinktech;

public class CarbonUtilities {

    public static String ConvertUpperCaseToCamelCase(String str) {
        if(str == null || str.length() == 0)
            return "";
        String[] words = str.split("_");

         String ret = words[0].toLowerCase();
            for (String s: words) {
                
            }
        return ret;
    }
}
