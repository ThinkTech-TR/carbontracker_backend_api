package com.thinktech.service;

import com.thinktech.model.domain.CarbonFootprint;
import com.thinktech.model.domain.Questionnaire;
import com.thinktech.model.enums.HouseType;

public class CalculateInitialCarbon {

    // Average carbon values in KgCO2e
    private static final double AVG_HOUSING = 2400;
    private static final double AVG_CAR = 1700;
    private static final double AVG_BUS = 250;
    private static final double AVG_TRAIN = 250;
    private static final double AVG_FLIGHTS = 1100;
    private static final double AVG_FOOD = 3200;
    private static final double AVG_PUBLIC_SERVICES = 1700;
    private static final double AVG_OTHER_CONSUMPTION = 2550;

    private Questionnaire questionnaire;

    public CalculateInitialCarbon(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public CarbonFootprint Calculate() {
        // ToDo estimate carbon for housing, car and food based on questionnaire

        double housingCarbon = CalculateHousing();

        return new CarbonFootprint(
                housingCarbon,
                1400,
                AVG_BUS,
                AVG_TRAIN,
                AVG_FLIGHTS,
                2200,
                AVG_PUBLIC_SERVICES,
                AVG_OTHER_CONSUMPTION);
    }

    public static CarbonFootprint CalculateAverage() {

       return new CarbonFootprint(
                AVG_HOUSING,
                AVG_CAR,
                AVG_BUS,
                AVG_TRAIN,
                AVG_FLIGHTS,
                AVG_FOOD,
                AVG_PUBLIC_SERVICES,
                AVG_OTHER_CONSUMPTION);
    }

    private double CalculateHousing(){
        // TODO Replace with values from DB
        if (this.questionnaire.getHouseType() == HouseType.DETACHED){
            return 2500;
        }
        return 2200;
    }


}
