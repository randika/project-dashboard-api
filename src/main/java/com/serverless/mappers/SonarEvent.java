package com.serverless.mappers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SonarEvent {
    private String serverUrl;
    private Project project;
    private QualityGate qualityGate;



    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public QualityGate getQualityGate() {
        return qualityGate;
    }

    public void setQualityGate(QualityGate qualityGate) {
        this.qualityGate = qualityGate;
    }



    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Project {

        private String name;
        private String key;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QualityGate{
        private String status;


        @JsonProperty("conditions")
        private List<Condition> conditions = null;


        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @JsonProperty("conditions")
        public List<Condition> getConditions() {
            return conditions;
        }

        @JsonProperty("conditions")
        public void setConditions(List<Condition> conditions) {
            this.conditions = conditions;
        }

        @Override
        public String toString() {
            return String.format("SonarEvent [status=%s, conditions=%s]", this.status, this.conditions);
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Condition{
            private String metric;
            private String operator;
            private String value;
            private String status;
            private String errorThreshold;

            public String getMetric() {
                return metric;
            }

            public void setMetric(String metric) {
                this.metric = metric;
            }

            public String getOperator() {
                return operator;
            }

            public void setOperator(String operator) {
                this.operator = operator;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getErrorThreshold() {
                return errorThreshold;
            }

            public void setErrorThreshold(String errorThreshold) {
                this.errorThreshold = errorThreshold;
            }
            @Override
            public String toString(){
                return String.format("Condition [metric=%s]", this.metric);
            }
        }
    }
}
