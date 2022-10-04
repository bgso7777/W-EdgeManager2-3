package com.inswave.appplatform.log.entity;

public class LogInfomation {

    private String key;
    private String url;
    private Integer logSavedDay=-1;
    private String description;
    private String indexNames;

    public String getIndexNames() {
        return indexNames;
    }

    public void setIndexNames(String indexNames) {
        this.indexNames = indexNames;
    }

    public Integer getLogSavedDay() {
        return logSavedDay;
    }

    public void setLogSavedDay(Integer logSavedDay) {
        this.logSavedDay = logSavedDay;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

}

