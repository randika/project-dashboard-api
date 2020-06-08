package com.serverless.model;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.serverless.config.DynamoDBAdapter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DynamoDBTable(tableName = "PLACEHOLDER_PRODUCTS_TABLE_NAME")
public class MetricGithub {
   private Logger logger = Logger.getLogger(this.getClass());


    // get the table name from env. var. set in serverless.yml
    private static final String DYNAMODB_TABLE_METRICS = System.getenv("DYNAMODB_TABLE_METRICS");

    private static DynamoDBAdapter db_adapter;
    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;

    private String metricId;
    private String metricType;
    private Date createdAt;
    private String username;
    private String branch;
    private String projectName;
    private String teamId;
    private String appId;

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


    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getBranch() {
        return this.branch;
    }
    public void setBranch(String branch) {
        this.branch = branch;
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

    @DynamoDBRangeKey(attributeName = "createdAt")
    public Date getCreatedAt(){
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt){
        this.createdAt = createdAt;
    }
    
    /**
	 * get metrics for a given project
	 * @param projectName
	 * @return
	 * @throws IOException
	 */
	public List<MetricGithub> getMetricListByProjectAndType(String projectName, String metricType) throws IOException {
		List<MetricGithub> metricsGithub = Collections.emptyList();
		
        HashMap<String, AttributeValue> av = new HashMap<String, AttributeValue>();
        av.put(":v1", new AttributeValue().withS(projectName));
        av.put(":v2", new AttributeValue().withS(metricType));
        DynamoDBQueryExpression<MetricGithub> queryExp = new DynamoDBQueryExpression<MetricGithub>()
        		.withIndexName("metricTypeIndex").withKeyConditionExpression("metricType = :v2")
                .withFilterExpression("projectName = :v1")
                .withExpressionAttributeValues(av)
                .withConsistentRead(false);

        PaginatedQueryList<MetricGithub> result = this.mapper.query(MetricGithub.class, queryExp);
        if (result.size() > 0) {
        	metricsGithub = result;
          logger.info("Metrics for project"+ projectName + ": " + metricsGithub);
        } else {
          logger.info("Metrics for the given project("+ projectName +"not Found.");
        }
        return metricsGithub;
    }

    public MetricGithub() {
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
        return String.format("Metric [metricId=%s, username=%s, branch=%s]", this.metricId, this.metricType, this.username, this.branch);
    }

    public void save(MetricGithub metric) throws IOException {
        logger.info("Metric - save(): " + metric.toString());
        this.mapper.save(metric);
    }

    /**
     * get the list of metrics - github
     * @return
     * @throws IOException
     */
    public List<MetricGithub> list() throws IOException {
        DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
        List<MetricGithub> results = this.mapper.scan(MetricGithub.class, scanExp);
        return results;

    }

    public List<MetricGithub> listByMetricsType(String metricType) throws IOException {


        Map<String, String> expressionAttributesNames = new HashMap<>();
        expressionAttributesNames.put("#metricType", "metricType");

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":metricType", new AttributeValue().withS(metricType));

        DynamoDBQueryExpression<MetricGithub> dynamoDBQueryExpression = new DynamoDBQueryExpression<MetricGithub>()
                .withIndexName("metricTypeIndex").withKeyConditionExpression("#metricType = :metricType")
                .withExpressionAttributeNames(expressionAttributesNames)
                .withExpressionAttributeValues(expressionAttributeValues).withScanIndexForward(true)
                .withConsistentRead(false);

        List<MetricGithub> metrics = mapper.query(MetricGithub.class, dynamoDBQueryExpression);
        return metrics;
    }
}
