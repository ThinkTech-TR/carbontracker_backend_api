package com.thinktech;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinktech.model.domain.DataForTrackingPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class GetTrackingCarbonForMonthHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>
    {
        private static final Logger LOG = LogManager.getLogger(GetTrackingCarbonForMonthHandler.class);
        private Connection connection = null;
        private PreparedStatement preparedStatement = null;
        private ResultSet resultSet = null;

        @Override
        public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        LOG.info("received request");

        String userId = request.getPathParameters().get("userId");


        DateTimeFormatter formatter_1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate finishDate = LocalDate.parse(request.getPathParameters().get("finishDate"), formatter_1);

        java.sql.Date sqlFinishDate = java.sql.Date.valueOf(finishDate);

        int year = finishDate.getYear();
        int month = finishDate.getMonthValue();
        int dayOfMonth = 1;

        LocalDate startDate = LocalDate.of(year, month, dayOfMonth);
        java.sql.Date sqlStartDate = java.sql.Date.valueOf(startDate);
        long amountOfDaysInPeriod = ChronoUnit.DAYS.between(startDate, finishDate) + 1;

        //get data from table Questionaire
        List<DataForTrackingPage> journeys = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s",
                    System.getenv("DB_HOST"),
                    System.getenv("DB_NAME"),
                    System.getenv("DB_USER"),
                    System.getenv("DB_PASSWORD")));
            preparedStatement = connection.prepareStatement("SELECT q.carMileage,   " +
                    "q.carUsage, " +
                    "        q.diet, " +
                    "        q.houseAge, " +
                    "        q.houseType, " +
                    "        q.numberInHousehold, " +
                    "        q.questionaire_id, " +
                    "        u.auth_user_id, " +
                    "        q.userCategory " +
                    "FROM carbon.questionaire as q, carbon.users u " +
                    "WHERE q.user_id = u.user_id "+
                    "       and u.auth_user_id = ?");
            preparedStatement.setString(1, userId);
            resultSet = preparedStatement.executeQuery();


            int counter = 1;
            while (resultSet.next()) {
                String strTrackingNameHousing = "";
                double emissionHousing = 0;
                String strTrackingNameCar = "";
                double emissionCar = 0;
                String strTrackingNameDiet = "";
                double emissionDiet = 0;
                if (resultSet.getString("houseType") != null) {
                    strTrackingNameHousing = "Housing";
                    emissionHousing = 4.0;
                }
                if (resultSet.getString("carUsage") != null) {
                    strTrackingNameCar = "Car";
                    emissionCar = 2.0;
                }
                if (resultSet.getString("diet") != null) {
                    strTrackingNameDiet = resultSet.getString("diet");
                    emissionDiet = 0.5;
                }
                counter ++;
                for (int i = 0; i < amountOfDaysInPeriod; i++){
                    DataForTrackingPage house = new DataForTrackingPage(
                            0,
                            strTrackingNameHousing,
                            0,
                            emissionHousing,
                            false,
                            startDate.plusDays(i).toString() ,
                            counter,
                            0,
                            resultSet.getString("auth_user_id"));
                    journeys.add(house);
                    counter ++;
                    if (emissionCar != 0) {
                        DataForTrackingPage car = new DataForTrackingPage(0,
                                strTrackingNameCar,
                                0,
                                emissionCar,
                                true,
                                startDate.plusDays(i).toString(),
                                counter,
                                0,
                                resultSet.getString("auth_user_id"));
                        journeys.add(car);
                        counter++;
                    }
                    DataForTrackingPage diet = new DataForTrackingPage(0,
                            strTrackingNameDiet,
                            0,
                            emissionDiet,
                            false,
                            startDate.plusDays(i).toString() ,
                            counter,
                            0,
                            resultSet.getString("auth_user_id"));
                    journeys.add(diet);
                    counter ++;
                }
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();



            //get data from table Journey
            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s",
                    System.getenv("DB_HOST"),
                    System.getenv("DB_NAME"),
                    System.getenv("DB_USER"),
                    System.getenv("DB_PASSWORD")));
            preparedStatement = connection.prepareStatement("SELECT j.distance_miles, " +
                    " j.journey_date, " +
                    " j.journey_id, " +
                    " j.transport_id, " +
                    " t.transport_type, " +
                    " u.auth_user_id " +
                    "FROM carbon.journey as j, carbon.transport t, carbon.users u  " +
                    "WHERE j.user_id = u.user_id " +
                    "and j.journey_date <= ? " +
                    "and j.journey_date >= ? " +
                    "and t.transport_id = j.transport_id " +
                    "and u.auth_user_id = ?" +
                    "ORDER BY j.journey_date");
            preparedStatement.setDate(1,sqlFinishDate);
            preparedStatement.setDate(2,sqlStartDate);
            preparedStatement.setString(3, userId);
            resultSet = preparedStatement.executeQuery();
            counter ++;
            while (resultSet.next()) {
                //toDo calculate emission
                double emission = 3.5;
                DataForTrackingPage journey = new DataForTrackingPage(resultSet.getInt("transport_id"),
                        resultSet.getNString("transport_type"),
                        resultSet.getInt("distance_miles"),
                        emission,
                        true,
                        resultSet.getString("journey_date"),
                        counter,
                        resultSet.getInt("journey_id"),
                        resultSet.getString("auth_user_id"));
                journeys.add(journey);
                counter ++;
            }
            }
            catch (Exception e) {
                LOG.error(String.format("Unable to query database for tasks for user %s", userId), e);
            }
            finally {
                closeConnection();
            }
        //sort journeys by date
        journeys.sort(Comparator.comparing(DataForTrackingPage::getTrackingDate).reversed());

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        response.setHeaders(headers);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String responseBody = objectMapper.writeValueAsString(journeys);
            response.setBody(responseBody);
        }
        catch (JsonProcessingException e) {
            LOG.error("Unable to convert result to JSON", e);
        }
        return response;
    }

    private void closeConnection() {
        try {
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
        catch (Exception e) {
            LOG.error("Unable to close connections to MySQL - {}", e.getMessage());
        }
    }
}
