package com.thinktech.model.domain;

import com.thinktech.model.enums.*;

public class Questionnaire {

   private UserCategory userCategory;
   private Diet diet;
   private CarUsage carUsage;
   private int carMileageMiles;
   private int numberInHousehold;
   private HouseType houseType;
   private CarMileage carMileage;
   private HouseAge houseAge;

   public Questionnaire(UserCategory userCategory, Diet diet, CarUsage carUsage, int numberInHousehold, HouseType houseType, CarMileage carMileage, HouseAge houseAge) {
      this.userCategory = userCategory;
      this.diet = diet;
      this.carUsage = carUsage;
      this.carMileageMiles = this.calculateCarMileageMiles(carMileage);
      this.numberInHousehold = numberInHousehold;
      this.houseType = houseType;
      this.carMileage = carMileage;
      this.houseAge = houseAge;
   }

   private int calculateCarMileageMiles(CarMileage mileage){
      switch (mileage) {
         case NONE:
            return 0;
         case LESS_THAN_1000:
            return 500;
         case FROM_1001_TO_5000:
            return 2500;
         case FROM_5001_TO_10000:
            return 7500;
         case OVER_10000:
            return 12500;
      }
      throw new IllegalArgumentException(String.format("Cannot convert %s to miles", mileage));
   }

   public UserCategory getUserCategory() {
      return userCategory;
   }

   public Diet getDiet() {
      return diet;
   }

   public CarUsage getCarUsage() {
      return carUsage;
   }

   public int getCarMileageMiles() {
      return carMileageMiles;
   }

   public int getNumberInHousehold() {
      return numberInHousehold;
   }

   public HouseType getHouseType() {
      return houseType;
   }

   public CarMileage getCarMileage() {
      return carMileage;
   }

   public HouseAge getHouseAge() {
      return houseAge;
   }
}