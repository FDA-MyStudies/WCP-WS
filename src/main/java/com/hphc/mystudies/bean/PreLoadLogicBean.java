package com.hphc.mystudies.bean;

public class PreLoadLogicBean {

    private String value = "";
    private String operator = "";
    private String activityId = "";
    private String activityVersion = "";
    private String destinationStepKey = "";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        if (activityId != null) {
            this.activityId = activityId;
        } else {
            this.activityId = "";
        }
    }

    public String getActivityVersion() {
        return activityVersion;
    }

    public void setActivityVersion(String activityVersion) {
        if (activityVersion != null) {
            this.activityVersion = activityVersion;
        } else {
            this.activityVersion = "";
        }

    }

    public String getDestinationStepKey() {
        return destinationStepKey;
    }

    public void setDestinationStepKey(String destinationStepKey) {
        if (destinationStepKey != null) {
            this.destinationStepKey = destinationStepKey;
        } else {
            this.destinationStepKey = "";
        }
    }
}
