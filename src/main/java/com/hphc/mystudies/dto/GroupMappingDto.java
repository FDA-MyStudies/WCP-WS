package com.hphc.mystudies.dto;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "group_mapping")
public class GroupMappingDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "grp_id")
    private Integer grpId;

    @Column(name = "step_id")
    private String stepId = null;

    @Column(name = "status")
    private Boolean status = false;

    @Column(name = "quetstionnaire_stepId")
    private Integer questionnaireStepId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGrpId() {
        return grpId;
    }

    public void setGrpId(Integer grpId) {
        this.grpId = grpId;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getQuestionnaireStepId() {
        return questionnaireStepId;
    }

    public void setQuestionnaireStepId(Integer questionnaireStepId) {
        this.questionnaireStepId = questionnaireStepId;
    }
}

