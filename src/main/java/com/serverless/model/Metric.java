package com.serverless.model;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.serverless.config.DynamoDBAdapter;
//import org.apache.log4j.Logger;

import java.io.IOException;

@DynamoDBTable(tableName = "PLACEHOLDER_PRODUCTS_TABLE_NAME")
public class Metric {
   // private Logger logger = Logger.getLogger(this.getClass());


    // get the table name from env. var. set in serverless.yml
    private static final String DYNAMODB_TABLE_METRICS = System.getenv("DYNAMODB_TABLE_METRICS");

    private static DynamoDBAdapter db_adapter;
    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;


    private String metricId;
    private String metricType;

    @DynamoDBHashKey(attributeName = "metricId")
    @DynamoDBAutoGeneratedKey
    public String getMetricId() {
        return this.metricId;
    }
    public void setMetricId(String metricId) {
        this.metricId = metricId;
    }

    public String getMetricType() {
        return this.metricType;
    }
    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public Metric() {
        // build the mapper config
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(DYNAMODB_TABLE_METRICS))
                .build();
        // get the db adapter
        this.db_adapter = DynamoDBAdapter.getInstance();
        this.client = this.db_adapter.getDbClient();
        // create the mapper with config
        this.mapper = this.db_adapter.createDbMapper(mapperConfig);
    }

    public String toString() {
        return String.format("Metric [metricId=%s, metricType=%s]", this.metricId, this.metricType);
    }

    public void save(Metric metric) throws IOException {
        //logger.info("Metric - save(): " + metric.toString());
        this.mapper.save(metric);
    }
}
