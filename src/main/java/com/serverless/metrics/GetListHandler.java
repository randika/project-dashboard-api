package com.serverless.metrics;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.model.MetricGithub;
import com.serverless.model.MetricSonar;
import com.serverless.model.Project;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetListHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());



    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "*");
        headers.put("Access-Control-Allow-Methods", "OPTIONS,POST,GET");

        try {
            Map<String,String> queryStringParameters =  (Map<String,String>)input.get("queryStringParameters");

            String projectId = queryStringParameters.get("projectId");
            String metricType = queryStringParameters.get("metricType");





            if(metricType.contains("github")){
                logger.info(">>>>>>>>>>>>>>>> listByMetricsType:"+ metricType);
                List<MetricGithub> metricsGithubList = new MetricGithub().listByMetricsType(metricType);
                return ApiGatewayResponse.builder()
                        .setStatusCode(200)
                        .setObjectBody(metricsGithubList)
                        .setHeaders(headers)
                        .build();
            }

            if(metricType.contains("sonar")){
                logger.info(">>>>>>>>>>>>>>>> listByMetricsType:"+ metricType);
                List<MetricSonar> metricSonarList = new MetricSonar().listByMetricsType(metricType);

                return ApiGatewayResponse.builder()
                        .setStatusCode(200)
                        .setObjectBody(metricSonarList)
                        .setHeaders(headers)
                        .build();
            }else {

                Response responseBody = new Response("invalid metric", input);

                return ApiGatewayResponse.builder()
                        .setStatusCode(200)
                        .setObjectBody(responseBody)
                        .setHeaders(headers)
                        .build();
            }

        } catch (Exception ex) {

            logger.error("E004: Error in fetching the metrics list: " + ex);

            Response responseBody = new Response("Error in fetching metrics list: ", input);

            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(headers)
                    .build();
        }
    }
}
