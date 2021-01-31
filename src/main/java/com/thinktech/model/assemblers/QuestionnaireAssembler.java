package com.thinktech.model.assemblers;

import com.thinktech.model.domain.Questionnaire;
import com.thinktech.model.dtos.QuestionnaireDto;
import com.thinktech.model.enums.*;
import com.thinktech.service.CarbonUtilities;

public class QuestionnaireAssembler {

    public static Questionnaire Assemble(QuestionnaireDto dto){
        UserCategory userCategory = UserCategory.valueOf(CarbonUtilities.ConvertCamelCaseToUpperCase(dto.getUserCategory()));
        Diet diet = Diet.valueOf(CarbonUtilities.ConvertCamelCaseToUpperCase(dto.getDiet()));
        CarUsage carUsage = CarUsage.valueOf(CarbonUtilities.ConvertCamelCaseToUpperCase(dto.getCarUsage()));
        HouseType houseType = HouseType.valueOf(CarbonUtilities.ConvertCamelCaseToUpperCase(dto.getHouseType()));
        CarMileage carMileage = QuestionnaireAssembler.Assemble(dto.getCarMileage(), carUsage);
        HouseAge houseAge = QuestionnaireAssembler.Assemble(dto.getHouseAge());

        return new Questionnaire(userCategory, diet, carUsage, dto.getNumberInHousehold(), houseType, carMileage, houseAge);
    }

    private static CarMileage Assemble(String carMileage, CarUsage carUsage){
        if (carUsage == CarUsage.NO_CAR){
            return CarMileage.NONE;
        }
        switch (carMileage) {
            case "lessThan1000":
               return CarMileage.LESS_THAN_1000;
            case "1001to5000":
                return CarMileage.FROM_1001_TO_5000;
            case "5001to10000":
                return CarMileage.FROM_5001_TO_10000;
            case "over10000":
                return CarMileage.OVER_10000;
        }
        throw new IllegalArgumentException(String.format("Cannot convert %s to CarMileage", carMileage));
    }

    private static HouseAge Assemble(String houseAge){
       switch (houseAge) {
            case "pre1919":
                return HouseAge.PRE_1919;
            case "1919-44":
                return HouseAge.FROM_1919_TO_44;
            case "1945-64":
                return HouseAge.FROM_1945_TO_64;
           case "1965-83":
               return HouseAge.FROM_1965_TO_83;
           case "1983-92":
               return HouseAge.FROM_1983_TO_92;
           case "1993-99":
               return HouseAge.FROM_1993_TO_99;
           case "post1999":
               return HouseAge.POST_1999;
        }
        throw new IllegalArgumentException(String.format("Cannot convert %s to HouseAge", houseAge));
    }

    public static String DisassembleHouseAge(HouseAge houseAge){
        switch (houseAge) {
            case PRE_1919 :
                return "pre1919";
            case FROM_1919_TO_44:
                return "1919-44";
            case FROM_1945_TO_64:
                return "1945-64";
            case FROM_1965_TO_83:
                return "1965-83";
            case FROM_1983_TO_92:
                return "1983-92";
            case FROM_1993_TO_99:
                return "1993-99";
            case POST_1999:
                return "post1999";
        }
        throw new IllegalArgumentException(String.format("Cannot convert HouseAge %s to String", houseAge));
    }
}
