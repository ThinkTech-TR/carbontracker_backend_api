package com.thinktech;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinktech.model.CarbonFootprint;
import com.thinktech.model.CarbonFootprintResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CalculateInitialCarbonHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = LogManager.getLogger(CalculateInitialCarbonHandler.class);

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		LOG.info("received request");

		CarbonFootprint averageFootprint = new CarbonFootprint(2400,
				1700, 250, 250, 1100, 3200, 1700,
				2550);

		CarbonFootprint userFootprint = new CarbonFootprint(2200,
				1400, 250, 250, 1100, 2200, 1700,
				2550);

		CarbonFootprintResult result = new CarbonFootprintResult(userFootprint, averageFootprint);

		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		response.setStatusCode(200);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String responseBody = objectMapper.writeValueAsString(result);
			response.setBody(responseBody);
		}
		catch (JsonProcessingException e) {
           LOG.error("Unable to convert result to JSON", e);
		}
		return response;
	}
}
