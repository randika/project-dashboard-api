package com.serverless.projects;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.model.Project;

public class UpdateHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse>{
	private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
        	
        	JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            Project project = new Project().get(body.get("projectId").asText());
            project.setProjectName(body.get("projectName").asText());
            project.setTeamId(body.get("teamId").asText());
            project.setTeamName(body.get("teamName").asText());
            project.save(project);
            
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Access-Control-Allow-Origin", "*");
            headers.put("Access-Control-Allow-Headers", "*");
            headers.put("Access-Control-Allow-Methods", "OPTIONS,POST,GET,DELETE,PUT");


            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(201)
                    .setObjectBody(project)
                    .setHeaders(headers)
                    .build();

        } catch (Exception ex) {
        	
            Response responseBody = new Response("Error in updating the project: ", input);
          
            logger.error("E002: Error in updating the project: " + ex);

            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}
