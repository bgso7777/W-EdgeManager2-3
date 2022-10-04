package com.inswave.appplatform.log.translate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientProcessCreationLog;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientProcessCreationLogDaily;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientProcessCreationLogData;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientProcessCreationLogOriginal;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.log.util.LogDataObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ClientProcessCreation {

	public ClientProcessCreation () {
	}

	public ClientProcessCreationLogOriginal getOriginalObject(StringBuffer message) {
		ClientProcessCreationLogOriginal clientProcessCreationLogOriginal = null;
		return clientProcessCreationLogOriginal;
	}

	public ClientProcessCreationLogOriginal getOriginalObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, ClientProcessCreationLogOriginal.class);
	}


	public List<ClientProcessCreationLogDaily> reconstructDailyDocument(ClientProcessCreationLogOriginal clientProcessCreationLogOriginal) {

		List<ClientProcessCreationLogDaily> clientProcessCreationLogDailies = new ArrayList<ClientProcessCreationLogDaily>();

		for( ClientProcessCreationLogData clientProcessCreationLogData : clientProcessCreationLogOriginal.getProcessCreationLogData() ) {

			ClientProcessCreationLogDaily clientProcessCreationLogDaily = new ClientProcessCreationLogDaily();
			clientProcessCreationLogDaily.setService(clientProcessCreationLogOriginal.getService());
			clientProcessCreationLogDaily.setAppId(clientProcessCreationLogOriginal.getAppId());
			clientProcessCreationLogDaily.setOsType(clientProcessCreationLogOriginal.getOsType());
			clientProcessCreationLogDaily.setSource(clientProcessCreationLogOriginal.getSource());
			clientProcessCreationLogDaily.setDeviceId(clientProcessCreationLogOriginal.getDeviceId());
			clientProcessCreationLogDaily.setIp(clientProcessCreationLogOriginal.getIp());
			clientProcessCreationLogDaily.setHostName(clientProcessCreationLogOriginal.getHostName());
			clientProcessCreationLogDaily.setTimeCreated(clientProcessCreationLogOriginal.getTimeCreated());

			clientProcessCreationLogDaily.setUserId(clientProcessCreationLogOriginal.getUserId());
			clientProcessCreationLogDaily.setTermNo(clientProcessCreationLogOriginal.getTermNo());
			clientProcessCreationLogDaily.setSsoBrNo(clientProcessCreationLogOriginal.getSsoBrNo());
			clientProcessCreationLogDaily.setBrNo(clientProcessCreationLogOriginal.getBrNo());
			clientProcessCreationLogDaily.setDeptName(clientProcessCreationLogOriginal.getDeptName());
			clientProcessCreationLogDaily.setHwnNo(clientProcessCreationLogOriginal.getHwnNo());
			clientProcessCreationLogDaily.setUserName(clientProcessCreationLogOriginal.getUserName());
			clientProcessCreationLogDaily.setSsoType(clientProcessCreationLogOriginal.getSsoType());
			clientProcessCreationLogDaily.setPcName(clientProcessCreationLogOriginal.getPcName());
			clientProcessCreationLogDaily.setPhoneNo(clientProcessCreationLogOriginal.getPhoneNo());
			clientProcessCreationLogDaily.setJKGP(clientProcessCreationLogOriginal.getJKGP());
			clientProcessCreationLogDaily.setJKWI(clientProcessCreationLogOriginal.getJKWI());
			clientProcessCreationLogDaily.setMaxAddress(clientProcessCreationLogOriginal.getMaxAddress());
			clientProcessCreationLogDaily.setFirstWork(clientProcessCreationLogOriginal.getFirstWork());

			clientProcessCreationLogDaily.setChannel(clientProcessCreationLogData.getChannel());
			clientProcessCreationLogDaily.setEventId(clientProcessCreationLogData.getEventId());
			clientProcessCreationLogDaily.setLevel(clientProcessCreationLogData.getLevel());
			clientProcessCreationLogDaily.setKeywords(clientProcessCreationLogData.getKeywords());
			clientProcessCreationLogDaily.setMessage(clientProcessCreationLogData.getMessage());

			clientProcessCreationLogDaily.setNewProcessId(clientProcessCreationLogData.getNewProcessId());
			clientProcessCreationLogDaily.setNewProcessName(clientProcessCreationLogData.getNewProcessName());
			clientProcessCreationLogDaily.setParentProcessId(clientProcessCreationLogData.getParentProcessId());
			clientProcessCreationLogDaily.setNewProcessId(clientProcessCreationLogData.getNewProcessId());
			clientProcessCreationLogDaily.setParentProcessName(clientProcessCreationLogData.getParentProcessName());

			try{clientProcessCreationLogDaily.setTimeCreatedSystemTime(clientProcessCreationLogData.getTimeCreatedSystemTime());}catch(Exception e) {}
			clientProcessCreationLogDaily.setTimeCurrent(clientProcessCreationLogData.getTimeCreated());
			clientProcessCreationLogDaily.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientProcessCreationLogData.getTimeCreated()));

			clientProcessCreationLogDailies.add(clientProcessCreationLogDaily);
		}

		return clientProcessCreationLogDailies;
	}


	public List<ClientProcessCreationLog> reconstructDocument(ClientProcessCreationLogOriginal clientProcessCreationLogOriginal) {

		List<ClientProcessCreationLog> clientProcessCreationLogs = new ArrayList<ClientProcessCreationLog>();

		for( ClientProcessCreationLogData clientProcessCreationLogData : clientProcessCreationLogOriginal.getProcessCreationLogData() ) {

			ClientProcessCreationLog clientProcessCreationLog = new ClientProcessCreationLog();
			clientProcessCreationLog.setService(clientProcessCreationLogOriginal.getService());
			clientProcessCreationLog.setAppId(clientProcessCreationLogOriginal.getAppId());
			clientProcessCreationLog.setOsType(clientProcessCreationLogOriginal.getOsType());
			clientProcessCreationLog.setSource(clientProcessCreationLogOriginal.getSource());
			clientProcessCreationLog.setDeviceId(clientProcessCreationLogOriginal.getDeviceId());
			clientProcessCreationLog.setIp(clientProcessCreationLogOriginal.getIp());
			clientProcessCreationLog.setHostName(clientProcessCreationLogOriginal.getHostName());
			clientProcessCreationLog.setTimeCreated(clientProcessCreationLogOriginal.getTimeCreated());

			clientProcessCreationLog.setUserId(clientProcessCreationLogOriginal.getUserId());
			clientProcessCreationLog.setTermNo(clientProcessCreationLogOriginal.getTermNo());
			clientProcessCreationLog.setSsoBrNo(clientProcessCreationLogOriginal.getSsoBrNo());
			clientProcessCreationLog.setBrNo(clientProcessCreationLogOriginal.getBrNo());
			clientProcessCreationLog.setDeptName(clientProcessCreationLogOriginal.getDeptName());
			clientProcessCreationLog.setHwnNo(clientProcessCreationLogOriginal.getHwnNo());
			clientProcessCreationLog.setUserName(clientProcessCreationLogOriginal.getUserName());
			clientProcessCreationLog.setSsoType(clientProcessCreationLogOriginal.getSsoType());
			clientProcessCreationLog.setPcName(clientProcessCreationLogOriginal.getPcName());
			clientProcessCreationLog.setPhoneNo(clientProcessCreationLogOriginal.getPhoneNo());
			clientProcessCreationLog.setJKGP(clientProcessCreationLogOriginal.getJKGP());
			clientProcessCreationLog.setJKWI(clientProcessCreationLogOriginal.getJKWI());
			clientProcessCreationLog.setMaxAddress(clientProcessCreationLogOriginal.getMaxAddress());
			clientProcessCreationLog.setFirstWork(clientProcessCreationLogOriginal.getFirstWork());

			clientProcessCreationLog.setChannel(clientProcessCreationLogData.getChannel());
			clientProcessCreationLog.setEventId(clientProcessCreationLogData.getEventId());
			clientProcessCreationLog.setLevel(clientProcessCreationLogData.getLevel());
			clientProcessCreationLog.setKeywords(clientProcessCreationLogData.getKeywords());
			clientProcessCreationLog.setMessage(clientProcessCreationLogData.getMessage());

			clientProcessCreationLog.setNewProcessId(clientProcessCreationLogData.getNewProcessId());
			clientProcessCreationLog.setNewProcessName(clientProcessCreationLogData.getNewProcessName());
			clientProcessCreationLog.setParentProcessId(clientProcessCreationLogData.getParentProcessId());
			clientProcessCreationLog.setNewProcessId(clientProcessCreationLogData.getNewProcessId());
			clientProcessCreationLog.setParentProcessName(clientProcessCreationLogData.getParentProcessName());

			try{clientProcessCreationLog.setTimeCreatedSystemTime(clientProcessCreationLogData.getTimeCreatedSystemTime());}catch(Exception e) {}
			clientProcessCreationLog.setTimeCurrent(clientProcessCreationLogData.getTimeCreated());
			clientProcessCreationLog.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientProcessCreationLogData.getTimeCreated()));

			clientProcessCreationLogs.add(clientProcessCreationLog);
		}

		return clientProcessCreationLogs;
	}

}
