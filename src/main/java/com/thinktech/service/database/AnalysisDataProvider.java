package com.thinktech.service.database;

import com.thinktech.model.domain.DataForTrackingPage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AnalysisDataProvider extends CarbonDataProvider {


    public List<DataForTrackingPage> DataFromQuestionnaire (String authUserId, long amountOfDaysInPeriod, LocalDate startDate) throws Exception {
        List<DataForTrackingPage> items = new ArrayList<>();
        try {
            connection = this.OpenConnection();
            preparedStatement = connection.prepareStatement("SELECT q.carMileage, " +
                    "q.carUsage, " +
                    "q.diet, " +
                    "q.houseAge, " +
                    "q.houseType, " +
                    "q.numberInHousehold, " +
                    "q.questionaire_id, " +
                    "q.user_id, " +
                    "q.userCategory, " +
                    "u.nickname, " +
                    "car.kg_carbon_per_mile car_carbon_per_mile, " +
                    "h.kg_carbon_annual house_carbon_annual, " +
                    "d.kg_carbon_annual diet_carbon_annual " +
                    "FROM carbon.questionaire as q " +
                    "LEFT JOIN carbon.car_carbon car ON car.car_type = q.carUsage " +
                    "LEFT JOIN carbon.housing h ON (h.housing_age = q.houseAge and h.housing_type = q.houseType) " +
                    "LEFT JOIN carbon.diet_carbon d ON (d.diet_type = q.diet) " +
                    "INNER join carbon.users u ON  (q.user_id = u.user_id)" +
                    "ORDER BY house_carbon_annual DESC"  +
                    "LIMIT 5");
            //preparedStatement.setString(1, authUserId);
            //System.out.println(preparedStatement);
            //System.out.println("startDate  " +startDate);
            resultSet = preparedStatement.executeQuery();
            resultSet.first();
            int counter = 1;
            for (int i = 0; i < amountOfDaysInPeriod; i++){
                    DataForTrackingPage house = new DataForTrackingPage(0,
                            "House",
                            0,
                            Math.round(100.00 * resultSet.getDouble("house_carbon_annual")/365) / 100.00,
                            false,
                            startDate.plusDays(i).toString() ,
                            counter,
                            0,
                            authUserId);
                    items.add(house);
                    counter ++;
                    if (resultSet.getInt("carMileage") != 0) {
                        DataForTrackingPage car = new DataForTrackingPage(0,
                                "Car",
                                Math.round(resultSet.getInt("carMileage") / 365),
                                Math.round(100.00 * resultSet.getDouble("car_carbon_per_mile") * resultSet.getInt("carMileage") / 365) /100.00,
                                true,
                                startDate.plusDays(i).toString(),
                                counter,
                                0,
                                authUserId);
                        items.add(car);
                        counter++;
                    }
                    DataForTrackingPage diet = new DataForTrackingPage(0,
                            "Diet",
                            0,
                            Math.round(100.00 * resultSet.getInt("diet_carbon_annual") / 365) / 100.00,
                            false,
                            startDate.plusDays(i).toString() ,
                            counter,
                            0,
                            authUserId);
                    items.add(diet);
                    counter ++;
            }
            return items;
        } catch (Exception e) {
            String message = String.format("Unable to query database to check user %s", authUserId);
            throw new Exception(message, e);
        } finally {
            CloseConnection();
        }
    }

    public List<DataForTrackingPage> DataFromJourney (String authUserId, long amountOfDaysInPeriod,
                                                      java.sql.Date sqlStartDate, java.sql.Date sqlFinishDate) throws Exception {
        List<DataForTrackingPage> items = new ArrayList<>();
        try {
            connection = this.OpenConnection();
            preparedStatement = connection.prepareStatement("SELECT sum(j.distance_miles) as distance_miles, " +
                    " j.journey_date, " +
                    " j.journey_id, " +
                    " j.transport_id, " +
                    " t.transport_type, " +
                    " t.kg_carbon_per_mile, " +
                    " u.auth_user_id " +
                    " u.nickname " +
                    "FROM carbon.journey as j, carbon.transport t, carbon.users u  " +
                    "WHERE j.user_id = u.user_id " +
                    "and j.journey_date >= ? " +
                    "and t.transport_id = j.transport_id " +
                    "GROUP BY" +
                    " j.journey_date, " +
                    " j.journey_id, " +
                    " j.transport_id, " +
                    " t.transport_type, " +
                    " t.kg_carbon_per_mile, " +
                    " u.auth_user_id " +
                    " u.nickname ");
            preparedStatement.setDate(1,sqlStartDate);
            resultSet = preparedStatement.executeQuery();
            //System.out.println(preparedStatement);
            int counter = 2021;
            while (resultSet.next()) {
                DataForTrackingPage journey = new DataForTrackingPage(resultSet.getInt("transport_id"),
                        resultSet.getString("transport_type").substring(0,1).toUpperCase() + resultSet.getString("transport_type").substring(1),
                        resultSet.getInt("distance_miles"),
                        Math.round(100.00 * resultSet.getDouble("kg_carbon_per_mile") * resultSet.getInt("distance_miles") )/ 100.00,
                        true,
                        resultSet.getString("journey_date"),
                        counter,
                        resultSet.getInt("journey_id"),
                        resultSet.getString("auth_user_id"));
                items.add(journey);
                counter ++;
            }
            return items;
        } catch (Exception e) {
            String message = String.format("Unable to query database to check user %s", authUserId);
            throw new Exception(message, e);
        } finally {
            CloseConnection();
        }
    }
}
