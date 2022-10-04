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
public class WindowsEventSystemErrorAll {

	public WindowsEventSystemErrorAll () {
	}

	public WindowsEventSystemErrorAllLogOriginal getOriginalObject(StringBuffer message) {
		WindowsEventSystemErrorAllLogOriginal windowsEventSystemErrorAllLogOriginal = null;
		return windowsEventSystemErrorAllLogOriginal;
	}

	public WindowsEventSystemErrorAllLogOriginal getOriginalObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, WindowsEventSystemErrorAllLogOriginal.class);
	}


	private List<WindowsEventSystemErrorAllLogStatistics> reconstructMinutelyDocument(WindowsEventSystemErrorAllLogOriginal windowsEventSystemErrorAllLogOriginal) {

		List<WindowsEventSystemErrorAllLogStatistics> windowsEventSystemErrorAllLogMinutelies = new ArrayList<WindowsEventSystemErrorAllLogStatistics>();

		for( WindowsEventSystemErrorAllLogData collectWindowsEventLogData: windowsEventSystemErrorAllLogOriginal.getCollectWindowsEventLogData() ) {

			WindowsEventSystemErrorAllLogStatistics windowsEventSystemErrorAllLogMinutely = new WindowsEventSystemErrorAllLogStatistics();
			windowsEventSystemErrorAllLogMinutely.setService(windowsEventSystemErrorAllLogOriginal.getService());
			windowsEventSystemErrorAllLogMinutely.setAppId(windowsEventSystemErrorAllLogOriginal.getAppId());
			windowsEventSystemErrorAllLogMinutely.setOsType(windowsEventSystemErrorAllLogOriginal.getOsType());
			windowsEventSystemErrorAllLogMinutely.setSource(windowsEventSystemErrorAllLogOriginal.getSource());
			windowsEventSystemErrorAllLogMinutely.setDeviceId(windowsEventSystemErrorAllLogOriginal.getDeviceId());
			windowsEventSystemErrorAllLogMinutely.setIp(windowsEventSystemErrorAllLogOriginal.getIp());
			windowsEventSystemErrorAllLogMinutely.setHostName(windowsEventSystemErrorAllLogOriginal.getHostName());
			windowsEventSystemErrorAllLogMinutely.setTimeCreated(windowsEventSystemErrorAllLogOriginal.getTimeCreated());

			windowsEventSystemErrorAllLogMinutely.setUserId(windowsEventSystemErrorAllLogOriginal.getUserId());
			windowsEventSystemErrorAllLogMinutely.setTermNo(windowsEventSystemErrorAllLogOriginal.getTermNo());
			windowsEventSystemErrorAllLogMinutely.setSsoBrNo(windowsEventSystemErrorAllLogOriginal.getSsoBrNo());
			windowsEventSystemErrorAllLogMinutely.setBrNo(windowsEventSystemErrorAllLogOriginal.getBrNo());
			windowsEventSystemErrorAllLogMinutely.setDeptName(windowsEventSystemErrorAllLogOriginal.getDeptName());
			windowsEventSystemErrorAllLogMinutely.setHwnNo(windowsEventSystemErrorAllLogOriginal.getHwnNo());
			windowsEventSystemErrorAllLogMinutely.setUserName(windowsEventSystemErrorAllLogOriginal.getUserName());
			windowsEventSystemErrorAllLogMinutely.setSsoType(windowsEventSystemErrorAllLogOriginal.getSsoType());
			windowsEventSystemErrorAllLogMinutely.setPcName(windowsEventSystemErrorAllLogOriginal.getPcName());
			windowsEventSystemErrorAllLogMinutely.setPhoneNo(windowsEventSystemErrorAllLogOriginal.getPhoneNo());
			windowsEventSystemErrorAllLogMinutely.setJKGP(windowsEventSystemErrorAllLogOriginal.getJKGP());
			windowsEventSystemErrorAllLogMinutely.setJKWI(windowsEventSystemErrorAllLogOriginal.getJKWI());
			windowsEventSystemErrorAllLogMinutely.setMaxAddress(windowsEventSystemErrorAllLogOriginal.getMaxAddress());
			windowsEventSystemErrorAllLogMinutely.setFirstWork(windowsEventSystemErrorAllLogOriginal.getFirstWork());

			windowsEventSystemErrorAllLogMinutely.setCount(windowsEventSystemErrorAllLogMinutely.getCount()+1);

			windowsEventSystemErrorAllLogMinutely.getLevels().add(collectWindowsEventLogData.getLevel());
			windowsEventSystemErrorAllLogMinutely.getIds().add(collectWindowsEventLogData.getId());
			windowsEventSystemErrorAllLogMinutely.getChannels().add(collectWindowsEventLogData.getChannel());
			windowsEventSystemErrorAllLogMinutely.getActivityIds().add(collectWindowsEventLogData.getActivityId());
			windowsEventSystemErrorAllLogMinutely.getOpcodeDisplayNames().add(collectWindowsEventLogData.getOpcodeDisplayName());
			windowsEventSystemErrorAllLogMinutely.getTaskDisplayNames().add(collectWindowsEventLogData.getTaskDisplayName());

			windowsEventSystemErrorAllLogMinutely.setTimeCurrent(collectWindowsEventLogData.getTimeCreated());
			windowsEventSystemErrorAllLogMinutely.setStatisticsValue(DateTimeConvertor.getStatisticsValue(collectWindowsEventLogData.getTimeCreated()));

			windowsEventSystemErrorAllLogMinutelies.add(windowsEventSystemErrorAllLogMinutely);
		}

		return windowsEventSystemErrorAllLogMinutelies;
	}

	public List<WindowsEventSystemErrorAllLogDaily> reconstructDailyDocument(WindowsEventSystemErrorAllLogOriginal windowsEventSystemErrorAllLogOriginal) {

		List<WindowsEventSystemErrorAllLogDaily> windowsEventSystemErrorAllLogDailies = new ArrayList<WindowsEventSystemErrorAllLogDaily>();

		for( WindowsEventSystemErrorAllLogData collectWindowsEventLogData: windowsEventSystemErrorAllLogOriginal.getCollectWindowsEventLogData() ) {

			WindowsEventSystemErrorAllLogDaily windowsEventSystemErrorAllLogDaily = new WindowsEventSystemErrorAllLogDaily();
			windowsEventSystemErrorAllLogDaily.setService(windowsEventSystemErrorAllLogOriginal.getService());
			windowsEventSystemErrorAllLogDaily.setAppId(windowsEventSystemErrorAllLogOriginal.getAppId());
			windowsEventSystemErrorAllLogDaily.setOsType(windowsEventSystemErrorAllLogOriginal.getOsType());
			windowsEventSystemErrorAllLogDaily.setSource(windowsEventSystemErrorAllLogOriginal.getSource());
			windowsEventSystemErrorAllLogDaily.setDeviceId(windowsEventSystemErrorAllLogOriginal.getDeviceId());
			windowsEventSystemErrorAllLogDaily.setIp(windowsEventSystemErrorAllLogOriginal.getIp());
			windowsEventSystemErrorAllLogDaily.setHostName(windowsEventSystemErrorAllLogOriginal.getHostName());
			windowsEventSystemErrorAllLogDaily.setTimeCreated(windowsEventSystemErrorAllLogOriginal.getTimeCreated());

			windowsEventSystemErrorAllLogDaily.setUserId(windowsEventSystemErrorAllLogOriginal.getUserId());
			windowsEventSystemErrorAllLogDaily.setTermNo(windowsEventSystemErrorAllLogOriginal.getTermNo());
			windowsEventSystemErrorAllLogDaily.setSsoBrNo(windowsEventSystemErrorAllLogOriginal.getSsoBrNo());
			windowsEventSystemErrorAllLogDaily.setBrNo(windowsEventSystemErrorAllLogOriginal.getBrNo());
			windowsEventSystemErrorAllLogDaily.setDeptName(windowsEventSystemErrorAllLogOriginal.getDeptName());
			windowsEventSystemErrorAllLogDaily.setHwnNo(windowsEventSystemErrorAllLogOriginal.getHwnNo());
			windowsEventSystemErrorAllLogDaily.setUserName(windowsEventSystemErrorAllLogOriginal.getUserName());
			windowsEventSystemErrorAllLogDaily.setSsoType(windowsEventSystemErrorAllLogOriginal.getSsoType());
			windowsEventSystemErrorAllLogDaily.setPcName(windowsEventSystemErrorAllLogOriginal.getPcName());
			windowsEventSystemErrorAllLogDaily.setPhoneNo(windowsEventSystemErrorAllLogOriginal.getPhoneNo());
			windowsEventSystemErrorAllLogDaily.setJKGP(windowsEventSystemErrorAllLogOriginal.getJKGP());
			windowsEventSystemErrorAllLogDaily.setJKWI(windowsEventSystemErrorAllLogOriginal.getJKWI());
			windowsEventSystemErrorAllLogDaily.setMaxAddress(windowsEventSystemErrorAllLogOriginal.getMaxAddress());
			windowsEventSystemErrorAllLogDaily.setFirstWork(windowsEventSystemErrorAllLogOriginal.getFirstWork());

			windowsEventSystemErrorAllLogDaily.setLevel(collectWindowsEventLogData.getLevel());
			windowsEventSystemErrorAllLogDaily.setEventId(collectWindowsEventLogData.getId());
			windowsEventSystemErrorAllLogDaily.setChannel(collectWindowsEventLogData.getChannel());
			windowsEventSystemErrorAllLogDaily.setActivityId(collectWindowsEventLogData.getActivityId());
			windowsEventSystemErrorAllLogDaily.setBookmark(collectWindowsEventLogData.getBookmark());
			windowsEventSystemErrorAllLogDaily.setKeywords(collectWindowsEventLogData.getKeywords());
			windowsEventSystemErrorAllLogDaily.setKeywordsDisplayNames(collectWindowsEventLogData.getKeywordsDisplayNames());
			windowsEventSystemErrorAllLogDaily.setLevelDisplayName(collectWindowsEventLogData.getLevelDisplayName());
			windowsEventSystemErrorAllLogDaily.setLogName(collectWindowsEventLogData.getLogName());
			windowsEventSystemErrorAllLogDaily.setMachineName(collectWindowsEventLogData.getMachineName());
			windowsEventSystemErrorAllLogDaily.setOpcode(collectWindowsEventLogData.getOpcode());
			windowsEventSystemErrorAllLogDaily.setOpcodeDisplayName(collectWindowsEventLogData.getOpcodeDisplayName());
			windowsEventSystemErrorAllLogDaily.setProcessId(collectWindowsEventLogData.getProcessId());
			windowsEventSystemErrorAllLogDaily.setProperties(collectWindowsEventLogData.getProperties());
			windowsEventSystemErrorAllLogDaily.setProviderId(collectWindowsEventLogData.getProviderId());
			windowsEventSystemErrorAllLogDaily.setProviderName(collectWindowsEventLogData.getProviderName());
			windowsEventSystemErrorAllLogDaily.setQualifiers(collectWindowsEventLogData.getQualifiers());
			windowsEventSystemErrorAllLogDaily.setRecordId(collectWindowsEventLogData.getRecordId());
			windowsEventSystemErrorAllLogDaily.setRelatedActivityId(collectWindowsEventLogData.getRelatedActivityId());
			windowsEventSystemErrorAllLogDaily.setTask(collectWindowsEventLogData.getTask());
			windowsEventSystemErrorAllLogDaily.setTaskDisplayName(collectWindowsEventLogData.getTaskDisplayName());
			windowsEventSystemErrorAllLogDaily.setThreadId(collectWindowsEventLogData.getThreadId());
			windowsEventSystemErrorAllLogDaily.setSecurityUserId(collectWindowsEventLogData.getSecurityUserId());
			windowsEventSystemErrorAllLogDaily.setVersion(collectWindowsEventLogData.getVersion());
			windowsEventSystemErrorAllLogDaily.setMessage(collectWindowsEventLogData.getMessage());

			try{windowsEventSystemErrorAllLogDaily.setTimeCreatedSystemTime(collectWindowsEventLogData.getTimeCreatedSystemTime());}catch(Exception e) {}
			windowsEventSystemErrorAllLogDaily.setTimeCurrent(collectWindowsEventLogData.getTimeCreated());
			windowsEventSystemErrorAllLogDaily.setStatisticsValue(DateTimeConvertor.getStatisticsValue(collectWindowsEventLogData.getTimeCreated()));

			windowsEventSystemErrorAllLogDailies.add(windowsEventSystemErrorAllLogDaily);
		}

		return windowsEventSystemErrorAllLogDailies;
	}

	public List<WindowsEventSystemErrorAllLog> reconstructDocument(WindowsEventSystemErrorAllLogOriginal windowsEventSystemErrorAllLogOriginal) {

		List<WindowsEventSystemErrorAllLog> windowsEventSystemErrorAllLogs = new ArrayList<WindowsEventSystemErrorAllLog>();

		for( WindowsEventSystemErrorAllLogData collectWindowsEventLogData: windowsEventSystemErrorAllLogOriginal.getCollectWindowsEventLogData() ) {

			WindowsEventSystemErrorAllLog windowsEventSystemErrorAllLog = new WindowsEventSystemErrorAllLog();
			windowsEventSystemErrorAllLog.setService(windowsEventSystemErrorAllLogOriginal.getService());
			windowsEventSystemErrorAllLog.setAppId(windowsEventSystemErrorAllLogOriginal.getAppId());
			windowsEventSystemErrorAllLog.setOsType(windowsEventSystemErrorAllLogOriginal.getOsType());
			windowsEventSystemErrorAllLog.setSource(windowsEventSystemErrorAllLogOriginal.getSource());
			windowsEventSystemErrorAllLog.setDeviceId(windowsEventSystemErrorAllLogOriginal.getDeviceId());
			windowsEventSystemErrorAllLog.setIp(windowsEventSystemErrorAllLogOriginal.getIp());
			windowsEventSystemErrorAllLog.setHostName(windowsEventSystemErrorAllLogOriginal.getHostName());
			windowsEventSystemErrorAllLog.setTimeCreated(windowsEventSystemErrorAllLogOriginal.getTimeCreated());

			windowsEventSystemErrorAllLog.setUserId(windowsEventSystemErrorAllLogOriginal.getUserId());
			windowsEventSystemErrorAllLog.setTermNo(windowsEventSystemErrorAllLogOriginal.getTermNo());
			windowsEventSystemErrorAllLog.setSsoBrNo(windowsEventSystemErrorAllLogOriginal.getSsoBrNo());
			windowsEventSystemErrorAllLog.setBrNo(windowsEventSystemErrorAllLogOriginal.getBrNo());
			windowsEventSystemErrorAllLog.setDeptName(windowsEventSystemErrorAllLogOriginal.getDeptName());
			windowsEventSystemErrorAllLog.setHwnNo(windowsEventSystemErrorAllLogOriginal.getHwnNo());
			windowsEventSystemErrorAllLog.setUserName(windowsEventSystemErrorAllLogOriginal.getUserName());
			windowsEventSystemErrorAllLog.setSsoType(windowsEventSystemErrorAllLogOriginal.getSsoType());
			windowsEventSystemErrorAllLog.setPcName(windowsEventSystemErrorAllLogOriginal.getPcName());
			windowsEventSystemErrorAllLog.setPhoneNo(windowsEventSystemErrorAllLogOriginal.getPhoneNo());
			windowsEventSystemErrorAllLog.setJKGP(windowsEventSystemErrorAllLogOriginal.getJKGP());
			windowsEventSystemErrorAllLog.setJKWI(windowsEventSystemErrorAllLogOriginal.getJKWI());
			windowsEventSystemErrorAllLog.setMaxAddress(windowsEventSystemErrorAllLogOriginal.getMaxAddress());
			windowsEventSystemErrorAllLog.setFirstWork(windowsEventSystemErrorAllLogOriginal.getFirstWork());

			windowsEventSystemErrorAllLog.setLevel(collectWindowsEventLogData.getLevel());
			windowsEventSystemErrorAllLog.setEventId(collectWindowsEventLogData.getId());
			windowsEventSystemErrorAllLog.setChannel(collectWindowsEventLogData.getChannel());
			windowsEventSystemErrorAllLog.setActivityId(collectWindowsEventLogData.getActivityId());
			windowsEventSystemErrorAllLog.setBookmark(collectWindowsEventLogData.getBookmark());
			windowsEventSystemErrorAllLog.setKeywords(collectWindowsEventLogData.getKeywords());
			windowsEventSystemErrorAllLog.setKeywordsDisplayNames(collectWindowsEventLogData.getKeywordsDisplayNames());
			windowsEventSystemErrorAllLog.setLevelDisplayName(collectWindowsEventLogData.getLevelDisplayName());
			windowsEventSystemErrorAllLog.setLogName(collectWindowsEventLogData.getLogName());
			windowsEventSystemErrorAllLog.setMachineName(collectWindowsEventLogData.getMachineName());
			windowsEventSystemErrorAllLog.setOpcode(collectWindowsEventLogData.getOpcode());
			windowsEventSystemErrorAllLog.setOpcodeDisplayName(collectWindowsEventLogData.getOpcodeDisplayName());
			windowsEventSystemErrorAllLog.setProcessId(collectWindowsEventLogData.getProcessId());
			windowsEventSystemErrorAllLog.setProperties(collectWindowsEventLogData.getProperties());
			windowsEventSystemErrorAllLog.setProviderId(collectWindowsEventLogData.getProviderId());
			windowsEventSystemErrorAllLog.setProviderName(collectWindowsEventLogData.getProviderName());
			windowsEventSystemErrorAllLog.setQualifiers(collectWindowsEventLogData.getQualifiers());
			windowsEventSystemErrorAllLog.setRecordId(collectWindowsEventLogData.getRecordId());
			windowsEventSystemErrorAllLog.setRelatedActivityId(collectWindowsEventLogData.getRelatedActivityId());
			windowsEventSystemErrorAllLog.setTask(collectWindowsEventLogData.getTask());
			windowsEventSystemErrorAllLog.setTaskDisplayName(collectWindowsEventLogData.getTaskDisplayName());
			windowsEventSystemErrorAllLog.setThreadId(collectWindowsEventLogData.getThreadId());
			windowsEventSystemErrorAllLog.setSecurityUserId(collectWindowsEventLogData.getSecurityUserId());
			windowsEventSystemErrorAllLog.setVersion(collectWindowsEventLogData.getVersion());
			windowsEventSystemErrorAllLog.setMessage(collectWindowsEventLogData.getMessage());

			try{windowsEventSystemErrorAllLog.setTimeCreatedSystemTime(collectWindowsEventLogData.getTimeCreatedSystemTime());}catch(Exception e) {}
			windowsEventSystemErrorAllLog.setTimeCurrent(collectWindowsEventLogData.getTimeCreated());
			windowsEventSystemErrorAllLog.setStatisticsValue(DateTimeConvertor.getStatisticsValue(collectWindowsEventLogData.getTimeCreated()));

			windowsEventSystemErrorAllLogs.add(windowsEventSystemErrorAllLog);
		}

		return windowsEventSystemErrorAllLogs;
	}

}
