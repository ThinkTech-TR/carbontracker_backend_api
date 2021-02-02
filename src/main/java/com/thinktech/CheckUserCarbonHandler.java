package com.thinktech;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinktech.service.database.UserDataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.Map;

public class CheckUserCarbonHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger LOG = LogManager.getLogger(CheckUserCarbonHandler.class);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        LOG.info("received request");
        String userId = request.getPathParameters().get("userId");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        ObjectMapper objectMapper = new ObjectMapper();

        // Set default values for response assuming an error
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Credentials", "true");
        response.setHeaders(headers);
        String responseBody = "Unable to check user";
        response.setBody(responseBody);
        response.setStatusCode(500);

        try {
            // Calculate response
            UserDataProvider provider = new UserDataProvider();
            boolean userExists = provider.CheckUserExists(userId);

            // Create response
            if (userExists) {
                responseBody = "true";
            } else {
                responseBody = "false";
            }
            response.setStatusCode(200);

            response.setBody(responseBody);
        } catch (Exception e) {
            LOG.error("Error processing request", e);
        }
        return response;
    }

}