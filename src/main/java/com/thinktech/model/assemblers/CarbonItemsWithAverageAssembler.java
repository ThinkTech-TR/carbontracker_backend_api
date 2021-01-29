package com.thinktech.model.assemblers;

import com.thinktech.model.domain.CarbonItemWithAverage;
import com.thinktech.model.dtos.CarbonItemWithAverageDto;

import java.util.ArrayList;
import java.util.List;

public class CarbonItemsWithAverageAssembler {

    public static List<CarbonItemWithAverageDto> Disassemble(List<CarbonItemWithAverage> carbonItems){
        List<CarbonItemWithAverageDto> dto = new ArrayList<>();
        carbonItems.forEach(item -> {
            CarbonItemWithAverageDto dtoItem = CarbonItemsWithAverageAssembler.Disassemble(item);
            dto.add(dtoItem);
        });
        return dto;
    }

    public static CarbonItemWithAverageDto Disassemble(CarbonItemWithAverage carbonItem){
        CarbonItemWithAverageDto dto = new CarbonItemWithAverageDto();
        dto.setUserCarbon(carbonItem.getUserCarbon());
        dto.setAverageCarbon(carbonItem.getAverageCarbon());
        dto.setCarbonType(carbonItem.getCarbonType());
        return dto;
    }

}
