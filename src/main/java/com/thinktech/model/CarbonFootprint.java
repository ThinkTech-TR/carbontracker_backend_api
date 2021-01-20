package com.thinktech.model;

public class CarbonFootprint {

    public enum FootprintType {YEARLY, YTD, MTD}

    private String userId;
    private double housing;
    private double car;
    private double bus;
    private double train;
    private double flights;
    private double food;
    private double publicServices;
    private double otherConsumption;
    private double totalCarbon;
    private FootprintType footprintType;

    public CarbonFootprint(double housing, double car, double bus,
                           double train, double flights, double food,
                           double publicServices, double otherConsumption,
                           String userId, FootprintType footprintType) {
        this.userId = userId;
        this.housing = housing;
        this.car = car;
        this.bus = bus;
        this.train = train;
        this.flights = flights;
        this.food = food;
        this.publicServices = publicServices;
        this.otherConsumption = otherConsumption;
        this.footprintType = footprintType;
        this.totalCarbon = calculateTotal();
    }

    public CarbonFootprint(double housing, double car, double bus,
                           double train, double flights, double food,
                           double publicServices, double otherConsumption) {
        this(housing, car, bus, train, flights, food, publicServices, otherConsumption,
                "", FootprintType.YEARLY);
    }

    public double calculateTotal() {
        double num = this.housing + this.car + this.bus + this.train +
                this.flights + this.food + this.publicServices
                + this.otherConsumption;
       return Math.round(num*1000.0) / 1000.0;
    }

    public FootprintType getFootprintType() {
        return footprintType;
    }

    public String getUserId() {
        return userId;
    }

    public double getTotalCarbon() {
        return totalCarbon;
    }

    public double getHousing() {
        return housing;
    }

    public double getCar() {
        return car;
    }

    public double getBus() {
        return bus;
    }

    public double getTrain() {
        return train;
    }

    public double getFlights() {
        return flights;
    }

    public double getFood() {
        return food;
    }

    public double getPublicServices() {
        return publicServices;
    }

    public double getOtherConsumption() {
        return otherConsumption;
    }
}
