package com.thinktech.model.dtos;

public class CarbonFootprintResultDto {

    private CarbonFootprintDto userFootprint;
    private CarbonFootprintDto averageFootprint;

    public CarbonFootprintResultDto(){}

    public CarbonFootprintDto getUserFootprint() {
        return userFootprint;
    }

    public void setUserFootprint(CarbonFootprintDto userFootprint) {
        this.userFootprint = userFootprint;
    }

    public CarbonFootprintDto getAverageFootprint() {
        return averageFootprint;
    }

    public void setAverageFootprint(CarbonFootprintDto averageFootprint) {
        this.averageFootprint = averageFootprint;
    }
}
