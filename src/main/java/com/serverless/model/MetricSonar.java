package com.serverless.model;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.serverless.config.DynamoDBAdapter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@DynamoDBTable(tableName = "PLACEHOLDER_TABLE_NAME")
public class MetricSonar {
    private Logger logger = Logger.getLogger(this.getClass());
    private static final String DYNAMODB_TABLE_METRICS = System.getenv("DYNAMODB_TABLE_METRICS");

    private static DynamoDBAdapter db_adapter;
    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;

    private String metricId;
    private String metricType;
    private Date createdAt;
    private String projectName;
    private String teamId;
    private String appId;

    private String sonarServerUrl;
    private String sonarProjectKey;
    private String sonarQualityGateStatus;
    private String sonarOperator;
    private String sonarValue;
    private String sonarStatus;
    private String sonarErrorThreshold;

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

    public String getProjectName() {
        return this.projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAppId() {
        return this.appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTeamId() {
        return this.teamId;
    }
    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public Date getCreatedAt(){
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt){
        this.createdAt = createdAt;
    }

    public MetricSonar() {
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

    public void save(MetricSonar metric) throws IOException {
        logger.info("Metric - save(): " + metric.toString());
        this.mapper.save(metric);
    }


    public String getSonarServerUrl() {
        return sonarServerUrl;
    }

    public void setSonarServerUrl(String sonarServerUrl) {
        this.sonarServerUrl = sonarServerUrl;
    }

    public String getSonarProjectKey() {
        return sonarProjectKey;
    }

    public void setSonarProjectKey(String sonarProjectKey) {
        this.sonarProjectKey = sonarProjectKey;
    }

    public String getSonarQualityGateStatus() {
        return sonarQualityGateStatus;
    }

    public void setSonarQualityGateStatus(String sonarQualityGateStatus) {
        this.sonarQualityGateStatus = sonarQualityGateStatus;
    }

    public String getSonarOperator() {
        return sonarOperator;
    }

    public void setSonarOperator(String sonarOperator) {
        this.sonarOperator = sonarOperator;
    }

    public String getSonarValue() {
        return sonarValue;
    }

    public void setSonarValue(String sonarValue) {
        this.sonarValue = sonarValue;
    }

    public String getSonarStatus() {
        return sonarStatus;
    }

    public void setSonarStatus(String sonarStatus) {
        this.sonarStatus = sonarStatus;
    }

    public String getSonarErrorThreshold() {
        return sonarErrorThreshold;
    }

    public void setSonarErrorThreshold(String sonarErrorThreshold) {
        this.sonarErrorThreshold = sonarErrorThreshold;
    }

    public List<MetricSonar> listByMetricsType(String metricType) throws IOException {


        Map<String, String> expressionAttributesNames = new HashMap<>();
        expressionAttributesNames.put("#metricType", "metricType");

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":metricType", new AttributeValue().withS(metricType));

        DynamoDBQueryExpression<MetricSonar> dynamoDBQueryExpression = new DynamoDBQueryExpression<MetricSonar>()
                .withIndexName("metricTypeIndex").withKeyConditionExpression("#metricType = :metricType")
                .withExpressionAttributeNames(expressionAttributesNames)
                .withExpressionAttributeValues(expressionAttributeValues).withScanIndexForward(true)
                .withConsistentRead(false);

        List<MetricSonar> metrics = mapper.query(MetricSonar.class, dynamoDBQueryExpression);
        return metrics;
    }
}
