package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.mappers.GithubCreateEvent;
import com.serverless.mappers.GithubPushEvent;
import com.serverless.model.MetricGithub;
import org.apache.log4j.Logger;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

public class GithubMetricsCreateHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        try {

            long unixTimestamp = Instant.now().getEpochSecond();


            Map<String, Object> headers = (Map<String, Object>) input.get("headers");
            String githubEventName = (String) headers.get("X-GitHub-Event");

            logger.info(">>>>>>>>>>>>> Add github metric for event: " + githubEventName);

            String body = (String) input.get("body");
            MetricGithub metric = new MetricGithub();

            // Using separate object mappers may seems like duplicate codes to you, but chances are
            // github payload contracts may change, so isolation will minimize the code changes in future.
            // hence multiple object mappers used.


            if(githubEventName.equalsIgnoreCase("push")){

                GithubPushEvent githubPushEvent = OBJECT_MAPPER.readValue(body, GithubPushEvent.class);
                GithubPushEvent.Repository repositoryData = githubPushEvent.getRepository();

                String branch = githubPushEvent.getRef();
                String githubUser = repositoryData.getOwner().getName();
                String repositoryName = repositoryData.getName();

                metric.setMetricType("github.push.created"); // Triggered on a push to a repository branch or tag.
                metric.setBranch(branch);
                metric.setUsername(githubUser);
                metric.setProjectName(repositoryName);
                metric.setAppId("appId1");
                metric.setTeamId("team1");
                metric.setCreatedAt(unixTimestamp);

            }else if(githubEventName.equalsIgnoreCase("create")){
                GithubCreateEvent githubCreateEvent = OBJECT_MAPPER.readValue(body, GithubCreateEvent.class);
                GithubCreateEvent.Repository repositoryData = githubCreateEvent.getRepository();
                GithubCreateEvent.Sender senderData = githubCreateEvent.getSender();


                String branch = githubCreateEvent.getRef();
                String githubUser = senderData.getLogin();
                String repositoryName = repositoryData.getName();

                metric.setMetricType("github.branch.created"); // Triggered on created branch or tag.
                metric.setBranch(branch);
                metric.setUsername(githubUser);
                metric.setProjectName(repositoryName);
                metric.setAppId("appId1");
                metric.setTeamId("team1");
                metric.setCreatedAt(unixTimestamp);

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
