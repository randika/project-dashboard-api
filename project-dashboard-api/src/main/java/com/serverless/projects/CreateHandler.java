package com.serverless.projects;

import java.util.Collections;
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
    
    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            // create the Project object for post
//            Project project = new Project();
//            project.setProjectId(input.get("projectId").toString());
//            project.setProjectName(input.get("projectName").toString());
//            project.setTeamId(input.get("teamId").toString());
//            project.setTeamName(input.get("teamName").toString());
//            project.save(project);

            // get the 'body' from input
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));

            // create the Project object
            Project project = new Project();
            project.setProjectId(body.get("projectId").asText());
            project.setProjectName(body.get("projectName").asText());
            project.setTeamId(body.get("teamId").asText());
            project.setTeamName(body.get("teamName").asText());
            project.save(project);

            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(201)
                    .setObjectBody(project)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();

        } catch (Exception ex) {

            logger.error("E002: Error in saving the project: " + ex);

            // send the error response back
            Response responseBody = new Response("Error in saving the project: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}
