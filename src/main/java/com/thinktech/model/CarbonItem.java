package com.thinktech.model;

public class CarbonItem {
    private double mileage;
    private double carbonEquivalent;
    private CarbonSource carbonSource;
    private String description;

    public CarbonItem(double carbonEquivalent, CarbonSource carbonSource) {
        this(0, carbonEquivalent, carbonSource, "");
    }

    public CarbonItem(double mileage, double carbonEquivalent, CarbonSource carbonSource, String description) {
        this.mileage = mileage;
        this.carbonEquivalent = carbonEquivalent;
        this.carbonSource = carbonSource;
        this.description = description;
    }

    public double getMileage() {
        return mileage;
    }

    public double getCarbonEquivalent() {
        return carbonEquivalent;
    }

    public CarbonSource getCarbonSource() {
        return carbonSource;
    }

    public String getDescription() {
        return description;
    }
}
