package com.thinktech.service.database;

import com.thinktech.model.enums.CarUsage;
import com.thinktech.service.CarbonUtilities;

public class InitialCarbonDataProvider extends CarbonDataProvider {

    public double GetCarbonForCarType(CarUsage carUsage) throws Exception {
        double carbon = 0.0;

        if (carUsage == CarUsage.NO_CAR) {
            return carbon;
        }

        String carType = CarbonUtilities.ConvertUpperCaseToCamelCase(carUsage.toString());
        try {
            connection = this.OpenConnection();
            preparedStatement = connection.prepareStatement("SELECT kg_carbon_per_mile from car_carbon where car_type = ?");

            preparedStatement.setString(1, carType);
            resultSet = preparedStatement.executeQuery();
            resultSet.first();
            carbon = resultSet.getDouble("kg_carbon_per_mile");

        } catch (Exception e) {
            String message = String.format("Unable to query database for car_carbon for car_type %s", carType);
            throw new Exception(message, e);
        } finally {
            CloseConnection();
        }
        return carbon;
    }


}
