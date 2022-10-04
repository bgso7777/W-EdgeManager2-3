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
public class HWError {

	public HWError () {
	}

	public HWErrorLogOriginal getOriginalObject(StringBuffer message) {
		HWErrorLogOriginal hWErrorLogOriginal = null;
		return hWErrorLogOriginal;
	}

	public HWErrorLogOriginal getOriginalObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, HWErrorLogOriginal.class);
	}


	private List<HWErrorLogStatistics> reconstructMinutelyDocument(HWErrorLogOriginal hwErrorLogOriginal) {

		List<HWErrorLogStatistics> hWErrorLogMinutelies = new ArrayList<HWErrorLogStatistics>();

		for( HWErrorLogData hWErrorLogData: hwErrorLogOriginal.getHardwareErrorData() ) {

			HWErrorLogStatistics hWErrorLogMinutely = new HWErrorLogStatistics();
			hWErrorLogMinutely.setService(hwErrorLogOriginal.getService());
			hWErrorLogMinutely.setAppId(hwErrorLogOriginal.getAppId());
			hWErrorLogMinutely.setOsType(hwErrorLogOriginal.getOsType());
			hWErrorLogMinutely.setSource(hwErrorLogOriginal.getSource());
			hWErrorLogMinutely.setDeviceId(hwErrorLogOriginal.getDeviceId());
			hWErrorLogMinutely.setIp(hwErrorLogOriginal.getIp());
			hWErrorLogMinutely.setHostName(hwErrorLogOriginal.getHostName());
			hWErrorLogMinutely.setTimeCreated(hwErrorLogOriginal.getTimeCreated());

			hWErrorLogMinutely.setUserId(hwErrorLogOriginal.getUserId());
			hWErrorLogMinutely.setTermNo(hwErrorLogOriginal.getTermNo());
			hWErrorLogMinutely.setSsoBrNo(hwErrorLogOriginal.getSsoBrNo());
			hWErrorLogMinutely.setBrNo(hwErrorLogOriginal.getBrNo());
			hWErrorLogMinutely.setDeptName(hwErrorLogOriginal.getDeptName());
			hWErrorLogMinutely.setHwnNo(hwErrorLogOriginal.getHwnNo());
			hWErrorLogMinutely.setUserName(hwErrorLogOriginal.getUserName());
			hWErrorLogMinutely.setSsoType(hwErrorLogOriginal.getSsoType());
			hWErrorLogMinutely.setPcName(hwErrorLogOriginal.getPcName());
			hWErrorLogMinutely.setPhoneNo(hwErrorLogOriginal.getPhoneNo());
			hWErrorLogMinutely.setJKGP(hwErrorLogOriginal.getJKGP());
			hWErrorLogMinutely.setJKWI(hwErrorLogOriginal.getJKWI());
			hWErrorLogMinutely.setMaxAddress(hwErrorLogOriginal.getMaxAddress());
			hWErrorLogMinutely.setFirstWork(hwErrorLogOriginal.getFirstWork());

			hWErrorLogMinutely.setCount(hWErrorLogMinutely.getCount()+1);

			hWErrorLogMinutely.getLevels().add(hWErrorLogData.getLevel());
			hWErrorLogMinutely.getIds().add(hWErrorLogData.getId());
			hWErrorLogMinutely.getChannels().add(hWErrorLogData.getChannel());
			hWErrorLogMinutely.getActivityIds().add(hWErrorLogData.getActivityId());
			hWErrorLogMinutely.getOpcodeDisplayNames().add(hWErrorLogData.getOpcodeDisplayName());
			hWErrorLogMinutely.getTaskDisplayNames().add(hWErrorLogData.getTaskDisplayName());

			hWErrorLogMinutely.setTimeCurrent(hWErrorLogData.getTimeCreated());
			hWErrorLogMinutely.setStatisticsValue(DateTimeConvertor.getStatisticsValue(hWErrorLogData.getTimeCreated()));

			hWErrorLogMinutelies.add(hWErrorLogMinutely);
		}

		return hWErrorLogMinutelies;
	}

	public List<HWErrorLogDaily> reconstructDailyDocument(HWErrorLogOriginal hwErrorLogOriginal) {

		List<HWErrorLogDaily> hWErrorLogDailies = new ArrayList<HWErrorLogDaily>();

		for( HWErrorLogData hWErrorLogData: hwErrorLogOriginal.getHardwareErrorData() ) {

			HWErrorLogDaily hWErrorLogDaily = new HWErrorLogDaily();
			hWErrorLogDaily.setService(hwErrorLogOriginal.getService());
			hWErrorLogDaily.setAppId(hwErrorLogOriginal.getAppId());
			hWErrorLogDaily.setOsType(hwErrorLogOriginal.getOsType());
			hWErrorLogDaily.setSource(hwErrorLogOriginal.getSource());
			hWErrorLogDaily.setDeviceId(hwErrorLogOriginal.getDeviceId());
			hWErrorLogDaily.setIp(hwErrorLogOriginal.getIp());
			hWErrorLogDaily.setHostName(hwErrorLogOriginal.getHostName());
			hWErrorLogDaily.setTimeCreated(hwErrorLogOriginal.getTimeCreated());

			hWErrorLogDaily.setUserId(hwErrorLogOriginal.getUserId());
			hWErrorLogDaily.setTermNo(hwErrorLogOriginal.getTermNo());
			hWErrorLogDaily.setSsoBrNo(hwErrorLogOriginal.getSsoBrNo());
			hWErrorLogDaily.setBrNo(hwErrorLogOriginal.getBrNo());
			hWErrorLogDaily.setDeptName(hwErrorLogOriginal.getDeptName());
			hWErrorLogDaily.setHwnNo(hwErrorLogOriginal.getHwnNo());
			hWErrorLogDaily.setUserName(hwErrorLogOriginal.getUserName());
			hWErrorLogDaily.setSsoType(hwErrorLogOriginal.getSsoType());
			hWErrorLogDaily.setPcName(hwErrorLogOriginal.getPcName());
			hWErrorLogDaily.setPhoneNo(hwErrorLogOriginal.getPhoneNo());
			hWErrorLogDaily.setJKGP(hwErrorLogOriginal.getJKGP());
			hWErrorLogDaily.setJKWI(hwErrorLogOriginal.getJKWI());
			hWErrorLogDaily.setMaxAddress(hwErrorLogOriginal.getMaxAddress());
			hWErrorLogDaily.setFirstWork(hwErrorLogOriginal.getFirstWork());

			hWErrorLogDaily.setLevel(hWErrorLogData.getLevel());
			hWErrorLogDaily.setEventId(hWErrorLogData.getId());
			hWErrorLogDaily.setChannel(hWErrorLogData.getChannel());
			hWErrorLogDaily.setActivityId(hWErrorLogData.getActivityId());
			hWErrorLogDaily.setBookmark(hWErrorLogData.getBookmark());
			hWErrorLogDaily.setKeywords(hWErrorLogData.getKeywords());
			hWErrorLogDaily.setKeywordsDisplayNames(hWErrorLogData.getKeywordsDisplayNames());
			hWErrorLogDaily.setLevelDisplayName(hWErrorLogData.getLevelDisplayName());
			hWErrorLogDaily.setLogName(hWErrorLogData.getLogName());
			hWErrorLogDaily.setMachineName(hWErrorLogData.getMachineName());
			hWErrorLogDaily.setOpcode(hWErrorLogData.getOpcode());
			hWErrorLogDaily.setOpcodeDisplayName(hWErrorLogData.getOpcodeDisplayName());
			hWErrorLogDaily.setProcessId(hWErrorLogData.getProcessId());
			hWErrorLogDaily.setProperties(hWErrorLogData.getProperties());
			hWErrorLogDaily.setProviderId(hWErrorLogData.getProviderId());
			hWErrorLogDaily.setProviderName(hWErrorLogData.getProviderName());
			hWErrorLogDaily.setQualifiers(hWErrorLogData.getQualifiers());
			hWErrorLogDaily.setRecordId(hWErrorLogData.getRecordId());
			hWErrorLogDaily.setRelatedActivityId(hWErrorLogData.getRelatedActivityId());
			hWErrorLogDaily.setTask(hWErrorLogData.getTask());
			hWErrorLogDaily.setTaskDisplayName(hWErrorLogData.getTaskDisplayName());
			hWErrorLogDaily.setThreadId(hWErrorLogData.getThreadId());
			hWErrorLogDaily.setSecurityUserId(hWErrorLogData.getSecurityUserId());
			hWErrorLogDaily.setVersion(hWErrorLogData.getVersion());
			hWErrorLogDaily.setMessage(hWErrorLogData.getMessage());

			try{hWErrorLogDaily.setTimeCreatedSystemTime(hWErrorLogData.getTimeCreatedSystemTime());}catch(Exception e) {}
			hWErrorLogDaily.setTimeCurrent(hWErrorLogData.getTimeCreated());
			hWErrorLogDaily.setStatisticsValue(DateTimeConvertor.getStatisticsValue(hWErrorLogData.getTimeCreated()));

			hWErrorLogDailies.add(hWErrorLogDaily);
		}

		return hWErrorLogDailies;
	}

	public List<HWErrorLog> reconstructDocument(HWErrorLogOriginal hwErrorLogOriginal) {

		List<HWErrorLog> hWErrorLogs = new ArrayList<HWErrorLog>();

		for( HWErrorLogData hWErrorLogData: hwErrorLogOriginal.getHardwareErrorData() ) {

			HWErrorLog hWErrorLog = new HWErrorLog();
			hWErrorLog.setService(hwErrorLogOriginal.getService());
			hWErrorLog.setAppId(hwErrorLogOriginal.getAppId());
			hWErrorLog.setOsType(hwErrorLogOriginal.getOsType());
			hWErrorLog.setSource(hwErrorLogOriginal.getSource());
			hWErrorLog.setDeviceId(hwErrorLogOriginal.getDeviceId());
			hWErrorLog.setIp(hwErrorLogOriginal.getIp());
			hWErrorLog.setHostName(hwErrorLogOriginal.getHostName());

			hWErrorLog.setUserId(hwErrorLogOriginal.getUserId());
			hWErrorLog.setTermNo(hwErrorLogOriginal.getTermNo());
			hWErrorLog.setSsoBrNo(hwErrorLogOriginal.getSsoBrNo());
			hWErrorLog.setBrNo(hwErrorLogOriginal.getBrNo());
			hWErrorLog.setDeptName(hwErrorLogOriginal.getDeptName());
			hWErrorLog.setHwnNo(hwErrorLogOriginal.getHwnNo());
			hWErrorLog.setUserName(hwErrorLogOriginal.getUserName());
			hWErrorLog.setSsoType(hwErrorLogOriginal.getSsoType());
			hWErrorLog.setPcName(hwErrorLogOriginal.getPcName());
			hWErrorLog.setPhoneNo(hwErrorLogOriginal.getPhoneNo());
			hWErrorLog.setJKGP(hwErrorLogOriginal.getJKGP());
			hWErrorLog.setJKWI(hwErrorLogOriginal.getJKWI());
			hWErrorLog.setMaxAddress(hwErrorLogOriginal.getMaxAddress());
			hWErrorLog.setFirstWork(hwErrorLogOriginal.getFirstWork());
			hWErrorLog.setTimeCreated(hwErrorLogOriginal.getTimeCreated());

			hWErrorLog.setLevel(hWErrorLogData.getLevel());
			hWErrorLog.setEventId(hWErrorLogData.getId());
			hWErrorLog.setChannel(hWErrorLogData.getChannel());
			hWErrorLog.setActivityId(hWErrorLogData.getActivityId());
			hWErrorLog.setBookmark(hWErrorLogData.getBookmark());
			hWErrorLog.setKeywords(hWErrorLogData.getKeywords());
			hWErrorLog.setKeywordsDisplayNames(hWErrorLogData.getKeywordsDisplayNames());
			hWErrorLog.setLevelDisplayName(hWErrorLogData.getLevelDisplayName());
			hWErrorLog.setLogName(hWErrorLogData.getLogName());
			hWErrorLog.setMachineName(hWErrorLogData.getMachineName());
			hWErrorLog.setOpcode(hWErrorLogData.getOpcode());
			hWErrorLog.setOpcodeDisplayName(hWErrorLogData.getOpcodeDisplayName());
			hWErrorLog.setProcessId(hWErrorLogData.getProcessId());
			hWErrorLog.setProperties(hWErrorLogData.getProperties());
			hWErrorLog.setProviderId(hWErrorLogData.getProviderId());
			hWErrorLog.setProviderName(hWErrorLogData.getProviderName());
			hWErrorLog.setQualifiers(hWErrorLogData.getQualifiers());
			hWErrorLog.setRecordId(hWErrorLogData.getRecordId());
			hWErrorLog.setRelatedActivityId(hWErrorLogData.getRelatedActivityId());
			hWErrorLog.setTask(hWErrorLogData.getTask());
			hWErrorLog.setTaskDisplayName(hWErrorLogData.getTaskDisplayName());
			hWErrorLog.setThreadId(hWErrorLogData.getThreadId());
			hWErrorLog.setSecurityUserId(hWErrorLogData.getSecurityUserId());
			hWErrorLog.setVersion(hWErrorLogData.getVersion());
			hWErrorLog.setMessage(hWErrorLogData.getMessage());

			try{hWErrorLog.setTimeCreatedSystemTime(hWErrorLogData.getTimeCreatedSystemTime());}catch(Exception e) {}
			hWErrorLog.setTimeCurrent(hWErrorLogData.getTimeCreated());
			hWErrorLog.setStatisticsValue(DateTimeConvertor.getStatisticsValue(hWErrorLogData.getTimeCreated()));

			hWErrorLogs.add(hWErrorLog);
		}

		return hWErrorLogs;
	}

}
