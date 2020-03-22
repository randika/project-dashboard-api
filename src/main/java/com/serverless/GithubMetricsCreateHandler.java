package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.model.Metric;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class GithubMetricsCreateHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        logger.error(">>>>>>>>>>>>> Add github metric: ");

        try {

            Map<String, Object> headers = (Map<String, Object>) input.get("headers");
            String githubEvent = (String) headers.get("X-GitHub-Event");

            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            Metric metric = new Metric();




            if(githubEvent.equalsIgnoreCase("push")){
                metric.setMetricType("github.event.push.created"); // Triggered on a push to a repository branch or tag.
            }else if(githubEvent.equalsIgnoreCase("create")){
                    metric.setMetricType("github.event.branch.created"); // Represents a created branch or tag.
            }else{
                metric.setMetricType("github.event.unknown"); // catch everything else, un-tracked events
            }

            metric.setUsername(body.get("sender.login").asText());

            logger.info(">>>>>>>>" + body.get("sender.login").asText());

            metric.save(metric);

            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(metric)
                    .setHeaders(Collections.singletonMap("X-Client", "project-dashboard-api"))
                    .build();

        } catch (Exception ex) {
          logger.error("Error in saving metric: " + ex);

            // send the error response back
            Response responseBody = new Response("Error in saving metric: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}
