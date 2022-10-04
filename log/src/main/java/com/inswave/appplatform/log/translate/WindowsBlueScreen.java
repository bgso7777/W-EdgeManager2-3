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
public class WindowsBlueScreen {

	public WindowsBlueScreen () {
	}

	public WindowsBlueScreenLogOriginal getOriginalObject(StringBuffer message) {
		WindowsBlueScreenLogOriginal windowsBlueScreenLogOriginal = null;
		return windowsBlueScreenLogOriginal;
	}

	public WindowsBlueScreenLogOriginal getOriginalObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, WindowsBlueScreenLogOriginal.class);
	}


	private List<WindowsBlueScreenLogStatistics> reconstructMinutelyDocument(WindowsBlueScreenLogOriginal windowsBlueScreenLogOriginal) {

		List<WindowsBlueScreenLogStatistics> windowsBlueScreenLogMinutelies = new ArrayList<WindowsBlueScreenLogStatistics>();

		for( WindowsBlueScreenLogData windowsBlueScreenLogData: windowsBlueScreenLogOriginal.getBlueScreenData() ) {

			WindowsBlueScreenLogStatistics windowsBlueScreenLogMinutely = new WindowsBlueScreenLogStatistics();
			windowsBlueScreenLogMinutely.setService(windowsBlueScreenLogOriginal.getService());
			windowsBlueScreenLogMinutely.setAppId(windowsBlueScreenLogOriginal.getAppId());
			windowsBlueScreenLogMinutely.setOsType(windowsBlueScreenLogOriginal.getOsType());
			windowsBlueScreenLogMinutely.setSource(windowsBlueScreenLogOriginal.getSource());
			windowsBlueScreenLogMinutely.setDeviceId(windowsBlueScreenLogOriginal.getDeviceId());
			windowsBlueScreenLogMinutely.setIp(windowsBlueScreenLogOriginal.getIp());
			windowsBlueScreenLogMinutely.setHostName(windowsBlueScreenLogOriginal.getHostName());
			windowsBlueScreenLogMinutely.setTimeCreated(windowsBlueScreenLogOriginal.getTimeCreated());

			windowsBlueScreenLogMinutely.setUserId(windowsBlueScreenLogOriginal.getUserId());
			windowsBlueScreenLogMinutely.setTermNo(windowsBlueScreenLogOriginal.getTermNo());
			windowsBlueScreenLogMinutely.setSsoBrNo(windowsBlueScreenLogOriginal.getSsoBrNo());
			windowsBlueScreenLogMinutely.setBrNo(windowsBlueScreenLogOriginal.getBrNo());
			windowsBlueScreenLogMinutely.setDeptName(windowsBlueScreenLogOriginal.getDeptName());
			windowsBlueScreenLogMinutely.setHwnNo(windowsBlueScreenLogOriginal.getHwnNo());
			windowsBlueScreenLogMinutely.setUserName(windowsBlueScreenLogOriginal.getUserName());
			windowsBlueScreenLogMinutely.setSsoType(windowsBlueScreenLogOriginal.getSsoType());
			windowsBlueScreenLogMinutely.setPcName(windowsBlueScreenLogOriginal.getPcName());
			windowsBlueScreenLogMinutely.setPhoneNo(windowsBlueScreenLogOriginal.getPhoneNo());
			windowsBlueScreenLogMinutely.setJKGP(windowsBlueScreenLogOriginal.getJKGP());
			windowsBlueScreenLogMinutely.setJKWI(windowsBlueScreenLogOriginal.getJKWI());
			windowsBlueScreenLogMinutely.setMaxAddress(windowsBlueScreenLogOriginal.getMaxAddress());
			windowsBlueScreenLogMinutely.setFirstWork(windowsBlueScreenLogOriginal.getFirstWork());

			windowsBlueScreenLogMinutely.setCount(windowsBlueScreenLogMinutely.getCount()+1);

			windowsBlueScreenLogMinutely.getLevels().add(windowsBlueScreenLogData.getLevel());
			windowsBlueScreenLogMinutely.getIds().add(windowsBlueScreenLogData.getId());
			windowsBlueScreenLogMinutely.getChannels().add(windowsBlueScreenLogData.getChannel());
			windowsBlueScreenLogMinutely.getActivityIds().add(windowsBlueScreenLogData.getActivityId());
			windowsBlueScreenLogMinutely.getOpcodeDisplayNames().add(windowsBlueScreenLogData.getOpcodeDisplayName());
			windowsBlueScreenLogMinutely.getTaskDisplayNames().add(windowsBlueScreenLogData.getTaskDisplayName());

			windowsBlueScreenLogMinutely.setTimeCurrent(windowsBlueScreenLogData.getTimeCreated());
			windowsBlueScreenLogMinutely.setStatisticsValue(DateTimeConvertor.getStatisticsValue(windowsBlueScreenLogData.getTimeCreated()));

			windowsBlueScreenLogMinutelies.add(windowsBlueScreenLogMinutely);
		}

		return windowsBlueScreenLogMinutelies;
	}

	public List<WindowsBlueScreenLogDaily> reconstructDailyDocument(WindowsBlueScreenLogOriginal windowsBlueScreenLogOriginal) {

		List<WindowsBlueScreenLogDaily> windowsBlueScreenLogDailies = new ArrayList<WindowsBlueScreenLogDaily>();

		for( WindowsBlueScreenLogData windowsBlueScreenLogData: windowsBlueScreenLogOriginal.getBlueScreenData() ) {

			WindowsBlueScreenLogDaily windowsBlueScreenLogDaily = new WindowsBlueScreenLogDaily();
			windowsBlueScreenLogDaily.setService(windowsBlueScreenLogOriginal.getService());
			windowsBlueScreenLogDaily.setAppId(windowsBlueScreenLogOriginal.getAppId());
			windowsBlueScreenLogDaily.setOsType(windowsBlueScreenLogOriginal.getOsType());
			windowsBlueScreenLogDaily.setSource(windowsBlueScreenLogOriginal.getSource());
			windowsBlueScreenLogDaily.setDeviceId(windowsBlueScreenLogOriginal.getDeviceId());
			windowsBlueScreenLogDaily.setIp(windowsBlueScreenLogOriginal.getIp());
			windowsBlueScreenLogDaily.setHostName(windowsBlueScreenLogOriginal.getHostName());
			windowsBlueScreenLogDaily.setTimeCreated(windowsBlueScreenLogOriginal.getTimeCreated());

			windowsBlueScreenLogDaily.setUserId(windowsBlueScreenLogOriginal.getUserId());
			windowsBlueScreenLogDaily.setTermNo(windowsBlueScreenLogOriginal.getTermNo());
			windowsBlueScreenLogDaily.setSsoBrNo(windowsBlueScreenLogOriginal.getSsoBrNo());
			windowsBlueScreenLogDaily.setBrNo(windowsBlueScreenLogOriginal.getBrNo());
			windowsBlueScreenLogDaily.setDeptName(windowsBlueScreenLogOriginal.getDeptName());
			windowsBlueScreenLogDaily.setHwnNo(windowsBlueScreenLogOriginal.getHwnNo());
			windowsBlueScreenLogDaily.setUserName(windowsBlueScreenLogOriginal.getUserName());
			windowsBlueScreenLogDaily.setSsoType(windowsBlueScreenLogOriginal.getSsoType());
			windowsBlueScreenLogDaily.setPcName(windowsBlueScreenLogOriginal.getPcName());
			windowsBlueScreenLogDaily.setPhoneNo(windowsBlueScreenLogOriginal.getPhoneNo());
			windowsBlueScreenLogDaily.setJKGP(windowsBlueScreenLogOriginal.getJKGP());
			windowsBlueScreenLogDaily.setJKWI(windowsBlueScreenLogOriginal.getJKWI());
			windowsBlueScreenLogDaily.setMaxAddress(windowsBlueScreenLogOriginal.getMaxAddress());
			windowsBlueScreenLogDaily.setFirstWork(windowsBlueScreenLogOriginal.getFirstWork());

			windowsBlueScreenLogDaily.setLevel(windowsBlueScreenLogData.getLevel());
			windowsBlueScreenLogDaily.setEventId(windowsBlueScreenLogData.getId());
			windowsBlueScreenLogDaily.setChannel(windowsBlueScreenLogData.getChannel());
			windowsBlueScreenLogDaily.setActivityId(windowsBlueScreenLogData.getActivityId());
			windowsBlueScreenLogDaily.setBookmark(windowsBlueScreenLogData.getBookmark());
			windowsBlueScreenLogDaily.setKeywords(windowsBlueScreenLogData.getKeywords());
			windowsBlueScreenLogDaily.setKeywordsDisplayNames(windowsBlueScreenLogData.getKeywordsDisplayNames());
			windowsBlueScreenLogDaily.setLevelDisplayName(windowsBlueScreenLogData.getLevelDisplayName());
			windowsBlueScreenLogDaily.setLogName(windowsBlueScreenLogData.getLogName());
			windowsBlueScreenLogDaily.setMachineName(windowsBlueScreenLogData.getMachineName());
			windowsBlueScreenLogDaily.setOpcode(windowsBlueScreenLogData.getOpcode());
			windowsBlueScreenLogDaily.setOpcodeDisplayName(windowsBlueScreenLogData.getOpcodeDisplayName());
			windowsBlueScreenLogDaily.setProcessId(windowsBlueScreenLogData.getProcessId());
			windowsBlueScreenLogDaily.setProperties(windowsBlueScreenLogData.getProperties());
			windowsBlueScreenLogDaily.setProviderId(windowsBlueScreenLogData.getProviderId());
			windowsBlueScreenLogDaily.setProviderName(windowsBlueScreenLogData.getProviderName());
			windowsBlueScreenLogDaily.setQualifiers(windowsBlueScreenLogData.getQualifiers());
			windowsBlueScreenLogDaily.setRecordId(windowsBlueScreenLogData.getRecordId());
			windowsBlueScreenLogDaily.setRelatedActivityId(windowsBlueScreenLogData.getRelatedActivityId());
			windowsBlueScreenLogDaily.setTask(windowsBlueScreenLogData.getTask());
			windowsBlueScreenLogDaily.setTaskDisplayName(windowsBlueScreenLogData.getTaskDisplayName());
			windowsBlueScreenLogDaily.setThreadId(windowsBlueScreenLogData.getThreadId());
			windowsBlueScreenLogDaily.setSecurityUserId(windowsBlueScreenLogData.getSecurityUserId());
			windowsBlueScreenLogDaily.setVersion(windowsBlueScreenLogData.getVersion());
			windowsBlueScreenLogDaily.setMessage(windowsBlueScreenLogData.getMessage());
			windowsBlueScreenLogDaily.setMinidump(windowsBlueScreenLogData.getMinidump());

			try{windowsBlueScreenLogDaily.setTimeCreatedSystemTime(windowsBlueScreenLogData.getTimeCreatedSystemTime());}catch(Exception e) {}
			windowsBlueScreenLogDaily.setTimeCurrent(windowsBlueScreenLogData.getTimeCreated());
			windowsBlueScreenLogDaily.setStatisticsValue(DateTimeConvertor.getStatisticsValue(windowsBlueScreenLogData.getTimeCreated()));

			windowsBlueScreenLogDailies.add(windowsBlueScreenLogDaily);
		}

		return windowsBlueScreenLogDailies;
	}

	public List<WindowsBlueScreenLog> reconstructDocument(WindowsBlueScreenLogOriginal windowsBlueScreenLogOriginal) {

		List<WindowsBlueScreenLog> windowsBlueScreenLogs = new ArrayList<WindowsBlueScreenLog>();

		for( WindowsBlueScreenLogData windowsBlueScreenLogData: windowsBlueScreenLogOriginal.getBlueScreenData() ) {

			WindowsBlueScreenLog windowsBlueScreenLog = new WindowsBlueScreenLog();
			windowsBlueScreenLog.setService(windowsBlueScreenLogOriginal.getService());
			windowsBlueScreenLog.setAppId(windowsBlueScreenLogOriginal.getAppId());
			windowsBlueScreenLog.setOsType(windowsBlueScreenLogOriginal.getOsType());
			windowsBlueScreenLog.setSource(windowsBlueScreenLogOriginal.getSource());
			windowsBlueScreenLog.setDeviceId(windowsBlueScreenLogOriginal.getDeviceId());
			windowsBlueScreenLog.setIp(windowsBlueScreenLogOriginal.getIp());
			windowsBlueScreenLog.setHostName(windowsBlueScreenLogOriginal.getHostName());
			windowsBlueScreenLog.setTimeCreated(windowsBlueScreenLogOriginal.getTimeCreated());

			windowsBlueScreenLog.setUserId(windowsBlueScreenLogOriginal.getUserId());
			windowsBlueScreenLog.setTermNo(windowsBlueScreenLogOriginal.getTermNo());
			windowsBlueScreenLog.setSsoBrNo(windowsBlueScreenLogOriginal.getSsoBrNo());
			windowsBlueScreenLog.setBrNo(windowsBlueScreenLogOriginal.getBrNo());
			windowsBlueScreenLog.setDeptName(windowsBlueScreenLogOriginal.getDeptName());
			windowsBlueScreenLog.setHwnNo(windowsBlueScreenLogOriginal.getHwnNo());
			windowsBlueScreenLog.setUserName(windowsBlueScreenLogOriginal.getUserName());
			windowsBlueScreenLog.setSsoType(windowsBlueScreenLogOriginal.getSsoType());
			windowsBlueScreenLog.setPcName(windowsBlueScreenLogOriginal.getPcName());
			windowsBlueScreenLog.setPhoneNo(windowsBlueScreenLogOriginal.getPhoneNo());
			windowsBlueScreenLog.setJKGP(windowsBlueScreenLogOriginal.getJKGP());
			windowsBlueScreenLog.setJKWI(windowsBlueScreenLogOriginal.getJKWI());
			windowsBlueScreenLog.setMaxAddress(windowsBlueScreenLogOriginal.getMaxAddress());
			windowsBlueScreenLog.setFirstWork(windowsBlueScreenLogOriginal.getFirstWork());

			windowsBlueScreenLog.setLevel(windowsBlueScreenLogData.getLevel());
			windowsBlueScreenLog.setEventId(windowsBlueScreenLogData.getId());
			windowsBlueScreenLog.setChannel(windowsBlueScreenLogData.getChannel());
			windowsBlueScreenLog.setActivityId(windowsBlueScreenLogData.getActivityId());
			windowsBlueScreenLog.setBookmark(windowsBlueScreenLogData.getBookmark());
			windowsBlueScreenLog.setKeywords(windowsBlueScreenLogData.getKeywords());
			windowsBlueScreenLog.setKeywordsDisplayNames(windowsBlueScreenLogData.getKeywordsDisplayNames());
			windowsBlueScreenLog.setLevelDisplayName(windowsBlueScreenLogData.getLevelDisplayName());
			windowsBlueScreenLog.setLogName(windowsBlueScreenLogData.getLogName());
			windowsBlueScreenLog.setMachineName(windowsBlueScreenLogData.getMachineName());
			windowsBlueScreenLog.setOpcode(windowsBlueScreenLogData.getOpcode());
			windowsBlueScreenLog.setOpcodeDisplayName(windowsBlueScreenLogData.getOpcodeDisplayName());
			windowsBlueScreenLog.setProcessId(windowsBlueScreenLogData.getProcessId());
			windowsBlueScreenLog.setProperties(windowsBlueScreenLogData.getProperties());
			windowsBlueScreenLog.setProviderId(windowsBlueScreenLogData.getProviderId());
			windowsBlueScreenLog.setProviderName(windowsBlueScreenLogData.getProviderName());
			windowsBlueScreenLog.setQualifiers(windowsBlueScreenLogData.getQualifiers());
			windowsBlueScreenLog.setRecordId(windowsBlueScreenLogData.getRecordId());
			windowsBlueScreenLog.setRelatedActivityId(windowsBlueScreenLogData.getRelatedActivityId());
			windowsBlueScreenLog.setTask(windowsBlueScreenLogData.getTask());
			windowsBlueScreenLog.setTaskDisplayName(windowsBlueScreenLogData.getTaskDisplayName());
			windowsBlueScreenLog.setThreadId(windowsBlueScreenLogData.getThreadId());
			windowsBlueScreenLog.setSecurityUserId(windowsBlueScreenLogData.getSecurityUserId());
			windowsBlueScreenLog.setVersion(windowsBlueScreenLogData.getVersion());
			windowsBlueScreenLog.setMessage(windowsBlueScreenLogData.getMessage());
			windowsBlueScreenLog.setMinidump(windowsBlueScreenLogData.getMinidump());

			try{windowsBlueScreenLog.setTimeCreatedSystemTime(windowsBlueScreenLogData.getTimeCreatedSystemTime());}catch(Exception e) {}
			windowsBlueScreenLog.setTimeCurrent(windowsBlueScreenLogData.getTimeCreated());
			windowsBlueScreenLog.setStatisticsValue(DateTimeConvertor.getStatisticsValue(windowsBlueScreenLogData.getTimeCreated()));

			windowsBlueScreenLogs.add(windowsBlueScreenLog);
		}

		return windowsBlueScreenLogs;
	}

}
