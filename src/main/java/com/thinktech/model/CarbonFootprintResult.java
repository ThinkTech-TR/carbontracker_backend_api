package com.thinktech.model;

public class CarbonFootprintResult {

    private CarbonFootprint userFootprint;
    private CarbonFootprint averageFootprint;

    public CarbonFootprintResult(CarbonFootprint userFootprint,
                                 CarbonFootprint averageFootprint)
    {
        this.userFootprint = userFootprint;
        this.averageFootprint = averageFootprint;
    }

    public CarbonFootprint getUserFootprint() {
        return userFootprint;
    }

    public CarbonFootprint getAverageFootprint() {
        return averageFootprint;
    }
}
