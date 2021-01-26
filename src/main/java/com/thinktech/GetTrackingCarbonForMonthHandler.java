package com.thinktech;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinktech.model.domain.CarbonForDate;
import com.thinktech.model.domain.CarbonItem;
import com.thinktech.model.enums.CarbonSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class GetTrackingCarbonForMonthHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>
    {
        private static final Logger LOG = LogManager.getLogger(CalculateInitialCarbonHandler.class);

        @Override
        public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        LOG.info("received request");

        //String userId = request.getPathParameters().get("userId");

        DateTimeFormatter formatter_1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        //LocalDate finishDate = LocalDate.parse(request.getPathParameters().get("forDate"), formatter_1);


        //String result = java.net.URLDecoder.decode(request.getPath(), "UTF-8");
        LocalDate finishDate = LocalDate.parse("26/01/2021", formatter_1);
        int year = finishDate.getYear();
        int month = finishDate.getMonthValue();
        int dayOfMonth = 1;
        LocalDate startDate = LocalDate.of(year, month, dayOfMonth);
        long amountOfDaysInPeriod = ChronoUnit.DAYS.between(startDate, finishDate) + 1;

        ArrayList<CarbonForDate> carbonForDate = new ArrayList<>();
        //toDo estimate carbon for housing, diet and car from questionnaire
        double carbonEquivalentHousing = 4;
        double carbonEquivalentDiet = 3;
        double carbonEquivalentCar = 2.5;
        //add CarbonItem housing, diet, car from questionnaire
        CarbonItem veganDiet = new CarbonItem(0, carbonEquivalentDiet, CarbonSource.DIET, "Vegan");
        CarbonItem housing = new CarbonItem(0, carbonEquivalentHousing, CarbonSource.HOUSING, "");
        CarbonItem car = new CarbonItem(20, carbonEquivalentCar, CarbonSource.CAR, "");
        ArrayList<CarbonItem> itemsFromQuestionnaire = new ArrayList<>();
        itemsFromQuestionnaire.add(veganDiet);
        itemsFromQuestionnaire.add(housing);
        itemsFromQuestionnaire.add(car);
        //add to CarbonForDate for each date
        for (int i = 0; i < amountOfDaysInPeriod; i++){
            CarbonForDate carbon1 = new CarbonForDate(startDate.plusDays(i), itemsFromQuestionnaire);
            carbonForDate.add(carbon1);
        }

         //todo add CarbonItem from journey

/*
        if (userId.equals("abc123")){
            CarbonItem veganDiet = new CarbonItem(0, 3, CarbonSource.DIET, "Vegan");
            CarbonItem housing = new CarbonItem(4, CarbonSource.HOUSING);
            CarbonItem busJourney = new CarbonItem(5, 0.5, CarbonSource.BUS, "");
            CarbonItem trainJourney = new CarbonItem(50, 7.2, CarbonSource.TRAIN, "");
            ArrayList<CarbonItem> items1 = new ArrayList<>();
            items1.add(veganDiet);
            items1.add(housing);
            items1.add(busJourney);
            CarbonForDate carbon1 = new CarbonForDate(dateFirst, items1);
            ArrayList<CarbonItem> items2 = new ArrayList<>();
            items2.add(veganDiet);
            items2.add(housing);
            items2.add(trainJourney);
            CarbonForDate carbon2 = new CarbonForDate(dateSecond, items2);
            carbonForDate.add(carbon1);
            carbonForDate.add(carbon2);
        } else {
            CarbonItem meatDiet = new CarbonItem(0, 3, CarbonSource.DIET, "Meat");
            CarbonItem housing = new CarbonItem(5.3, CarbonSource.HOUSING);
            CarbonItem car = new CarbonItem(20, 2.5, CarbonSource.CAR, "");
            ArrayList<CarbonItem> items = new ArrayList<>();
            items.add(meatDiet);
            items.add(housing);
            items.add(car);
            carbonForDate.add(new CarbonForDate(dateFirst, items));
            carbonForDate.add(new CarbonForDate(dateSecond, items));
        }
*/
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String responseBody = objectMapper.writeValueAsString(carbonForDate);
            //String responseBody = objectMapper.writeValueAsString(itemsFromQuestionnaire);
            response.setBody(responseBody);
        }
        catch (JsonProcessingException e) {
            LOG.error("Unable to convert result to JSON", e);
        }
        return response;
    }
}
