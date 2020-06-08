package com.serverless.metrics;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.model.MetricGithub;
import com.serverless.model.MetricSonar;
import com.serverless.model.Project;

public class GetProjectMetricsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	   private final Logger logger = Logger.getLogger(this.getClass());

	    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
	    	
	    	Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Access-Control-Allow-Origin", "*");
            headers.put("Access-Control-Allow-Headers", "*");
            headers.put("Access-Control-Allow-Methods", "OPTIONS,POST,GET,DELETE");

	    	try {
	            Map<String,String> queryStringParameters =  (Map<String,String>)input.get("queryStringParameters");

	            String projectId = queryStringParameters.get("projectId");
	            String metricType = queryStringParameters.get("metricType");
	            Project project = new Project().get(projectId);
	            
	            if (project == null) {
	            	logger.info("E004: : Invalid project id" + projectId);

		            Response responseBody = new Response("Invalid project id: ", input);
		  
		            return ApiGatewayResponse.builder()
		                    .setStatusCode(400)
		                    .setObjectBody(responseBody)
		                    .setHeaders(headers)
		                    .build();
	            } else {
	            	String projectName = project.getProjectName();
	            	if(metricType.contains("github")){
		                logger.info(">>>>>>>>>>>>>>>> listByMetricsType:"+ metricType + "and Project: " + projectName);
		                List<MetricGithub> metricsGithubList = new MetricGithub().getMetricListByProjectAndType(projectName, metricType);
		                return ApiGatewayResponse.builder()
		                        .setStatusCode(200)
		                        .setObjectBody(metricsGithubList)
		                        .setHeaders(headers)
		                        .build();
		            }

		            if(metricType.contains("sonar")){
		                logger.info(">>>>>>>>>>>>>>>> listByMetricsType:"+ metricType + "and Project: " + projectName);
		                List<MetricSonar> metricSonarList = new MetricSonar().getMetricListByProjectAndType(projectName, metricType);
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
