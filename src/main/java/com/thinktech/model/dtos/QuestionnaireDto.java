package com.thinktech.model.dtos;

public class QuestionnaireDto {
    private String userCategory;
    private String diet;
    private String carUsage;
    private int numberInHousehold;
    private String houseType;
    private String carMileage;
    private String houseAge;

    public QuestionnaireDto() {
    }

    public String getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getCarUsage() {
        return carUsage;
    }

    public void setCarUsage(String carUsage) {
        this.carUsage = carUsage;
    }

    public int getNumberInHousehold() {
        return numberInHousehold;
    }

    public void setNumberInHousehold(int numberInHousehold) {
        this.numberInHousehold = numberInHousehold;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getCarMileage() {
        return carMileage;
    }

    public void setCarMileage(String carMileage) {
        this.carMileage = carMileage;
    }

    public String getHouseAge() {
        return houseAge;
    }

    public void setHouseAge(String houseAge) {
        this.houseAge = houseAge;
    }
}
