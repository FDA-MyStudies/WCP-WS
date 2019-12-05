package com.hphc.mystudies.bean;

public class ParticipantPropertyBean {
	private String propertyId = "", propertyType = "", propertyDataFormat = "";
	private boolean shouldRefresh = false;
	private String dataSource = "", status = "", externalPropertyId = "", dateOfEntryId = "";

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getPropertyDataFormat() {
		return propertyDataFormat;
	}

	public void setPropertyDataFormat(String propertyDataFormat) {
		this.propertyDataFormat = propertyDataFormat;
	}

	public boolean isShouldRefresh() {
		return shouldRefresh;
	}

	public void setShouldRefresh(boolean shouldRefresh) {
		this.shouldRefresh = shouldRefresh;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExternalPropertyId() {
		return externalPropertyId;
	}

	public void setExternalPropertyId(String externalPropertyId) {
		this.externalPropertyId = externalPropertyId;
	}

	public String getDateOfEntryId() {
		return dateOfEntryId;
	}

	public void setDateOfEntryId(String dateOfEntryId) {
		this.dateOfEntryId = dateOfEntryId;
	}
}
