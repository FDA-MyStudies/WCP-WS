package com.studymetadata.bean;

public class StatisticsDataSourceBean {
	private String type = "";
	private String key = "";
	private DashboardActivityBean activity = new DashboardActivityBean();
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public DashboardActivityBean getActivity() {
		return activity;
	}
	public void setActivity(DashboardActivityBean activity) {
		this.activity = activity;
	}
	
}
