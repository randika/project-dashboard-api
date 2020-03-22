# project-dashboard-api

Supported Metrics
=
1. As a project team member, I should be able to see branches or tags created in a gitgub project for a period of time.
2. As a project team member, I should be able to see code pushes created in a gitgub project for a period of time.

 
Metric types
=

| Event  | Description  |
|------|---|
|  `github.branch.created`    | Triggered on created branch or tag. |


```json
{
  "metricId": "230bd70f-1ac5-40f0-8b64-60bb05915c0c",
  "createdAt": "timestamp",
  "metricType": "github.push.created",
  "metadata": {
    "username": "randika",
    "branch": "master",
    "project": "project-dashboard-api"
  }
}
```

| Event  | Description  |
|------|---|
|  `github.push.created`    | Triggered on a push to a repository branch or tag |




```json
{
  "metricId": "230bd70f-1ac5-40f0-8b64-60bb05915c0c",
  "createdAt": "timestamp",
  "metricType": "github.branch.created",
  "metadata": {
    "username": "randika",
    "branch": "master",
    "project": "project-dashboard-api"
  }
}
```

