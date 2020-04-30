package com.serverless;

import java.util.Collections;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.model.Project;

public class DeleteProjectHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse>{
	@Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            String projectId = pathParameters.get("id");

            Boolean success = new Project().delete(projectId);

            if (success) {
              return ApiGatewayResponse.builder()
          				.setStatusCode(204)
          				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
          				.build();
            } else {
              return ApiGatewayResponse.builder()
          				.setStatusCode(404)
          				.setObjectBody("Project with id: '" + projectId + "' not found.")
          				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
          				.build();
            }

        } catch (Exception ex) {
            Response responseBody = new Response("Error in deleting the project: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}
