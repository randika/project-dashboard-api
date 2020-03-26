# project-dashboard-api

# Supported Metrics

## Github
1. As a project team member, I should be able to see branches or tags created in a github project for a given period of time.
2. As a project team member, I should be able to see code pushes created in a github project for a given period of time.

## SonarQube
1. Analysis 
# Metric types

## Github:: Branches

| Key  | Value |
|------|---|
| EventType |  `github.branch.created` |
| Event Description | Triggered on created branch or tag. |
| API endpoint | /metrics/github |
| Data Origin | Github |
| Data Fetched by  | Github events |

## Model schema
```json
{
  "metricId": "230bd70f-1ac5-40f0-8b64-60bb05915c0c",
  "createdAt": "timestamp",
  "metricType": "github.push.created",
  "username": "randika",
  "branch": "master",
  "appId": "project-dashboard-api",
  "teamId": "t1",
  "teamName": "Team A"
}
```

## Github:: Push

| Key  | Value |
|------|---|
| EventType |  `github.push.created` |
| Event Description | Triggered on a push to a repository branch or tag|
| API endpoint | /metrics/github |
| Data Origin | Github |
| Data Fetched by  | Github events |

## Model Schema

```json
{
  "metricId": "230bd70f-1ac5-40f0-8b64-60bb05915c0c",
  "createdAt": "timestamp",
  "metricType": "github.branch.created",
  "username": "randika",
  "branch": "master",
  "appId": "project-dashboard-api",
  "teamId": "t1",
  "teamName": "Team A"
}
```

