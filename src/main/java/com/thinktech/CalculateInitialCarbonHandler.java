package com.thinktech;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinktech.model.assemblers.CarbonItemsWithAverageAssembler;
import com.thinktech.model.domain.CarbonItemWithAverage;
import com.thinktech.model.domain.Questionnaire;
import com.thinktech.model.assemblers.QuestionnaireAssembler;
import com.thinktech.model.dtos.CarbonItemWithAverageDto;
import com.thinktech.model.dtos.QuestionnaireDto;
import com.thinktech.service.CalculateInitialCarbon;
import com.thinktech.service.database.InitialCarbonDataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculateInitialCarbonHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = LogManager.getLogger(CalculateInitialCarbonHandler.class);

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		LOG.info("received request");
		String requestBody = request.getBody();

		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// Read request
			QuestionnaireDto questionnaireDto = objectMapper.readValue(requestBody, QuestionnaireDto.class);
			Questionnaire questionnaire = QuestionnaireAssembler.Assemble(questionnaireDto);
			LOG.debug(questionnaire.getHouseType());

			// Calculate response
			InitialCarbonDataProvider dataProvider = new InitialCarbonDataProvider();
			CalculateInitialCarbon calculator = new CalculateInitialCarbon(questionnaire, dataProvider);
			String responseBody = "Unable to calculate initial carbon footprint";
			try {
				List<CarbonItemWithAverage> carbonItems = calculator.Calculate();

				// Create response
				List<CarbonItemWithAverageDto> result = CarbonItemsWithAverageAssembler.Disassemble(carbonItems);
				responseBody = objectMapper.writeValueAsString(result);
				response.setStatusCode(200);
				Map<String, String > headers = new HashMap<>();
				headers.put("Access-Control-Allow-Origin", "*");
				headers.put("Access-Control-Allow-Credentials", "true");
				response.setHeaders(headers);

			} catch (Exception e){
				LOG.error("Error calculating user initial carbon footprint", e);
				response.setStatusCode(500);
			}

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
