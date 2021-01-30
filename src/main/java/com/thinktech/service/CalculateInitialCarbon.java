package com.thinktech.service;

import com.thinktech.model.domain.CarbonFootprint;
import com.thinktech.model.domain.CarbonItemWithAverage;
import com.thinktech.model.domain.Questionnaire;
import com.thinktech.model.enums.HouseType;
import com.thinktech.service.database.CarbonDataProvider;
import com.thinktech.service.database.InitialCarbonDataProvider;

import java.util.ArrayList;
import java.util.List;

public class CalculateInitialCarbon {

    // Average carbon values in KgCO2e
    private static final double AVG_HOUSING = 2400;
    private static final double AVG_CAR = 1700;
    private static final double AVG_BUS = 250;
    private static final double AVG_TRAIN = 250;
    private static final double AVG_FLIGHTS = 1100;
    private static final double AVG_FOOD = 2500;
    private static final double AVG_PUBLIC_SERVICES = 1700;
    private static final double AVG_OTHER_CONSUMPTION = 2550;

    private Questionnaire questionnaire;
    private InitialCarbonDataProvider initialCarbonDataProvider;

    public CalculateInitialCarbon(Questionnaire questionnaire, InitialCarbonDataProvider carbonDataProvider) {
        this.questionnaire = questionnaire;
        this.initialCarbonDataProvider = carbonDataProvider;
    }

    public ArrayList<CarbonItemWithAverage> Calculate() throws Exception {
        // ToDo estimate carbon for housing based on questionnaire

        double housingCarbon = CalculateHousing();
        double carCarbon = CalculateCar();
        double dietCarbon = CalculateDiet();

        ArrayList<CarbonItemWithAverage> carbonItems = new ArrayList<>();
        carbonItems.add(new CarbonItemWithAverage("Housing", housingCarbon, AVG_HOUSING));
        carbonItems.add(new CarbonItemWithAverage("Car", carCarbon, AVG_CAR));
        carbonItems.add(new CarbonItemWithAverage("Bus", AVG_BUS, AVG_BUS));
        carbonItems.add(new CarbonItemWithAverage("Train", AVG_BUS, AVG_TRAIN));
        carbonItems.add(new CarbonItemWithAverage("Flights", AVG_FLIGHTS, AVG_FLIGHTS));
        carbonItems.add(new CarbonItemWithAverage("Diet", dietCarbon, AVG_FOOD));
        carbonItems.add(new CarbonItemWithAverage("Public Services", AVG_PUBLIC_SERVICES, AVG_PUBLIC_SERVICES));
        carbonItems.add(new CarbonItemWithAverage("Other Consumption", AVG_PUBLIC_SERVICES, AVG_OTHER_CONSUMPTION));

        carbonItems.sort(CarbonItemWithAverage::compareTo);

        return carbonItems;
    }

    private double CalculateHousing(){
        // TODO Replace with values from DB
        if (this.questionnaire.getHouseType() == HouseType.DETACHED){
            return 2500;
        }
        return 2200;
    }

    private double CalculateCar() throws Exception {
       double carCarbonEquivalentPerMileKg = this.initialCarbonDataProvider.GetCarbonForCarType(this.questionnaire.getCarUsage());
      return carCarbonEquivalentPerMileKg * this.questionnaire.getCarMileageMiles();
    }

    private double CalculateDiet() {
        switch (this.questionnaire.getDiet()){
            case FREQUENT_MEAT_EATER:
                return 3300;
            case MEAT_EATER:
                return 2500;
            case PESCATARIAN:
                return 1900;
            case VEGETARIAN:
                return 1700;
            case VEGAN:
                return 1500;
        }
        return AVG_FOOD;
    }


}
