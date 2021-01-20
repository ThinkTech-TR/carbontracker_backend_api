package com.thinktech;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinktech.model.CarbonFootprint;
import com.thinktech.model.CarbonFootprintResult;
import com.thinktech.model.Questionnaire;
import com.thinktech.service.CalculateInitialCarbon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.IOException;

public class CalculateInitialCarbonHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = LogManager.getLogger(CalculateInitialCarbonHandler.class);

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		LOG.info("received request");
		String requestBody = request.getBody();

		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		response.setStatusCode(200);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Questionnaire questionnaire = objectMapper.readValue(requestBody, Questionnaire.class);
			CalculateInitialCarbon calculator = new CalculateInitialCarbon(questionnaire);
			CarbonFootprint userFootprint = calculator.Calculate();
			CarbonFootprintResult result = new CarbonFootprintResult(userFootprint, CalculateInitialCarbon.CalculateAverage());
			LOG.debug(questionnaire.getHouseType());
			String responseBody = objectMapper.writeValueAsString(result);
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
