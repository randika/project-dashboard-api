package com.serverless;

import java.util.Collections;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.model.Project;

public class GetProjectHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse>{
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
    	try {
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            String projectId = pathParameters.get("id");

            Project project = new Project().get(projectId);

            if (project != null) {
              return ApiGatewayResponse.builder()
          				.setStatusCode(200)
          				.setObjectBody(project.getProjectId())
          				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
          				.build();
            } else {
              return ApiGatewayResponse.builder()
          				.setStatusCode(404)
                  .setObjectBody("Project: '" + projectId + "' not found.")
          				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
          				.build();
            }
        } catch (Exception ex) {
      			Response responseBody = new Response("Error in fetching the project: ", input);
      			return ApiGatewayResponse.builder()
      					.setStatusCode(500)
      					.setObjectBody(responseBody)
      					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
      					.build();
        }
    }
}
