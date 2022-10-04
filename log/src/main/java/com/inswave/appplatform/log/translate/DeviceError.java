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
public class DeviceError {

	public DeviceError () {
	}

	public DeviceErrorLogOriginal getOriginalObject(StringBuffer message) {
		DeviceErrorLogOriginal deviceErrorLogOriginal = null;
		return deviceErrorLogOriginal;
	}

	public DeviceErrorLogOriginal getOriginalObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, DeviceErrorLogOriginal.class);
	}


	private List<DeviceErrorLogStatistics> reconstructMinutelyDocument(DeviceErrorLogOriginal deviceErrorLogOriginal) {

		List<DeviceErrorLogStatistics> deviceErrorLogMinutelies = new ArrayList<DeviceErrorLogStatistics>();

		for( DeviceErrorLogData deviceDriverErrorData: deviceErrorLogOriginal.getDeviceDriverErrorData() ) {

			DeviceErrorLogStatistics deviceErrorLogMinutely = new DeviceErrorLogStatistics();
			deviceErrorLogMinutely.setService(deviceErrorLogOriginal.getService());
			deviceErrorLogMinutely.setAppId(deviceErrorLogOriginal.getAppId());
			deviceErrorLogMinutely.setOsType(deviceErrorLogOriginal.getOsType());
			deviceErrorLogMinutely.setSource(deviceErrorLogOriginal.getSource());
			deviceErrorLogMinutely.setDeviceId(deviceErrorLogOriginal.getDeviceId());
			deviceErrorLogMinutely.setIp(deviceErrorLogOriginal.getIp());
			deviceErrorLogMinutely.setHostName(deviceErrorLogOriginal.getHostName());
			deviceErrorLogMinutely.setTimeCreated(deviceErrorLogOriginal.getTimeCreated());

			deviceErrorLogMinutely.setUserId(deviceErrorLogOriginal.getUserId());
			deviceErrorLogMinutely.setTermNo(deviceErrorLogOriginal.getTermNo());
			deviceErrorLogMinutely.setSsoBrNo(deviceErrorLogOriginal.getSsoBrNo());
			deviceErrorLogMinutely.setBrNo(deviceErrorLogOriginal.getBrNo());
			deviceErrorLogMinutely.setDeptName(deviceErrorLogOriginal.getDeptName());
			deviceErrorLogMinutely.setHwnNo(deviceErrorLogOriginal.getHwnNo());
			deviceErrorLogMinutely.setUserName(deviceErrorLogOriginal.getUserName());
			deviceErrorLogMinutely.setSsoType(deviceErrorLogOriginal.getSsoType());
			deviceErrorLogMinutely.setPcName(deviceErrorLogOriginal.getPcName());
			deviceErrorLogMinutely.setPhoneNo(deviceErrorLogOriginal.getPhoneNo());
			deviceErrorLogMinutely.setJKGP(deviceErrorLogOriginal.getJKGP());
			deviceErrorLogMinutely.setJKWI(deviceErrorLogOriginal.getJKWI());
			deviceErrorLogMinutely.setMaxAddress(deviceErrorLogOriginal.getMaxAddress());
			deviceErrorLogMinutely.setFirstWork(deviceErrorLogOriginal.getFirstWork());

			deviceErrorLogMinutely.setCount(deviceErrorLogMinutely.getCount()+1);

			deviceErrorLogMinutely.getLevels().add(deviceDriverErrorData.getLevel());
			deviceErrorLogMinutely.getIds().add(deviceDriverErrorData.getId());
			deviceErrorLogMinutely.getChannels().add(deviceDriverErrorData.getChannel());
			deviceErrorLogMinutely.getActivityIds().add(deviceDriverErrorData.getActivityId());
			deviceErrorLogMinutely.getOpcodeDisplayNames().add(deviceDriverErrorData.getOpcodeDisplayName());
			deviceErrorLogMinutely.getTaskDisplayNames().add(deviceDriverErrorData.getTaskDisplayName());

			deviceErrorLogMinutely.setTimeCurrent(deviceDriverErrorData.getTimeCreated());
			deviceErrorLogMinutely.setStatisticsValue(DateTimeConvertor.getStatisticsValue(deviceDriverErrorData.getTimeCreated()));

			deviceErrorLogMinutelies.add(deviceErrorLogMinutely);
		}

		return deviceErrorLogMinutelies;
	}

	public List<DeviceErrorLogDaily> reconstructDailyDocument(DeviceErrorLogOriginal deviceErrorLogOriginal) {

		List<DeviceErrorLogDaily> deviceErrorLogDailies = new ArrayList<DeviceErrorLogDaily>();

		for( DeviceErrorLogData deviceDriverErrorData: deviceErrorLogOriginal.getDeviceDriverErrorData() ) {

			DeviceErrorLogDaily deviceErrorLogDaily = new DeviceErrorLogDaily();
			deviceErrorLogDaily.setService(deviceErrorLogOriginal.getService());
			deviceErrorLogDaily.setAppId(deviceErrorLogOriginal.getAppId());
			deviceErrorLogDaily.setOsType(deviceErrorLogOriginal.getOsType());
			deviceErrorLogDaily.setSource(deviceErrorLogOriginal.getSource());
			deviceErrorLogDaily.setDeviceId(deviceErrorLogOriginal.getDeviceId());
			deviceErrorLogDaily.setIp(deviceErrorLogOriginal.getIp());
			deviceErrorLogDaily.setHostName(deviceErrorLogOriginal.getHostName());
			deviceErrorLogDaily.setTimeCreated(deviceErrorLogOriginal.getTimeCreated());

			deviceErrorLogDaily.setUserId(deviceErrorLogOriginal.getUserId());
			deviceErrorLogDaily.setTermNo(deviceErrorLogOriginal.getTermNo());
			deviceErrorLogDaily.setSsoBrNo(deviceErrorLogOriginal.getSsoBrNo());
			deviceErrorLogDaily.setBrNo(deviceErrorLogOriginal.getBrNo());
			deviceErrorLogDaily.setDeptName(deviceErrorLogOriginal.getDeptName());
			deviceErrorLogDaily.setHwnNo(deviceErrorLogOriginal.getHwnNo());
			deviceErrorLogDaily.setUserName(deviceErrorLogOriginal.getUserName());
			deviceErrorLogDaily.setSsoType(deviceErrorLogOriginal.getSsoType());
			deviceErrorLogDaily.setPcName(deviceErrorLogOriginal.getPcName());
			deviceErrorLogDaily.setPhoneNo(deviceErrorLogOriginal.getPhoneNo());
			deviceErrorLogDaily.setJKGP(deviceErrorLogOriginal.getJKGP());
			deviceErrorLogDaily.setJKWI(deviceErrorLogOriginal.getJKWI());
			deviceErrorLogDaily.setMaxAddress(deviceErrorLogOriginal.getMaxAddress());
			deviceErrorLogDaily.setFirstWork(deviceErrorLogOriginal.getFirstWork());

			deviceErrorLogDaily.setLevel(deviceDriverErrorData.getLevel());
			deviceErrorLogDaily.setEventId(deviceDriverErrorData.getId());
			deviceErrorLogDaily.setChannel(deviceDriverErrorData.getChannel());
			deviceErrorLogDaily.setActivityId(deviceDriverErrorData.getActivityId());
			deviceErrorLogDaily.setBookmark(deviceDriverErrorData.getBookmark());
			deviceErrorLogDaily.setKeywords(deviceDriverErrorData.getKeywords());
			deviceErrorLogDaily.setKeywordsDisplayNames(deviceDriverErrorData.getKeywordsDisplayNames());
			deviceErrorLogDaily.setLevelDisplayName(deviceDriverErrorData.getLevelDisplayName());
			deviceErrorLogDaily.setLogName(deviceDriverErrorData.getLogName());
			deviceErrorLogDaily.setMachineName(deviceDriverErrorData.getMachineName());
			deviceErrorLogDaily.setOpcode(deviceDriverErrorData.getOpcode());
			deviceErrorLogDaily.setOpcodeDisplayName(deviceDriverErrorData.getOpcodeDisplayName());
			deviceErrorLogDaily.setProcessId(deviceDriverErrorData.getProcessId());
			deviceErrorLogDaily.setProperties(deviceDriverErrorData.getProperties());
			deviceErrorLogDaily.setProviderId(deviceDriverErrorData.getProviderId());
			deviceErrorLogDaily.setProviderName(deviceDriverErrorData.getProviderName());
			deviceErrorLogDaily.setQualifiers(deviceDriverErrorData.getQualifiers());
			deviceErrorLogDaily.setRecordId(deviceDriverErrorData.getRecordId());
			deviceErrorLogDaily.setRelatedActivityId(deviceDriverErrorData.getRelatedActivityId());
			deviceErrorLogDaily.setTask(deviceDriverErrorData.getTask());
			deviceErrorLogDaily.setTaskDisplayName(deviceDriverErrorData.getTaskDisplayName());
			deviceErrorLogDaily.setThreadId(deviceDriverErrorData.getThreadId());
			deviceErrorLogDaily.setSecurityUserId(deviceDriverErrorData.getSecurityUserId());
			deviceErrorLogDaily.setVersion(deviceDriverErrorData.getVersion());
			deviceErrorLogDaily.setMessage(deviceDriverErrorData.getMessage());

			try{deviceErrorLogDaily.setTimeCreatedSystemTime(deviceDriverErrorData.getTimeCreatedSystemTime());}catch(Exception e) {}
			deviceErrorLogDaily.setTimeCurrent(deviceDriverErrorData.getTimeCreated());
			deviceErrorLogDaily.setStatisticsValue(DateTimeConvertor.getStatisticsValue(deviceDriverErrorData.getTimeCreated()));

			deviceErrorLogDailies.add(deviceErrorLogDaily);
		}

		return deviceErrorLogDailies;
	}

	public List<DeviceErrorLog> reconstructDocument(DeviceErrorLogOriginal deviceErrorLogOriginal) {

		List<DeviceErrorLog> deviceErrorLogs = new ArrayList<DeviceErrorLog>();

		for( DeviceErrorLogData deviceDriverErrorData: deviceErrorLogOriginal.getDeviceDriverErrorData() ) {

			DeviceErrorLog deviceErrorLog = new DeviceErrorLog();
			deviceErrorLog.setService(deviceErrorLogOriginal.getService());
			deviceErrorLog.setAppId(deviceErrorLogOriginal.getAppId());
			deviceErrorLog.setOsType(deviceErrorLogOriginal.getOsType());
			deviceErrorLog.setSource(deviceErrorLogOriginal.getSource());
			deviceErrorLog.setDeviceId(deviceErrorLogOriginal.getDeviceId());
			deviceErrorLog.setIp(deviceErrorLogOriginal.getIp());
			deviceErrorLog.setHostName(deviceErrorLogOriginal.getHostName());
			deviceErrorLog.setTimeCreated(deviceErrorLogOriginal.getTimeCreated());

			deviceErrorLog.setUserId(deviceErrorLogOriginal.getUserId());
			deviceErrorLog.setTermNo(deviceErrorLogOriginal.getTermNo());
			deviceErrorLog.setSsoBrNo(deviceErrorLogOriginal.getSsoBrNo());
			deviceErrorLog.setBrNo(deviceErrorLogOriginal.getBrNo());
			deviceErrorLog.setDeptName(deviceErrorLogOriginal.getDeptName());
			deviceErrorLog.setHwnNo(deviceErrorLogOriginal.getHwnNo());
			deviceErrorLog.setUserName(deviceErrorLogOriginal.getUserName());
			deviceErrorLog.setSsoType(deviceErrorLogOriginal.getSsoType());
			deviceErrorLog.setPcName(deviceErrorLogOriginal.getPcName());
			deviceErrorLog.setPhoneNo(deviceErrorLogOriginal.getPhoneNo());
			deviceErrorLog.setJKGP(deviceErrorLogOriginal.getJKGP());
			deviceErrorLog.setJKWI(deviceErrorLogOriginal.getJKWI());
			deviceErrorLog.setMaxAddress(deviceErrorLogOriginal.getMaxAddress());
			deviceErrorLog.setFirstWork(deviceErrorLogOriginal.getFirstWork());

			deviceErrorLog.setLevel(deviceDriverErrorData.getLevel());
			deviceErrorLog.setEventId(deviceDriverErrorData.getId());
			deviceErrorLog.setChannel(deviceDriverErrorData.getChannel());
			deviceErrorLog.setActivityId(deviceDriverErrorData.getActivityId());
			deviceErrorLog.setBookmark(deviceDriverErrorData.getBookmark());
			deviceErrorLog.setKeywords(deviceDriverErrorData.getKeywords());
			deviceErrorLog.setKeywordsDisplayNames(deviceDriverErrorData.getKeywordsDisplayNames());
			deviceErrorLog.setLevelDisplayName(deviceDriverErrorData.getLevelDisplayName());
			deviceErrorLog.setLogName(deviceDriverErrorData.getLogName());
			deviceErrorLog.setMachineName(deviceDriverErrorData.getMachineName());
			deviceErrorLog.setOpcode(deviceDriverErrorData.getOpcode());
			deviceErrorLog.setOpcodeDisplayName(deviceDriverErrorData.getOpcodeDisplayName());
			deviceErrorLog.setProcessId(deviceDriverErrorData.getProcessId());
			deviceErrorLog.setProperties(deviceDriverErrorData.getProperties());
			deviceErrorLog.setProviderId(deviceDriverErrorData.getProviderId());
			deviceErrorLog.setProviderName(deviceDriverErrorData.getProviderName());
			deviceErrorLog.setQualifiers(deviceDriverErrorData.getQualifiers());
			deviceErrorLog.setRecordId(deviceDriverErrorData.getRecordId());
			deviceErrorLog.setRelatedActivityId(deviceDriverErrorData.getRelatedActivityId());
			deviceErrorLog.setTask(deviceDriverErrorData.getTask());
			deviceErrorLog.setTaskDisplayName(deviceDriverErrorData.getTaskDisplayName());
			deviceErrorLog.setThreadId(deviceDriverErrorData.getThreadId());
			deviceErrorLog.setSecurityUserId(deviceDriverErrorData.getSecurityUserId());
			deviceErrorLog.setVersion(deviceDriverErrorData.getVersion());
			deviceErrorLog.setMessage(deviceDriverErrorData.getMessage());

			deviceErrorLog.setTimeCurrent(deviceDriverErrorData.getTimeCreated());
			try{deviceErrorLog.setTimeCreatedSystemTime(deviceDriverErrorData.getTimeCreatedSystemTime());}catch(Exception e) {}
			deviceErrorLog.setStatisticsValue(DateTimeConvertor.getStatisticsValue(deviceDriverErrorData.getTimeCreated()));

			deviceErrorLogs.add(deviceErrorLog);
		}

		return deviceErrorLogs;
	}

}
