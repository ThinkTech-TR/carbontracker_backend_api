package com.thinktech.model.assemblers;

import com.thinktech.model.domain.CarbonFootprint;
import com.thinktech.model.dtos.CarbonFootprintDto;

public class CarbonFootprintAssembler {

    public static CarbonFootprintDto Disassemble(CarbonFootprint carbonFootprint){
        CarbonFootprintDto dto = new CarbonFootprintDto();
        dto.setBus(carbonFootprint.getBus());
        dto.setCar(carbonFootprint.getCar());
        dto.setFlights(carbonFootprint.getFlights());
        dto.setDiet(carbonFootprint.getDiet());
        dto.setFootprintType(carbonFootprint.getFootprintType().toString());
        dto.setHousing(carbonFootprint.getHousing());
        dto.setOtherConsumption(carbonFootprint.getOtherConsumption());
        dto.setPublicServices(carbonFootprint.getPublicServices());
        dto.setTotalCarbon(carbonFootprint.getTotalCarbon());
        dto.setTrain(carbonFootprint.getTrain());
        dto.setUserId(carbonFootprint.getUserId());
        return dto;
    }

}
