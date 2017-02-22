package com.studymetadata.service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.studymetadata.exception.ErrorCodes;
import com.studymetadata.integration.StudyMetaDataOrchestration;
import com.studymetadata.util.StudyMetaDataConstants;
import com.studymetadata.util.StudyMetaDataUtil;
import com.studymetadata.bean.ActivityResponse;
import com.studymetadata.bean.EligibilityConsentResponse;
import com.studymetadata.bean.FailureResponse;
import com.studymetadata.bean.GatewayInfoResponse;
import com.studymetadata.bean.NotificationsResponse;
import com.studymetadata.bean.ResourcesResponse;
import com.studymetadata.bean.StudyDashboardResponse;
import com.studymetadata.bean.StudyInfoResponse;
import com.studymetadata.bean.StudyResponse;
import com.studymetadata.bean.SuccessResponse;
import com.studymetadata.bean.TermsPolicyResponse;

@Path("/")
public class StudyMetaDataService {
	private static final Logger LOGGER = Logger.getLogger(StudyMetaDataService.class);
	StudyMetaDataOrchestration studyMetaDataOrchestration = new StudyMetaDataOrchestration();

	/*------------------------------------FDA-HPHI Study Meta Data Web Services Starts------------------------------------*/
	/**
	 * @author Mohan
	 * {@link Description : This Method is used to getGatewayAppResourcesInfo}
	 * @param authorization
	 * @param context
	 * @param response
	 * @return Object
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("gatewayInfo")
	public Object gatewayAppResourcesInfo(@HeaderParam("Authorization") String authorization, @Context ServletContext context, @Context HttpServletResponse response){
		LOGGER.info("INFO: StudyMetaDataService - gatewayAppResourcesInfo() :: Starts");
		GatewayInfoResponse gatewayInfo = new GatewayInfoResponse();
		try{
			if(StringUtils.isNotEmpty(authorization)){
				gatewayInfo = studyMetaDataOrchestration.gatewayAppResourcesInfo();
				if(!gatewayInfo.getMessage().equals(StudyMetaDataConstants.SUCCESS)){
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103, ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE, response);
					return Response.status(Response.Status.NO_CONTENT).entity(StudyMetaDataConstants.NO_RECORD).build();
				}
			}else{
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102, ErrorCodes.INVALID_INPUT, StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG, response);
				return Response.status(Response.Status.BAD_REQUEST).entity(StudyMetaDataConstants.INVALID_INPUT).build();
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataService - gatewayAppResourcesInfo() :: ERROR ", e);
			e.printStackTrace();
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104, ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE, response);
			return Response.status(Response.Status.NOT_FOUND).entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - gatewayAppResourcesInfo() :: Ends");
		return gatewayInfo;
	}
	
	/**
	 * @author Mohan
	 * {@link Description : This Method is used to getStudyList}
	 * @param authorization
	 * @param context
	 * @param response
	 * @return Object
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("studyList")
	public Object studyList(@HeaderParam("Authorization") String authorization, @Context ServletContext context, @Context HttpServletResponse response){
		LOGGER.info("INFO: StudyMetaDataService - studyList() :: Starts");
		StudyResponse studyResponse = new StudyResponse();
		try{
			if(StringUtils.isNotEmpty(authorization)){
				studyResponse = studyMetaDataOrchestration.studyList(authorization);
				if(!studyResponse.getMessage().equals(StudyMetaDataConstants.SUCCESS)){
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103, ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE, response);
					return Response.status(Response.Status.NO_CONTENT).entity(StudyMetaDataConstants.NO_RECORD).build();
				}
			}else{
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102, ErrorCodes.INVALID_INPUT, StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG, response);
				return Response.status(Response.Status.BAD_REQUEST).entity(StudyMetaDataConstants.INVALID_INPUT).build();
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataService - studyList() :: ERROR ", e);
			e.printStackTrace();
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104, ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE, response);
			return Response.status(Response.Status.NOT_FOUND).entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - studyList() :: Ends");
		return studyResponse;
	}
	
	/**
	 * @author Mohan
	 * {@link Description : This method is used to getEligibilityConsentMetadata}
	 * @param authorization
	 * @param studyId
	 * @param context
	 * @param response
	 * @return Object
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("eligibilityConsent")
	public Object eligibilityConsentMetadata(@HeaderParam("Authorization") String authorization, @HeaderParam("studyId") String studyId, @Context ServletContext context, @Context HttpServletResponse response){
		LOGGER.info("INFO: StudyMetaDataService - eligibilityConsentMetadata() :: Starts");
		EligibilityConsentResponse eligibilityConsentResponse = new EligibilityConsentResponse();
		try{
			if(StringUtils.isNotEmpty(authorization)){
				if(StringUtils.isNotEmpty(studyId)){
					eligibilityConsentResponse = studyMetaDataOrchestration.eligibilityConsentMetadata(studyId);
					if(!eligibilityConsentResponse.getMessage().equals(StudyMetaDataConstants.SUCCESS)){
						StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103, ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE, response);
						return Response.status(Response.Status.NO_CONTENT).entity(StudyMetaDataConstants.NO_RECORD).build();
					}
				}else{
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102, ErrorCodes.INVALID_INPUT, StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG, response);
					return Response.status(Response.Status.BAD_REQUEST).entity(StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG).build();
				}
			}else{
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102, ErrorCodes.INVALID_INPUT, StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG, response);
				return Response.status(Response.Status.BAD_REQUEST).entity(StudyMetaDataConstants.INVALID_INPUT).build();
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataService - eligibilityConsentMetadata() :: ERROR", e);
			e.printStackTrace();
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104, ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE, response);
			return Response.status(Response.Status.NOT_FOUND).entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - eligibilityConsentMetadata() :: Ends");
		return eligibilityConsentResponse;
	}
	
	

	/**
	 * @author Mohan
	 * {@link Description : This method is used to getResourcesForStudy}
	 * @param authorization
	 * @param studyId
	 * @param context
	 * @param response
	 * @return Object
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("resources")
	public Object resourcesForStudy(@HeaderParam("Authorization") String authorization, @HeaderParam("studyId") String studyId, @Context ServletContext context, @Context HttpServletResponse response){
		LOGGER.info("INFO: StudyMetaDataService - resourcesForStudy() :: Starts");
		ResourcesResponse resourcesResponse = new ResourcesResponse();
		try{
			if(StringUtils.isNotEmpty(authorization)){
				if(StringUtils.isNotEmpty(studyId)){
					resourcesResponse = studyMetaDataOrchestration.resourcesForStudy(studyId);
					if(!resourcesResponse.getMessage().equals(StudyMetaDataConstants.SUCCESS)){
						StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103, ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE, response);
						return Response.status(Response.Status.NO_CONTENT).entity(StudyMetaDataConstants.NO_RECORD).build();
					}
				}else{
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102, ErrorCodes.INVALID_INPUT, StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG, response);
					return Response.status(Response.Status.BAD_REQUEST).entity(StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG).build();
				}
			}else{
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102, ErrorCodes.INVALID_INPUT, StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG, response);
				return Response.status(Response.Status.BAD_REQUEST).entity(StudyMetaDataConstants.INVALID_INPUT).build();
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataService - resourcesForStudy() :: ERROR", e);
			e.printStackTrace();
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104, ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE, response);
			return Response.status(Response.Status.NOT_FOUND).entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - resourcesForStudy() :: Ends");
		return resourcesResponse;
	}
	
	/**
	 * @author Mohan
	 * {@link Description : This method is used to getStudyInfo}
	 * @param authorization
	 * @param studyId
	 * @param context
	 * @param response
	 * @return Object
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("studyInfo")
	public Object studyInfo(@HeaderParam("Authorization") String authorization, @HeaderParam("studyId") String studyId, @Context ServletContext context, @Context HttpServletResponse response){
		LOGGER.info("INFO: StudyMetaDataService - studyInfo() :: Starts");
		StudyInfoResponse studyInfoResponse = new StudyInfoResponse();
		try{
			if(StringUtils.isNotEmpty(authorization)){
				if(StringUtils.isNotEmpty(studyId)){
					studyInfoResponse = studyMetaDataOrchestration.studyInfo(studyId);
					if(!studyInfoResponse.getMessage().equals(StudyMetaDataConstants.SUCCESS)){
						StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103, ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE, response);
						return Response.status(Response.Status.NO_CONTENT).entity(StudyMetaDataConstants.NO_RECORD).build();
					}
				}else{
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102, ErrorCodes.INVALID_INPUT, StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG, response);
					return Response.status(Response.Status.BAD_REQUEST).entity(StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG).build();
				}
			}else{
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102, ErrorCodes.INVALID_INPUT, StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG, response);
				return Response.status(Response.Status.BAD_REQUEST).entity(StudyMetaDataConstants.INVALID_INPUT).build();
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataService - studyInfo() :: ERROR", e);
			e.printStackTrace();
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104, ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE, response);
			return Response.status(Response.Status.NOT_FOUND).entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - studyInfo() :: Ends");
		return studyInfoResponse;
	}
	
	
	/**
	 * @author Mohan
	 * {@link Description : This method is used to getStudyActivityList}
	 * @param authorization
	 * @param studyId
	 * @param context
	 * @param response
	 * @return Object
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("activityList")
	public Object studyActivityList(@HeaderParam("Authorization") String authorization, @HeaderParam("studyId") String studyId, @Context ServletContext context, @Context HttpServletResponse response){
		LOGGER.info("INFO: StudyMetaDataService - studyActivityList() :: Starts");
		ActivityResponse activityResponse = new ActivityResponse();
		try{
			if(StringUtils.isNotEmpty(authorization)){
				if(StringUtils.isNotEmpty(studyId)){
					activityResponse = studyMetaDataOrchestration.studyActivityList(studyId);
					if(!activityResponse.getMessage().equals(StudyMetaDataConstants.SUCCESS)){
						StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103, ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE, response);
						return Response.status(Response.Status.NO_CONTENT).entity(StudyMetaDataConstants.NO_RECORD).build();
					}
				}else{
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102, ErrorCodes.INVALID_INPUT, StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG, response);
					return Response.status(Response.Status.BAD_REQUEST).entity(StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG).build();
				}
			}else{
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102, ErrorCodes.INVALID_INPUT, StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG, response);
				return Response.status(Response.Status.BAD_REQUEST).entity(StudyMetaDataConstants.INVALID_INPUT).build();
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataService - studyActivityList() :: ERROR", e);
			e.printStackTrace();
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104, ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE, response);
			return Response.status(Response.Status.NOT_FOUND).entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - studyActivityList() :: Ends");
		return activityResponse;
	}
	
	
	/**
	 * @author Mohan
	 * {@link Description : This method is used to getStudyActivityMetadata}
	 * @param authorization
	 * @param studyId
	 * @param activityId
	 * @param activityVersion
	 * @param context
	 * @param response
	 * @return Object
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("activity")
	public Object studyActivityMetadata(@HeaderParam("Authorization") String authorization, @HeaderParam("studyId") String studyId, @HeaderParam("activityId") String activityId, @HeaderParam("activityVersion") String activityVersion, @Context ServletContext context, @Context HttpServletResponse response){
		LOGGER.info("INFO: StudyMetaDataService - studyActivityMetadata() :: Starts");
		ActivityResponse activityResponse = new ActivityResponse();
		try{
			if(StringUtils.isNotEmpty(authorization)){
				if(StringUtils.isNotEmpty(studyId) && StringUtils.isNotEmpty(activityId) && StringUtils.isNotEmpty(activityVersion)){
					activityResponse = studyMetaDataOrchestration.studyActivityMetadata(studyId, activityId, activityVersion);
					if(!activityResponse.getMessage().equals(StudyMetaDataConstants.SUCCESS)){
						StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103, ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE, response);
						return Response.status(Response.Status.NO_CONTENT).entity(StudyMetaDataConstants.NO_RECORD).build();
					}
				}else{
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102, ErrorCodes.INVALID_INPUT, StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG, response);
					return Response.status(Response.Status.BAD_REQUEST).entity(StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG).build();
				}
			}else{
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102, ErrorCodes.INVALID_INPUT, StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG, response);
				return Response.status(Response.Status.BAD_REQUEST).entity(StudyMetaDataConstants.INVALID_INPUT).build();
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataService - studyActivityMetadata() :: ERROR", e);
			e.printStackTrace();
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104, ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE, response);
			return Response.status(Response.Status.NOT_FOUND).entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - studyActivityMetadata() :: Ends");
		return activityResponse;
	}
	
	/**
	 * @author Mohan
	 * {@link Description : This method is used to getStudyDashboardInfo}
	 * @param authorization
	 * @param studyId
	 * @param context
	 * @param response
	 * @return Object
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("studyDashboard")
	public Object studyDashboardInfo(@HeaderParam("Authorization") String authorization, @HeaderParam("studyId") String studyId, @Context ServletContext context, @Context HttpServletResponse response){
		LOGGER.info("INFO: StudyMetaDataService - studyDashboardInfo() :: Starts");
		StudyDashboardResponse studyDashboardResponse = new StudyDashboardResponse();
		try{
			if(StringUtils.isNotEmpty(authorization)){
				if(StringUtils.isNotEmpty(studyId)){
					studyDashboardResponse = studyMetaDataOrchestration.studyDashboardInfo(studyId);
					if(!studyDashboardResponse.getMessage().equals(StudyMetaDataConstants.SUCCESS)){
						StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103, ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE, response);
						return Response.status(Response.Status.NO_CONTENT).entity(StudyMetaDataConstants.NO_RECORD).build();
					}
				}else{
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102, ErrorCodes.INVALID_INPUT, StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG, response);
					return Response.status(Response.Status.BAD_REQUEST).entity(StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG).build();
				}
			}else{
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102, ErrorCodes.INVALID_INPUT, StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG, response);
				return Response.status(Response.Status.BAD_REQUEST).entity(StudyMetaDataConstants.INVALID_INPUT).build();
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataService - studyDashboardInfo() :: ERROR", e);
			e.printStackTrace();
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104, ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE, response);
			return Response.status(Response.Status.NOT_FOUND).entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - studyDashboardInfo() :: Ends");
		return studyDashboardResponse;
	}
	
	/**
	 * @author Mohan
	 * {@link Description : This method is used to getTermsPolicy}
	 * @param authorization
	 * @param studyId
	 * @param context
	 * @param response
	 * @return Object
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("termsPolicy")
	public Object termsPolicy(@HeaderParam("Authorization") String authorization, @HeaderParam("studyId") String studyId, @Context ServletContext context, @Context HttpServletResponse response){
		LOGGER.info("INFO: StudyMetaDataService - termsPolicy() :: Starts");
		TermsPolicyResponse termsPolicyResponse = new TermsPolicyResponse();
		try{
			if(StringUtils.isNotEmpty(authorization)){
				if(StringUtils.isNotEmpty(studyId)){
					termsPolicyResponse = studyMetaDataOrchestration.termsPolicy(studyId);
					if(!termsPolicyResponse.getMessage().equals(StudyMetaDataConstants.SUCCESS)){
						StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103, ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE, response);
						return Response.status(Response.Status.NO_CONTENT).entity(StudyMetaDataConstants.NO_RECORD).build();
					}
				}else{
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102, ErrorCodes.INVALID_INPUT, StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG, response);
					return Response.status(Response.Status.BAD_REQUEST).entity(StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG).build();
				}
			}else{
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102, ErrorCodes.INVALID_INPUT, StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG, response);
				return Response.status(Response.Status.BAD_REQUEST).entity(StudyMetaDataConstants.INVALID_INPUT).build();
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataService - termsPolicy() :: ERROR", e);
			e.printStackTrace();
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104, ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE, response);
			return Response.status(Response.Status.NOT_FOUND).entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - termsPolicy() :: Ends");
		return termsPolicyResponse;
	}
	
	/**
	 * @author Mohan
	 * {@link Description : This method is used to getNotifications}
	 * @param authorization
	 * @param skip
	 * @param context
	 * @param response
	 * @return Object
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("notifications")
	public Object notifications(@HeaderParam("Authorization") String authorization, @HeaderParam("skip") String skip, @Context ServletContext context, @Context HttpServletResponse response){
		LOGGER.info("INFO: StudyMetaDataService - notifications() :: Starts");
		NotificationsResponse notificationsResponse = new NotificationsResponse();
		try{
			if(StringUtils.isNotEmpty(authorization)){
				if(StringUtils.isNotEmpty(skip)){
					notificationsResponse = studyMetaDataOrchestration.notifications(skip);
					if(!notificationsResponse.getMessage().equals(StudyMetaDataConstants.SUCCESS)){
						StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_103, ErrorCodes.NO_DATA, StudyMetaDataConstants.FAILURE, response);
						return Response.status(Response.Status.NO_CONTENT).entity(StudyMetaDataConstants.NO_RECORD).build();
					}
				}else{
					StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102, ErrorCodes.INVALID_INPUT, StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG, response);
					return Response.status(Response.Status.BAD_REQUEST).entity(StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG).build();
				}
			}else{
				StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_102, ErrorCodes.INVALID_INPUT, StudyMetaDataConstants.INVALID_INPUT_ERROR_MSG, response);
				return Response.status(Response.Status.BAD_REQUEST).entity(StudyMetaDataConstants.INVALID_INPUT).build();
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataService - notifications() :: ERROR", e);
			e.printStackTrace();
			StudyMetaDataUtil.getFailureResponse(ErrorCodes.STATUS_104, ErrorCodes.UNKNOWN, StudyMetaDataConstants.FAILURE, response);
			return Response.status(Response.Status.NOT_FOUND).entity(StudyMetaDataConstants.FAILURE).build();
		}
		LOGGER.info("INFO: StudyMetaDataService - notifications() :: Ends");
		return notificationsResponse;
	}
	
	/*------------------------------------FDA-HPHI Study Meta Data Web Services Starts------------------------------------*/
	
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

	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("test")
	public Object test(@HeaderParam("type") String type, String params, @Context ServletContext context, @Context HttpServletResponse response) {
		LOGGER.info("INFO: StudyMetaDataService - test() :: Starts ");
		String type1 = "";
		try {
			type1 = type;
			JSONObject serviceJson = new JSONObject(params);
			LOGGER.info("json type  " + serviceJson.get("type"));
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataService.signin() :: ERROR ==> 'type' is missing... ");
		}
		if(StudyMetaDataUtil.isNotEmpty(type1) && "1".equals(type1)){
			SuccessResponse sr = new SuccessResponse();
			sr.setResultType(StudyMetaDataConstants.SUCCESS);
			LOGGER.info("INFO: StudyMetaDataService - test() :: Ends ");
			return sr;
		} else {
			FailureResponse fr = new FailureResponse();
			fr.setResultType(StudyMetaDataConstants.FAILURE);
			fr.getErrors().setStatus(ErrorCodes.STATUS_104);
			fr.getErrors().setTitle(ErrorCodes.UNKNOWN);
			fr.getErrors().setDetail("Testing failure response.");
			LOGGER.info("INFO: StudyMetaDataService - test() :: Ends ");
			return fr;
		}
	}
}
