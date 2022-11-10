package com.hphc.mystudies.dto;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "grouppp")
public class GroupsDto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "created_on")
    private String createdOn = "";

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_on")
    private String modifiedOn;

    @Column(name = "is_published")
    private Integer isPublished=0;

    @Transient
    private String userId;

    @Transient private String buttonText;

    @Transient private String type;


    @Column(name = "action", length = 1)
    private Boolean action;

    @Transient
    private String userName;

    @Column(name = "study_id")
    private Integer studyId;

    @Column(name="questionnaire_id")
    private Integer questionnaireId;

    @Column(name = "destination_true_as_group")
    private Integer destinationTrueAsGroup;

    @Column(name = "default_visibility")
    private Boolean defaultVisibility;

    @Column(name = "step_or_group")
    private String stepOrGroup;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
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

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Integer getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Integer isPublished) {
        this.isPublished = isPublished;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getAction() {
        return action;
    }

    public void setAction(Boolean action) {
        this.action = action;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getStudyId() {
        return studyId;
    }

    public void setStudyId(Integer studyId) {
        this.studyId = studyId;
    }

    public Integer getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Integer questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public Integer getDestinationTrueAsGroup() {
        return destinationTrueAsGroup;
    }

    public void setDestinationTrueAsGroup(Integer destinationTrueAsGroup) {
        this.destinationTrueAsGroup = destinationTrueAsGroup;
    }

    public Boolean getDefaultVisibility() {
        return defaultVisibility;
    }

    public void setDefaultVisibility(Boolean defaultVisibility) {
        this.defaultVisibility = defaultVisibility;
    }

    public String getStepOrGroup() {
        return stepOrGroup;
    }

    public void setStepOrGroup(String stepOrGroup) {
        this.stepOrGroup = stepOrGroup;
    }
}
