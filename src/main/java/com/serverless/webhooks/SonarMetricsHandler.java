package com.serverless.webhooks;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutRequest;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.mappers.SonarEvent;
import com.serverless.model.MetricSonar;
import org.apache.log4j.Logger;

import java.time.Instant;
import java.util.*;

public class SonarMetricsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            String body = (String) input.get("body");
            logger.info("saving metric: " + body);




            SonarEvent sonarEvent = OBJECT_MAPPER.readValue(body, SonarEvent.class);

            SonarEvent.Project sonarProject = sonarEvent.getProject();
            SonarEvent.QualityGate sonarQualityGate = sonarEvent.getQualityGate();
            List<SonarEvent.QualityGate.Condition> sonarQualityGateConditions = sonarQualityGate.getConditions();

            String serverUrl = sonarEvent.getServerUrl();
            String projectName = sonarProject.getName();
            String projectKey = sonarProject.getKey();
            String qualityGateStatus = sonarQualityGate.getStatus();

//            Map<String, List<WriteRequest>> writeRequestItems = new HashMap<String, List<WriteRequest>>();
//
//            List<WriteRequest> metricList = new ArrayList<WriteRequest>();


           // TODO this must be changed to DynamoDb batch Put 

            for (SonarEvent.QualityGate.Condition condition : sonarQualityGateConditions) {
                logger.info("Saving metrics for condition: " + condition.toString());
                MetricSonar metric = new MetricSonar();
                String sonarMetricType = condition.getMetric();

                metric.setMetricType("sonar."+sonarMetricType+".created");
                metric.setSonarServerUrl(serverUrl);
                metric.setSonarProjectKey(projectKey);
                metric.setProjectName(projectName);
                metric.setSonarQualityGateStatus(qualityGateStatus);
                metric.setSonarOperator(condition.getOperator());
                metric.setSonarValue(condition.getValue());
                metric.setSonarErrorThreshold(condition.getErrorThreshold());
                metric.setSonarStatus(condition.getStatus());
                metric.setAppId("appId1");
                metric.setTeamId("team1");
                metric.setCreatedAt(Date.from(Instant.now()));
                metric.save(metric);
            }
            Response responseBody = new Response("OK", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(201)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-App", "project-dashboard-api")) // TODO: Get it from config
                    .build();

        } catch (Exception ex) {
            logger.error("Error in saving metric: " + ex);

            // send the error response back
            Response responseBody = new Response("Error in saving sonar metric: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}
