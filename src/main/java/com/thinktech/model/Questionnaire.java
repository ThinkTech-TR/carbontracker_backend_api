package com.thinktech.model;

import com.thinktech.model.enums.CarUsage;
import com.thinktech.model.enums.Diet;
import com.thinktech.model.enums.HouseType;
import com.thinktech.model.enums.UserCategory;
import com.thinktech.service.CarbonUtilities;

public class Questionnaire {
   private UserCategory userCategory;
   private Diet diet;
   private CarUsage carUsage;
   private int numberInHousehold;
   private HouseType houseType;
   private String carMileage;
   private String houseAge;

   public Questionnaire() {
   }

   public Questionnaire(String userCategory,
                        String diet,
                        String carUsage,
                        int numberInHousehold,
                        String houseType,
                        String carMileage,
                        String houseAge) {
      setUserCategory(userCategory);
      setDiet(diet);
      setCarUsage(carUsage);
      this.carMileage = carMileage;
      this.numberInHousehold = numberInHousehold;
      setHouseType(houseType);
      this.houseAge = houseAge;
   }

   public UserCategory getUserCategory() {
      return userCategory;
   }

   public void setUserCategory(String userCategory) {
      this.userCategory = UserCategory.valueOf(CarbonUtilities.ConvertCamelCaseToUpperCase(userCategory));
   }

   public Diet getDiet() {
      return diet;
   }

   public void setDiet(String diet) {
      this.diet = Diet.valueOf(CarbonUtilities.ConvertCamelCaseToUpperCase(diet));
   }

   public CarUsage getCarUsage() {
      return carUsage;
   }

   public void setCarUsage(String carUsage) {
      this.carUsage = CarUsage.valueOf(CarbonUtilities.ConvertCamelCaseToUpperCase(carUsage));
   }

   private int getCarMileageAsNumber() {
      if (this.carUsage == CarUsage.NO_CAR){
         return 0;
      }
      int ret = 0;
      switch (this.carMileage) {
         case "lessThan1000":
            ret = 500;
            break;
         case "1001to5000":
            ret = 2500;
            break;
         case "5001to10000":
            ret = 7500;
            break;
         case "over10000":
            ret = 12500;
            break;
      }
      return ret;
   }

   public int getNumberInHousehold() {
      return numberInHousehold;
   }

   public void setNumberInHousehold(int numberInHousehold) {
      this.numberInHousehold = numberInHousehold;
   }

   public HouseType getHouseType() {
      return houseType;
   }

   public void setHouseType(String houseType) {
      this.houseType = HouseType.valueOf(CarbonUtilities.ConvertCamelCaseToUpperCase(houseType));
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