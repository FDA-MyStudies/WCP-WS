package com.studymetadata.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Mohan
 *
 */
@Entity
@Table(name="notification")
public class NotificationDto implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="notification_id")
	private Integer notificationId;
	
	@Column(name = "study_id")
	private Integer studyId;
	
	@Column(name = "notification_text")
	private String notificationText;
	
	@Column(name = "schedule_date")
	private String scheduleDate;
	
	@Column(name = "schedule_time")
	private String scheduleTime;
	
	@Column(name = "notification_action", length = 1)
	private boolean notificationAction;
	
	@Column(name="notification_sent", length = 1)
	private boolean notificationSent = false;

	@Column(name = "notification_type")
	private String notificationType;
	
	@Column(name = "notification_sent_date_time")
	private String notificationSentDateTime;
	
	@Column(name="notification_schedule_type")
	private String notificationScheduleType;
	
	@Column(name="notification_done", length = 1)
	private boolean notificationDone = false;
	
	@Column(name="notification_status", length = 1)
	private boolean notificationStatus = false;
	
	@Column(name = "study_version")
	private Integer studyVersion=1;

	public Integer getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Integer notificationId) {
		this.notificationId = notificationId;
	}

	public Integer getStudyId() {
		return studyId;
	}

	public void setStudyId(Integer studyId) {
		this.studyId = studyId;
	}

	public String getNotificationText() {
		return notificationText;
	}

	public void setNotificationText(String notificationText) {
		this.notificationText = notificationText;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public String getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public boolean isNotificationAction() {
		return notificationAction;
	}

	public void setNotificationAction(boolean notificationAction) {
		this.notificationAction = notificationAction;
	}

	public boolean isNotificationSent() {
		return notificationSent;
	}

	public void setNotificationSent(boolean notificationSent) {
		this.notificationSent = notificationSent;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getNotificationSentDateTime() {
		return notificationSentDateTime;
	}

	public void setNotificationSentDateTime(String notificationSentDateTime) {
		this.notificationSentDateTime = notificationSentDateTime;
	}

	public String getNotificationScheduleType() {
		return notificationScheduleType;
	}

	public void setNotificationScheduleType(String notificationScheduleType) {
		this.notificationScheduleType = notificationScheduleType;
	}

	public boolean isNotificationDone() {
		return notificationDone;
	}

	public void setNotificationDone(boolean notificationDone) {
		this.notificationDone = notificationDone;
	}

	public boolean isNotificationStatus() {
		return notificationStatus;
	}

	public void setNotificationStatus(boolean notificationStatus) {
		this.notificationStatus = notificationStatus;
	}

	public Integer getStudyVersion() {
		return studyVersion;
	}

	public void setStudyVersion(Integer studyVersion) {
		this.studyVersion = studyVersion;
	}
	
}
