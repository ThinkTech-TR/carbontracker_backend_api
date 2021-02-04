package com.thinktech;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinktech.model.domain.DataForTrackingPage;
import com.thinktech.service.database.TrackingDataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UpdateJourneyHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>
{
    private static final Logger LOG = LogManager.getLogger(UpdateJourneyHandler.class);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        LOG.info("received request");

        //String userId = request.getPathParameters().get("userId");

        String requestBody = request.getBody();

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        ObjectMapper objMapper = new ObjectMapper();
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Credentials", "true");
        response.setHeaders(headers);
        String responseBody = "Unable to update journey";
        response.setBody(responseBody);
        response.setStatusCode(500);

        try {
            DataForTrackingPage journey = objMapper.readValue(requestBody, DataForTrackingPage.class);
            System.out.println(journey);
            try {
                TrackingDataProvider provider = new TrackingDataProvider();

                provider.UpdateJourney(journey);
                responseBody = objMapper.writeValueAsString(journey);
                response.setBody(responseBody);
                response.setStatusCode(200);
                LOG.debug("Updated journey " + journey.getTrackingItemName() + " date " + journey.getTrackingDate());
            }
            catch (Exception e){
                LOG.error("Error processing request", e);
            }

        } catch (JsonParseException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            LOG.error("Unable to unmarshal JSON for updating a journey", ex);
        }
        return response;
    }
}
