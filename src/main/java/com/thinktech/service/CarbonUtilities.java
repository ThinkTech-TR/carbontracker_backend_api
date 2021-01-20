package com.thinktech.service;

public class CarbonUtilities {

    public static String ConvertUpperCaseToCamelCase(String str) {
        StringBuilder sb = new StringBuilder();
        String[] words = str.split("_");
        sb.append(words[0].toLowerCase());
            for (int i=1; i < words.length; i++) {
                sb.append(CarbonUtilities.Capitalize(words[i]));
            }
        return sb.toString();
    }

    public static String ConvertCamelCaseToUpperCase(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : str.toCharArray()) {
            char cUpper = Character.toUpperCase(c);
            if (Character.isUpperCase(c)) {
                stringBuilder.append('_').append(cUpper);
            } else {
                stringBuilder.append(cUpper);
            }
        }
        return stringBuilder.toString();
    }

    public static String Capitalize(String str){
        if(str == null || str.length() == 0)
            return "";

        if(str.length() == 1)
            return str.toUpperCase();

        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
