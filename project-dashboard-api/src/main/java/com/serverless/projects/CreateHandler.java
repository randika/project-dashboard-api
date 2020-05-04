package com.serverless.projects;

import java.util.Collections;
import java.util.Map;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.model.Project;

public class CreateHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse>{

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            // create the Project object for post
            Project project = new Project();
            project.setProjectId(input.get("projectId").toString());
            project.setProjectName(input.get("projectName").toString());
            project.setTeamId(input.get("teamId").toString());
            project.setTeamName(input.get("teamName").toString());
            project.save(project);

            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(201)
                    .setObjectBody(project)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();

        } catch (Exception ex) {
//          logger.error("Error in saving the project: " + ex);

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
