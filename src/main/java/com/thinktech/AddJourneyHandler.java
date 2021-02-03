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
import com.thinktech.service.database.TrackingDataProvider;
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

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        LOG.info("received request");

        String userId = request.getPathParameters().get("userId");
        DateTimeFormatter formatter_1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate finishDate = LocalDate.parse(request.getPathParameters().get("finishDate"), formatter_1);
        String requestBody = request.getBody();

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        ObjectMapper objMapper = new ObjectMapper();
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Credentials", "true");
        response.setHeaders(headers);
        String responseBody = "Unable to add new journey";
        response.setBody(responseBody);
        response.setStatusCode(500);

        try {
            DataForTrackingPage journey = objMapper.readValue(requestBody, DataForTrackingPage.class);
            try {
                TrackingDataProvider provider = new TrackingDataProvider();
                provider.AddJourney(journey);
                responseBody = objMapper.writeValueAsString(journey);
                response.setBody(responseBody);
                response.setStatusCode(200);
                LOG.debug("Saved journey " + journey.getTrackingItemName() + " date " + journey.getTrackingDate());
            }
            catch (Exception e){
                LOG.error("Error processing request", e);
            }

        } catch (JsonParseException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            LOG.error("Unable to unmarshal JSON for adding a journey", ex);
        }
        return response;
    }
}
