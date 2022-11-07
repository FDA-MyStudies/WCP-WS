/*
 * Copyright © 2017-2018 Harvard Pilgrim Health Care Institute (HPHCI) and its Contributors.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * Funding Source: Food and Drug Administration ("Funding Agency") effective 18 September 2014 as Contract no.
 * HHSF22320140030I/HHSF22301006T (the "Prime Contract").
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.hphc.mystudies.dto;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Provides questionnaire steps details for study {@link StudyDto}.
 *
 * @author BTC
 */
@Entity
@Table(name = "questionnaires_steps")
public class QuestionnairesStepsDto implements Serializable {

  /** */
  private static final long serialVersionUID = -6626878023643784669L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "step_id")
  private Integer stepId;

  @Column(name = "questionnaires_id")
  private Integer questionnairesId;

  @Column(name = "instruction_form_id")
  private Integer instructionFormId;

  @Column(name = "step_type")
  private String stepType;

  @Column(name = "sequence_no")
  private Integer sequenceNo;

  @Column(name = "step_short_title")
  private String stepShortTitle;

  @Column(name = "skiappable")
  private String skiappable;

  @Column(name = "destination_step")
  private Integer destinationStep;

  @Column(name = "repeatable")
  private String repeatable = "No";

  @Column(name = "repeatable_text")
  private String repeatableText;

  @Column(name = "status")
  private Boolean status;

  @Column(name = "created_on")
  private String createdOn;

  @Column(name = "modified_on")
  private String modifiedOn;

  @Column(name = "created_by")
  private Integer createdBy;

  @Column(name = "modified_by")
  private Integer modifiedBy;

  @Column(name = "study_version")
  private Integer studyVersion = 1;

  @Column(name = "active")
  private Boolean active;

  @Column(name = "default_visibility")
  private Boolean defaultVisibility;

  @Column(name = "destination_true_as_group")
  private Integer destinationTrueAsGroup;

  @Column(name = "is_piping")
  private Boolean isPiping;

  @Column(name = "piping_snippet")
  private String pipingSnippet;

  @Column(name = "piping_source_question_key")
  private Integer pipingSourceQuestionKey;

  @Column(name = "is_different_survey")
  private Boolean differentSurvey;

  @Column(name = "piping_survey_Id")
  private Integer pipingSurveyId;

  @Column(name = "is_different_survey_pre_load")
  private Boolean differentSurveyPreLoad;

  @Column(name = "pre_load_survey_Id")
  private Integer preLoadSurveyId;

  @Column(name = "stepOrGroup")
  private String stepOrGroup;

  @Column(name = "stepOrGroupPostLoad")
  private String stepOrGroupPostLoad;

  @Transient private String destinationStepType;

  public Integer getStepId() {
    return stepId;
  }

  public void setStepId(Integer stepId) {
    this.stepId = stepId;
  }

  public Integer getQuestionnairesId() {
    return questionnairesId;
  }

  public void setQuestionnairesId(Integer questionnairesId) {
    this.questionnairesId = questionnairesId;
  }

  public Integer getInstructionFormId() {
    return instructionFormId;
  }

  public void setInstructionFormId(Integer instructionFormId) {
    this.instructionFormId = instructionFormId;
  }

  public String getStepType() {
    return stepType;
  }

  public void setStepType(String stepType) {
    this.stepType = stepType;
  }

  public Integer getSequenceNo() {
    return sequenceNo;
  }

  public void setSequenceNo(Integer sequenceNo) {
    this.sequenceNo = sequenceNo;
  }

  public String getStepShortTitle() {
    return stepShortTitle;
  }

  public void setStepShortTitle(String stepShortTitle) {
    this.stepShortTitle = stepShortTitle;
  }

  public String getSkiappable() {
    return skiappable;
  }

  public void setSkiappable(String skiappable) {
    this.skiappable = skiappable;
  }

  public Integer getDestinationStep() {
    return destinationStep;
  }

  public void setDestinationStep(Integer destinationStep) {
    this.destinationStep = destinationStep;
  }

  public String getRepeatable() {
    return repeatable;
  }

  public void setRepeatable(String repeatable) {
    this.repeatable = repeatable;
  }

  public String getRepeatableText() {
    return repeatableText;
  }

  public void setRepeatableText(String repeatableText) {
    this.repeatableText = repeatableText;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public String getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(String createdOn) {
    this.createdOn = createdOn;
  }

  public String getModifiedOn() {
    return modifiedOn;
  }

  public void setModifiedOn(String modifiedOn) {
    this.modifiedOn = modifiedOn;
  }

  public Integer getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Integer createdBy) {
    this.createdBy = createdBy;
  }

  public Integer getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(Integer modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public Integer getStudyVersion() {
    return studyVersion;
  }

  public void setStudyVersion(Integer studyVersion) {
    this.studyVersion = studyVersion;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getDestinationStepType() {
    return destinationStepType;
  }

  public void setDestinationStepType(String destinationStepType) {
    this.destinationStepType = destinationStepType;
  }

  public Boolean getDefaultVisibility() {
    return defaultVisibility;
  }

  public void setDefaultVisibility(Boolean defaultVisibility) {
    this.defaultVisibility = defaultVisibility;
  }

  public Integer getDestinationTrueAsGroup() {
    return destinationTrueAsGroup;
  }

  public void setDestinationTrueAsGroup(Integer destinationTrueAsGroup) {
    this.destinationTrueAsGroup = destinationTrueAsGroup;
  }

  public Boolean getIsPiping() {
    return isPiping;
  }

  public void setIsPiping(Boolean isPiping) {
    this.isPiping = isPiping;
  }

  public String getPipingSnippet() {
    return pipingSnippet;
  }

  public void setPipingSnippet(String pipingSnippet) {
    this.pipingSnippet = pipingSnippet;
  }

  public Integer getPipingSourceQuestionKey() {
    return pipingSourceQuestionKey;
  }

  public void setPipingSourceQuestionKey(Integer pipingSourceQuestionKey) {
    this.pipingSourceQuestionKey = pipingSourceQuestionKey;
  }

  public Boolean getPiping() {
    return isPiping;
  }

  public void setPiping(Boolean piping) {
    isPiping = piping;
  }

  public Boolean getDifferentSurvey() {
    return differentSurvey;
  }

  public void setDifferentSurvey(Boolean differentSurvey) {
    this.differentSurvey = differentSurvey;
  }

  public Integer getPipingSurveyId() {
    return pipingSurveyId;
  }

  public void setPipingSurveyId(Integer pipingSurveyId) {
    this.pipingSurveyId = pipingSurveyId;
  }

  public Boolean getDifferentSurveyPreLoad() {
    return differentSurveyPreLoad;
  }

  public void setDifferentSurveyPreLoad(Boolean differentSurveyPreLoad) {
    this.differentSurveyPreLoad = differentSurveyPreLoad;
  }

  public Integer getPreLoadSurveyId() {
    return preLoadSurveyId;
  }

  public void setPreLoadSurveyId(Integer preLoadSurveyId) {
    this.preLoadSurveyId = preLoadSurveyId;
  }

  public String getStepOrGroup() {
    return stepOrGroup;
  }

  public void setStepOrGroup(String stepOrGroup) {
    this.stepOrGroup = stepOrGroup;
  }

  public String getStepOrGroupPostLoad() {
    return stepOrGroupPostLoad;
  }

  public void setStepOrGroupPostLoad(String stepOrGroupPostLoad) {
    this.stepOrGroupPostLoad = stepOrGroupPostLoad;
  }
}
