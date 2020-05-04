package com.serverless.projects;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.model.Project;

public class GetListHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse>{
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
    	try {
            List<Project> projects = new Project().list();
            return ApiGatewayResponse.builder()
        				.setStatusCode(200)
        				.setObjectBody(projects)
        				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
        				.build();
        } catch (Exception ex) {
      			Response responseBody = new Response("Error in fetching projects: ", input);
      			return ApiGatewayResponse.builder()
      					.setStatusCode(500)
      					.setObjectBody(responseBody)
      					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
      					.build();
        }
    }
}
