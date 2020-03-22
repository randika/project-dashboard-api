package com.serverless.model;

public class MetricGithub {
    private String username;
    private String branch;

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

    public MetricGithub(String username, String branch) {
        this.username = username;
        this.branch = branch;
    }
}
