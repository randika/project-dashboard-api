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
            // get the 'body' from input
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));

            // create the Product object for post
            Metric metric = new Metric();

            JsonNode headers = new ObjectMapper().readTree((String) input.get("headers"));

            logger.info("Headers >>>>>>>>>>>> ");
            logger.info(headers.toString());
            logger.info("Headers >>>>>>>>>>>> END ");

            metric.setMetricType("github.branch.created");


            metric.save(metric);

            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(metric)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
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
