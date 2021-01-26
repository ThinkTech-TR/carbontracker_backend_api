package com.thinktech;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinktech.model.domain.Questionnaire;
import com.thinktech.model.assemblers.QuestionnaireAssembler;
import com.thinktech.model.dtos.QuestionnaireDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.DriverManager;

public class AddUserCarbonHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger LOG = LogManager.getLogger(CalculateInitialCarbonHandler.class);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        LOG.info("received request");

        String userId = request.getPathParameters().get("userId");
        String requestBody = request.getBody();

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            QuestionnaireDto questionnaireDto = objectMapper.readValue(requestBody, QuestionnaireDto.class);
            Questionnaire questionnaire = QuestionnaireAssembler.Assemble(questionnaireDto);
            LOG.debug("House Type:" + questionnaire.getHouseType());



            String responseBody = "Added new user with id " + userId;
            response.setBody(responseBody);
        }
        catch (JsonProcessingException e) {
            LOG.error("Unable to convert result to JSON", e);
        }
        catch (IOException e) {
            LOG.error("Unable to convert input from JSON", e);
        }
        return response;
    }
}