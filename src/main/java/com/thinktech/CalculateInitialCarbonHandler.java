package com.thinktech;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinktech.model.assemblers.CarbonFootprintAssembler;
import com.thinktech.model.domain.CarbonFootprint;
import com.thinktech.model.domain.Questionnaire;
import com.thinktech.model.assemblers.QuestionnaireAssembler;
import com.thinktech.model.dtos.CarbonFootprintDto;
import com.thinktech.model.dtos.CarbonFootprintResultDto;
import com.thinktech.model.dtos.QuestionnaireDto;
import com.thinktech.service.CalculateInitialCarbon;
import com.thinktech.service.CarbonDataProvider;
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
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// Read request
			QuestionnaireDto questionnaireDto = objectMapper.readValue(requestBody, QuestionnaireDto.class);
			Questionnaire questionnaire = QuestionnaireAssembler.Assemble(questionnaireDto);
			LOG.debug(questionnaire.getHouseType());

			// Calculate response
			CarbonDataProvider carbonDataProvider = new CarbonDataProvider();
			CalculateInitialCarbon calculator = new CalculateInitialCarbon(questionnaire, carbonDataProvider);
			String responseBody = "Unable to calculate initial carbon footprint";
			try {
				CarbonFootprint userFootprint = calculator.Calculate();
				CarbonFootprint averageFootprint = CalculateInitialCarbon.CalculateAverage();

				// Create response
				CarbonFootprintResultDto result = new CarbonFootprintResultDto();
				result.setUserFootprint(CarbonFootprintAssembler.Disassemble(userFootprint));
				result.setAverageFootprint(CarbonFootprintAssembler.Disassemble(averageFootprint));
				responseBody = objectMapper.writeValueAsString(result);
				response.setStatusCode(200);

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
