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

public class GetAnalysisYTDHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>
{
    private static final Logger LOG = LogManager.getLogger(GetAnalysisYTDHandler.class);
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        LOG.info("received request");
        String userId = request.getPathParameters().get("userId");
        DateTimeFormatter formatter_1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate inputDate = LocalDate.parse(request.getPathParameters().get("finishDate"), formatter_1);
        java.sql.Date sqlFinishDate = java.sql.Date.valueOf(LocalDate.now());

        int year = inputDate.getYear();
        int month = 1;
        int dayOfMonth = 1;
        List<DataForTrackingPage> yearList = new ArrayList<DataForTrackingPage>();

        LocalDate startDate = LocalDate.of(year, month, dayOfMonth);
        java.sql.Date sqlStartDate = java.sql.Date.valueOf(startDate);
        long amountOfDaysInPeriod = ChronoUnit.DAYS.between(startDate, LocalDate.now()) + 1;
        System.out.println("amountOfDaysInPeriod " + amountOfDaysInPeriod);

        //get data from table Questionnaire
        List<DataForTrackingPage> journeys = new ArrayList<>();

        TrackingDataProvider provider = new TrackingDataProvider();
        try {
            List<DataForTrackingPage> itemsFromQuestionnaire = provider.DataFromQuestionnaire(userId, amountOfDaysInPeriod, startDate);
            List<DataForTrackingPage> itemsFromJourney = provider.DataFromJourney(userId,amountOfDaysInPeriod, sqlStartDate, sqlFinishDate);

            List<DataForTrackingPage> duplicates = new ArrayList<>();
            for (DataForTrackingPage t : itemsFromJourney){
                for (DataForTrackingPage q : itemsFromQuestionnaire){
                    if (t.getTrackingItemName().equalsIgnoreCase(q.getTrackingItemName().toLowerCase()) &&
                            t.getTrackingDate().equalsIgnoreCase(q.getTrackingDate())){
                        duplicates.add(q);
                    }
                }
            }
            itemsFromQuestionnaire.removeAll(duplicates);

            journeys.addAll(itemsFromQuestionnaire);
            journeys.addAll(itemsFromJourney);
            ///////////////////////////////////
            Set<String> attr = new HashSet<String>();

            for (DataForTrackingPage d : journeys) {
                attr.add(d.getTrackingItemName().toLowerCase());
            }

            for (String obj : attr){
                double CO2 = 0.00;
                for (DataForTrackingPage d : journeys){
                    if (d.getTrackingItemName().equalsIgnoreCase(obj)) {
                        CO2 += d.getEmission();
                    }
                }
                DataForTrackingPage item = new DataForTrackingPage(0, obj, 0, CO2, false, sqlFinishDate.toString(), 0, 0, userId );
                yearList.add(item);
            }
            ///////////////////////////////////
        } catch (Exception e) {
            LOG.error("Error processing request", e);
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
            String responseBody = objectMapper.writeValueAsString(yearList);
            response.setBody(responseBody);
        }
        catch (JsonProcessingException e) {
            LOG.error("Unable to convert result to JSON", e);
        }
        return response;
    }
}
