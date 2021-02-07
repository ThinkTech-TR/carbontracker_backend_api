package com.thinktech.service;

import com.thinktech.CalculateInitialCarbonHandler;
import com.thinktech.model.domain.CarbonItemWithAverage;
import com.thinktech.model.domain.Questionnaire;
import com.thinktech.model.enums.*;
import com.thinktech.service.database.InitialCarbonDataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CalculateInitialCarbonTest {

    private CalculateInitialCarbon initialCarbon;

    private InitialCarbonDataProvider dataProvider;

    private Questionnaire questionnaire;

    private static final Logger LOG = LogManager.getLogger(CalculateInitialCarbonTest.class);


    @Before
    public void setup(){
        try {
            questionnaire = new Questionnaire(UserCategory.INDIVIDUAL,
                    Diet.VEGAN,
                    CarUsage.SMALL_PETROL_DIESEL,
                    2,
                    HouseType.BUNGALOW,
                    CarMileage.FROM_5001_TO_10000,
                    HouseAge.FROM_1945_TO_64);

            dataProvider = mock(InitialCarbonDataProvider.class);
            initialCarbon = new CalculateInitialCarbon(questionnaire, dataProvider);
            when(dataProvider.GetCarbonForCarType(CarUsage.SMALL_PETROL_DIESEL)).thenReturn(0.232540);
            when(dataProvider.GetCarbonForHousing(HouseAge.FROM_1945_TO_64, HouseType.BUNGALOW)).thenReturn(3390.17);
        }
        catch (Exception e){
            LOG.error("Unable to set up test", e);
        }
    }

    @Test
    public void TestCalculateCarbon() {
        try {
           ArrayList<CarbonItemWithAverage> carbonItems = initialCarbon.Calculate();
           CarbonItemWithAverage dietItem = carbonItems.stream().filter(i -> i.getCarbonType().equals("Diet")).findFirst().orElse(null);
           assertEquals( 1500, dietItem == null ? 0 : dietItem.getUserCarbon(), 0);
           CarbonItemWithAverage housingItem = carbonItems.stream().filter(i -> i.getCarbonType().equals("Housing")).findFirst().orElse(null);
           assertEquals( 1695.085, housingItem == null ? 0 : housingItem.getUserCarbon(),0);
           CarbonItemWithAverage carItem = carbonItems.stream().filter(i -> i.getCarbonType().equals("Car")).findFirst().orElse(null);
           assertEquals( 1744.05, carItem == null ? 0 : carItem.getUserCarbon(),0);
        }
        catch (Exception e){
            LOG.error("Unable to run test", e);
        }
   }

}
