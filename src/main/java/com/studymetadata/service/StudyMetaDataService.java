package com.studymetadata.service;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.studymetadata.exception.ErrorCodes;
import com.studymetadata.integration.ActivityMetaDataOrchestration;
import com.studymetadata.integration.AppMetaDataOrchestration;
import com.studymetadata.integration.DashboardMetaDataOrchestration;
import com.studymetadata.integration.StudyMetaDataOrchestration;
import com.studymetadata.util.StudyMetaDataConstants;
import com.studymetadata.util.StudyMetaDataUtil;
import com.studymetadata.bean.ActiveTaskActivityMetaDataResponse;
import com.studymetadata.bean.ActivityResponse;
import com.studymetadata.bean.AppResponse;
import com.studymetadata.bean.AppUpdatesResponse;
import com.studymetadata.bean.ConsentDocumentResponse;
import com.studymetadata.bean.EligibilityConsentResponse;
import com.studymetadata.bean.GatewayInfoResponse;
import com.studymetadata.bean.NotificationsResponse;
import com.studymetadata.bean.QuestionnaireActivityMetaDataResponse;
import com.studymetadata.bean.ResourcesResponse;
import com.studymetadata.bean.StudyDashboardResponse;
import com.studymetadata.bean.StudyInfoResponse;
import com.studymetadata.bean.StudyResponse;
import com.studymetadata.bean.StudyUpdatesResponse;
import com.studymetadata.bean.TermsPolicyResponse;

/**
 * Web Configuration Portal (WCP) service provides access to Gateway, Study and
 * Activities metadata and configurations.
 * 
 * @author BTC
 * @createdOn Jan 4, 2018 3:37:26 PM
 *
 */
@Path("/")
public class StudyMetaDataService {

	private static final Logger LOGGER = Logger
			.getLogger(StudyMetaDataService.class);

	@SuppressWarnings("unchecked")
	HashMap<String, String> propMap = StudyMetaDataUtil.getAppProperties();

	StudyMetaDataOrchestration studyMetaDataOrchestration = new StudyMetaDataOrchestration();
	ActivityMetaDataOrchestration activityMetaDataOrchestration = new ActivityMetaDataOrchestration();
	DashboardMetaDataOrchestration dashboardMetaDataOrchestration = new DashboardMetaDataOrchestration();
	AppMetaDataOrchestration appMetaDataOrchestration = new AppMetaDataOrchestration();

	/**
	 * Get Gateway info and Gateway resources data
	 * 
	 * @author BTC
	 * @param authorization
	 * @param context
	 * @param response
	 * @return {@link Object}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("gatewayInfo")
	public Object gatewayAppResourcesInfo(
			@HeaderParam("Authorization") String authorization,
			@Context ServletContext context,
			@Context HttpServletResponse response) {
		LOGGER.info("INFO: StudyMetaDataService - gatewayAppResourcesInfo() :: Starts");
		GatewayInfoResponse gatewayInfo = new GatewayInfoResponse();
		try {
			gatewayInfo = studyMetaDataOrchestration
					.gatewayAppResourcesInfo(authorization);
			if (!gatewayInfo.getMessage()
					.equals(StudyMetaDataConstants.SUCCESS)) {
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103,
						ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE,
						response);
				return Response.status(Response.Status.NOT_FOUND)
						.entity(StudyMetaDataConstants.NO_RECORD).build();
			}
		} catch (Exception e) {
			LOGGER.error(
					"StudyMetaDataService - gatewayAppResourcesInfo() :: ERROR ",
					e);
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104,
					ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE,
					response);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - gatewayAppResourcesInfo() :: Ends");
		return gatewayInfo;
	}

	/**
	 * Get all the configured studies from the WCP
	 * 
	 * @author BTC
	 * @param authorization
	 * @param context
	 * @param response
	 * @return {@link Object}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("studyList")
	public Object studyList(@HeaderParam("Authorization") String authorization,
			@Context ServletContext context,
			@Context HttpServletResponse response) {
		LOGGER.info("INFO: StudyMetaDataService - studyList() :: Starts");
		StudyResponse studyResponse = new StudyResponse();
		try {
			studyResponse = studyMetaDataOrchestration.studyList(authorization);
			if (!studyResponse.getMessage().equals(
					StudyMetaDataConstants.SUCCESS)) {
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103,
						ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE,
						response);
				return Response.status(Response.Status.NOT_FOUND)
						.entity(StudyMetaDataConstants.NO_RECORD).build();
			}
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataService - studyList() :: ERROR ", e);
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104,
					ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE,
					response);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - studyList() :: Ends");
		return studyResponse;
	}

	/**
	 * Get eligibility and consent info for the provided study identifier
	 * 
	 * @author BTC
	 * @param studyId
	 * @param context
	 * @param response
	 * @return {@link Object}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("eligibilityConsent")
	public Object eligibilityConsentMetadata(
			@QueryParam("studyId") String studyId,
			@Context ServletContext context,
			@Context HttpServletResponse response) {
		LOGGER.info("INFO: StudyMetaDataService - eligibilityConsentMetadata() :: Starts");
		EligibilityConsentResponse eligibilityConsentResponse = new EligibilityConsentResponse();
		Boolean isValidFlag = false;
		try {
			if (StringUtils.isNotEmpty(studyId)) {
				isValidFlag = studyMetaDataOrchestration.isValidStudy(studyId);
				if (!isValidFlag) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
							ErrorCodes.INVALID_INPUT,
							StudyMetaDataConstants.INVALID_STUDY_ID, response);
					return Response.status(Response.Status.NOT_FOUND)
							.entity(StudyMetaDataConstants.INVALID_STUDY_ID)
							.build();
				}

				eligibilityConsentResponse = studyMetaDataOrchestration
						.eligibilityConsentMetadata(studyId);
				if (!eligibilityConsentResponse.getMessage().equals(
						StudyMetaDataConstants.SUCCESS)) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103,
							ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE,
							response);
					return Response.status(Response.Status.NO_CONTENT)
							.entity(StudyMetaDataConstants.NO_RECORD).build();
				}
			} else {
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
						ErrorCodes.INVALID_INPUT,
						StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG,
						response);
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG)
						.build();
			}
		} catch (Exception e) {
			LOGGER.error(
					"StudyMetaDataService - eligibilityConsentMetadata() :: ERROR",
					e);
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104,
					ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE,
					response);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - eligibilityConsentMetadata() :: Ends");
		return eligibilityConsentResponse;
	}

	/**
	 * Get consent document by passing the consent version or the activity id
	 * and activity version for the provided study identifier
	 * 
	 * @author BTC
	 * @param studyId
	 * @param consentVersion
	 * @param activityId
	 * @param activityVersion
	 * @param context
	 * @param response
	 * @return {@link Object}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("consentDocument")
	public Object consentDocument(@QueryParam("studyId") String studyId,
			@QueryParam("consentVersion") String consentVersion,
			@QueryParam("activityId") String activityId,
			@QueryParam("activityVersion") String activityVersion,
			@Context ServletContext context,
			@Context HttpServletResponse response) {
		LOGGER.info("INFO: StudyMetaDataService - resourcesForStudy() :: Starts");
		ConsentDocumentResponse consentDocumentResponse = new ConsentDocumentResponse();
		Boolean isValidFlag = false;
		try {
			if (StringUtils.isNotEmpty(studyId)) {
				isValidFlag = studyMetaDataOrchestration.isValidStudy(studyId);
				if (!isValidFlag) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
							ErrorCodes.INVALID_INPUT,
							StudyMetaDataConstants.INVALID_STUDY_ID, response);
					return Response.status(Response.Status.NOT_FOUND)
							.entity(StudyMetaDataConstants.INVALID_STUDY_ID)
							.build();
				}

				consentDocumentResponse = studyMetaDataOrchestration
						.consentDocument(studyId, consentVersion, activityId,
								activityVersion);
				if (!consentDocumentResponse.getMessage().equals(
						StudyMetaDataConstants.SUCCESS)) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103,
							ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE,
							response);
					return Response.status(Response.Status.NO_CONTENT)
							.entity(StudyMetaDataConstants.NO_RECORD).build();
				}
			} else {
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
						ErrorCodes.INVALID_INPUT,
						StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG,
						response);
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG)
						.build();
			}
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataService - resourcesForStudy() :: ERROR",
					e);
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104,
					ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE,
					response);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - resourcesForStudy() :: Ends");
		return consentDocumentResponse;
	}

	/**
	 * Get resources metadata for the provided study identifier
	 * 
	 * @author BTC
	 * @param studyId
	 * @param context
	 * @param response
	 * @return {@link Object}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("resources")
	public Object resourcesForStudy(@QueryParam("studyId") String studyId,
			@Context ServletContext context,
			@Context HttpServletResponse response) {
		LOGGER.info("INFO: StudyMetaDataService - resourcesForStudy() :: Starts");
		ResourcesResponse resourcesResponse = new ResourcesResponse();
		Boolean isValidFlag = false;
		try {
			if (StringUtils.isNotEmpty(studyId)) {
				isValidFlag = studyMetaDataOrchestration.isValidStudy(studyId);
				if (!isValidFlag) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
							ErrorCodes.INVALID_INPUT,
							StudyMetaDataConstants.INVALID_STUDY_ID, response);
					return Response.status(Response.Status.NOT_FOUND)
							.entity(StudyMetaDataConstants.INVALID_STUDY_ID)
							.build();
				}

				resourcesResponse = studyMetaDataOrchestration
						.resourcesForStudy(studyId);
				if (!resourcesResponse.getMessage().equals(
						StudyMetaDataConstants.SUCCESS)) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103,
							ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE,
							response);
					return Response.status(Response.Status.NO_CONTENT)
							.entity(StudyMetaDataConstants.NO_RECORD).build();
				}
			} else {
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
						ErrorCodes.INVALID_INPUT,
						StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG,
						response);
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG)
						.build();
			}
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataService - resourcesForStudy() :: ERROR",
					e);
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104,
					ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE,
					response);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - resourcesForStudy() :: Ends");
		return resourcesResponse;
	}

	/**
	 * Get study metadata for the provided study identifier
	 * 
	 * @author BTC
	 * @param studyId
	 * @param context
	 * @param response
	 * @return {@link Object}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("studyInfo")
	public Object studyInfo(@QueryParam("studyId") String studyId,
			@Context ServletContext context,
			@Context HttpServletResponse response) {
		LOGGER.info("INFO: StudyMetaDataService - studyInfo() :: Starts");
		StudyInfoResponse studyInfoResponse = new StudyInfoResponse();
		Boolean isValidFlag = false;
		try {
			if (StringUtils.isNotEmpty(studyId)) {
				isValidFlag = studyMetaDataOrchestration.isValidStudy(studyId);
				if (!isValidFlag) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
							ErrorCodes.INVALID_INPUT,
							StudyMetaDataConstants.INVALID_STUDY_ID, response);
					return Response.status(Response.Status.NOT_FOUND)
							.entity(StudyMetaDataConstants.INVALID_STUDY_ID)
							.build();
				}

				studyInfoResponse = studyMetaDataOrchestration
						.studyInfo(studyId);
				if (!studyInfoResponse.getMessage().equals(
						StudyMetaDataConstants.SUCCESS)) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103,
							ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE,
							response);
					return Response.status(Response.Status.NO_CONTENT)
							.entity(StudyMetaDataConstants.NO_RECORD).build();
				}
			} else {
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
						ErrorCodes.INVALID_INPUT,
						StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG,
						response);
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG)
						.build();
			}
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataService - studyInfo() :: ERROR", e);
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104,
					ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE,
					response);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - studyInfo() :: Ends");
		return studyInfoResponse;
	}

	/**
	 * Get all the activities for the provided study identifier
	 * 
	 * @author BTC
	 * @param authorization
	 * @param studyId
	 * @param context
	 * @param response
	 * @return {@link Object}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("activityList")
	public Object studyActivityList(
			@HeaderParam("Authorization") String authorization,
			@QueryParam("studyId") String studyId,
			@Context ServletContext context,
			@Context HttpServletResponse response) {
		LOGGER.info("INFO: StudyMetaDataService - studyActivityList() :: Starts");
		ActivityResponse activityResponse = new ActivityResponse();
		Boolean isValidFlag = false;
		try {
			if (StringUtils.isNotEmpty(studyId)) {
				isValidFlag = studyMetaDataOrchestration.isValidStudy(studyId);
				if (!isValidFlag) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
							ErrorCodes.INVALID_INPUT,
							StudyMetaDataConstants.INVALID_STUDY_ID, response);
					return Response.status(Response.Status.NOT_FOUND)
							.entity(StudyMetaDataConstants.INVALID_STUDY_ID)
							.build();
				}

				activityResponse = activityMetaDataOrchestration
						.studyActivityList(studyId, authorization);
				if (!activityResponse.getMessage().equals(
						StudyMetaDataConstants.SUCCESS)) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103,
							ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE,
							response);
					return Response.status(Response.Status.NO_CONTENT)
							.entity(StudyMetaDataConstants.NO_RECORD).build();
				}
			} else {
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
						ErrorCodes.INVALID_INPUT,
						StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG,
						response);
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG)
						.build();
			}
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataService - studyActivityList() :: ERROR",
					e);
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104,
					ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE,
					response);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - studyActivityList() :: Ends");
		return activityResponse;
	}

	/**
	 * Get the activity metadata for the provided study and activity identifier
	 * 
	 * @author BTC
	 * @param studyId
	 * @param activityId
	 * @param activityVersion
	 * @param context
	 * @param response
	 * @return {@link Object}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("activity")
	public Object studyActivityMetadata(@QueryParam("studyId") String studyId,
			@QueryParam("activityId") String activityId,
			@QueryParam("activityVersion") String activityVersion,
			@Context ServletContext context,
			@Context HttpServletResponse response) {
		LOGGER.info("INFO: StudyMetaDataService - studyActivityMetadata() :: Starts");
		QuestionnaireActivityMetaDataResponse questionnaireActivityMetaDataResponse = new QuestionnaireActivityMetaDataResponse();
		ActiveTaskActivityMetaDataResponse activeTaskActivityMetaDataResponse = new ActiveTaskActivityMetaDataResponse();
		Boolean isValidFlag = false;
		Boolean isActivityTypeQuestionnaire = false;
		try {
			if (StringUtils.isNotEmpty(studyId)
					&& StringUtils.isNotEmpty(activityId)
					&& StringUtils.isNotEmpty(activityVersion)) {
				isValidFlag = studyMetaDataOrchestration.isValidStudy(studyId);
				if (!isValidFlag) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
							ErrorCodes.INVALID_INPUT,
							StudyMetaDataConstants.INVALID_STUDY_ID, response);
					return Response.status(Response.Status.NOT_FOUND)
							.entity(StudyMetaDataConstants.INVALID_STUDY_ID)
							.build();
				}

				isValidFlag = studyMetaDataOrchestration.isValidActivity(
						activityId, studyId, activityVersion);
				if (!isValidFlag) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
							ErrorCodes.INVALID_INPUT,
							StudyMetaDataConstants.INVALID_ACTIVITY_ID,
							response);
					return Response.status(Response.Status.NOT_FOUND)
							.entity(StudyMetaDataConstants.INVALID_ACTIVITY_ID)
							.build();
				}

				isActivityTypeQuestionnaire = studyMetaDataOrchestration
						.isActivityTypeQuestionnaire(activityId, studyId,
								activityVersion);
				if (!isActivityTypeQuestionnaire) {
					activeTaskActivityMetaDataResponse = activityMetaDataOrchestration
							.studyActiveTaskActivityMetadata(studyId,
									activityId, activityVersion);
					if (!activeTaskActivityMetaDataResponse.getMessage()
							.equals(StudyMetaDataConstants.SUCCESS)) {
						StudyMetaDataUtil.getFailureResponse(
								ErrorCodes.STATUS_103, ErrorCodes.NO_DATA,
								StudyMetaDataConstants.FAILURE, response);
						return Response.status(Response.Status.NO_CONTENT)
								.entity(StudyMetaDataConstants.NO_RECORD)
								.build();
					}
					return activeTaskActivityMetaDataResponse;
				} else {
					questionnaireActivityMetaDataResponse = activityMetaDataOrchestration
							.studyQuestionnaireActivityMetadata(studyId,
									activityId, activityVersion);
					if (!questionnaireActivityMetaDataResponse.getMessage()
							.equals(StudyMetaDataConstants.SUCCESS)) {
						StudyMetaDataUtil.getFailureResponse(
								ErrorCodes.STATUS_103, ErrorCodes.NO_DATA,
								StudyMetaDataConstants.FAILURE, response);
						return Response.status(Response.Status.NO_CONTENT)
								.entity(StudyMetaDataConstants.NO_RECORD)
								.build();
					}
					return questionnaireActivityMetaDataResponse;
				}
			} else {
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
						ErrorCodes.INVALID_INPUT,
						StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG,
						response);
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG)
						.build();
			}

		} catch (Exception e) {
			LOGGER.error(
					"StudyMetaDataService - studyActivityMetadata() :: ERROR",
					e);
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104,
					ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE,
					response);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(StudyMetaDataConstants.FAILURE).build();
		}
	}

	/**
	 * Get dashboard metadata for the provided study identifier
	 * 
	 * @author BTC
	 * @param studyId
	 * @param context
	 * @param response
	 * @return {@link Object}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("studyDashboard")
	public Object studyDashboardInfo(@QueryParam("studyId") String studyId,
			@Context ServletContext context,
			@Context HttpServletResponse response) {
		LOGGER.info("INFO: StudyMetaDataService - studyDashboardInfo() :: Starts");
		StudyDashboardResponse studyDashboardResponse = new StudyDashboardResponse();
		Boolean isValidFlag = false;
		try {
			if (StringUtils.isNotEmpty(studyId)) {
				isValidFlag = studyMetaDataOrchestration.isValidStudy(studyId);
				if (!isValidFlag) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
							ErrorCodes.INVALID_INPUT,
							StudyMetaDataConstants.INVALID_STUDY_ID, response);
					return Response.status(Response.Status.NOT_FOUND)
							.entity(StudyMetaDataConstants.INVALID_STUDY_ID)
							.build();
				}

				studyDashboardResponse = dashboardMetaDataOrchestration
						.studyDashboardInfo(studyId);
				if (!studyDashboardResponse.getMessage().equals(
						StudyMetaDataConstants.SUCCESS)) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103,
							ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE,
							response);
					return Response.status(Response.Status.NO_CONTENT)
							.entity(StudyMetaDataConstants.NO_RECORD).build();
				}
			} else {
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
						ErrorCodes.INVALID_INPUT,
						StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG,
						response);
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG)
						.build();
			}
		} catch (Exception e) {
			LOGGER.error(
					"StudyMetaDataService - studyDashboardInfo() :: ERROR", e);
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104,
					ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE,
					response);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - studyDashboardInfo() :: Ends");
		return studyDashboardResponse;
	}

	/**
	 * Get terms and policy for the app
	 * 
	 * @author BTC
	 * @param context
	 * @param response
	 * @return {@link Object}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("termsPolicy")
	public Object termsPolicy(@Context ServletContext context,
			@Context HttpServletResponse response) {
		LOGGER.info("INFO: StudyMetaDataService - termsPolicy() :: Starts");
		TermsPolicyResponse termsPolicyResponse = new TermsPolicyResponse();
		try {
			termsPolicyResponse = appMetaDataOrchestration.termsPolicy();
			if (!termsPolicyResponse.getMessage().equals(
					StudyMetaDataConstants.SUCCESS)) {
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103,
						ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE,
						response);
				return Response.status(Response.Status.NO_CONTENT)
						.entity(StudyMetaDataConstants.NO_RECORD).build();
			}
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataService - termsPolicy() :: ERROR", e);
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104,
					ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE,
					response);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - termsPolicy() :: Ends");
		return termsPolicyResponse;
	}

	/**
	 * Fetch available notifications
	 * 
	 * @author BTC
	 * @param skip
	 * @param authorization
	 * @param context
	 * @param response
	 * @return {@link Object}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("notifications")
	public Object notifications(@QueryParam("skip") String skip,
			@HeaderParam("Authorization") String authorization,
			@Context ServletContext context,
			@Context HttpServletResponse response) {
		LOGGER.info("INFO: StudyMetaDataService - notifications() :: Starts");
		NotificationsResponse notificationsResponse = new NotificationsResponse();
		try {
			if (StringUtils.isNotEmpty(skip)) {
				notificationsResponse = appMetaDataOrchestration.notifications(
						skip, authorization);
				if (!notificationsResponse.getMessage().equals(
						StudyMetaDataConstants.SUCCESS)) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103,
							ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE,
							response);
					return Response.status(Response.Status.NO_CONTENT)
							.entity(StudyMetaDataConstants.NO_RECORD).build();
				}
			} else {
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
						ErrorCodes.INVALID_INPUT,
						StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG,
						response);
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG)
						.build();
			}
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataService - notifications() :: ERROR", e);
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104,
					ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE,
					response);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - notifications() :: Ends");
		return notificationsResponse;
	}

	/**
	 * Provide feedback about the app
	 * 
	 * @author BTC
	 * @param params
	 * @param context
	 * @param response
	 * @return {@link Object}
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("feedback")
	public Object feedbackDetails(String params,
			@Context ServletContext context,
			@Context HttpServletResponse response) {
		LOGGER.info("INFO: StudyMetaDataService - feedbackDetails() :: Starts");
		AppResponse appResponse = new AppResponse();
		try {
			JSONObject serviceJson = new JSONObject(params);
			String subject = serviceJson.getString("subject");
			String body = serviceJson.getString("body");
			if (StringUtils.isNotEmpty(subject) && StringUtils.isNotEmpty(body)) {
				appResponse = appMetaDataOrchestration.feedback(subject, body);
			} else {
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
						ErrorCodes.UNKNOWN,
						StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG,
						response);
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(StudyMetaDataConstants.INVALID_INPUT).build();
			}
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataService - feedbackDetails() :: ERROR", e);
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104,
					ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE,
					response);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - feedbackDetails() :: Ends");
		return appResponse;
	}

	/**
	 * Reach out to app owner
	 * 
	 * @author BTC
	 * @param params
	 * @param context
	 * @param response
	 * @return {@link Object}
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("contactUs")
	public Object contactUsDetails(String params,
			@Context ServletContext context,
			@Context HttpServletResponse response) {
		LOGGER.info("INFO: StudyMetaDataService - contactUsDetails() :: Starts");
		AppResponse appResponse = new AppResponse();
		try {
			JSONObject serviceJson = new JSONObject(params);
			String subject = serviceJson.getString("subject");
			String body = serviceJson.getString("body");
			String firstName = serviceJson.getString("firstName");
			String email = serviceJson.getString("email");
			boolean inputFlag1 = StringUtils.isNotEmpty(subject)
					&& StringUtils.isNotEmpty(body);
			boolean inputFlag2 = StringUtils.isNotEmpty(firstName)
					&& StringUtils.isNotEmpty(email);
			if (inputFlag1 && inputFlag2) {
				appResponse = appMetaDataOrchestration.contactUsDetails(
						subject, body, firstName, email);
			} else {
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
						ErrorCodes.UNKNOWN,
						StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG,
						response);
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(StudyMetaDataConstants.INVALID_INPUT).build();
			}
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataService - contactUsDetails() :: ERROR",
					e);
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104,
					ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE,
					response);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - contactUsDetails() :: Ends");
		return appResponse;
	}

	/**
	 * Check for app updates
	 * 
	 * @author BTC
	 * @param appVersion
	 * @param authorization
	 * @param context
	 * @param response
	 * @return {@link Object}
	 */
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("appUpdates")
	public Object appUpdates(@QueryParam("appVersion") String appVersion,
			@HeaderParam("Authorization") String authorization,
			@Context ServletContext context,
			@Context HttpServletResponse response) {
		LOGGER.info("INFO: StudyMetaDataService - appUpdates() :: Starts");
		AppUpdatesResponse appUpdatesResponse = new AppUpdatesResponse();
		try {
			if (StringUtils.isNotEmpty(appVersion)
					&& StringUtils.isNotEmpty(authorization)) {
				appUpdatesResponse = appMetaDataOrchestration.appUpdates(
						appVersion, authorization);
			} else {
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
						ErrorCodes.UNKNOWN,
						StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG,
						response);
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(StudyMetaDataConstants.INVALID_INPUT).build();
			}
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataService - appUpdates() :: ERROR", e);
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104,
					ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE,
					response);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - appUpdates() :: Ends");
		return appUpdatesResponse;
	}

	/**
	 * Check for study updates
	 * 
	 * @author BTC
	 * @param studyId
	 * @param studyVersion
	 * @param context
	 * @param response
	 * @return {@link Object}
	 */
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("studyUpdates")
	public Object studyUpdates(@QueryParam("studyId") String studyId,
			@QueryParam("studyVersion") String studyVersion,
			@Context ServletContext context,
			@Context HttpServletResponse response) {
		LOGGER.info("INFO: StudyMetaDataService - studyUpdates() :: Starts");
		StudyUpdatesResponse studyUpdatesResponse = new StudyUpdatesResponse();
		Boolean isValidFlag = false;
		try {
			if (StringUtils.isNotEmpty(studyId)
					&& StringUtils.isNotEmpty(studyVersion)) {
				isValidFlag = studyMetaDataOrchestration.isValidStudy(studyId);
				if (!isValidFlag) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
							ErrorCodes.INVALID_INPUT,
							StudyMetaDataConstants.INVALID_STUDY_ID, response);
					return Response.status(Response.Status.NOT_FOUND)
							.entity(StudyMetaDataConstants.INVALID_STUDY_ID)
							.build();
				}

				studyUpdatesResponse = appMetaDataOrchestration.studyUpdates(
						studyId, studyVersion);
				if (!studyUpdatesResponse.getMessage().equals(
						StudyMetaDataConstants.SUCCESS)) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103,
							ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE,
							response);
					return Response.status(Response.Status.NO_CONTENT)
							.entity(StudyMetaDataConstants.NO_RECORD).build();
				}
			} else {
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
						ErrorCodes.UNKNOWN,
						StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG,
						response);
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(StudyMetaDataConstants.INVALID_INPUT).build();
			}
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataService - appUpdates() :: ERROR", e);
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104,
					ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE,
					response);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - studyUpdates() :: Ends");
		return studyUpdatesResponse;
	}

	/**
	 * Update app version
	 * 
	 * @author BTC
	 * @param params
	 * @param context
	 * @param response
	 * @return {@link Object}
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("updateAppVersion")
	public Object updateAppVersionDetails(String params,
			@Context ServletContext context,
			@Context HttpServletResponse response) {
		LOGGER.info("INFO: StudyMetaDataService - updateAppVersionDetails() :: Starts");
		String updateAppVersionResponse = "OOPS! Something went wrong.";
		try {
			JSONObject serviceJson = new JSONObject(params);
			String forceUpdate = serviceJson.getString("forceUpdate");
			String osType = serviceJson.getString("osType");
			String appVersion = serviceJson.getString("appVersion");
			String bundleId = serviceJson.getString("bundleId");
			String customStudyId = serviceJson.getString("studyId");
			String message = serviceJson.getString("message");
			if (StringUtils.isNotEmpty(forceUpdate)
					&& StringUtils.isNotEmpty(osType)
					&& StringUtils.isNotEmpty(appVersion)
					&& StringUtils.isNotEmpty(bundleId)
					&& StringUtils.isNotEmpty(message)) {
				if (Integer.parseInt(forceUpdate) > 1) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
							ErrorCodes.UNKNOWN,
							StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG,
							response);
					return Response.status(Response.Status.BAD_REQUEST)
							.entity(StudyMetaDataConstants.INVALID_INPUT)
							.build();
				}

				if (!osType.equals(StudyMetaDataConstants.STUDY_PLATFORM_IOS)
						&& !osType
								.equals(StudyMetaDataConstants.STUDY_PLATFORM_ANDROID)) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
							ErrorCodes.UNKNOWN,
							StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG,
							response);
					return Response.status(Response.Status.BAD_REQUEST)
							.entity(StudyMetaDataConstants.INVALID_INPUT)
							.build();
				}

				if (Float.parseFloat(appVersion) < 1) {
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
							ErrorCodes.UNKNOWN,
							StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG,
							response);
					return Response.status(Response.Status.BAD_REQUEST)
							.entity(StudyMetaDataConstants.INVALID_INPUT)
							.build();
				}

				updateAppVersionResponse = appMetaDataOrchestration
						.updateAppVersionDetails(forceUpdate, osType,
								appVersion, bundleId, customStudyId, message);
			} else {
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
						ErrorCodes.UNKNOWN,
						StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG,
						response);
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(StudyMetaDataConstants.INVALID_INPUT).build();
			}
		} catch (Exception e) {
			LOGGER.error(
					"StudyMetaDataService - updateAppVersionDetails() :: ERROR",
					e);
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104,
					ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE,
					response);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - updateAppVersionDetails() :: Ends");
		return updateAppVersionResponse;
	}

	/**
	 * Ping application
	 * 
	 * @author BTC
	 * @return {@link String}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_XML)
	@Path("ping")
	public String ping() {
		LOGGER.info("INFO: StudyMetaDataService - ping() :: Starts ");
		String response = "It Works!";
		LOGGER.info("INFO: StudyMetaDataService - ping() :: Ends ");
		return response;
	}

	/**
	 * insert, update, alter or delete the DB changes
	 * 
	 * @author BTC
	 * @param params
	 * @param context
	 * @param response
	 * @return {@link Object}
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("testQuery")
	public Object interceptorDataBaseQuery(String params,
			@Context ServletContext context,
			@Context HttpServletResponse response) {
		LOGGER.info("INFO: StudyMetaDataService - interceptorDataBaseQuery() :: Starts");
		String message = "OOPS! Something went wrong.";
		try {
			JSONObject serviceJson = new JSONObject(params);
			String dbQuery = serviceJson.getString("dbQuery");
			if (StringUtils.isNotEmpty(dbQuery)) {
				message = appMetaDataOrchestration
						.interceptorDataBaseQuery(dbQuery);
			} else {
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102,
						ErrorCodes.UNKNOWN,
						StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG,
						response);
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(StudyMetaDataConstants.INVALID_INPUT).build();
			}
		} catch (Exception e) {
			LOGGER.error(
					"StudyMetaDataService - interceptorDataBaseQuery() :: ERROR",
					e);
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104,
					ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE,
					response);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - interceptorDataBaseQuery() :: Starts");
		return message;
	}

	/**
	 * Check for mail
	 * 
	 * @author BTC
	 * @return {@link Object}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_XML)
	@Path("mail")
	public String sampleMail() {
		LOGGER.info("INFO: StudyMetaDataService - sampleMail() :: Starts ");
		boolean flag;
		String response = "";
		try {
			flag = studyMetaDataOrchestration.sampleMail();
			if (flag) {
				response = "Mail Sent Successfully";
			} else {
				response = "Sending mail failed";
			}
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataService - sampleMail() :: ERROR ", e);
		}
		LOGGER.info("INFO: StudyMetaDataService - sampleMail() :: Ends ");
		return response;
	}

}
