package com.inswave.appplatform.log.translate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.transaver.ConstantsTranSaver;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientProgramListResourceLog;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientProgramListResourceLogDaily;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientProgramListResourceLogData;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientProgramListResourceLogOriginal;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.log.util.LogDataObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ClientProgramListResource {

	public ClientProgramListResource () {
	}

	public ClientProgramListResourceLogOriginal getOriginalObject(StringBuffer message) {
		ClientProgramListResourceLogOriginal clientProgramListResourceLogOriginal = null;
		return clientProgramListResourceLogOriginal;
	}

	public ClientProgramListResourceLogOriginal getOriginalObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, ClientProgramListResourceLogOriginal.class);
	}


	public List<ClientProgramListResourceLogDaily> reconstructDailyDocument(ClientProgramListResourceLogOriginal clientProgramListResourceLogOriginal) {

		List<ClientProgramListResourceLogDaily> clientProgramListResourceLogDailies = new ArrayList<ClientProgramListResourceLogDaily>();

		for( ClientProgramListResourceLogData clientProgramListResourceLogData : clientProgramListResourceLogOriginal.getClientProgramListResourceData() ) {

			ClientProgramListResourceLogDaily clientProgramListResourceLogDaily = new ClientProgramListResourceLogDaily();
			clientProgramListResourceLogDaily.setService(clientProgramListResourceLogOriginal.getService());
			clientProgramListResourceLogDaily.setAppId(clientProgramListResourceLogOriginal.getAppId());
			clientProgramListResourceLogDaily.setOsType(clientProgramListResourceLogOriginal.getOsType());
			clientProgramListResourceLogDaily.setSource(clientProgramListResourceLogOriginal.getSource());
			clientProgramListResourceLogDaily.setDeviceId(clientProgramListResourceLogOriginal.getDeviceId());
			clientProgramListResourceLogDaily.setIp(clientProgramListResourceLogOriginal.getIp());
			clientProgramListResourceLogDaily.setHostName(clientProgramListResourceLogOriginal.getHostName());
			clientProgramListResourceLogDaily.setTimeCreated(clientProgramListResourceLogOriginal.getTimeCreated());

			clientProgramListResourceLogDaily.setUserId(clientProgramListResourceLogOriginal.getUserId());
			clientProgramListResourceLogDaily.setTermNo(clientProgramListResourceLogOriginal.getTermNo());
			clientProgramListResourceLogDaily.setSsoBrNo(clientProgramListResourceLogOriginal.getSsoBrNo());
			clientProgramListResourceLogDaily.setBrNo(clientProgramListResourceLogOriginal.getBrNo());
			clientProgramListResourceLogDaily.setDeptName(clientProgramListResourceLogOriginal.getDeptName());
			clientProgramListResourceLogDaily.setHwnNo(clientProgramListResourceLogOriginal.getHwnNo());
			clientProgramListResourceLogDaily.setUserName(clientProgramListResourceLogOriginal.getUserName());
			clientProgramListResourceLogDaily.setSsoType(clientProgramListResourceLogOriginal.getSsoType());
			clientProgramListResourceLogDaily.setPcName(clientProgramListResourceLogOriginal.getPcName());
			clientProgramListResourceLogDaily.setPhoneNo(clientProgramListResourceLogOriginal.getPhoneNo());
			clientProgramListResourceLogDaily.setJKGP(clientProgramListResourceLogOriginal.getJKGP());
			clientProgramListResourceLogDaily.setJKWI(clientProgramListResourceLogOriginal.getJKWI());
			clientProgramListResourceLogDaily.setMaxAddress(clientProgramListResourceLogOriginal.getMaxAddress());
			clientProgramListResourceLogDaily.setFirstWork(clientProgramListResourceLogOriginal.getFirstWork());

			clientProgramListResourceLogDaily.setName(clientProgramListResourceLogData.getName());
			clientProgramListResourceLogDaily.setVersion(clientProgramListResourceLogData.getVersion());
			clientProgramListResourceLogDaily.setPublisher(clientProgramListResourceLogData.getPublisher());
			clientProgramListResourceLogDaily.setSize(clientProgramListResourceLogData.getSize());
			clientProgramListResourceLogDaily.setTimeCurrent(clientProgramListResourceLogData.getTimeCreated());
			clientProgramListResourceLogDaily.setInstallDate(DateTimeConvertor.getDateForInstallDate(ConstantsTranSaver.TAG_DATE_PATTERN_YYYYMMDD,clientProgramListResourceLogData.getInstallDate()));

			clientProgramListResourceLogDaily.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientProgramListResourceLogData.getTimeCreated()));

			clientProgramListResourceLogDailies.add(clientProgramListResourceLogDaily);
		}

		return clientProgramListResourceLogDailies;
	}


	public List<ClientProgramListResourceLog> reconstructDocument(ClientProgramListResourceLogOriginal clientProgramListResourceLogOriginal) {

		List<ClientProgramListResourceLog> clientProgramListResourceLogs = new ArrayList<ClientProgramListResourceLog>();

		for( ClientProgramListResourceLogData clientProgramListResourceLogData : clientProgramListResourceLogOriginal.getClientProgramListResourceData() ) {

			ClientProgramListResourceLog clientProgramListResourceLog = new ClientProgramListResourceLog();
			clientProgramListResourceLog.setService(clientProgramListResourceLogOriginal.getService());
			clientProgramListResourceLog.setAppId(clientProgramListResourceLogOriginal.getAppId());
			clientProgramListResourceLog.setOsType(clientProgramListResourceLogOriginal.getOsType());
			clientProgramListResourceLog.setSource(clientProgramListResourceLogOriginal.getSource());
			clientProgramListResourceLog.setDeviceId(clientProgramListResourceLogOriginal.getDeviceId());
			clientProgramListResourceLog.setIp(clientProgramListResourceLogOriginal.getIp());
			clientProgramListResourceLog.setHostName(clientProgramListResourceLogOriginal.getHostName());
			clientProgramListResourceLog.setTimeCreated(clientProgramListResourceLogOriginal.getTimeCreated());

			clientProgramListResourceLog.setUserId(clientProgramListResourceLogOriginal.getUserId());
			clientProgramListResourceLog.setTermNo(clientProgramListResourceLogOriginal.getTermNo());
			clientProgramListResourceLog.setSsoBrNo(clientProgramListResourceLogOriginal.getSsoBrNo());
			clientProgramListResourceLog.setBrNo(clientProgramListResourceLogOriginal.getBrNo());
			clientProgramListResourceLog.setDeptName(clientProgramListResourceLogOriginal.getDeptName());
			clientProgramListResourceLog.setHwnNo(clientProgramListResourceLogOriginal.getHwnNo());
			clientProgramListResourceLog.setUserName(clientProgramListResourceLogOriginal.getUserName());
			clientProgramListResourceLog.setSsoType(clientProgramListResourceLogOriginal.getSsoType());
			clientProgramListResourceLog.setPcName(clientProgramListResourceLogOriginal.getPcName());
			clientProgramListResourceLog.setPhoneNo(clientProgramListResourceLogOriginal.getPhoneNo());
			clientProgramListResourceLog.setJKGP(clientProgramListResourceLogOriginal.getJKGP());
			clientProgramListResourceLog.setJKWI(clientProgramListResourceLogOriginal.getJKWI());
			clientProgramListResourceLog.setMaxAddress(clientProgramListResourceLogOriginal.getMaxAddress());
			clientProgramListResourceLog.setFirstWork(clientProgramListResourceLogOriginal.getFirstWork());

			clientProgramListResourceLog.setName(clientProgramListResourceLogData.getName());
			clientProgramListResourceLog.setVersion(clientProgramListResourceLogData.getVersion());
			clientProgramListResourceLog.setPublisher(clientProgramListResourceLogData.getPublisher());
			clientProgramListResourceLog.setSize(clientProgramListResourceLogData.getSize());
			clientProgramListResourceLog.setTimeCurrent(clientProgramListResourceLogData.getTimeCreated());
			clientProgramListResourceLog.setInstallDate(DateTimeConvertor.getDateForInstallDate(ConstantsTranSaver.TAG_DATE_PATTERN_YYYYMMDD,clientProgramListResourceLogData.getInstallDate()));
			clientProgramListResourceLog.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientProgramListResourceLogData.getTimeCreated()));

			clientProgramListResourceLogs.add(clientProgramListResourceLog);
		}

		return clientProgramListResourceLogs;
	}

}
