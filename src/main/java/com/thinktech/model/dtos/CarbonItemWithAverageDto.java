package com.thinktech.model.dtos;

public class CarbonItemWithAverageDto {

    private String CarbonType;
    private Double UserCarbon;
    private Double AverageCarbon;

    public CarbonItemWithAverageDto() {}

    public String getCarbonType() {
        return CarbonType;
    }

    public void setCarbonType(String carbonType) {
        CarbonType = carbonType;
    }

    public Double getUserCarbon() {
        return UserCarbon;
    }

    public void setUserCarbon(Double userCarbon) {
        UserCarbon = userCarbon;
    }

    public Double getAverageCarbon() {
        return AverageCarbon;
    }

    public void setAverageCarbon(Double averageCarbon) {
        AverageCarbon = averageCarbon;
    }



}
