package com.thinktech;

import java.util.Collections;
import java.util.Map;

import com.thinktech.model.CarbonFootprint;
import com.thinktech.model.CarbonFootprintResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("received: {}", input);

		CarbonFootprint averageFootprint = new CarbonFootprint(2.4,
				1.7, 0.25, 0.25, 1.1, 3.2, 1.7,
				2.55);

		CarbonFootprint userFootprint = new CarbonFootprint(2.2,
				1.4, 0.25, 0.25, 1.1, 2.2, 1.7,
				2.55);

		CarbonFootprintResult result = new CarbonFootprintResult(userFootprint, averageFootprint);

		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(result)
				.build();
	}
}
