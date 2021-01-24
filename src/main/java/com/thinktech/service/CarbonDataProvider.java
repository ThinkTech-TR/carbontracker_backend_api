package com.thinktech.service;


import com.thinktech.model.enums.CarUsage;

import java.sql.*;

public class CarbonDataProvider {
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

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

    private Connection OpenConnection() throws ClassNotFoundException, SQLException {
        String connectionString = String.format("jdbc:mysql://%s/%s?user=%s&password=%s",
                System.getenv("DB_HOST"),
                System.getenv("DB_NAME"),
                System.getenv("DB_USER"),
                System.getenv("DB_PASSWORD"));
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(connectionString);
    }

    private void CloseConnection() throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }

        if (preparedStatement != null) {
            preparedStatement.close();
        }

        if (connection != null) {
            connection.close();
        }
    }


}
