package com.thinktech.model.domain;

import java.util.Comparator;

public class CarbonItemWithAverage implements Comparator<CarbonItemWithAverage>, Comparable<CarbonItemWithAverage>  {

    private String CarbonType;
    private Double UserCarbon;
    private Double AverageCarbon;

    public CarbonItemWithAverage(String carbonType, Double userCarbon, Double averageCarbon) {
        CarbonType = carbonType;
        UserCarbon = userCarbon;
        AverageCarbon = averageCarbon;
    }

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

    public double DistanceAboveAverage() {
        return this.UserCarbon - this.AverageCarbon;
    }

    @Override
    public int compareTo(CarbonItemWithAverage o) {
        return (int) (o.DistanceAboveAverage() - this.DistanceAboveAverage());
    }

    @Override
    public int compare(CarbonItemWithAverage o1, CarbonItemWithAverage o2) {
        return (int) (o2.DistanceAboveAverage() - o1.DistanceAboveAverage());
    }
}
