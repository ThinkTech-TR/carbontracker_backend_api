package com.thinktech.model.dtos;

public class CarbonFootprintDto {

    private String userId;
    private double housing;
    private double car;
    private double bus;
    private double train;
    private double flights;
    private double diet;
    private double publicServices;
    private double otherConsumption;
    private double totalCarbon;
    private String footprintType;

    public CarbonFootprintDto() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getHousing() {
        return housing;
    }

    public void setHousing(double housing) {
        this.housing = housing;
    }

    public double getCar() {
        return car;
    }

    public void setCar(double car) {
        this.car = car;
    }

    public double getBus() {
        return bus;
    }

    public void setBus(double bus) {
        this.bus = bus;
    }

    public double getTrain() {
        return train;
    }

    public void setTrain(double train) {
        this.train = train;
    }

    public double getFlights() {
        return flights;
    }

    public void setFlights(double flights) {
        this.flights = flights;
    }

    public double getDiet() {
        return diet;
    }

    public void setDiet(double diet) {
        this.diet = diet;
    }

    public double getPublicServices() {
        return publicServices;
    }

    public void setPublicServices(double publicServices) {
        this.publicServices = publicServices;
    }

    public double getOtherConsumption() {
        return otherConsumption;
    }

    public void setOtherConsumption(double otherConsumption) {
        this.otherConsumption = otherConsumption;
    }

    public double getTotalCarbon() {
        return totalCarbon;
    }

    public void setTotalCarbon(double totalCarbon) {
        this.totalCarbon = totalCarbon;
    }

    public String getFootprintType() {
        return footprintType;
    }

    public void setFootprintType(String footprintType) {
        this.footprintType = footprintType;
    }
}
