package com.studymetadata.dao;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.studymetadata.dto.ActiveTaskDto;
import com.studymetadata.dto.ComprehensionTestQuestionDto;
import com.studymetadata.dto.ComprehensionTestResponseDto;
import com.studymetadata.dto.ConsentDto;
import com.studymetadata.dto.ConsentInfoDto;
import com.studymetadata.dto.ConsentMasterInfoDto;
import com.studymetadata.dto.EligibilityDto;
import com.studymetadata.dto.EligibilityTestDto;
import com.studymetadata.dto.FormMappingDto;
import com.studymetadata.dto.GatewayInfoDto;
import com.studymetadata.dto.GatewayWelcomeInfoDto;
import com.studymetadata.dto.QuestionnairesDto;
import com.studymetadata.dto.QuestionnairesStepsDto;
import com.studymetadata.dto.QuestionsDto;
import com.studymetadata.dto.ReferenceTablesDto;
import com.studymetadata.dto.ResourcesDto;
import com.studymetadata.dto.StudyDto;
import com.studymetadata.dto.StudyPageDto;
import com.studymetadata.dto.StudySequenceDto;
import com.studymetadata.dto.StudyVersionDto;
import com.studymetadata.exception.DAOException;
import com.studymetadata.util.StudyMetaDataConstants;
import com.studymetadata.util.HibernateUtil;
import com.studymetadata.util.StudyMetaDataUtil;
import com.studymetadata.bean.AnchorDateBean;
import com.studymetadata.bean.ComprehensionDetailsBean;
import com.studymetadata.bean.ConsentBean;
import com.studymetadata.bean.ConsentDetailsBean;
import com.studymetadata.bean.ConsentDocumentBean;
import com.studymetadata.bean.ConsentDocumentResponse;
import com.studymetadata.bean.CorrectAnswersBean;
import com.studymetadata.bean.EligibilityBean;
import com.studymetadata.bean.EligibilityConsentResponse;
import com.studymetadata.bean.GatewayInfoResourceBean;
import com.studymetadata.bean.GatewayInfoResponse;
import com.studymetadata.bean.InfoBean;
import com.studymetadata.bean.QuestionInfoBean;
import com.studymetadata.bean.QuestionnaireActivityStepsBean;
import com.studymetadata.bean.ResourcesBean;
import com.studymetadata.bean.ResourcesResponse;
import com.studymetadata.bean.ReviewBean;
import com.studymetadata.bean.SettingsBean;
import com.studymetadata.bean.SharingBean;
import com.studymetadata.bean.StudyBean;
import com.studymetadata.bean.StudyInfoResponse;
import com.studymetadata.bean.StudyResponse;
import com.studymetadata.bean.WithdrawalConfigBean;

/**
 * 
 * @author Mohan
 * @createdOn Jan 4, 2018 3:23:46 PM
 *
 */
public class StudyMetaDataDao {

	private static final Logger LOGGER = Logger.getLogger(StudyMetaDataDao.class);

	@SuppressWarnings("unchecked")
	HashMap<String, String> propMap = StudyMetaDataUtil.getAppProperties();

	@SuppressWarnings("unchecked")
	HashMap<String, String> authPropMap = StudyMetaDataUtil.getAuthorizationProperties();

	SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	Query query = null;
	String queryString = "";

	/**
	 * Check Authorization for the provided authorization identifier
	 * 
	 * @author Mohan
	 * @param authorization
	 * @return {@link Boolean}
	 * @throws DAOException
	 */
	public boolean isValidAuthorizationId(String authorization) throws DAOException{
		LOGGER.info("INFO: StudyMetaDataOrchestration - isValidAuthorizationId() :: Starts");
		boolean hasValidAuthorization = false;
		String bundleIdAndAppToken = null;
		try{
			byte[] decodedBytes = Base64.getDecoder().decode(authorization);
			bundleIdAndAppToken =  new String(decodedBytes, StudyMetaDataConstants.TYPE_UTF8);
			final StringTokenizer tokenizer = new StringTokenizer(bundleIdAndAppToken, ":");
			final String bundleId = tokenizer.nextToken();
			final String appToken = tokenizer.nextToken();
			if(authPropMap.containsValue(bundleId) && authPropMap.containsValue(appToken)){
				hasValidAuthorization = true;
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataOrchestration - isValidAuthorizationId() :: ERROR", e);
		}
		LOGGER.info("INFO: StudyMetaDataOrchestration - isValidAuthorizationId() :: Ends");
		return hasValidAuthorization;
	}

	/**
	 * Get Gateway info and Gateway resources data
	 * 
	 * @author Mohan
	 * @param authorization
	 * @return {@link GatewayInfoResponse}
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public GatewayInfoResponse gatewayAppResourcesInfo(String authorization) throws DAOException{
		LOGGER.info("INFO: StudyMetaDataDao - gatewayAppResourcesInfo() :: Starts");
		Session session = null;
		GatewayInfoResponse gatewayInfoResponse = new GatewayInfoResponse();
		GatewayInfoDto gatewayInfo = null;
		List<GatewayWelcomeInfoDto> gatewayWelcomeInfoList = null;
		List<ResourcesDto> resourcesList = null;
		String platformType = "";
		try{
			session = sessionFactory.openSession();
			gatewayInfo = (GatewayInfoDto) session.getNamedQuery("getGatewayInfo")
					.uniqueResult();
			if( null != gatewayInfo){

				gatewayWelcomeInfoList = session.getNamedQuery("getGatewayWelcomeInfoList")
						.list();
				if( null != gatewayWelcomeInfoList && !gatewayWelcomeInfoList.isEmpty()){
					List<InfoBean> infoBeanList = new ArrayList<>();
					for(GatewayWelcomeInfoDto gatewayWelcomeInfo : gatewayWelcomeInfoList){
						
						InfoBean infoBean = new InfoBean();
						infoBean.setTitle(StringUtils.isEmpty(gatewayWelcomeInfo.getAppTitle())?"":gatewayWelcomeInfo.getAppTitle());
						infoBean.setImage(StringUtils.isEmpty(gatewayWelcomeInfo.getImagePath())?"":propMap.get("fda.smd.study.thumbnailPath")+gatewayWelcomeInfo.getImagePath()+StudyMetaDataUtil.getMilliSecondsForImagePath());
						infoBean.setText(StringUtils.isEmpty(gatewayWelcomeInfo.getDescription())?"":gatewayWelcomeInfo.getDescription());
						if(infoBeanList.isEmpty()){
							infoBean.setType(StudyMetaDataConstants.TYPE_VIDEO);
							infoBean.setVideoLink(StringUtils.isEmpty(gatewayInfo.getVideoUrl())?"":gatewayInfo.getVideoUrl());
						}else{
							infoBean.setType(StudyMetaDataConstants.TYPE_TEXT);
						}
						infoBeanList.add(infoBean);
					}
					gatewayInfoResponse.setInfo(infoBeanList);
				}
			}

			//get resources details
			platformType = StudyMetaDataUtil.platformType(authorization, StudyMetaDataConstants.STUDY_AUTH_TYPE_PLATFORM);
			if(StringUtils.isNotEmpty(platformType)){
				resourcesList = session.createQuery("from ResourcesDto RDTO"
						+ " where RDTO.studyId in ( select SDTO.id"
						+ " from StudyDto SDTO"
						+ " where SDTO.platform like '%"+platformType+"%' and SDTO.type='"+StudyMetaDataConstants.STUDY_TYPE_GT+"' and SDTO.live=1)"
						+ " and RDTO.status=true and RDTO.action=true"
						+ " ORDER BY RDTO.sequenceNo")
						.list();
				if( null != resourcesList && !resourcesList.isEmpty()){
					List<GatewayInfoResourceBean> resourceBeanList = new ArrayList<>();
					for(ResourcesDto resource : resourcesList){
						
						GatewayInfoResourceBean resourceBean = new GatewayInfoResourceBean();
						resourceBean.setTitle(StringUtils.isEmpty(resource.getTitle())?"":resource.getTitle());
						if(!resource.isTextOrPdf()){
							resourceBean.setType(StudyMetaDataConstants.TYPE_HTML);
							resourceBean.setContent(StringUtils.isEmpty(resource.getRichText())?"":resource.getRichText());
						}else{
							resourceBean.setType(StudyMetaDataConstants.TYPE_PDF);
							resourceBean.setContent(StringUtils.isEmpty(resource.getPdfUrl())?"":propMap.get("fda.smd.resource.pdfPath")+resource.getPdfUrl());
						}
						
						resourceBean.setResourcesId(resource.getId() == null?"":String.valueOf(resource.getId()));
						resourceBeanList.add(resourceBean);
					}
					gatewayInfoResponse.setResources(resourceBeanList);
				}
			}
			
			gatewayInfoResponse.setMessage(StudyMetaDataConstants.SUCCESS);
		}catch(Exception e){
			LOGGER.error("StudyMetaDataDao - gatewayAppResourcesInfo() :: ERROR", e);
		}finally{
			if(session != null){
				session.close();
			}
		}
		LOGGER.info("INFO: StudyMetaDataDao - gatewayAppResourcesInfo() :: Ends");
		return gatewayInfoResponse;
	}

	/**
	 * Get all the configured studies from the WCP
	 * 
	 * @author Mohan
	 * @param authorization
	 * @return {@link StudyResponse}
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public StudyResponse studyList(String authorization) throws DAOException{
		LOGGER.info("INFO: StudyMetaDataDao - studyList() :: Starts");
		Session session = null;
		StudyResponse studyResponse = new StudyResponse();
		List<StudyDto> studiesList = null;
		String platformType = "";
		try{
			platformType = StudyMetaDataUtil.platformType(authorization, StudyMetaDataConstants.STUDY_AUTH_TYPE_PLATFORM);
			if(StringUtils.isNotEmpty(platformType)){
				session = sessionFactory.openSession();

				//fetch all Gateway studies based on the platform supported (iOS/android)
				studiesList = session.createQuery("from StudyDto SDTO"
						+ " where SDTO.type='"+StudyMetaDataConstants.STUDY_TYPE_GT+"' and SDTO.platform like '%"+platformType+"%'"
						+ " and (SDTO.status = '"+StudyMetaDataConstants.STUDY_STATUS_PRE_PUBLISH+"' OR SDTO.live=1)")
						.list();
				if(null != studiesList && !studiesList.isEmpty()){
					List<StudyBean> studyBeanList = new ArrayList<>();
					for(StudyDto studyDto : studiesList){
						
						StudyBean studyBean = new StudyBean();
						studyBean.setStudyVersion(studyDto.getVersion() == null?StudyMetaDataConstants.STUDY_DEFAULT_VERSION:studyDto.getVersion().toString());
						studyBean.setTagline(StringUtils.isEmpty(studyDto.getStudyTagline())?"":studyDto.getStudyTagline());
						
						switch (studyDto.getStatus()) {
							case StudyMetaDataConstants.STUDY_STATUS_ACTIVE: 
								studyBean.setStatus(StudyMetaDataConstants.STUDY_ACTIVE);
								break;
							case StudyMetaDataConstants.STUDY_STATUS_PAUSED: 
								studyBean.setStatus(StudyMetaDataConstants.STUDY_PAUSED);
								break;
							case StudyMetaDataConstants.STUDY_STATUS_PRE_PUBLISH: 
								studyBean.setStatus(StudyMetaDataConstants.STUDY_UPCOMING);
								break;
							case StudyMetaDataConstants.STUDY_STATUS_DEACTIVATED: 
								studyBean.setStatus(StudyMetaDataConstants.STUDY_CLOSED);
								break;
							default:
								break;
						}
						
						studyBean.setTitle(StringUtils.isEmpty(studyDto.getName())?"":studyDto.getName());
						studyBean.setLogo(StringUtils.isEmpty(studyDto.getThumbnailImage())?"":propMap.get("fda.smd.study.thumbnailPath")+studyDto.getThumbnailImage()+StudyMetaDataUtil.getMilliSecondsForImagePath());
						studyBean.setStudyId(StringUtils.isEmpty(studyDto.getCustomStudyId())?"":studyDto.getCustomStudyId());

						if(StringUtils.isNotEmpty(studyDto.getCategory()) && StringUtils.isNotEmpty(studyDto.getResearchSponsor())){
							List<ReferenceTablesDto> referenceTablesList = session.createQuery("from ReferenceTablesDto RTDTO"
									+ " where RTDTO.id IN ("+studyDto.getCategory()+","+studyDto.getResearchSponsor()+")")
									.list();
							if(null != referenceTablesList && !referenceTablesList.isEmpty()){
								for(ReferenceTablesDto reference : referenceTablesList){
									if(reference.getCategory().equalsIgnoreCase(StudyMetaDataConstants.STUDY_REF_CATEGORIES)){
										studyBean.setCategory(StringUtils.isEmpty(reference.getValue())?"":reference.getValue());
									}else{
										studyBean.setSponsorName(StringUtils.isEmpty(reference.getValue())?"":reference.getValue());
									}
								}
							}
						}

						SettingsBean settings = new SettingsBean();
						if(studyDto.getPlatform().contains(",")){
							settings.setPlatform(StudyMetaDataConstants.STUDY_PLATFORM_ALL);
						}else{
							switch (studyDto.getPlatform()) {
								case StudyMetaDataConstants.STUDY_PLATFORM_TYPE_IOS:	
									settings.setPlatform(StudyMetaDataConstants.STUDY_PLATFORM_IOS);
									break;
								case StudyMetaDataConstants.STUDY_PLATFORM_TYPE_ANDROID:	
									settings.setPlatform(StudyMetaDataConstants.STUDY_PLATFORM_ANDROID);
									break;
								default:
									break;
							}
						}
						if(StringUtils.isNotEmpty(studyDto.getAllowRejoin()) 
								&& studyDto.getAllowRejoin().equalsIgnoreCase(StudyMetaDataConstants.YES)){
							settings.setRejoin(true);
						}else{
							settings.setRejoin(false);
						}
						if(StringUtils.isNotEmpty(studyDto.getEnrollingParticipants()) 
								&& studyDto.getEnrollingParticipants().equalsIgnoreCase(StudyMetaDataConstants.YES)){
							settings.setEnrolling(true);
						}else{
							settings.setEnrolling(false);
						}
						studyBean.setSettings(settings);
						studyBeanList.add(studyBean);
					}
					studyResponse.setStudies(studyBeanList);
				}
				studyResponse.setMessage(StudyMetaDataConstants.SUCCESS);
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataDao - studyList() :: ERROR", e);
		}finally{
			if(session != null){
				session.close();
			}
		}
		LOGGER.info("INFO: StudyMetaDataDao - studyList() :: Ends");
		return studyResponse;
	}

	/**
	 * Get eligibility and consent info for the provided study identifier
	 * 
	 * @author Mohan
	 * @param studyId
	 * @return EligibilityConsentResponse
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public EligibilityConsentResponse eligibilityConsentMetadata(String studyId) throws DAOException{
		LOGGER.info("INFO: StudyMetaDataDao - eligibilityConsentMetadata() :: Starts");
		Session session = null;
		EligibilityConsentResponse eligibilityConsentResponse = new EligibilityConsentResponse();
		EligibilityDto eligibilityDto = null;
		ConsentDto consentDto = null;
		List<ConsentInfoDto> consentInfoDtoList = null;
		List<ComprehensionTestQuestionDto> comprehensionQuestionList = null;
		List<ConsentMasterInfoDto> consentMasterInfoList = null;
		ConsentDetailsBean consent = new ConsentDetailsBean();
		StudySequenceDto studySequenceDto = null;
		StudyDto studyDto = null;
		StudyVersionDto studyVersionDto = null;
		List<EligibilityTestDto> eligibilityTestList = null;
		try{
			session = sessionFactory.openSession();

			studyDto = (StudyDto) session.getNamedQuery("getLiveStudyIdByCustomStudyId")
					.setString("customStudyId", studyId)
					.uniqueResult();
			if(studyDto != null){
				studyVersionDto = (StudyVersionDto) session.getNamedQuery("getLiveVersionDetailsByCustomStudyIdAndVersion")
						.setString("customStudyId", studyDto.getCustomStudyId())
						.setFloat("studyVersion", studyDto.getVersion())
						.setMaxResults(1)
						.uniqueResult();
				
				studySequenceDto = (StudySequenceDto) session.getNamedQuery("getStudySequenceDetailsByStudyId")
						.setInteger("studyId", studyDto.getId())
						.uniqueResult();
				if(studySequenceDto != null){

					if(studySequenceDto.getEligibility().equalsIgnoreCase(StudyMetaDataConstants.STUDY_SEQUENCE_Y)){
						
						eligibilityDto = (EligibilityDto) session.getNamedQuery("eligibilityDtoByStudyId")
								.setInteger("studyId", studyDto.getId())
								.uniqueResult();
						if( eligibilityDto != null){
							
							EligibilityBean eligibility = new EligibilityBean();
							if(null != eligibilityDto.getEligibilityMechanism()){
								switch (eligibilityDto.getEligibilityMechanism()) {
									case 1: eligibility.setType(StudyMetaDataConstants.TYPE_TOKEN);
										break;
									case 2: eligibility.setType(StudyMetaDataConstants.TYPE_BOTH);
										break;
									case 3: eligibility.setType(StudyMetaDataConstants.TYPE_TEST);
										break;
									default:eligibility.setType("");
										break;
								}
							}
							eligibility.setTokenTitle(StringUtils.isEmpty(eligibilityDto.getInstructionalText())?"":eligibilityDto.getInstructionalText());

							eligibilityTestList = session.createQuery("from EligibilityTestDto ETDTO"
									+ " where ETDTO.eligibilityId="+eligibilityDto.getId()+" and ETDTO.status=true and ETDTO.active=true"
									+ " ORDER BY ETDTO.sequenceNo")
									.list();
							if(eligibilityTestList!=null && !eligibilityTestList.isEmpty()){
								List<QuestionnaireActivityStepsBean> test = new ArrayList<>();
								
								List<HashMap<String,Object>> correctAnswers = new ArrayList<>();
								for(EligibilityTestDto eligibilityTest : eligibilityTestList){
									QuestionnaireActivityStepsBean questionStep = new QuestionnaireActivityStepsBean();
									questionStep.setType(StudyMetaDataConstants.QUESTIONAIRE_STEP_TYPE_QUESTION);
									questionStep.setResultType(StudyMetaDataConstants.QUESTION_BOOLEAN);
									questionStep.setKey(eligibilityTest.getShortTitle());
									questionStep.setTitle(eligibilityTest.getShortTitle());
									questionStep.setText(eligibilityTest.getQuestion());
									questionStep.setSkippable(false);
									questionStep.setGroupName("");
									questionStep.setRepeatable(false);
									questionStep.setRepeatableText("");
									questionStep.setHealthDataKey("");
									test.add(questionStep);
									
									//yes option
									if(eligibilityTest.getResponseYesOption()){
										HashMap<String,Object> correctAnsHashMap = new HashMap<>();
										correctAnsHashMap.put("key", eligibilityTest.getShortTitle());
										correctAnsHashMap.put("answer", true);
										correctAnswers.add(correctAnsHashMap);
									}
									
									//no option
									if(eligibilityTest.getResponseNoOption()){
										HashMap<String,Object> correctAnsHashMap = new HashMap<>();
										correctAnsHashMap.put("key", eligibilityTest.getShortTitle());
										correctAnsHashMap.put("answer", false);
										correctAnswers.add(correctAnsHashMap);
									}
								}
								eligibility.setTest(test);
								eligibility.setCorrectAnswers(correctAnswers);
							}
							eligibilityConsentResponse.setEligibility(eligibility);
						}
					}

					consentDto = (ConsentDto) session.getNamedQuery("consentDetailsByCustomStudyIdAndVersion")
							.setString("customStudyId", studyVersionDto.getCustomStudyId())
							.setFloat("version", studyVersionDto.getConsentVersion())
							.uniqueResult();
					if( null != consentDto){
						consent.setVersion(consentDto.getVersion() == null?StudyMetaDataConstants.STUDY_DEFAULT_VERSION:consentDto.getVersion().toString());

						SharingBean sharingBean = new SharingBean();
						if(StringUtils.isNotEmpty(consentDto.getShareDataPermissions()) 
								&& consentDto.getShareDataPermissions().equalsIgnoreCase(StudyMetaDataConstants.YES)){
							sharingBean.setTitle(StringUtils.isEmpty(consentDto.getTitle())?"":consentDto.getTitle());
							sharingBean.setText(StringUtils.isEmpty(consentDto.getTaglineDescription())?"":consentDto.getTaglineDescription());
							sharingBean.setLearnMore(StringUtils.isEmpty(consentDto.getLearnMoreText())?"":consentDto.getLearnMoreText());
							sharingBean.setLongDesc(StringUtils.isEmpty(consentDto.getLongDescription())?"":consentDto.getLongDescription());
							sharingBean.setShortDesc(StringUtils.isEmpty(consentDto.getShortDescription())?"":consentDto.getShortDescription());
							if(consentDto.getAllowWithoutPermission() != null 
									&& StudyMetaDataConstants.YES.equalsIgnoreCase(consentDto.getAllowWithoutPermission())){
								sharingBean.setAllowWithoutSharing(true);
							}
						}
						consent.setSharing(sharingBean);
					}

					consentMasterInfoList = session.createQuery("from ConsentMasterInfoDto CMIDTO")
							.list();

					if(studySequenceDto.getConsentEduInfo().equalsIgnoreCase(StudyMetaDataConstants.STUDY_SEQUENCE_Y)){
						
						consentInfoDtoList = session.getNamedQuery("consentInfoDetailsByCustomStudyIdAndVersion")
								.setString("customStudyId", studyVersionDto.getCustomStudyId())
								.setFloat("version", studyVersionDto.getConsentVersion())
								.list();
						if( null != consentInfoDtoList && !consentInfoDtoList.isEmpty()){
							
							List<ConsentBean> consentBeanList = new ArrayList<>();
							for(ConsentInfoDto consentInfoDto : consentInfoDtoList){
								
								ConsentBean consentBean = new ConsentBean();
								consentBean.setText(StringUtils.isEmpty(consentInfoDto.getBriefSummary())?"":consentInfoDto.getBriefSummary()
										.replaceAll("&#34;", "\"")
										.replaceAll("&#39;", "'"));
								consentBean.setTitle(StringUtils.isEmpty(consentInfoDto.getDisplayTitle())?"":consentInfoDto.getDisplayTitle()
										.replaceAll("&#34;", "\"")
										.replaceAll("&#39;", "'"));
								if(consentInfoDto.getConsentItemTitleId() != null){
									if(consentMasterInfoList != null && !consentMasterInfoList.isEmpty()){
										for(ConsentMasterInfoDto masterInfo : consentMasterInfoList){
											if(masterInfo.getId().intValue() == consentInfoDto.getConsentItemTitleId().intValue()){
												consentBean.setType(masterInfo.getCode());
												break;
											}
										}
									}
								}else{
									consentBean.setType(StudyMetaDataConstants.CONSENT_TYPE_CUSTOM.toLowerCase());
								}
								consentBean.setDescription("");
								consentBean.setHtml(StringUtils.isEmpty(consentInfoDto.getElaborated())?"":consentInfoDto.getElaborated()
										.replaceAll("&#34;", "'").replaceAll("em>", "i>")
										.replaceAll("<a", "<a style='text-decoration:underline;color:blue;'"));
								consentBean.setUrl(StringUtils.isEmpty(consentInfoDto.getUrl())?"":consentInfoDto.getUrl());
								
								if(StringUtils.isNotEmpty(consentInfoDto.getVisualStep()) 
										&& consentInfoDto.getVisualStep().equalsIgnoreCase(StudyMetaDataConstants.YES)){
									consentBean.setVisualStep(true);
								}else{
									consentBean.setVisualStep(false);
								}
								consentBeanList.add(consentBean);
							}
							consent.setVisualScreens(consentBeanList);
						}
					}

					//Check whether Comprehension List module is done or not
					if(studySequenceDto.getComprehensionTest().equalsIgnoreCase(StudyMetaDataConstants.STUDY_SEQUENCE_Y) 
							&& (consentDto!=null 
							&& consentDto.getNeedComprehensionTest()!=null 
							&& consentDto.getNeedComprehensionTest().equalsIgnoreCase(StudyMetaDataConstants.YES))){
						
						comprehensionQuestionList = session.getNamedQuery("comprehensionQuestionByStudyId")
								.setInteger("studyId", studyDto.getId())
								.list();
						if( null != comprehensionQuestionList && !comprehensionQuestionList.isEmpty()){
							ComprehensionDetailsBean comprehensionDetailsBean = new ComprehensionDetailsBean();
							if(consentDto != null && consentDto.getComprehensionTestMinimumScore() != null){
								comprehensionDetailsBean.setPassScore(consentDto.getComprehensionTestMinimumScore());
							}else{
								comprehensionDetailsBean.setPassScore(0);
							}

							List<QuestionnaireActivityStepsBean> comprehensionList = new ArrayList<>();
							List<CorrectAnswersBean> correctAnswerBeanList = new ArrayList<>();
							for(ComprehensionTestQuestionDto comprehensionQuestionDto : comprehensionQuestionList){
								QuestionnaireActivityStepsBean questionStep = new QuestionnaireActivityStepsBean();
								questionStep.setType(StudyMetaDataConstants.QUESTIONAIRE_STEP_TYPE_QUESTION);
								questionStep.setResultType(StudyMetaDataConstants.QUESTION_TEXT_CHOICE);
								questionStep.setKey(comprehensionQuestionDto.getId().toString());
								questionStep.setTitle("");
								questionStep.setText(comprehensionQuestionDto.getQuestionText());
								questionStep.setSkippable(false);
								questionStep.setGroupName("");
								questionStep.setRepeatable(false);
								questionStep.setRepeatableText("");
								questionStep.setHealthDataKey("");
								
								List<ComprehensionTestResponseDto> comprehensionTestResponseList = session.getNamedQuery("comprehensionQuestionResponseByCTID")
										.setInteger("comprehensionTestQuestionId", comprehensionQuestionDto.getId())
										.list();
								if(comprehensionTestResponseList!=null && !comprehensionTestResponseList.isEmpty()){
									
									CorrectAnswersBean correctAnswerBean = new CorrectAnswersBean();
									Map<String, Object> questionFormat = new LinkedHashMap<>();
									List<LinkedHashMap<String, Object>> textChoiceMapList = new ArrayList<>();
									StringBuilder sb = new StringBuilder();
									
									for(ComprehensionTestResponseDto compResp : comprehensionTestResponseList){
										if(compResp.getCorrectAnswer()){
											sb.append(StringUtils.isEmpty(sb)?compResp.getResponseOption().trim():"&@##@&"+compResp.getResponseOption().trim());
										}
										LinkedHashMap<String, Object> textChoiceMap = new LinkedHashMap<>();
										textChoiceMap.put("text", StringUtils.isEmpty(compResp.getResponseOption().trim())?"":compResp.getResponseOption().trim());
										textChoiceMap.put("value", StringUtils.isEmpty(compResp.getResponseOption().trim())?"":compResp.getResponseOption().trim());
										textChoiceMap.put("detail", "");
										textChoiceMap.put("exclusive", false);
										textChoiceMapList.add(textChoiceMap);
									}
									
									questionFormat.put("textChoices", textChoiceMapList);
									
									if(comprehensionQuestionDto.getStructureOfCorrectAns()){
										questionFormat.put("selectionStyle", "Multiple");
									}else{
										questionFormat.put("selectionStyle", "Single");
									}
									
									questionStep.setFormat(questionFormat);
									if(StringUtils.isNotEmpty(sb.toString())){
										correctAnswerBean.setAnswer(sb.toString().split("&@##@&"));
									}
									
									correctAnswerBean.setKey(comprehensionQuestionDto.getId().toString());
									correctAnswerBean.setEvaluation(comprehensionQuestionDto.getStructureOfCorrectAns()?StudyMetaDataConstants.COMPREHENSION_RESPONSE_STRUCTURE_ALL:StudyMetaDataConstants.COMPREHENSION_RESPONSE_STRUCTURE_ANY);
									correctAnswerBeanList.add(correctAnswerBean);
								}
								
								comprehensionList.add(questionStep);
							}
							comprehensionDetailsBean.setQuestions(comprehensionList);
							comprehensionDetailsBean.setCorrectAnswers(correctAnswerBeanList);
							consent.setComprehension(comprehensionDetailsBean);
						}
					}

					//Review
					if( consentDto != null){
						ReviewBean reviewBean = new ReviewBean();
						if(consentDto.getConsentDocType().equals(StudyMetaDataConstants.CONSENT_DOC_TYPE_NEW)){
							reviewBean.setReviewHTML(StringUtils.isEmpty(consentDto.getConsentDocContent())?"":consentDto.getConsentDocContent()
									.replaceAll("&#34;", "'").replaceAll("em>", "i>")
									.replaceAll("<a", "<a style='text-decoration:underline;color:blue;'"));
						}
						
						reviewBean.setReasonForConsent(StringUtils.isNotEmpty(consentDto.getAggrementOfConsent())?consentDto.getAggrementOfConsent():StudyMetaDataConstants.REASON_FOR_CONSENT);
						consent.setReview(reviewBean);
					}
					eligibilityConsentResponse.setConsent(consent);

					eligibilityConsentResponse.setMessage(StudyMetaDataConstants.SUCCESS);
				}
			}else{
				eligibilityConsentResponse.setMessage(StudyMetaDataConstants.INVALID_STUDY_ID);
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataDao - eligibilityConsentMetadata() :: ERROR", e);
		}finally{
			if(session != null){
				session.close();
			}
		}
		LOGGER.info("INFO: StudyMetaDataDao - eligibilityConsentMetadata() :: Ends");
		return eligibilityConsentResponse;
	}

	/**
	 * Get consent document by passing the consent version or
	 * the activity id and activity version for the provided study identifier
	 * 
	 * @author Mohan
	 * @param studyId
	 * @param consentVersion
	 * @param activityId
	 * @param activityVersion
	 * @return {@link ConsentDocumentResponse}
	 * @throws DAOException
	 */
	public ConsentDocumentResponse consentDocument(String studyId, 
			String consentVersion, 
			String activityId, 
			String activityVersion) throws DAOException{
		LOGGER.info("INFO: StudyMetaDataDao - consentDocument() :: Starts");
		Session session = null;
		ConsentDocumentResponse consentDocumentResponse = new ConsentDocumentResponse();
		ConsentDto consent = null;
		StudyDto studyDto = null;
		StudyVersionDto studyVersionDto = null;
		String studyVersionQuery = "from StudyVersionDto SVDTO"
				+ " where SVDTO.customStudyId='"+studyId+"' ";
		try{
			session = sessionFactory.openSession();
			
			studyDto = (StudyDto) session.getNamedQuery("getLiveStudyIdByCustomStudyId")
					.setString("customStudyId", studyId)
					.uniqueResult();
			if(studyDto == null){
				studyDto = (StudyDto) session.getNamedQuery("getPublishedStudyByCustomId")
						.setString("customStudyId", studyId)
						.uniqueResult();
			}
			
			if(studyDto != null){
				if(StringUtils.isNotEmpty(consentVersion)){
					studyVersionQuery += " and ROUND(SVDTO.consentVersion, 1)="+consentVersion
							+" ORDER BY SVDTO.versionId DESC";
				}else if(StringUtils.isNotEmpty(activityId) && StringUtils.isNotEmpty(activityVersion)){
					studyVersionQuery += " and ROUND(SVDTO.activityVersion, 1)="+activityVersion
							+" ORDER BY SVDTO.versionId DESC";
				}else{
					studyVersionQuery += " ORDER BY SVDTO.versionId DESC";
				}
				
				if(!studyDto.getStatus().equalsIgnoreCase(StudyMetaDataConstants.STUDY_STATUS_PRE_PUBLISH)){
					studyVersionDto = (StudyVersionDto) session.createQuery(studyVersionQuery)
							.setMaxResults(1)
							.uniqueResult();
				}else{
					studyVersionDto = new StudyVersionDto();
					studyVersionDto.setConsentVersion(0f);
				}
				if(studyVersionDto != null){
					if(!studyDto.getStatus().equalsIgnoreCase(StudyMetaDataConstants.STUDY_STATUS_PRE_PUBLISH)){
						consent = (ConsentDto) session.getNamedQuery("consentDetailsByCustomStudyIdAndVersion")
								.setString("customStudyId", studyId)
								.setFloat("version", studyVersionDto.getConsentVersion())
								.uniqueResult();
					}else{
						consent = (ConsentDto) session.getNamedQuery("consentDtoByStudyId")
								.setInteger("studyId", studyDto.getId())
								.uniqueResult();
					}

					//check the consentBo is empty or not
					if( consent != null){
						ConsentDocumentBean consentDocumentBean = new ConsentDocumentBean();
						consentDocumentBean.setType("text/html");
						consentDocumentBean.setVersion((consent.getVersion() == null)?StudyMetaDataConstants.STUDY_DEFAULT_VERSION:String.valueOf(consent.getVersion()));
						consentDocumentBean.setContent(StringUtils.isEmpty(consent.getConsentDocContent())?"":consent.getConsentDocContent()
								.replaceAll("&#34;", "'").replaceAll("em>", "i>")
								.replaceAll("<a", "<a style='text-decoration:underline;color:blue;'"));
						consentDocumentResponse.setConsent(consentDocumentBean);
					}
					consentDocumentResponse.setMessage(StudyMetaDataConstants.SUCCESS);
				}
			}else{
				consentDocumentResponse.setMessage(StudyMetaDataConstants.INVALID_STUDY_ID);
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataDao - consentDocument() :: ERROR", e);
		}finally{
			if(session != null){
				session.close();
			}
		}
		LOGGER.info("INFO: StudyMetaDataDao - consentDocument() :: Ends");
		return consentDocumentResponse;
	}

	/**
	 * Get resources metadata for the provided study identifier
	 * 
	 * @author Mohan
	 * @param studyId
	 * @return {@link ResourcesResponse}
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public ResourcesResponse resourcesForStudy(String studyId) throws DAOException{
		LOGGER.info("INFO: StudyMetaDataDao - resourcesForStudy() :: Starts");
		Session session = null;
		ResourcesResponse resourcesResponse = new ResourcesResponse();
		List<ResourcesDto> resourcesDtoList = null;
		StudyDto studyDto = null;
		try{
			session = sessionFactory.openSession();

			studyDto = (StudyDto) session.getNamedQuery("getLiveStudyIdByCustomStudyId")
					.setString("customStudyId", studyId)
					.uniqueResult();
			if(studyDto != null){
				
				resourcesDtoList = session.getNamedQuery("getResourcesListByStudyId")
						.setInteger("studyId", studyDto.getId())
						.list();
				if( null != resourcesDtoList && !resourcesDtoList.isEmpty()){
					
					List<ResourcesBean> resourcesBeanList = new ArrayList<>();
					for(ResourcesDto resourcesDto : resourcesDtoList){
						
						ResourcesBean resourcesBean = new ResourcesBean();
						resourcesBean.setAudience(resourcesDto.isResourceType()?StudyMetaDataConstants.RESOURCE_AUDIENCE_TYPE_LIMITED:StudyMetaDataConstants.RESOURCE_AUDIENCE_TYPE_ALL);
						resourcesBean.setTitle(StringUtils.isEmpty(resourcesDto.getTitle())?"":resourcesDto.getTitle());
						if(!resourcesDto.isTextOrPdf()){
							resourcesBean.setType(StudyMetaDataConstants.TYPE_TEXT);
							resourcesBean.setContent(StringUtils.isEmpty(resourcesDto.getRichText())?"":resourcesDto.getRichText());
						}else{
							resourcesBean.setType(StudyMetaDataConstants.TYPE_PDF);
							resourcesBean.setContent(StringUtils.isEmpty(resourcesDto.getPdfUrl())?"":propMap.get("fda.smd.resource.pdfPath")+resourcesDto.getPdfUrl());
						}
						resourcesBean.setResourcesId(resourcesDto.getId() == null?"":String.valueOf(resourcesDto.getId()));

						if(!resourcesDto.isResourceVisibility()){
							Map<String, Object> availability = new LinkedHashMap<>();
							availability.put("availableDate", StringUtils.isEmpty(resourcesDto.getStartDate())?"":resourcesDto.getStartDate());
							availability.put("expiryDate", StringUtils.isEmpty(resourcesDto.getEndDate())?"":resourcesDto.getEndDate());
							
							if(resourcesDto.getTimePeriodFromDays()!=null){
								availability.put("startDays", resourcesDto.isxDaysSign()?Integer.parseInt("-"+resourcesDto.getTimePeriodFromDays()):resourcesDto.getTimePeriodFromDays());
							}else{
								availability.put("startDays", 0);
							}
							
							if(resourcesDto.getTimePeriodToDays()!=null){
								availability.put("endDays", resourcesDto.isyDaysSign()?Integer.parseInt("-"+resourcesDto.getTimePeriodToDays()):resourcesDto.getTimePeriodToDays());
							}else{
								availability.put("endDays", 0);
							}
							resourcesBean.setAvailability(availability);
						}
						resourcesBean.setNotificationText(StringUtils.isEmpty(resourcesDto.getResourceText())?"":resourcesDto.getResourceText());
						resourcesBeanList.add(resourcesBean);
					}
					resourcesResponse.setResources(resourcesBeanList);
				}
				resourcesResponse.setMessage(StudyMetaDataConstants.SUCCESS);
			}else{
				resourcesResponse.setMessage(StudyMetaDataConstants.INVALID_STUDY_ID);
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataDao - resourcesForStudy() :: ERROR", e);
		}finally{
			if(session != null){
				session.close();
			}
		}
		LOGGER.info("INFO: StudyMetaDataDao - resourcesForStudy() :: Ends");
		return resourcesResponse;
	}

	/**
	 * Get study metadata for the provided study identifier
	 * 
	 * @author Mohan
	 * @param studyId
	 * @return {@link StudyInfoResponse}
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public StudyInfoResponse studyInfo(String studyId) throws DAOException{
		LOGGER.info("INFO: StudyMetaDataDao - studyInfo() :: Starts");
		Session session = null;
		StudyInfoResponse studyInfoResponse = new StudyInfoResponse();
		List<StudyPageDto> studyPageDtoList = null;
		StudyDto studyDto = null;
		try{
			session = sessionFactory.openSession();
			studyDto = (StudyDto) session.getNamedQuery("getLiveStudyIdByCustomStudyId")
					.setString("customStudyId", studyId)
					.uniqueResult();
			if(studyDto == null){
				studyDto = (StudyDto) session.getNamedQuery("getPublishedStudyByCustomId")
						.setString("customStudyId", studyId)
						.uniqueResult();
			}
			
			if(studyDto != null){
				
				studyInfoResponse.setStudyWebsite(StringUtils.isEmpty(studyDto.getStudyWebsite())?"":studyDto.getStudyWebsite());
				
				List<InfoBean> infoList = new ArrayList<>();
				studyPageDtoList = session.getNamedQuery("studyPageDetailsByStudyId")
						.setInteger("studyId", studyDto.getId())
						.list();
				if( null != studyPageDtoList && !studyPageDtoList.isEmpty()){
					for(StudyPageDto studyPageInfo : studyPageDtoList){
						InfoBean info = new InfoBean();
						
						if(infoList.isEmpty()){
							info.setType(StudyMetaDataConstants.TYPE_VIDEO);
							info.setVideoLink(StringUtils.isEmpty(studyDto.getMediaLink())?"":studyDto.getMediaLink());
						}else{
							info.setType(StudyMetaDataConstants.TYPE_TEXT);
							info.setVideoLink("");
						}
						
						info.setTitle(StringUtils.isEmpty(studyPageInfo.getTitle())?"":studyPageInfo.getTitle());
						info.setImage(StringUtils.isEmpty(studyPageInfo.getImagePath())?"":propMap.get("fda.smd.study.pagePath")+studyPageInfo.getImagePath()+StudyMetaDataUtil.getMilliSecondsForImagePath());
						info.setText(StringUtils.isEmpty(studyPageInfo.getDescription())?"":studyPageInfo.getDescription());
						infoList.add(info);
					}
				}else{
					
					InfoBean info = new InfoBean();
					
					if(infoList.isEmpty()){
						info.setType(StudyMetaDataConstants.TYPE_VIDEO);
						info.setVideoLink(StringUtils.isEmpty(studyDto.getMediaLink())?"":studyDto.getMediaLink());
					}else{
						info.setType(StudyMetaDataConstants.TYPE_TEXT);
						info.setVideoLink("");
					}
					
					info.setTitle(StringUtils.isEmpty(studyDto.getName())?"":studyDto.getName());
					info.setImage(StringUtils.isEmpty(studyDto.getThumbnailImage())?"":propMap.get("fda.smd.study.thumbnailPath")+studyDto.getThumbnailImage()+StudyMetaDataUtil.getMilliSecondsForImagePath());
					info.setText(StringUtils.isEmpty(studyDto.getFullName())?"":studyDto.getFullName());
					infoList.add(info);
				}
				studyInfoResponse.setInfo(infoList);

				WithdrawalConfigBean withdrawConfig = new WithdrawalConfigBean();
				switch (studyDto.getRetainParticipant()) {
					case StudyMetaDataConstants.YES: 
						withdrawConfig.setType(StudyMetaDataConstants.STUDY_WITHDRAW_CONFIG_NO_ACTION);
						break;
					case StudyMetaDataConstants.NO: 
						withdrawConfig.setType(StudyMetaDataConstants.STUDY_WITHDRAW_CONFIG_DELETE_DATA);
						break;
					case StudyMetaDataConstants.ALL: 
						withdrawConfig.setType(StudyMetaDataConstants.STUDY_WITHDRAW_CONFIG_ASK_USER);
						break;
					default:
						break;
				}
				withdrawConfig.setMessage(StringUtils.isEmpty(studyDto.getAllowRejoinText())?"":studyDto.getAllowRejoinText());
				studyInfoResponse.setWithdrawalConfig(withdrawConfig);
				
				if(!studyDto.getStatus().equalsIgnoreCase(StudyMetaDataConstants.STUDY_STATUS_PRE_PUBLISH)){
					List<QuestionnairesDto> questionnairesList = session.createQuery("from QuestionnairesDto QDTO"
							+ " where QDTO.customStudyId='"+studyDto.getCustomStudyId()+"' and QDTO.active=true"
							+ " and QDTO.status=true and QDTO.live=1")
							.list();
					if(questionnairesList != null && !questionnairesList.isEmpty()){
						
						List<Integer> questionnaireIdsList = new ArrayList<>();
						Map<Integer, QuestionnairesDto> questionnaireMap = new TreeMap<>();
						Map<String, QuestionnairesStepsDto> stepsMap = new TreeMap<>();
						Map<Integer, QuestionsDto> questionsMap = null;
						Map<Integer, FormMappingDto> formMappingMap = new TreeMap<>();
						
						for(QuestionnairesDto questionnaire : questionnairesList){
							questionnaireIdsList.add(questionnaire.getId());
							questionnaireMap.put(questionnaire.getId(), questionnaire);
						}
						
						if(!questionnaireIdsList.isEmpty()){
							
							List<Integer> questionIdsList = new ArrayList<>();
							List<Integer> formIdsList = new ArrayList<>();
							List<QuestionnairesStepsDto> questionnairesStepsList = session.createQuery("from QuestionnairesStepsDto QSDTO"
									+ " where QSDTO.active=true and QSDTO.status=true"
									+ " and QSDTO.questionnairesId in ("+StringUtils.join(questionnaireIdsList, ',')+")"
									+ " and QSDTO.stepType in ('"+StudyMetaDataConstants.QUESTIONAIRE_STEP_TYPE_QUESTION+"','"
									+StudyMetaDataConstants.QUESTIONAIRE_STEP_TYPE_FORM+"')")
									.list();
							if(questionnairesStepsList != null && !questionnairesStepsList.isEmpty()){
								
								for(QuestionnairesStepsDto stepsDto : questionnairesStepsList){
									if(stepsDto.getStepType().equalsIgnoreCase(StudyMetaDataConstants.QUESTIONAIRE_STEP_TYPE_QUESTION)){
										questionIdsList.add(stepsDto.getInstructionFormId());
										stepsMap.put(stepsDto.getInstructionFormId()+StudyMetaDataConstants.QUESTIONAIRE_STEP_TYPE_QUESTION, stepsDto);
									}else{
										formIdsList.add(stepsDto.getInstructionFormId());
										stepsMap.put(stepsDto.getInstructionFormId()+StudyMetaDataConstants.QUESTIONAIRE_STEP_TYPE_FORM, stepsDto);
									}
								}
								
								if(!questionIdsList.isEmpty()){
									List<QuestionsDto> questionnsList = session.createQuery("from QuestionsDto QDTO"
											+ " where QDTO.active=true and QDTO.status=true"
											+ " and QDTO.id in ("+StringUtils.join(questionIdsList, ',')+")"
											+ " and QDTO.responseType=10 and QDTO.useAnchorDate=true")
											.setMaxResults(1)
											.list();
									if(questionnsList != null && !questionnsList.isEmpty()){
										
										questionsMap = new TreeMap<>();
										for(QuestionsDto question : questionnsList){
											questionsMap.put(question.getId(), question);
										}
									}
								}
								
								if(questionsMap == null && !formIdsList.isEmpty()){
									
									List<Integer> formQuestionsList = new ArrayList<>();
									List<FormMappingDto> formMappingList = session.createQuery("from FormMappingDto FMDTO"
											+ " where FMDTO.formId in (select FDTO.formId"
											+ " from FormDto FDTO"
											+ " where FDTO.formId in ("+StringUtils.join(formIdsList, ',')+")"
											+ " and FDTO.active=true) and FMDTO.active=true"
											+ " ORDER BY FMDTO.formId, FMDTO.sequenceNo")
											.list();
									if(formMappingList!= null && !formMappingList.isEmpty()){
										
										for(FormMappingDto formMapping : formMappingList){
											formQuestionsList.add(formMapping.getQuestionId());
											formMappingMap.put(formMapping.getQuestionId(), formMapping);
										}
										
										if(!formQuestionsList.isEmpty()){
											List<QuestionsDto> questionnsList = session.createQuery("from QuestionsDto QDTO"
													+ " where QDTO.active=true and QDTO.status=true"
													+ " and QDTO.id in ("+StringUtils.join(formQuestionsList, ',')+")"
													+ " and QDTO.responseType=10 and QDTO.useAnchorDate=true")
													.setMaxResults(1)
													.list();
											if(questionnsList != null && !questionnsList.isEmpty()){
												
												questionsMap = new TreeMap<>();
												for(QuestionsDto question : questionnsList){
													questionsMap.put(question.getId(), question);
												}
											}
										}
									}
								}
								
								if(questionsMap != null){
									AnchorDateBean anchorDate = new AnchorDateBean();
									anchorDate.setType(StudyMetaDataConstants.ANCHORDATE_TYPE_QUESTION);
									for(Map.Entry<Integer, QuestionsDto> map : questionsMap.entrySet()){
										QuestionsDto questionDto = map.getValue();
										if(questionDto != null){
											QuestionnairesStepsDto questionnairesSteps;
											
											if(StringUtils.isNotEmpty(questionDto.getShortTitle())){
												FormMappingDto formMapping = formMappingMap.get(questionDto.getId());
												questionnairesSteps = stepsMap.get(formMapping.getFormId()+StudyMetaDataConstants.QUESTIONAIRE_STEP_TYPE_FORM);
											}else{
												questionnairesSteps = stepsMap.get(questionDto.getId()+StudyMetaDataConstants.QUESTIONAIRE_STEP_TYPE_QUESTION);
											}
											
											if(questionnairesSteps != null){
												QuestionnairesDto questionnairesDto = questionnaireMap.get(questionnairesSteps.getQuestionnairesId());
												
												if(questionnairesDto != null){
													QuestionInfoBean questionInfoBean = new QuestionInfoBean();
													questionInfoBean.setActivityId(questionnairesDto.getShortTitle());
													questionInfoBean.setActivityVersion(questionnairesDto.getVersion().toString());
													
													if(questionnairesSteps.getStepType().equalsIgnoreCase(StudyMetaDataConstants.QUESTIONAIRE_STEP_TYPE_FORM)){
														questionInfoBean.setKey(questionDto.getShortTitle());
													}else{
														questionInfoBean.setKey(questionnairesSteps.getStepShortTitle());
													}
													anchorDate.setQuestionInfo(questionInfoBean);
												}
											}
										}
									}
									studyInfoResponse.setAnchorDate(anchorDate);
								}
							}
						}
					}
				}
				studyInfoResponse.setMessage(StudyMetaDataConstants.SUCCESS);
			}else{
				studyInfoResponse.setMessage(StudyMetaDataConstants.INVALID_STUDY_ID);
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataDao - studyInfo() :: ERROR", e);
		}finally{
			if(session != null){
				session.close();
			}
		}
		LOGGER.info("INFO: StudyMetaDataDao - studyInfo() :: Ends");
		return studyInfoResponse;
	}
	
	/**
	 * Check the StudyId is valid or not
	 * 
	 * @author Mohan
	 * @param studyId
	 * @return {@link Boolean}
	 * @throws DAOException
	 */
	public boolean isValidStudy(String studyId) throws DAOException{
		LOGGER.info("INFO: StudyMetaDataOrchestration - isValidStudy() :: Starts");
		Session session = null;
		boolean isValidStudy = false;
		StudyDto studyDto = null;
		try{
			session = sessionFactory.openSession();
			studyDto = (StudyDto) session.createQuery("from StudyDto SDTO"
					+ " where SDTO.customStudyId='"+studyId+"'"
					+ " ORDER BY SDTO.id DESC")
					.setMaxResults(1)
					.uniqueResult();
			isValidStudy = (studyDto == null)?false:true;
		}catch(Exception e){
			LOGGER.error("StudyMetaDataOrchestration - isValidStudy() :: ERROR", e);
		}finally{
			if(session != null ){
				session.close();
			}
		}
		LOGGER.info("INFO: StudyMetaDataOrchestration - isValidStudy() :: Ends");
		return isValidStudy;
	}
	
	/**
	 * Check the ActivityId is valid or not i.e. Active Task, Questionnaire
	 * 
	 * @author Mohan
	 * @param activityId
	 * @param studyId
	 * @param activityVersion
	 * @return {@link Boolean}
	 * @throws DAOException
	 */
	public boolean isValidActivity(String activityId, String studyId, String activityVersion) throws DAOException{
		LOGGER.info("INFO: StudyMetaDataOrchestration - isValidActivity() :: Starts");
		Session session = null;
		boolean isValidActivity = false;
		ActiveTaskDto activeTaskDto = null;
		QuestionnairesDto questionnaireDto = null;
		try{
			session = sessionFactory.openSession();
			activeTaskDto = (ActiveTaskDto) session.createQuery("from ActiveTaskDto ATDTO"
					+ " where ATDTO.shortTitle='"+StudyMetaDataUtil.replaceSingleQuotes(activityId)+"'"
					+ " and ROUND(ATDTO.version, 1)="+Float.parseFloat(activityVersion)+" and ATDTO.customStudyId='"+studyId+"'"
					+ " ORDER BY ATDTO.id DESC")
					.setMaxResults(1)
					.uniqueResult();
			isValidActivity = (activeTaskDto == null)?false:true;
			
			if(!isValidActivity){
				questionnaireDto = (QuestionnairesDto) session.createQuery("from QuestionnairesDto QDTO"
						+ " where QDTO.shortTitle='"+StudyMetaDataUtil.replaceSingleQuotes(activityId)+"'"
						+ " and ROUND(QDTO.version, 1)="+Float.parseFloat(activityVersion)+" and QDTO.customStudyId='"+studyId+"'"
						+ " ORDER BY QDTO.id DESC")
						.setMaxResults(1)
						.uniqueResult();
				isValidActivity = (questionnaireDto == null)?false:true;
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataOrchestration - isValidActivity() :: ERROR", e);
		}finally{
			if(session != null ){
				session.close();
			}
		}
		LOGGER.info("INFO: StudyMetaDataOrchestration - isValidActivity() :: Ends");
		return isValidActivity;
	}
	
	/**
	 * Get the Activity Type based on the ActivityId, StudyId & ActivityVersion
	 * 
	 * @author Mohan
	 * @param activityId
	 * @param studyId
	 * @param activityVersion
	 * @return {@link Boolean}
	 * @throws DAOException
	 */
	public boolean isActivityTypeQuestionnaire(String activityId, String studyId, String activityVersion) throws DAOException{
		LOGGER.info("INFO: StudyMetaDataOrchestration - isActivityTypeQuestionnaire() :: Starts");
		Session session = null;
		boolean isActivityTypeQuestionnaire = true;
		ActiveTaskDto activeTaskDto = null;
		try{
			session = sessionFactory.openSession();
			activeTaskDto = (ActiveTaskDto) session.createQuery("from ActiveTaskDto ATDTO"
					+ " where ATDTO.shortTitle='"+StudyMetaDataUtil.replaceSingleQuotes(activityId)+"'"
					+ " and ROUND(ATDTO.version, 1)="+Float.parseFloat(activityVersion)+" and ATDTO.customStudyId='"+studyId+"'"
					+ " ORDER BY ATDTO.id DESC")
					.setMaxResults(1)
					.uniqueResult();
			if(activeTaskDto != null){
				isActivityTypeQuestionnaire = false;
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataOrchestration - isActivityTypeQuestionnaire() :: ERROR", e);
		}finally{
			if(session != null ){
				session.close();
			}
		}
		LOGGER.info("INFO: StudyMetaDataOrchestration - isActivityTypeQuestionnaire() :: Ends");
		return isActivityTypeQuestionnaire;
	}
	
	/**
	 * Get the Consent Document Display Title
	 * 
	 * @author Mohan
	 * @param displaytitle
	 * @return {@link String}
	 * @throws DAOException
	 */
	public String getconsentDocumentDisplayTitle(String displaytitle) throws DAOException{
		LOGGER.info("INFO: StudyMetaDataDao - getconsentDocumentDisplayTitle() :: Starts");
		String consentTitle = "";
		try {
			switch (displaytitle) {
				case "overview": 
					consentTitle = "Overview";
					break;
				case "dataGathering": 
					consentTitle = "Data Gathering";
					break;
				case "privacy": 
					consentTitle = "Privacy";
					break;
				case "dataUse": 
					consentTitle = "Data Use";
					break;
				case "timeCommitment": 
					consentTitle = "Time Commitment";
					break;
				case "studySurvey": 
					consentTitle = "Study Survey";
					break;
				case "studyTasks": 
					consentTitle = "Study Tasks";
					break;
				case "withdrawing": 
					consentTitle = "Withdrawing";
					break;
				case "customService": 
					consentTitle = "Custom Service";
					break;
				default: 
					consentTitle = displaytitle;
					break;
			}
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataDao - getconsentDocumentDisplayTitle() :: ERROR", e);
		}
		LOGGER.info("INFO: StudyMetaDataDao - getconsentDocumentDisplayTitle() :: Ends");
		return consentTitle;
	}
	
}
