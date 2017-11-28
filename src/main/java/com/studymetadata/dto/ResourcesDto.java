package com.studymetadata.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * 
 * @author Mohan
 *
 */
@Entity
@Table(name = "resources")
@NamedQueries({
	@NamedQuery(name="getResourcesListByStudyId", query=" from ResourcesDto RDTO where RDTO.studyId =:studyId  and RDTO.status=true and RDTO.action=true ORDER BY RDTO.sequenceNo DESC"),
})
public class ResourcesDto implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="study_id")
	private Integer studyId;
	
	@Column(name="title")
	private String title;
	
	@Column(name = "text_or_pdf", length = 1)
	private boolean textOrPdf;
	
	@Column(name="rich_text")
	private String richText;
	
	@Column(name="pdf_url")
	private String pdfUrl;
	
	@Column(name="pdf_name")
	private String pdfName;
	
	@Column(name = "resource_visibility", length = 1)
	private boolean resourceVisibility;
	
	@Column(name="time_period_from_days")
	private Integer timePeriodFromDays;
	
	@Column(name="time_period_to_days")
	private Integer timePeriodToDays;
	
	@Column(name="start_date")
	private String startDate;
	
	@Column(name="end_date")
	private String endDate;
	
	@Column(name="resource_text")
	private String resourceText;
	
	@Column(name = "action", length = 1)
	private boolean action;
	
	@Column(name = "study_protocol", length = 1)
	private boolean studyProtocol;
	
	@Column(name = "status", length = 1)
	private boolean status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_on")
	private String createdOn;
	
	@Column(name = "modified_by")
	private Integer modifiedBy;
	
	@Column(name = "modified_on")
	private String modifiedOn;
	
	@Column(name = "study_version")
	private Integer studyVersion=1;
	
	@Column(name="anchor_date")
	private String anchorDate;
	
	@Column(name = "resource_type", length = 1)
	private boolean resourceType=false;
	
	@Column(name = "custom_study_id")
	private String customStudyId;
	 
	@Column(name = "x_days_sign", length = 1)
	private boolean xDaysSign = false;
	 
	@Column(name = "y_days_sign", length = 1)
	private boolean yDaysSign = false;
	
	@Column(name = "sequence_no")
	private Integer sequenceNo;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStudyId() {
		return studyId;
	}

	public void setStudyId(Integer studyId) {
		this.studyId = studyId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isTextOrPdf() {
		return textOrPdf;
	}

	public void setTextOrPdf(boolean textOrPdf) {
		this.textOrPdf = textOrPdf;
	}

	public String getRichText() {
		return richText;
	}

	public void setRichText(String richText) {
		this.richText = richText;
	}

	public String getPdfUrl() {
		return pdfUrl;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	public String getPdfName() {
		return pdfName;
	}

	public void setPdfName(String pdfName) {
		this.pdfName = pdfName;
	}

	public boolean isResourceVisibility() {
		return resourceVisibility;
	}

	public void setResourceVisibility(boolean resourceVisibility) {
		this.resourceVisibility = resourceVisibility;
	}

	public Integer getTimePeriodFromDays() {
		return timePeriodFromDays;
	}

	public void setTimePeriodFromDays(Integer timePeriodFromDays) {
		this.timePeriodFromDays = timePeriodFromDays;
	}

	public Integer getTimePeriodToDays() {
		return timePeriodToDays;
	}

	public void setTimePeriodToDays(Integer timePeriodToDays) {
		this.timePeriodToDays = timePeriodToDays;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getResourceText() {
		return resourceText;
	}

	public void setResourceText(String resourceText) {
		this.resourceText = resourceText;
	}

	public boolean isAction() {
		return action;
	}

	public void setAction(boolean action) {
		this.action = action;
	}

	public boolean isStudyProtocol() {
		return studyProtocol;
	}

	public void setStudyProtocol(boolean studyProtocol) {
		this.studyProtocol = studyProtocol;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Integer getStudyVersion() {
		return studyVersion;
	}

	public void setStudyVersion(Integer studyVersion) {
		this.studyVersion = studyVersion;
	}

	public String getAnchorDate() {
		return anchorDate;
	}

	public void setAnchorDate(String anchorDate) {
		this.anchorDate = anchorDate;
	}

	public boolean isResourceType() {
		return resourceType;
	}

	public void setResourceType(boolean resourceType) {
		this.resourceType = resourceType;
	}

	public String getCustomStudyId() {
		return customStudyId;
	}

	public void setCustomStudyId(String customStudyId) {
		this.customStudyId = customStudyId;
	}

	public boolean isxDaysSign() {
		return xDaysSign;
	}

	public void setxDaysSign(boolean xDaysSign) {
		this.xDaysSign = xDaysSign;
	}

	public boolean isyDaysSign() {
		return yDaysSign;
	}

	public void setyDaysSign(boolean yDaysSign) {
		this.yDaysSign = yDaysSign;
	}
	
}
