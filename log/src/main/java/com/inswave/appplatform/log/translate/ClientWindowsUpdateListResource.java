package com.inswave.appplatform.log.translate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientWindowsUpdateListResourceLog;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientWindowsUpdateListResourceLogDaily;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientWindowsUpdateListResourceLogData;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientWindowsUpdateListResourceLogOriginal;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.log.util.LogDataObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ClientWindowsUpdateListResource {

	public ClientWindowsUpdateListResource () {
	}

	public ClientWindowsUpdateListResourceLogOriginal getOriginalObject(StringBuffer message) {
		ClientWindowsUpdateListResourceLogOriginal clientWindowsUpdateListResourceLogOriginal = null;
		return clientWindowsUpdateListResourceLogOriginal;
	}

	public ClientWindowsUpdateListResourceLogOriginal getOriginalObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, ClientWindowsUpdateListResourceLogOriginal.class);
	}


	public List<ClientWindowsUpdateListResourceLogDaily> reconstructDailyDocument(ClientWindowsUpdateListResourceLogOriginal clientWindowsUpdateListResourceLogOriginal) {

		List<ClientWindowsUpdateListResourceLogDaily> clientWindowsUpdateListResourceLogDailies = new ArrayList<ClientWindowsUpdateListResourceLogDaily>();

		for( ClientWindowsUpdateListResourceLogData clientWindowsUpdateListResourceLogData: clientWindowsUpdateListResourceLogOriginal.getClientWindowsUpdateListResourceData() ) {

			ClientWindowsUpdateListResourceLogDaily clientWindowsUpdateListResourceLogDaily = new ClientWindowsUpdateListResourceLogDaily();
			clientWindowsUpdateListResourceLogDaily.setService(clientWindowsUpdateListResourceLogOriginal.getService());
			clientWindowsUpdateListResourceLogDaily.setAppId(clientWindowsUpdateListResourceLogOriginal.getAppId());
			clientWindowsUpdateListResourceLogDaily.setOsType(clientWindowsUpdateListResourceLogOriginal.getOsType());
			clientWindowsUpdateListResourceLogDaily.setSource(clientWindowsUpdateListResourceLogOriginal.getSource());
			clientWindowsUpdateListResourceLogDaily.setDeviceId(clientWindowsUpdateListResourceLogOriginal.getDeviceId());
			clientWindowsUpdateListResourceLogDaily.setIp(clientWindowsUpdateListResourceLogOriginal.getIp());
			clientWindowsUpdateListResourceLogDaily.setHostName(clientWindowsUpdateListResourceLogOriginal.getHostName());
			clientWindowsUpdateListResourceLogDaily.setUserId(clientWindowsUpdateListResourceLogOriginal.getUserId());
			clientWindowsUpdateListResourceLogDaily.setTermNo(clientWindowsUpdateListResourceLogOriginal.getTermNo());
			clientWindowsUpdateListResourceLogDaily.setSsoBrNo(clientWindowsUpdateListResourceLogOriginal.getSsoBrNo());
			clientWindowsUpdateListResourceLogDaily.setBrNo(clientWindowsUpdateListResourceLogOriginal.getBrNo());
			clientWindowsUpdateListResourceLogDaily.setDeptName(clientWindowsUpdateListResourceLogOriginal.getDeptName());
			clientWindowsUpdateListResourceLogDaily.setHwnNo(clientWindowsUpdateListResourceLogOriginal.getHwnNo());
			clientWindowsUpdateListResourceLogDaily.setUserName(clientWindowsUpdateListResourceLogOriginal.getUserName());
			clientWindowsUpdateListResourceLogDaily.setSsoType(clientWindowsUpdateListResourceLogOriginal.getSsoType());
			clientWindowsUpdateListResourceLogDaily.setPcName(clientWindowsUpdateListResourceLogOriginal.getPcName());
			clientWindowsUpdateListResourceLogDaily.setPhoneNo(clientWindowsUpdateListResourceLogOriginal.getPhoneNo());
			clientWindowsUpdateListResourceLogDaily.setJKGP(clientWindowsUpdateListResourceLogOriginal.getJKGP());
			clientWindowsUpdateListResourceLogDaily.setJKWI(clientWindowsUpdateListResourceLogOriginal.getJKWI());
			clientWindowsUpdateListResourceLogDaily.setMaxAddress(clientWindowsUpdateListResourceLogOriginal.getMaxAddress());
			clientWindowsUpdateListResourceLogDaily.setFirstWork(clientWindowsUpdateListResourceLogOriginal.getFirstWork());
			clientWindowsUpdateListResourceLogDaily.setTimeCreated(clientWindowsUpdateListResourceLogOriginal.getTimeCreated());

			clientWindowsUpdateListResourceLogDaily.setTitle(clientWindowsUpdateListResourceLogData.getTitle());
			clientWindowsUpdateListResourceLogDaily.setDate(clientWindowsUpdateListResourceLogData.getDate());
			clientWindowsUpdateListResourceLogDaily.setClientApplicationID(clientWindowsUpdateListResourceLogData.getClientApplicationID());
			clientWindowsUpdateListResourceLogDaily.setServerSelection(clientWindowsUpdateListResourceLogData.getServerSelection());
			clientWindowsUpdateListResourceLogDaily.setTimeCurrent(clientWindowsUpdateListResourceLogData.getTimeCreated());

			clientWindowsUpdateListResourceLogDaily.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientWindowsUpdateListResourceLogData.getTimeCreated()));

			clientWindowsUpdateListResourceLogDailies.add(clientWindowsUpdateListResourceLogDaily);
		}

		return clientWindowsUpdateListResourceLogDailies;
	}


	public List<ClientWindowsUpdateListResourceLog> reconstructDocument(ClientWindowsUpdateListResourceLogOriginal clientWindowsUpdateListResourceLogOriginal) {

		List<ClientWindowsUpdateListResourceLog> clientWindowsUpdateListResourceLogs = new ArrayList<ClientWindowsUpdateListResourceLog>();

		for( ClientWindowsUpdateListResourceLogData clientWindowsUpdateListResourceLogData: clientWindowsUpdateListResourceLogOriginal.getClientWindowsUpdateListResourceData() ) {

			ClientWindowsUpdateListResourceLog clientWindowsUpdateListResourceLog = new ClientWindowsUpdateListResourceLog();
			clientWindowsUpdateListResourceLog.setService(clientWindowsUpdateListResourceLogOriginal.getService());
			clientWindowsUpdateListResourceLog.setAppId(clientWindowsUpdateListResourceLogOriginal.getAppId());
			clientWindowsUpdateListResourceLog.setOsType(clientWindowsUpdateListResourceLogOriginal.getOsType());
			clientWindowsUpdateListResourceLog.setSource(clientWindowsUpdateListResourceLogOriginal.getSource());
			clientWindowsUpdateListResourceLog.setDeviceId(clientWindowsUpdateListResourceLogOriginal.getDeviceId());
			clientWindowsUpdateListResourceLog.setIp(clientWindowsUpdateListResourceLogOriginal.getIp());
			clientWindowsUpdateListResourceLog.setHostName(clientWindowsUpdateListResourceLogOriginal.getHostName());
			clientWindowsUpdateListResourceLog.setUserId(clientWindowsUpdateListResourceLogOriginal.getUserId());
			clientWindowsUpdateListResourceLog.setTermNo(clientWindowsUpdateListResourceLogOriginal.getTermNo());
			clientWindowsUpdateListResourceLog.setSsoBrNo(clientWindowsUpdateListResourceLogOriginal.getSsoBrNo());
			clientWindowsUpdateListResourceLog.setBrNo(clientWindowsUpdateListResourceLogOriginal.getBrNo());
			clientWindowsUpdateListResourceLog.setDeptName(clientWindowsUpdateListResourceLogOriginal.getDeptName());
			clientWindowsUpdateListResourceLog.setHwnNo(clientWindowsUpdateListResourceLogOriginal.getHwnNo());
			clientWindowsUpdateListResourceLog.setUserName(clientWindowsUpdateListResourceLogOriginal.getUserName());
			clientWindowsUpdateListResourceLog.setSsoType(clientWindowsUpdateListResourceLogOriginal.getSsoType());
			clientWindowsUpdateListResourceLog.setPcName(clientWindowsUpdateListResourceLogOriginal.getPcName());
			clientWindowsUpdateListResourceLog.setPhoneNo(clientWindowsUpdateListResourceLogOriginal.getPhoneNo());
			clientWindowsUpdateListResourceLog.setJKGP(clientWindowsUpdateListResourceLogOriginal.getJKGP());
			clientWindowsUpdateListResourceLog.setJKWI(clientWindowsUpdateListResourceLogOriginal.getJKWI());
			clientWindowsUpdateListResourceLog.setMaxAddress(clientWindowsUpdateListResourceLogOriginal.getMaxAddress());
			clientWindowsUpdateListResourceLog.setFirstWork(clientWindowsUpdateListResourceLogOriginal.getFirstWork());
			clientWindowsUpdateListResourceLog.setTimeCreated(clientWindowsUpdateListResourceLogOriginal.getTimeCreated());

			clientWindowsUpdateListResourceLog.setTitle(clientWindowsUpdateListResourceLogData.getTitle());
			clientWindowsUpdateListResourceLog.setDate(clientWindowsUpdateListResourceLogData.getDate());
			clientWindowsUpdateListResourceLog.setClientApplicationID(clientWindowsUpdateListResourceLogData.getClientApplicationID());
			clientWindowsUpdateListResourceLog.setServerSelection(clientWindowsUpdateListResourceLogData.getServerSelection());
			clientWindowsUpdateListResourceLog.setTimeCurrent(clientWindowsUpdateListResourceLogData.getTimeCreated());
			clientWindowsUpdateListResourceLog.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientWindowsUpdateListResourceLogData.getTimeCreated()));

			clientWindowsUpdateListResourceLogs.add(clientWindowsUpdateListResourceLog);
		}

		return clientWindowsUpdateListResourceLogs;
	}

}
