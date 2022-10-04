package com.inswave.appplatform.log.translate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientControlProcessResourceLog;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientControlProcessResourceLogDaily;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientControlProcessResourceLogData;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientControlProcessResourceLogOriginal;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.log.util.LogDataObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ClientControlProcessResource {

	public ClientControlProcessResource () {
	}

	public ClientControlProcessResourceLogOriginal getOriginalObject(StringBuffer message) {
		ClientControlProcessResourceLogOriginal clientControlProcessResourceLogOriginal = null;
		return clientControlProcessResourceLogOriginal;
	}

	public ClientControlProcessResourceLogOriginal getOriginalObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, ClientControlProcessResourceLogOriginal.class);
	}


	public List<ClientControlProcessResourceLogDaily> reconstructDailyDocument(ClientControlProcessResourceLogOriginal clientControlProcessResourceLogOriginal) {

		List<ClientControlProcessResourceLogDaily> clientControlProcessResourceLogDailies = new ArrayList<ClientControlProcessResourceLogDaily>();

		for( ClientControlProcessResourceLogData clientDefragAnalysisResourceData: clientControlProcessResourceLogOriginal.getClientControlProcessResourceData() ) {

			ClientControlProcessResourceLogDaily clientControlProcessResourceLogDaily = new ClientControlProcessResourceLogDaily();
			clientControlProcessResourceLogDaily.setService(clientControlProcessResourceLogOriginal.getService());
			clientControlProcessResourceLogDaily.setAppId(clientControlProcessResourceLogOriginal.getAppId());
			clientControlProcessResourceLogDaily.setOsType(clientControlProcessResourceLogOriginal.getOsType());
			clientControlProcessResourceLogDaily.setSource(clientControlProcessResourceLogOriginal.getSource());
			clientControlProcessResourceLogDaily.setDeviceId(clientControlProcessResourceLogOriginal.getDeviceId());
			clientControlProcessResourceLogDaily.setIp(clientControlProcessResourceLogOriginal.getIp());
			clientControlProcessResourceLogDaily.setHostName(clientControlProcessResourceLogOriginal.getHostName());
			clientControlProcessResourceLogDaily.setTimeCreated(clientControlProcessResourceLogOriginal.getTimeCreated());

			clientControlProcessResourceLogDaily.setUserId(clientControlProcessResourceLogOriginal.getUserId());
			clientControlProcessResourceLogDaily.setTermNo(clientControlProcessResourceLogOriginal.getTermNo());
			clientControlProcessResourceLogDaily.setSsoBrNo(clientControlProcessResourceLogOriginal.getSsoBrNo());
			clientControlProcessResourceLogDaily.setBrNo(clientControlProcessResourceLogOriginal.getBrNo());
			clientControlProcessResourceLogDaily.setDeptName(clientControlProcessResourceLogOriginal.getDeptName());
			clientControlProcessResourceLogDaily.setHwnNo(clientControlProcessResourceLogOriginal.getHwnNo());
			clientControlProcessResourceLogDaily.setUserName(clientControlProcessResourceLogOriginal.getUserName());
			clientControlProcessResourceLogDaily.setSsoType(clientControlProcessResourceLogOriginal.getSsoType());
			clientControlProcessResourceLogDaily.setPcName(clientControlProcessResourceLogOriginal.getPcName());
			clientControlProcessResourceLogDaily.setPhoneNo(clientControlProcessResourceLogOriginal.getPhoneNo());
			clientControlProcessResourceLogDaily.setJKGP(clientControlProcessResourceLogOriginal.getJKGP());
			clientControlProcessResourceLogDaily.setJKWI(clientControlProcessResourceLogOriginal.getJKWI());
			clientControlProcessResourceLogDaily.setMaxAddress(clientControlProcessResourceLogOriginal.getMaxAddress());
			clientControlProcessResourceLogDaily.setFirstWork(clientControlProcessResourceLogOriginal.getFirstWork());

			clientControlProcessResourceLogDaily.setPid(clientDefragAnalysisResourceData.getPid());
			clientControlProcessResourceLogDaily.setProcName(clientDefragAnalysisResourceData.getProcName());
			clientControlProcessResourceLogDaily.setProcParentPid(clientDefragAnalysisResourceData.getProcParentPid());
			clientControlProcessResourceLogDaily.setProcSessionID(clientDefragAnalysisResourceData.getProcSessionID());
			clientControlProcessResourceLogDaily.setExecResult(clientDefragAnalysisResourceData.getExecResult());

			clientControlProcessResourceLogDaily.setTimeCurrent(clientDefragAnalysisResourceData.getTimeCreated());
			clientControlProcessResourceLogDaily.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientDefragAnalysisResourceData.getTimeCreated()));

			clientControlProcessResourceLogDailies.add(clientControlProcessResourceLogDaily);
		}

		return clientControlProcessResourceLogDailies;
	}

	public List<ClientControlProcessResourceLog> reconstructDocument(ClientControlProcessResourceLogOriginal clientControlProcessResourceLogOriginal) {

		List<ClientControlProcessResourceLog> clientControlProcessResourceLogs = new ArrayList<ClientControlProcessResourceLog>();

		for( ClientControlProcessResourceLogData clientDefragAnalysisResourceData: clientControlProcessResourceLogOriginal.getClientControlProcessResourceData() ) {

			ClientControlProcessResourceLog clientControlProcessResourceLog = new ClientControlProcessResourceLog();
			clientControlProcessResourceLog.setService(clientControlProcessResourceLogOriginal.getService());
			clientControlProcessResourceLog.setAppId(clientControlProcessResourceLogOriginal.getAppId());
			clientControlProcessResourceLog.setOsType(clientControlProcessResourceLogOriginal.getOsType());
			clientControlProcessResourceLog.setSource(clientControlProcessResourceLogOriginal.getSource());
			clientControlProcessResourceLog.setDeviceId(clientControlProcessResourceLogOriginal.getDeviceId());
			clientControlProcessResourceLog.setIp(clientControlProcessResourceLogOriginal.getIp());
			clientControlProcessResourceLog.setHostName(clientControlProcessResourceLogOriginal.getHostName());
			clientControlProcessResourceLog.setTimeCreated(clientControlProcessResourceLogOriginal.getTimeCreated());

			clientControlProcessResourceLog.setUserId(clientControlProcessResourceLogOriginal.getUserId());
			clientControlProcessResourceLog.setTermNo(clientControlProcessResourceLogOriginal.getTermNo());
			clientControlProcessResourceLog.setSsoBrNo(clientControlProcessResourceLogOriginal.getSsoBrNo());
			clientControlProcessResourceLog.setBrNo(clientControlProcessResourceLogOriginal.getBrNo());
			clientControlProcessResourceLog.setDeptName(clientControlProcessResourceLogOriginal.getDeptName());
			clientControlProcessResourceLog.setHwnNo(clientControlProcessResourceLogOriginal.getHwnNo());
			clientControlProcessResourceLog.setUserName(clientControlProcessResourceLogOriginal.getUserName());
			clientControlProcessResourceLog.setSsoType(clientControlProcessResourceLogOriginal.getSsoType());
			clientControlProcessResourceLog.setPcName(clientControlProcessResourceLogOriginal.getPcName());
			clientControlProcessResourceLog.setPhoneNo(clientControlProcessResourceLogOriginal.getPhoneNo());
			clientControlProcessResourceLog.setJKGP(clientControlProcessResourceLogOriginal.getJKGP());
			clientControlProcessResourceLog.setJKWI(clientControlProcessResourceLogOriginal.getJKWI());
			clientControlProcessResourceLog.setMaxAddress(clientControlProcessResourceLogOriginal.getMaxAddress());
			clientControlProcessResourceLog.setFirstWork(clientControlProcessResourceLogOriginal.getFirstWork());

			clientControlProcessResourceLog.setPid(clientDefragAnalysisResourceData.getPid());
			clientControlProcessResourceLog.setProcName(clientDefragAnalysisResourceData.getProcName());
			clientControlProcessResourceLog.setProcParentPid(clientDefragAnalysisResourceData.getProcParentPid());
			clientControlProcessResourceLog.setProcSessionID(clientDefragAnalysisResourceData.getProcSessionID());
			clientControlProcessResourceLog.setExecResult(clientDefragAnalysisResourceData.getExecResult());

			clientControlProcessResourceLog.setTimeCurrent(clientDefragAnalysisResourceData.getTimeCreated());
			clientControlProcessResourceLog.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientDefragAnalysisResourceData.getTimeCreated()));

			clientControlProcessResourceLogs.add(clientControlProcessResourceLog);
		}

		return clientControlProcessResourceLogs;
	}

}
