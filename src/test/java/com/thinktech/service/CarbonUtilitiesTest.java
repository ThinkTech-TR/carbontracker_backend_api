package com.thinktech.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CarbonUtilitiesTest {

    @Test
    public void TestConvertUpperCaseToCamelCase() {
        String result = CarbonUtilities.ConvertUpperCaseToCamelCase("I_AM_EASY_TO_CONVERT");
        assertEquals(result, "iAmEasyToConvert");
    }

    @Test
    public void TestConvertCamelCaseToUpperCase() {
        String result = CarbonUtilities.ConvertCamelCaseToUpperCase("iAmEasyToConvertBackAgain");
        assertEquals(result, "I_AM_EASY_TO_CONVERT_BACK_AGAIN");
    }
}
