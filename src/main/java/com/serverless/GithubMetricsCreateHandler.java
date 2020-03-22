package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.mappers.GithubPushEvent;
import com.serverless.model.Metric;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class GithubMetricsCreateHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        logger.info(">>>>>>>>>>>>> Add github metric: ");

        try {

            Map<String, Object> headers = (Map<String, Object>) input.get("headers");
            String githubEventName = (String) headers.get("X-GitHub-Event");

            String body = (String) input.get("body");
            Metric metric = new Metric();

            if(githubEventName.equalsIgnoreCase("push")){
                metric.setMetricType("github.push.created"); // Triggered on a push to a repository branch or tag.
                GithubPushEvent githubPushEvent = OBJECT_MAPPER.readValue(body, GithubPushEvent.class);

                String branch = githubPushEvent.getRef();
                logger.info("branch = " + branch);

            }else if(githubEventName.equalsIgnoreCase("create")){
                    metric.setMetricType("github.branch.created"); // Represents a created branch or tag.
            }else{
                metric.setMetricType("github.uncategorized"); // catch everything else, un-tracked events
            }
            metric.save(metric);
            logger.info("metricId: " + metric.getMetricId() + " Saved successfully");
            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(metric)
                    .setHeaders(Collections.singletonMap("X-App", "project-dashboard-api")) // TODO: Get it from config
                    .build();

        } catch (Exception ex) {
          logger.error("E001: Error in saving metric: " + ex);

            // send the error response back
            Response responseBody = new Response("E001: Error in saving metric: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-App", "project-dashboard-api")) // TODO: Get it from config
                    .build();
        }
    }
}
