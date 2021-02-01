package com.thinktech;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinktech.model.domain.DataForTrackingPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AddJourneyHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>
{
    private static final Logger LOG = LogManager.getLogger(AddJourneyHandler.class);
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        LOG.info("received request");

        String userId = request.getPathParameters().get("userId");
        DateTimeFormatter formatter_1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate finishDate = LocalDate.parse(request.getPathParameters().get("finishDate"), formatter_1);
        String requestBody = request.getBody();

        ObjectMapper objMapper = new ObjectMapper();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Credentials", "true");
        response.setHeaders(headers);

        try {
            System.out.println(requestBody);
            DataForTrackingPage journey = objMapper.readValue(requestBody, DataForTrackingPage.class);
            //get new journey_id from table Journey
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s&useSSL=false",
                        System.getenv("DB_HOST"),
                        System.getenv("DB_NAME"),
                        System.getenv("DB_USER"),
                        System.getenv("DB_PASSWORD")));
                preparedStatement = connection.prepareStatement("SELECT max(j.journey_id)+1 as journey_id FROM carbon.journey j");

                resultSet = preparedStatement.executeQuery();
                resultSet.first();
                journey.setIdJourney(resultSet.getInt("journey_id"));
            }
            catch (Exception e) {
                LOG.error(String.format("Unable to get next journey_id from Journey table for user %s", userId), e);
            }
            finally {
                closeConnection();
            }
            //toDo calculate Carbon
            //insert to table Journey
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s&useSSL=false",
                        System.getenv("DB_HOST"),
                        System.getenv("DB_NAME"),
                        System.getenv("DB_USER"),
                        System.getenv("DB_PASSWORD")));
                preparedStatement = connection.prepareStatement("insert into carbon.journey (user_id, transport_id, journey_date, distance_miles, journey_id) " +
                        "values (?, ?, ?, ?, ?);");
                preparedStatement.setInt(1, Integer.parseInt(userId));
                preparedStatement.setInt(2, journey.getTrackingItemId());
                preparedStatement.setDate(3, java.sql.Date.valueOf(journey.getTrackingDate()));
                preparedStatement.setInt(4, journey.getDistance());
                preparedStatement.setInt(5, journey.getIdJourney());
                preparedStatement.execute();
            }
            catch (Exception e) {
                LOG.error(String.format("Unable to insert data into Journey table  for user %s", userId), e);
            }
            finally {
                closeConnection();
            }
            LOG.debug("Saved journey " + journey.getTrackingItemName() + " date " + journey.getTrackingDate());

            try {
                String responseBody = objMapper.writeValueAsString(journey);
                response.setBody(responseBody);
            }
            catch (JsonProcessingException e) {
                LOG.error("Unable to convert result to JSON", e);
            }
        } catch (JsonParseException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            LOG.error("Unable to unmarshal JSON for adding a journey", ex);
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
