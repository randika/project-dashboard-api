package com.serverless.metrics;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.model.MetricGithub;
import com.serverless.model.Project;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GetListHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());

    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            Map<String,String> queryStringParameters =  (Map<String,String>)input.get("queryStringParameters");

            String projectId = queryStringParameters.get("projectId");
            String metricType = queryStringParameters.get("metricType");
            List<MetricGithub> metrics = null;

            if(metricType.isEmpty()){
                logger.info(">>>>>>>>>>>>>>>> list:all");
                metrics = new MetricGithub().list();
            }else{
                logger.info(">>>>>>>>>>>>>>>> listByMetricsType:"+ metricType);
                metrics = new MetricGithub().listByMetricsType(metricType);
            }

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(metrics)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        } catch (Exception ex) {

            logger.error("E004: Error in fetching the metrics list: " + ex);

            Response responseBody = new Response("Error in fetching metrics list: ", input);

            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}
