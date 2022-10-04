package com.inswave.appplatform.log.translate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientMBRResourceLog;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientMBRResourceLogDaily;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientMBRResourceLogData;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientMBRResourceLogOriginal;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.log.util.LogDataObjectMapper;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ClientMBRResource {

	public ClientMBRResource () {
	}

	public ClientMBRResourceLogOriginal getOriginalObject(StringBuffer message) {
		ClientMBRResourceLogOriginal clientMBRResourceLogOriginal = null;
		return clientMBRResourceLogOriginal;
	}

	public ClientMBRResourceLogOriginal getOriginalObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, ClientMBRResourceLogOriginal.class);
	}

	public List<ClientMBRResourceLogDaily> reconstructDailyDocument(ClientMBRResourceLogOriginal clientMBRResourceLogOriginal) {

		List<ClientMBRResourceLogDaily> clientMBRResourceLogDailies = new ArrayList<ClientMBRResourceLogDaily>();

		for( ClientMBRResourceLogData clientMBRResourceLogData: clientMBRResourceLogOriginal.getClientMBRResourceData() ) {

			ClientMBRResourceLogDaily clientMBRResourceLogDaily = new ClientMBRResourceLogDaily();
			clientMBRResourceLogDaily.setService(clientMBRResourceLogOriginal.getService());
			clientMBRResourceLogDaily.setAppId(clientMBRResourceLogOriginal.getAppId());
			clientMBRResourceLogDaily.setOsType(clientMBRResourceLogOriginal.getOsType());
			clientMBRResourceLogDaily.setSource(clientMBRResourceLogOriginal.getSource());
			clientMBRResourceLogDaily.setDeviceId(clientMBRResourceLogOriginal.getDeviceId());
			clientMBRResourceLogDaily.setIp(clientMBRResourceLogOriginal.getIp());
			clientMBRResourceLogDaily.setHostName(clientMBRResourceLogOriginal.getHostName());
			clientMBRResourceLogDaily.setTimeCreated(clientMBRResourceLogOriginal.getTimeCreated());

			clientMBRResourceLogDaily.setUserId(clientMBRResourceLogOriginal.getUserId());
			clientMBRResourceLogDaily.setTermNo(clientMBRResourceLogOriginal.getTermNo());
			clientMBRResourceLogDaily.setSsoBrNo(clientMBRResourceLogOriginal.getSsoBrNo());
			clientMBRResourceLogDaily.setBrNo(clientMBRResourceLogOriginal.getBrNo());
			clientMBRResourceLogDaily.setDeptName(clientMBRResourceLogOriginal.getDeptName());
			clientMBRResourceLogDaily.setHwnNo(clientMBRResourceLogOriginal.getHwnNo());
			clientMBRResourceLogDaily.setUserName(clientMBRResourceLogOriginal.getUserName());
			clientMBRResourceLogDaily.setSsoType(clientMBRResourceLogOriginal.getSsoType());
			clientMBRResourceLogDaily.setPcName(clientMBRResourceLogOriginal.getPcName());
			clientMBRResourceLogDaily.setPhoneNo(clientMBRResourceLogOriginal.getPhoneNo());
			clientMBRResourceLogDaily.setJKGP(clientMBRResourceLogOriginal.getJKGP());
			clientMBRResourceLogDaily.setJKWI(clientMBRResourceLogOriginal.getJKWI());
			clientMBRResourceLogDaily.setMaxAddress(clientMBRResourceLogOriginal.getMaxAddress());
			clientMBRResourceLogDaily.setFirstWork(clientMBRResourceLogOriginal.getFirstWork());

			clientMBRResourceLogDaily.setName(clientMBRResourceLogData.getName());
			clientMBRResourceLogDaily.setDeviceID(clientMBRResourceLogData.getDeviceID());
			clientMBRResourceLogDaily.setSectorMBR(clientMBRResourceLogData.getSectorMBR());
			clientMBRResourceLogDaily.setTimeCurrent(clientMBRResourceLogData.getTimeCreated());
			clientMBRResourceLogDaily.setPartitionType(clientMBRResourceLogData.getPartitionType());
			clientMBRResourceLogDaily.setBootPartition(clientMBRResourceLogData.getBootPartition());
			clientMBRResourceLogDaily.setBootable(clientMBRResourceLogData.getBootable());
			clientMBRResourceLogDaily.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientMBRResourceLogData.getTimeCreated()));

			clientMBRResourceLogDailies.add(clientMBRResourceLogDaily);
		}

		return clientMBRResourceLogDailies;
	}

	public List<ClientMBRResourceLog> reconstructDocument(ClientMBRResourceLogOriginal clientMBRResourceLogOriginal) {

		List<ClientMBRResourceLog> clientMBRResourceLogs = new ArrayList<ClientMBRResourceLog>();

		for( ClientMBRResourceLogData clientMBRResourceLogData: clientMBRResourceLogOriginal.getClientMBRResourceData() ) {

			ClientMBRResourceLog clientMBRResourceLog = new ClientMBRResourceLog();
			clientMBRResourceLog.setService(clientMBRResourceLogOriginal.getService());
			clientMBRResourceLog.setAppId(clientMBRResourceLogOriginal.getAppId());
			clientMBRResourceLog.setOsType(clientMBRResourceLogOriginal.getOsType());
			clientMBRResourceLog.setSource(clientMBRResourceLogOriginal.getSource());
			clientMBRResourceLog.setDeviceId(clientMBRResourceLogOriginal.getDeviceId());
			clientMBRResourceLog.setIp(clientMBRResourceLogOriginal.getIp());
			clientMBRResourceLog.setHostName(clientMBRResourceLogOriginal.getHostName());
			clientMBRResourceLog.setTimeCreated(clientMBRResourceLogOriginal.getTimeCreated());

			clientMBRResourceLog.setUserId(clientMBRResourceLogOriginal.getUserId());
			clientMBRResourceLog.setTermNo(clientMBRResourceLogOriginal.getTermNo());
			clientMBRResourceLog.setSsoBrNo(clientMBRResourceLogOriginal.getSsoBrNo());
			clientMBRResourceLog.setBrNo(clientMBRResourceLogOriginal.getBrNo());
			clientMBRResourceLog.setDeptName(clientMBRResourceLogOriginal.getDeptName());
			clientMBRResourceLog.setHwnNo(clientMBRResourceLogOriginal.getHwnNo());
			clientMBRResourceLog.setUserName(clientMBRResourceLogOriginal.getUserName());
			clientMBRResourceLog.setSsoType(clientMBRResourceLogOriginal.getSsoType());
			clientMBRResourceLog.setPcName(clientMBRResourceLogOriginal.getPcName());
			clientMBRResourceLog.setPhoneNo(clientMBRResourceLogOriginal.getPhoneNo());
			clientMBRResourceLog.setJKGP(clientMBRResourceLogOriginal.getJKGP());
			clientMBRResourceLog.setJKWI(clientMBRResourceLogOriginal.getJKWI());
			clientMBRResourceLog.setMaxAddress(clientMBRResourceLogOriginal.getMaxAddress());
			clientMBRResourceLog.setFirstWork(clientMBRResourceLogOriginal.getFirstWork());

			clientMBRResourceLog.setName(clientMBRResourceLogData.getName());
			clientMBRResourceLog.setDeviceID(clientMBRResourceLogData.getDeviceID());
			clientMBRResourceLog.setSectorMBR(clientMBRResourceLogData.getSectorMBR());
			clientMBRResourceLog.setTimeCurrent(clientMBRResourceLogData.getTimeCreated());
			clientMBRResourceLog.setPartitionType(clientMBRResourceLogData.getPartitionType());
			clientMBRResourceLog.setBootPartition(clientMBRResourceLogData.getBootPartition());
			clientMBRResourceLog.setBootable(clientMBRResourceLogData.getBootable());
			clientMBRResourceLog.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientMBRResourceLogData.getTimeCreated()));

			clientMBRResourceLogs.add(clientMBRResourceLog);
		}

		return clientMBRResourceLogs;
	}

}
