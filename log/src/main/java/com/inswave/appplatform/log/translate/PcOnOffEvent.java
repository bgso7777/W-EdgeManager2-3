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
public class PcOnOffEvent {

	public PcOnOffEvent () {
	}

	public PcOnOffEventLogOriginal getOriginalObject(StringBuffer message) {
		PcOnOffEventLogOriginal pcOnOffEventLogOriginal = null;
		return pcOnOffEventLogOriginal;
	}

	public PcOnOffEventLogOriginal getOriginalObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, PcOnOffEventLogOriginal.class);
	}


	private List<PcOnOffEventLogStatistics> reconstructMinutelyDocument(PcOnOffEventLogOriginal pcOnOffEventLogOriginal) {

		List<PcOnOffEventLogStatistics> pcOnOffEventLogMinutelies = new ArrayList<PcOnOffEventLogStatistics>();

		for( PcOnOffEventLogData pcOnOffEventLogData: pcOnOffEventLogOriginal.getPcOnOffEventLogData() ) {

			PcOnOffEventLogStatistics pcOnOffEventLogMinutely = new PcOnOffEventLogStatistics();
			pcOnOffEventLogMinutely.setService(pcOnOffEventLogOriginal.getService());
			pcOnOffEventLogMinutely.setAppId(pcOnOffEventLogOriginal.getAppId());
			pcOnOffEventLogMinutely.setOsType(pcOnOffEventLogOriginal.getOsType());
			pcOnOffEventLogMinutely.setSource(pcOnOffEventLogOriginal.getSource());
			pcOnOffEventLogMinutely.setDeviceId(pcOnOffEventLogOriginal.getDeviceId());
			pcOnOffEventLogMinutely.setIp(pcOnOffEventLogOriginal.getIp());
			pcOnOffEventLogMinutely.setHostName(pcOnOffEventLogOriginal.getHostName());
			pcOnOffEventLogMinutely.setTimeCreated(pcOnOffEventLogOriginal.getTimeCreated());

			pcOnOffEventLogMinutely.setUserId(pcOnOffEventLogOriginal.getUserId());
			pcOnOffEventLogMinutely.setTermNo(pcOnOffEventLogOriginal.getTermNo());
			pcOnOffEventLogMinutely.setSsoBrNo(pcOnOffEventLogOriginal.getSsoBrNo());
			pcOnOffEventLogMinutely.setBrNo(pcOnOffEventLogOriginal.getBrNo());
			pcOnOffEventLogMinutely.setDeptName(pcOnOffEventLogOriginal.getDeptName());
			pcOnOffEventLogMinutely.setHwnNo(pcOnOffEventLogOriginal.getHwnNo());
			pcOnOffEventLogMinutely.setUserName(pcOnOffEventLogOriginal.getUserName());
			pcOnOffEventLogMinutely.setSsoType(pcOnOffEventLogOriginal.getSsoType());
			pcOnOffEventLogMinutely.setPcName(pcOnOffEventLogOriginal.getPcName());
			pcOnOffEventLogMinutely.setPhoneNo(pcOnOffEventLogOriginal.getPhoneNo());
			pcOnOffEventLogMinutely.setJKGP(pcOnOffEventLogOriginal.getJKGP());
			pcOnOffEventLogMinutely.setJKWI(pcOnOffEventLogOriginal.getJKWI());
			pcOnOffEventLogMinutely.setMaxAddress(pcOnOffEventLogOriginal.getMaxAddress());
			pcOnOffEventLogMinutely.setFirstWork(pcOnOffEventLogOriginal.getFirstWork());

			pcOnOffEventLogMinutely.setCount(pcOnOffEventLogMinutely.getCount()+1);

			pcOnOffEventLogMinutely.getLevels().add(pcOnOffEventLogData.getLevel());
			pcOnOffEventLogMinutely.getIds().add(pcOnOffEventLogData.getId());
			pcOnOffEventLogMinutely.getChannels().add(pcOnOffEventLogData.getChannel());
			pcOnOffEventLogMinutely.getActivityIds().add(pcOnOffEventLogData.getActivityId());
			pcOnOffEventLogMinutely.getOpcodeDisplayNames().add(pcOnOffEventLogData.getOpcodeDisplayName());
			pcOnOffEventLogMinutely.getTaskDisplayNames().add(pcOnOffEventLogData.getTaskDisplayName());

			pcOnOffEventLogMinutely.setTimeCurrent(pcOnOffEventLogData.getTimeCreated());
			pcOnOffEventLogMinutely.setStatisticsValue(DateTimeConvertor.getStatisticsValue(pcOnOffEventLogData.getTimeCreated()));

			pcOnOffEventLogMinutelies.add(pcOnOffEventLogMinutely);
		}

		return pcOnOffEventLogMinutelies;
	}

	public List<PcOnOffEventLogDaily> reconstructDailyDocument(PcOnOffEventLogOriginal pcOnOffEventLogOriginal) {

		List<PcOnOffEventLogDaily> pcOnOffEventLogDailies = new ArrayList<PcOnOffEventLogDaily>();

		for( PcOnOffEventLogData pcOnOffEventLogData: pcOnOffEventLogOriginal.getPcOnOffEventLogData() ) {

			PcOnOffEventLogDaily pcOnOffEventLogDaily = new PcOnOffEventLogDaily();
			pcOnOffEventLogDaily.setService(pcOnOffEventLogOriginal.getService());
			pcOnOffEventLogDaily.setAppId(pcOnOffEventLogOriginal.getAppId());
			pcOnOffEventLogDaily.setOsType(pcOnOffEventLogOriginal.getOsType());
			pcOnOffEventLogDaily.setSource(pcOnOffEventLogOriginal.getSource());
			pcOnOffEventLogDaily.setDeviceId(pcOnOffEventLogOriginal.getDeviceId());
			pcOnOffEventLogDaily.setIp(pcOnOffEventLogOriginal.getIp());
			pcOnOffEventLogDaily.setHostName(pcOnOffEventLogOriginal.getHostName());

			pcOnOffEventLogDaily.setUserId(pcOnOffEventLogOriginal.getUserId());
			pcOnOffEventLogDaily.setTermNo(pcOnOffEventLogOriginal.getTermNo());
			pcOnOffEventLogDaily.setSsoBrNo(pcOnOffEventLogOriginal.getSsoBrNo());
			pcOnOffEventLogDaily.setBrNo(pcOnOffEventLogOriginal.getBrNo());
			pcOnOffEventLogDaily.setDeptName(pcOnOffEventLogOriginal.getDeptName());
			pcOnOffEventLogDaily.setHwnNo(pcOnOffEventLogOriginal.getHwnNo());
			pcOnOffEventLogDaily.setUserName(pcOnOffEventLogOriginal.getUserName());
			pcOnOffEventLogDaily.setSsoType(pcOnOffEventLogOriginal.getSsoType());
			pcOnOffEventLogDaily.setPcName(pcOnOffEventLogOriginal.getPcName());
			pcOnOffEventLogDaily.setPhoneNo(pcOnOffEventLogOriginal.getPhoneNo());
			pcOnOffEventLogDaily.setJKGP(pcOnOffEventLogOriginal.getJKGP());
			pcOnOffEventLogDaily.setJKWI(pcOnOffEventLogOriginal.getJKWI());
			pcOnOffEventLogDaily.setMaxAddress(pcOnOffEventLogOriginal.getMaxAddress());
			pcOnOffEventLogDaily.setFirstWork(pcOnOffEventLogOriginal.getFirstWork());
			pcOnOffEventLogDaily.setTimeCreated(pcOnOffEventLogOriginal.getTimeCreated());

			pcOnOffEventLogDaily.setLevel(pcOnOffEventLogData.getLevel());
			pcOnOffEventLogDaily.setEventId(pcOnOffEventLogData.getId());
			pcOnOffEventLogDaily.setChannel(pcOnOffEventLogData.getChannel());
			pcOnOffEventLogDaily.setActivityId(pcOnOffEventLogData.getActivityId());
			pcOnOffEventLogDaily.setBookmark(pcOnOffEventLogData.getBookmark());
			pcOnOffEventLogDaily.setKeywords(pcOnOffEventLogData.getKeywords());
			pcOnOffEventLogDaily.setKeywordsDisplayNames(pcOnOffEventLogData.getKeywordsDisplayNames());
			pcOnOffEventLogDaily.setLevelDisplayName(pcOnOffEventLogData.getLevelDisplayName());
			pcOnOffEventLogDaily.setLogName(pcOnOffEventLogData.getLogName());
			pcOnOffEventLogDaily.setMachineName(pcOnOffEventLogData.getMachineName());
			pcOnOffEventLogDaily.setOpcode(pcOnOffEventLogData.getOpcode());
			pcOnOffEventLogDaily.setOpcodeDisplayName(pcOnOffEventLogData.getOpcodeDisplayName());
			pcOnOffEventLogDaily.setProcessId(pcOnOffEventLogData.getProcessId());
			pcOnOffEventLogDaily.setProperties(pcOnOffEventLogData.getProperties());
			pcOnOffEventLogDaily.setProviderId(pcOnOffEventLogData.getProviderId());
			pcOnOffEventLogDaily.setProviderName(pcOnOffEventLogData.getProviderName());
			pcOnOffEventLogDaily.setQualifiers(pcOnOffEventLogData.getQualifiers());
			pcOnOffEventLogDaily.setRecordId(pcOnOffEventLogData.getRecordId());
			pcOnOffEventLogDaily.setRelatedActivityId(pcOnOffEventLogData.getRelatedActivityId());
			pcOnOffEventLogDaily.setTask(pcOnOffEventLogData.getTask());
			pcOnOffEventLogDaily.setTaskDisplayName(pcOnOffEventLogData.getTaskDisplayName());
			pcOnOffEventLogDaily.setThreadId(pcOnOffEventLogData.getThreadId());
			pcOnOffEventLogDaily.setSecurityUserId(pcOnOffEventLogData.getSecurityUserId());
			pcOnOffEventLogDaily.setVersion(pcOnOffEventLogData.getVersion());
			pcOnOffEventLogDaily.setMessage(pcOnOffEventLogData.getMessage());

			try{pcOnOffEventLogDaily.setTimeCreatedSystemTime(pcOnOffEventLogData.getTimeCreatedSystemTime());}catch(Exception e) {}
			pcOnOffEventLogDaily.setTimeCurrent(pcOnOffEventLogData.getTimeCreated());
			pcOnOffEventLogDaily.setStatisticsValue(DateTimeConvertor.getStatisticsValue(pcOnOffEventLogData.getTimeCreated()));

			pcOnOffEventLogDailies.add(pcOnOffEventLogDaily);
		}

		return pcOnOffEventLogDailies;
	}

	public List<PcOnOffEventLog> reconstructDocument(PcOnOffEventLogOriginal pcOnOffEventLogOriginal) {

		List<PcOnOffEventLog> pcOnOffEventLogs = new ArrayList<PcOnOffEventLog>();

		for( PcOnOffEventLogData pcOnOffEventLogData: pcOnOffEventLogOriginal.getPcOnOffEventLogData() ) {

			PcOnOffEventLog pcOnOffEventLog = new PcOnOffEventLog();
			pcOnOffEventLog.setService(pcOnOffEventLogOriginal.getService());
			pcOnOffEventLog.setAppId(pcOnOffEventLogOriginal.getAppId());
			pcOnOffEventLog.setOsType(pcOnOffEventLogOriginal.getOsType());
			pcOnOffEventLog.setSource(pcOnOffEventLogOriginal.getSource());
			pcOnOffEventLog.setDeviceId(pcOnOffEventLogOriginal.getDeviceId());
			pcOnOffEventLog.setIp(pcOnOffEventLogOriginal.getIp());
			pcOnOffEventLog.setHostName(pcOnOffEventLogOriginal.getHostName());
			pcOnOffEventLog.setTimeCreated(pcOnOffEventLogOriginal.getTimeCreated());

			pcOnOffEventLog.setUserId(pcOnOffEventLogOriginal.getUserId());
			pcOnOffEventLog.setTermNo(pcOnOffEventLogOriginal.getTermNo());
			pcOnOffEventLog.setSsoBrNo(pcOnOffEventLogOriginal.getSsoBrNo());
			pcOnOffEventLog.setBrNo(pcOnOffEventLogOriginal.getBrNo());
			pcOnOffEventLog.setDeptName(pcOnOffEventLogOriginal.getDeptName());
			pcOnOffEventLog.setHwnNo(pcOnOffEventLogOriginal.getHwnNo());
			pcOnOffEventLog.setUserName(pcOnOffEventLogOriginal.getUserName());
			pcOnOffEventLog.setSsoType(pcOnOffEventLogOriginal.getSsoType());
			pcOnOffEventLog.setPcName(pcOnOffEventLogOriginal.getPcName());
			pcOnOffEventLog.setPhoneNo(pcOnOffEventLogOriginal.getPhoneNo());
			pcOnOffEventLog.setJKGP(pcOnOffEventLogOriginal.getJKGP());
			pcOnOffEventLog.setJKWI(pcOnOffEventLogOriginal.getJKWI());
			pcOnOffEventLog.setMaxAddress(pcOnOffEventLogOriginal.getMaxAddress());
			pcOnOffEventLog.setFirstWork(pcOnOffEventLogOriginal.getFirstWork());
			pcOnOffEventLog.setJKGP(pcOnOffEventLogOriginal.getJKGP());
			pcOnOffEventLog.setJKWI(pcOnOffEventLogOriginal.getJKWI());
			pcOnOffEventLog.setMaxAddress(pcOnOffEventLogOriginal.getMaxAddress());
			pcOnOffEventLog.setFirstWork(pcOnOffEventLogOriginal.getFirstWork());
			pcOnOffEventLog.setTimeCreated(pcOnOffEventLogOriginal.getTimeCreated());

			pcOnOffEventLog.setLevel(pcOnOffEventLogData.getLevel());
			pcOnOffEventLog.setEventId(pcOnOffEventLogData.getId());
			pcOnOffEventLog.setChannel(pcOnOffEventLogData.getChannel());
			pcOnOffEventLog.setActivityId(pcOnOffEventLogData.getActivityId());
			pcOnOffEventLog.setBookmark(pcOnOffEventLogData.getBookmark());
			pcOnOffEventLog.setKeywords(pcOnOffEventLogData.getKeywords());
			pcOnOffEventLog.setKeywordsDisplayNames(pcOnOffEventLogData.getKeywordsDisplayNames());
			pcOnOffEventLog.setLevelDisplayName(pcOnOffEventLogData.getLevelDisplayName());
			pcOnOffEventLog.setLogName(pcOnOffEventLogData.getLogName());
			pcOnOffEventLog.setMachineName(pcOnOffEventLogData.getMachineName());
			pcOnOffEventLog.setOpcode(pcOnOffEventLogData.getOpcode());
			pcOnOffEventLog.setOpcodeDisplayName(pcOnOffEventLogData.getOpcodeDisplayName());
			pcOnOffEventLog.setProcessId(pcOnOffEventLogData.getProcessId());
			pcOnOffEventLog.setProperties(pcOnOffEventLogData.getProperties());
			pcOnOffEventLog.setProviderId(pcOnOffEventLogData.getProviderId());
			pcOnOffEventLog.setProviderName(pcOnOffEventLogData.getProviderName());
			pcOnOffEventLog.setQualifiers(pcOnOffEventLogData.getQualifiers());
			pcOnOffEventLog.setRecordId(pcOnOffEventLogData.getRecordId());
			pcOnOffEventLog.setRelatedActivityId(pcOnOffEventLogData.getRelatedActivityId());
			pcOnOffEventLog.setTask(pcOnOffEventLogData.getTask());
			pcOnOffEventLog.setTaskDisplayName(pcOnOffEventLogData.getTaskDisplayName());
			pcOnOffEventLog.setThreadId(pcOnOffEventLogData.getThreadId());
			pcOnOffEventLog.setSecurityUserId(pcOnOffEventLogData.getSecurityUserId());
			pcOnOffEventLog.setVersion(pcOnOffEventLogData.getVersion());
			pcOnOffEventLog.setMessage(pcOnOffEventLogData.getMessage());

			try{pcOnOffEventLog.setTimeCreatedSystemTime(pcOnOffEventLogData.getTimeCreatedSystemTime());}catch(Exception e) {}
			pcOnOffEventLog.setTimeCurrent(pcOnOffEventLogData.getTimeCreated());
			pcOnOffEventLog.setStatisticsValue(DateTimeConvertor.getStatisticsValue(pcOnOffEventLogData.getTimeCreated()));

			pcOnOffEventLogs.add(pcOnOffEventLog);
		}

		return pcOnOffEventLogs;
	}

}
