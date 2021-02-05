package com.thinktech;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinktech.model.domain.DataForTrackingPage;
import com.thinktech.service.database.TrackingDataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
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

        //get data from table Questionnaire
        List<DataForTrackingPage> journeys = new ArrayList<>();

        TrackingDataProvider provider = new TrackingDataProvider();
            try {
                List<DataForTrackingPage> itemsFromQuestionnaire = provider.DataFromQuestionnaire(userId, amountOfDaysInPeriod, startDate);
                List<DataForTrackingPage> itemsFromJourney = provider.DataFromJourney(userId,amountOfDaysInPeriod, sqlStartDate, sqlFinishDate);

                journeys.addAll(itemsFromQuestionnaire);
                journeys.addAll(itemsFromJourney);
            } catch (Exception e) {
                LOG.error("Error processing request", e);
            }
        //sort journeys by date
        journeys.sort(Comparator.comparing(DataForTrackingPage::getTrackingDate).reversed());

        //toDo delete car-journey (based on Questionnaire)  if there is a real journey based on Journey table for specific date
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
}
