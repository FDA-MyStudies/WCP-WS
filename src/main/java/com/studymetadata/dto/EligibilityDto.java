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
 * Provides eligibility details for study {@link StudyDto}.
 * 
 * @author BTC
 * @createdOn Jan 4, 2018 3:27:36 PM
 *
 */
@Entity
@Table(name = "eligibility")
@NamedQueries({

@NamedQuery(name = "eligibilityDtoByStudyId", query = "from EligibilityDto EDTO"
		+ " where EDTO.studyId =:studyId "), })
public class EligibilityDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4520158278072115802L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "study_id")
	private Integer studyId;

	@Column(name = "eligibility_mechanism")
	private Integer eligibilityMechanism;

	@Column(name = "instructional_text")
	private String instructionalText;

	@Column(name = "failure_outcome_text")
	private String failureOutcomeText;

	@Column(name = "study_version")
	private Integer studyVersion = 1;

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

	public Integer getEligibilityMechanism() {
		return eligibilityMechanism;
	}

	public void setEligibilityMechanism(Integer eligibilityMechanism) {
		this.eligibilityMechanism = eligibilityMechanism;
	}

	public String getInstructionalText() {
		return instructionalText;
	}

	public void setInstructionalText(String instructionalText) {
		this.instructionalText = instructionalText;
	}

	public String getFailureOutcomeText() {
		return failureOutcomeText;
	}

	public void setFailureOutcomeText(String failureOutcomeText) {
		this.failureOutcomeText = failureOutcomeText;
	}

	public Integer getStudyVersion() {
		return studyVersion;
	}

	public void setStudyVersion(Integer studyVersion) {
		this.studyVersion = studyVersion;
	}

}
