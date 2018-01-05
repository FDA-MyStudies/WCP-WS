package com.studymetadata.bean;

import com.studymetadata.util.StudyMetaDataConstants;

/**
 * Provides active task metadata details in response. i.e. status and metadata
 * information of activity {@link ActiveTaskActivityStructureBean}.
 * 
 * @author BTC
 * @createdOn Jan 4, 2018 3:04:23 PM
 *
 */
public class ActiveTaskActivityMetaDataResponse {

	private String message = StudyMetaDataConstants.FAILURE;
	private ActiveTaskActivityStructureBean activity = new ActiveTaskActivityStructureBean();

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ActiveTaskActivityStructureBean getActivity() {
		return activity;
	}

	public void setActivity(ActiveTaskActivityStructureBean activity) {
		this.activity = activity;
	}

}
