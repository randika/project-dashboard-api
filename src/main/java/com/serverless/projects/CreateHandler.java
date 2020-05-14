package com.serverless.projects;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.model.Project;
import org.apache.log4j.Logger;

public class CreateHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse>{
	private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
           
        	// create the Project object
            Project project = new Project();
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            project.setProjectName(body.get("projectName").asText());
            project.setTeamId(body.get("teamId").asText());
            project.setTeamName(body.get("teamName").asText());
            project.setCreatedAt(Date.from(Instant.now()));
            project.save(project);
            
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Access-Control-Allow-Origin", "*");
            headers.put("Access-Control-Allow-Headers", "*");
            headers.put("Access-Control-Allow-Methods", "OPTIONS,POST,GET,DELETE");

            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(201)
                    .setObjectBody(project)
                    .setHeaders(headers)
                    .build();

        } catch (Exception ex) {

            logger.error("E002: Error in saving the project: " + ex);

            // send the error response back
            Response responseBody = new Response("Error in saving the project: ", input);
            
            // get logs locally: LambdaLogger logger = context.getLogger();
		    // log execution details
		    // logger.log("ENVIRONMENT VARIABLES: " + ex);
            
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}
