package com.thinktech.service;

public class CarbonUtilities {

    public static String ConvertUpperCaseToCamelCase(String str) {
        if(str == null || str.length() == 0)
            return "";
        String[] words = str.split("_");

        StringBuilder sb = new StringBuilder();
        sb.append(words[0].toLowerCase());
            for (int i=1; i < words.length; i++) {
                sb.append(CarbonUtilities.Capitalize(words[i]));
            }
        return sb.toString();
    }

    public static String Capitalize(String str){
        if(str == null || str.length() == 0)
            return "";

        if(str.length() == 1)
            return str.toUpperCase();

        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
