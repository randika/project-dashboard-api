package com.serverless.projects;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.model.Project;

public class DeleteHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse>{
	private Logger logger = Logger.getLogger(this.getClass());

	@Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            String projectId = pathParameters.get("id");

            Boolean success = new Project().delete(projectId);
            
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Access-Control-Allow-Origin", "*");
            headers.put("Access-Control-Allow-Headers", "*");
            headers.put("Access-Control-Allow-Methods", "OPTIONS,POST,GET,DELETE");

            if (success) {
              return ApiGatewayResponse.builder()
          				.setStatusCode(204)
          				.setHeaders(headers)
          				.build();
            } else {
              return ApiGatewayResponse.builder()
          				.setStatusCode(404)
          				.setObjectBody("Project with id: '" + projectId + "' not found.")
          				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
          				.build();
            }

        } catch (Exception ex) {
            logger.error("E002: Error in deleting the project: " + ex);

        	Response responseBody = new Response("Error in deleting the project: ", input);
            
        	return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}
