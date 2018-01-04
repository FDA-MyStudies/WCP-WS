package com.studymetadata.bean;

import com.studymetadata.util.StudyMetaDataConstants;

/**
 * 
 * @author Mohan
 * @createdOn Jan 4, 2018 3:14:52 PM
 *
 */
public class StudyActivityResponse {
	
	private String message = StudyMetaDataConstants.FAILURE;
	private ActivitiesBean activity = new ActivitiesBean();
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ActivitiesBean getActivity() {
		return activity;
	}
	public void setActivity(ActivitiesBean activity) {
		this.activity = activity;
	}
}
