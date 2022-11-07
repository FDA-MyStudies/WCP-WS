package com.hphc.mystudies.bean;

public class PipingBean {

    private String pipingSnippet = "";
    private String sourceQuestionKey = "";
    private String activityId = "";
    private String activityVersion = "";

    public String getPipingSnippet() {
        return pipingSnippet;
    }

    public void setPipingSnippet(String pipingSnippet) {
        if (pipingSnippet != null) {
            this.pipingSnippet = pipingSnippet;
        } else {
            this.pipingSnippet = "";
        }
    }

    public String getSourceQuestionKey() {
        return sourceQuestionKey;
    }

    public void setSourceQuestionKey(String sourceQuestionKey) {
        if (sourceQuestionKey != null) {
            this.sourceQuestionKey = sourceQuestionKey;
        } else {
            this.sourceQuestionKey = "";
        }
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityVersion() {
        return activityVersion;
    }

    public void setActivityVersion(String activityVersion) {
        this.activityVersion = activityVersion;
    }
}
