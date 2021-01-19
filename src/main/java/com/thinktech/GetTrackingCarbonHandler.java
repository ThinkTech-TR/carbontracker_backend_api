package com.thinktech;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinktech.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;

public class GetTrackingCarbonHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>
    {

        private static final Logger LOG = LogManager.getLogger(GetInitialCarbonHandler.class);

        @Override
        public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        LOG.info("received request");

        String userId = request.getPathParameters().get("userId");

        ArrayList<CarbonForDate> carbonForDate = new ArrayList<>();
        LocalDate dateFirst = LocalDate.of(2021, 1, 18);
        LocalDate dateSecond= LocalDate.of(2021, 1, 19);

        if (userId.equals("abc123")){
            CarbonItem veganDiet = new CarbonItem(0, 3, CarbonSource.Food, "Vegan");
            CarbonItem housing = new CarbonItem(4, CarbonSource.Housing);
            CarbonItem busJourney = new CarbonItem(5, 0.5, CarbonSource.Bus, "");
            CarbonItem trainJourney = new CarbonItem(50, 7.2, CarbonSource.Train, "");
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
            CarbonItem meatDiet = new CarbonItem(0, 3, CarbonSource.Food, "Meat");
            CarbonItem housing = new CarbonItem(5.3, CarbonSource.Housing);
            CarbonItem car = new CarbonItem(20, 2.5, CarbonSource.Car, "");
            ArrayList<CarbonItem> items = new ArrayList<>();
            items.add(meatDiet);
            items.add(housing);
            items.add(car);
            carbonForDate.add(new CarbonForDate(dateFirst, items));
            carbonForDate.add(new CarbonForDate(dateSecond, items));
        }

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String responseBody = objectMapper.writeValueAsString(carbonForDate);
            response.setBody(responseBody);
        }
        catch (JsonProcessingException e) {
            LOG.error("Unable to convert result to JSON", e);
        }
        return response;
    }
}
