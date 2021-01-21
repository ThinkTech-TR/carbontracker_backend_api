package com.thinktech;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinktech.model.*;
import com.thinktech.model.enums.CarbonSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GetAnalysisCarbonHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>
    {
        private static final Logger LOG = LogManager.getLogger(CalculateInitialCarbonHandler.class);

        @Override
        public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
            LOG.info("received request");

            String userId = request.getPathParameters().get("userId");

            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setStatusCode(200);
            ObjectMapper objectMapper = new ObjectMapper();
            response.setBody("Hello");
           /* try {
               String responseBody = objectMapper.writeValueAsString(carbonForDate);
               response.setBody(responseBody);
            }
            catch (JsonProcessingException e) {
                LOG.error("Unable to convert result to JSON", e);
            }*/
            return response;
        }
}
