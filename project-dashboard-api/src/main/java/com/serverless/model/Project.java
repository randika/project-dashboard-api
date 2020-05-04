package com.serverless.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.serverless.config.DynamoDBAdapter;

@DynamoDBTable(tableName = "PLACEHOLDER_PRODUCTS_TABLE_NAME")
public class Project {
	private Logger logger = Logger.getLogger(this.getClass());
	private static final String DYNAMODB_TABLE_PROJECTS = System.getenv("DYNAMODB_TABLE_PROJECTS");

	private static DynamoDBAdapter db_adapter;
	private final AmazonDynamoDB client;
	private final DynamoDBMapper mapper;

	private String projectId;
	private String projectName;
	private Long createdAt;
	private String teamId;
	private String teamName;

	@DynamoDBHashKey(attributeName = "projectId")
	public String getProjectId() {
		return this.projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getTeamName() {
		return this.teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamId() {
		return this.teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public Long getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public Project() {
		DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
				.withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(DYNAMODB_TABLE_PROJECTS)).build();
		this.db_adapter = DynamoDBAdapter.getInstance();
		this.client = this.db_adapter.getDbClient();
		this.mapper = this.db_adapter.createDbMapper(mapperConfig);
	}

	public String toString() {
		return String.format("Project [projectId=%s, projectName=%s]", this.projectId, this.projectName);
	}
	
	/**
	 * get a project from the id
	 * @param id
	 * @return
	 * @throws IOException
	 */
	public Project get(String id) throws IOException {
        Project project = null;

        HashMap<String, AttributeValue> av = new HashMap<String, AttributeValue>();
        av.put(":v1", new AttributeValue().withS(id));

        DynamoDBQueryExpression<Project> queryExp = new DynamoDBQueryExpression<Project>()
            .withKeyConditionExpression("projectId = :v1")
            .withExpressionAttributeValues(av);

        PaginatedQueryList<Project> result = this.mapper.query(Project.class, queryExp);
        if (result.size() > 0) {
          project = result.get(0);
          logger.info("Project: " + project.toString());
        } else {
          logger.info("Project Not Found.");
        }
        return project;
    }
	
	/**
	 * delete a project
	 * @param id
	 * @return
	 * @throws IOException
	 */
	public Boolean delete(String id) throws IOException {
		Project project = null;
		project = get(id);
		if (project != null) {
			logger.info("Project - delete(): " + project.toString());
			this.mapper.delete(project);
		} else {
			logger.info("Project - delete function(): project does not exist.");
			return false;
		}
		return true;
	}
	  
	/**
	 * get the list of projects
	 * @return
	 * @throws IOException
	 */
	public List<Project> list() throws IOException {
		DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
		List<Project> results = this.mapper.scan(Project.class, scanExp);
		return results;
	}

	/**
	 * create a new project
	 * @param project
	 * @throws IOException
	 */
	public void save(Project project) throws IOException {
		logger.info("Project - save(): " + project.toString());
		this.mapper.save(project);
	}
}
