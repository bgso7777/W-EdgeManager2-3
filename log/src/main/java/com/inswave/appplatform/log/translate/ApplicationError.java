package com.inswave.appplatform.log.translate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.log.util.LogDataObjectMapper;
import com.inswave.appplatform.transaver.elasticsearch.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ApplicationError {

	public ApplicationError () {
	}

	public ApplicationErrorLogOriginal getOriginalObject(StringBuffer message) {
		ApplicationErrorLogOriginal applicationErrorLogOriginal = null;
		return applicationErrorLogOriginal;
	}

	public ApplicationErrorLogOriginal getOriginalObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, ApplicationErrorLogOriginal.class);
	}

	public List<ApplicationErrorLogStatistics> reconstructMinutelyDocument(ApplicationErrorLogOriginal applicationErrorLogOriginal) {

		List<ApplicationErrorLogStatistics> applicationErrorLogMinutelies = new ArrayList<ApplicationErrorLogStatistics>();

		for( ApplicationErrorLogData applicationErrorData: applicationErrorLogOriginal.getApplicationErrorData() ) {

			ApplicationErrorLogStatistics applicationErrorLogMinutely = new ApplicationErrorLogStatistics();
			applicationErrorLogMinutely.setService(applicationErrorLogOriginal.getService());
			applicationErrorLogMinutely.setAppId(applicationErrorLogOriginal.getAppId());
			applicationErrorLogMinutely.setOsType(applicationErrorLogOriginal.getOsType());
			applicationErrorLogMinutely.setSource(applicationErrorLogOriginal.getSource());
			applicationErrorLogMinutely.setDeviceId(applicationErrorLogOriginal.getDeviceId());
			applicationErrorLogMinutely.setIp(applicationErrorLogOriginal.getIp());
			applicationErrorLogMinutely.setHostName(applicationErrorLogOriginal.getHostName());
			applicationErrorLogMinutely.setTimeCreated(applicationErrorLogOriginal.getTimeCreated());

			applicationErrorLogMinutely.setUserId(applicationErrorLogOriginal.getUserId());
			applicationErrorLogMinutely.setTermNo(applicationErrorLogOriginal.getTermNo());
			applicationErrorLogMinutely.setSsoBrNo(applicationErrorLogOriginal.getSsoBrNo());
			applicationErrorLogMinutely.setBrNo(applicationErrorLogOriginal.getBrNo());
			applicationErrorLogMinutely.setDeptName(applicationErrorLogOriginal.getDeptName());
			applicationErrorLogMinutely.setHwnNo(applicationErrorLogOriginal.getHwnNo());
			applicationErrorLogMinutely.setUserName(applicationErrorLogOriginal.getUserName());
			applicationErrorLogMinutely.setSsoType(applicationErrorLogOriginal.getSsoType());
			applicationErrorLogMinutely.setPcName(applicationErrorLogOriginal.getPcName());
			applicationErrorLogMinutely.setPhoneNo(applicationErrorLogOriginal.getPhoneNo());
			applicationErrorLogMinutely.setJKGP(applicationErrorLogOriginal.getJKGP());
			applicationErrorLogMinutely.setJKWI(applicationErrorLogOriginal.getJKWI());
			applicationErrorLogMinutely.setMaxAddress(applicationErrorLogOriginal.getMaxAddress());
			applicationErrorLogMinutely.setFirstWork(applicationErrorLogOriginal.getFirstWork());

			applicationErrorLogMinutely.setCount(applicationErrorLogMinutely.getCount()+1);

			applicationErrorLogMinutely.getLevels().add(applicationErrorData.getLevel());
			applicationErrorLogMinutely.getIds().add(applicationErrorData.getId());
			applicationErrorLogMinutely.getChannels().add(applicationErrorData.getChannel());
			applicationErrorLogMinutely.getActivityIds().add(applicationErrorData.getActivityId());
			applicationErrorLogMinutely.getOpcodeDisplayNames().add(applicationErrorData.getOpcodeDisplayName());
			applicationErrorLogMinutely.getTaskDisplayNames().add(applicationErrorData.getTaskDisplayName());

			applicationErrorLogMinutely.setTimeCurrent(applicationErrorData.getTimeCreated());
			applicationErrorLogMinutely.setStatisticsValue(DateTimeConvertor.getStatisticsValue(applicationErrorData.getTimeCreated()));

			applicationErrorLogMinutelies.add(applicationErrorLogMinutely);
		}

		return applicationErrorLogMinutelies;
	}

	public List<ApplicationErrorLogDaily> reconstructDailyDocument(ApplicationErrorLogOriginal applicationErrorLogOriginal) {

		List<ApplicationErrorLogDaily> applicationErrorLogDailies = new ArrayList<ApplicationErrorLogDaily>();

		for( ApplicationErrorLogData applicationErrorData: applicationErrorLogOriginal.getApplicationErrorData() ) {

			ApplicationErrorLogDaily applicationErrorLogDaily = new ApplicationErrorLogDaily();
			applicationErrorLogDaily.setService(applicationErrorLogOriginal.getService());
			applicationErrorLogDaily.setAppId(applicationErrorLogOriginal.getAppId());
			applicationErrorLogDaily.setOsType(applicationErrorLogOriginal.getOsType());
			applicationErrorLogDaily.setSource(applicationErrorLogOriginal.getSource());
			applicationErrorLogDaily.setDeviceId(applicationErrorLogOriginal.getDeviceId());
			applicationErrorLogDaily.setIp(applicationErrorLogOriginal.getIp());
			applicationErrorLogDaily.setHostName(applicationErrorLogOriginal.getHostName());
			applicationErrorLogDaily.setTimeCreated(applicationErrorLogOriginal.getTimeCreated());

			applicationErrorLogDaily.setUserId(applicationErrorLogOriginal.getUserId());
			applicationErrorLogDaily.setTermNo(applicationErrorLogOriginal.getTermNo());
			applicationErrorLogDaily.setSsoBrNo(applicationErrorLogOriginal.getSsoBrNo());
			applicationErrorLogDaily.setBrNo(applicationErrorLogOriginal.getBrNo());
			applicationErrorLogDaily.setDeptName(applicationErrorLogOriginal.getDeptName());
			applicationErrorLogDaily.setHwnNo(applicationErrorLogOriginal.getHwnNo());
			applicationErrorLogDaily.setUserName(applicationErrorLogOriginal.getUserName());
			applicationErrorLogDaily.setSsoType(applicationErrorLogOriginal.getSsoType());
			applicationErrorLogDaily.setPcName(applicationErrorLogOriginal.getPcName());
			applicationErrorLogDaily.setPhoneNo(applicationErrorLogOriginal.getPhoneNo());
			applicationErrorLogDaily.setJKGP(applicationErrorLogOriginal.getJKGP());
			applicationErrorLogDaily.setJKWI(applicationErrorLogOriginal.getJKWI());
			applicationErrorLogDaily.setMaxAddress(applicationErrorLogOriginal.getMaxAddress());
			applicationErrorLogDaily.setFirstWork(applicationErrorLogOriginal.getFirstWork());

			applicationErrorLogDaily.setLevel(applicationErrorData.getLevel());
			applicationErrorLogDaily.setEventId(applicationErrorData.getId());
			applicationErrorLogDaily.setChannel(applicationErrorData.getChannel());
			applicationErrorLogDaily.setActivityId(applicationErrorData.getActivityId());
			applicationErrorLogDaily.setBookmark(applicationErrorData.getBookmark());
			applicationErrorLogDaily.setKeywords(applicationErrorData.getKeywords());
			applicationErrorLogDaily.setKeywordsDisplayNames(applicationErrorData.getKeywordsDisplayNames());
			applicationErrorLogDaily.setLevelDisplayName(applicationErrorData.getLevelDisplayName());
			applicationErrorLogDaily.setLogName(applicationErrorData.getLogName());
			applicationErrorLogDaily.setMachineName(applicationErrorData.getMachineName());
			applicationErrorLogDaily.setOpcode(applicationErrorData.getOpcode());
			applicationErrorLogDaily.setOpcodeDisplayName(applicationErrorData.getOpcodeDisplayName());
			applicationErrorLogDaily.setProcessId(applicationErrorData.getProcessId());
			applicationErrorLogDaily.setProperties(applicationErrorData.getProperties());
			applicationErrorLogDaily.setProviderId(applicationErrorData.getProviderId());
			applicationErrorLogDaily.setProviderName(applicationErrorData.getProviderName());
			applicationErrorLogDaily.setQualifiers(applicationErrorData.getQualifiers());
			applicationErrorLogDaily.setRecordId(applicationErrorData.getRecordId());
			applicationErrorLogDaily.setRelatedActivityId(applicationErrorData.getRelatedActivityId());
			applicationErrorLogDaily.setTask(applicationErrorData.getTask());
			applicationErrorLogDaily.setTaskDisplayName(applicationErrorData.getTaskDisplayName());
			applicationErrorLogDaily.setThreadId(applicationErrorData.getThreadId());
			applicationErrorLogDaily.setSecurityUserId(applicationErrorData.getSecurityUserId());
			applicationErrorLogDaily.setVersion(applicationErrorData.getVersion());
			applicationErrorLogDaily.setMessage(applicationErrorData.getMessage());

			try{applicationErrorLogDaily.setTimeCreatedSystemTime(applicationErrorData.getTimeCreatedSystemTime());}catch(Exception e) {}
			applicationErrorLogDaily.setTimeCurrent(applicationErrorData.getTimeCreated());
			applicationErrorLogDaily.setStatisticsValue(DateTimeConvertor.getStatisticsValue(applicationErrorData.getTimeCreated()));

			applicationErrorLogDailies.add(applicationErrorLogDaily);
		}

		return applicationErrorLogDailies;
	}

	public List<ApplicationErrorLog> reconstructDocument(ApplicationErrorLogOriginal applicationErrorLogOriginal) {

		List<ApplicationErrorLog> applicationErrorLogs = new ArrayList<ApplicationErrorLog>();

		for( ApplicationErrorLogData applicationErrorData: applicationErrorLogOriginal.getApplicationErrorData() ) {

			ApplicationErrorLog applicationErrorLog = new ApplicationErrorLog();
			applicationErrorLog.setService(applicationErrorLogOriginal.getService());
			applicationErrorLog.setAppId(applicationErrorLogOriginal.getAppId());
			applicationErrorLog.setOsType(applicationErrorLogOriginal.getOsType());
			applicationErrorLog.setSource(applicationErrorLogOriginal.getSource());
			applicationErrorLog.setDeviceId(applicationErrorLogOriginal.getDeviceId());
			applicationErrorLog.setIp(applicationErrorLogOriginal.getIp());
			applicationErrorLog.setHostName(applicationErrorLogOriginal.getHostName());
			applicationErrorLog.setTimeCreated(applicationErrorLogOriginal.getTimeCreated());

			applicationErrorLog.setUserId(applicationErrorLogOriginal.getUserId());
			applicationErrorLog.setTermNo(applicationErrorLogOriginal.getTermNo());
			applicationErrorLog.setSsoBrNo(applicationErrorLogOriginal.getSsoBrNo());
			applicationErrorLog.setBrNo(applicationErrorLogOriginal.getBrNo());
			applicationErrorLog.setDeptName(applicationErrorLogOriginal.getDeptName());
			applicationErrorLog.setHwnNo(applicationErrorLogOriginal.getHwnNo());
			applicationErrorLog.setUserName(applicationErrorLogOriginal.getUserName());
			applicationErrorLog.setSsoType(applicationErrorLogOriginal.getSsoType());
			applicationErrorLog.setPcName(applicationErrorLogOriginal.getPcName());
			applicationErrorLog.setPhoneNo(applicationErrorLogOriginal.getPhoneNo());
			applicationErrorLog.setJKGP(applicationErrorLogOriginal.getJKGP());
			applicationErrorLog.setJKWI(applicationErrorLogOriginal.getJKWI());
			applicationErrorLog.setMaxAddress(applicationErrorLogOriginal.getMaxAddress());
			applicationErrorLog.setFirstWork(applicationErrorLogOriginal.getFirstWork());

			applicationErrorLog.setLevel(applicationErrorData.getLevel());
			applicationErrorLog.setEventId(applicationErrorData.getId());
			applicationErrorLog.setChannel(applicationErrorData.getChannel());
			applicationErrorLog.setActivityId(applicationErrorData.getActivityId());
			applicationErrorLog.setBookmark(applicationErrorData.getBookmark());
			applicationErrorLog.setKeywords(applicationErrorData.getKeywords());
			applicationErrorLog.setKeywordsDisplayNames(applicationErrorData.getKeywordsDisplayNames());
			applicationErrorLog.setLevelDisplayName(applicationErrorData.getLevelDisplayName());
			applicationErrorLog.setLogName(applicationErrorData.getLogName());
			applicationErrorLog.setMachineName(applicationErrorData.getMachineName());
			applicationErrorLog.setOpcode(applicationErrorData.getOpcode());
			applicationErrorLog.setOpcodeDisplayName(applicationErrorData.getOpcodeDisplayName());
			applicationErrorLog.setProcessId(applicationErrorData.getProcessId());
			applicationErrorLog.setProperties(applicationErrorData.getProperties());
			applicationErrorLog.setProviderId(applicationErrorData.getProviderId());
			applicationErrorLog.setProviderName(applicationErrorData.getProviderName());
			applicationErrorLog.setQualifiers(applicationErrorData.getQualifiers());
			applicationErrorLog.setRecordId(applicationErrorData.getRecordId());
			applicationErrorLog.setRelatedActivityId(applicationErrorData.getRelatedActivityId());
			applicationErrorLog.setTask(applicationErrorData.getTask());
			applicationErrorLog.setTaskDisplayName(applicationErrorData.getTaskDisplayName());
			applicationErrorLog.setThreadId(applicationErrorData.getThreadId());
			applicationErrorLog.setSecurityUserId(applicationErrorData.getSecurityUserId());
			applicationErrorLog.setVersion(applicationErrorData.getVersion());
			applicationErrorLog.setMessage(applicationErrorData.getMessage());

			try{applicationErrorLog.setTimeCreatedSystemTime(applicationErrorData.getTimeCreatedSystemTime());}catch(Exception e) {}
			applicationErrorLog.setTimeCurrent(applicationErrorData.getTimeCreated());
			applicationErrorLog.setStatisticsValue(DateTimeConvertor.getStatisticsValue(applicationErrorData.getTimeCreated()));

			applicationErrorLogs.add(applicationErrorLog);
		}

		return applicationErrorLogs;
	}

}
