package com.inswave.appplatform.log.translate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientActivePortListResourceLog;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientActivePortListResourceLogDaily;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientActivePortListResourceLogData;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientActivePortListResourceLogOriginal;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.log.util.LogDataObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ClientActivePortListResource {

	public ClientActivePortListResource () {
	}

	public ClientActivePortListResourceLogOriginal getOriginalObject(StringBuffer message) {
		ClientActivePortListResourceLogOriginal clientActivePortListResourceLogOriginal = null;
		return clientActivePortListResourceLogOriginal;
	}

	public ClientActivePortListResourceLogOriginal getOriginalObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, ClientActivePortListResourceLogOriginal.class);
	}

	public List<ClientActivePortListResourceLogDaily> reconstructDailyDocument(ClientActivePortListResourceLogOriginal clientActivePortListResourceLogOriginal) {

		List<ClientActivePortListResourceLogDaily> clientActivePortListResourceLogDailies = new ArrayList<ClientActivePortListResourceLogDaily>();

		for( ClientActivePortListResourceLogData clientDefragAnalysisResourceData: clientActivePortListResourceLogOriginal.getClientActivePortListResourceData() ) {

			ClientActivePortListResourceLogDaily clientActivePortListResourceLogDaily = new ClientActivePortListResourceLogDaily();
			clientActivePortListResourceLogDaily.setService(clientActivePortListResourceLogOriginal.getService());
			clientActivePortListResourceLogDaily.setAppId(clientActivePortListResourceLogOriginal.getAppId());
			clientActivePortListResourceLogDaily.setOsType(clientActivePortListResourceLogOriginal.getOsType());
			clientActivePortListResourceLogDaily.setSource(clientActivePortListResourceLogOriginal.getSource());
			clientActivePortListResourceLogDaily.setDeviceId(clientActivePortListResourceLogOriginal.getDeviceId());
			clientActivePortListResourceLogDaily.setIp(clientActivePortListResourceLogOriginal.getIp());
			clientActivePortListResourceLogDaily.setHostName(clientActivePortListResourceLogOriginal.getHostName());
			clientActivePortListResourceLogDaily.setTimeCreated(clientActivePortListResourceLogOriginal.getTimeCreated());

			clientActivePortListResourceLogDaily.setUserId(clientActivePortListResourceLogOriginal.getUserId());
			clientActivePortListResourceLogDaily.setTermNo(clientActivePortListResourceLogOriginal.getTermNo());
			clientActivePortListResourceLogDaily.setSsoBrNo(clientActivePortListResourceLogOriginal.getSsoBrNo());
			clientActivePortListResourceLogDaily.setBrNo(clientActivePortListResourceLogOriginal.getBrNo());
			clientActivePortListResourceLogDaily.setDeptName(clientActivePortListResourceLogOriginal.getDeptName());
			clientActivePortListResourceLogDaily.setHwnNo(clientActivePortListResourceLogOriginal.getHwnNo());
			clientActivePortListResourceLogDaily.setUserName(clientActivePortListResourceLogOriginal.getUserName());
			clientActivePortListResourceLogDaily.setSsoType(clientActivePortListResourceLogOriginal.getSsoType());
			clientActivePortListResourceLogDaily.setPcName(clientActivePortListResourceLogOriginal.getPcName());
			clientActivePortListResourceLogDaily.setPhoneNo(clientActivePortListResourceLogOriginal.getPhoneNo());
			clientActivePortListResourceLogDaily.setJKGP(clientActivePortListResourceLogOriginal.getJKGP());
			clientActivePortListResourceLogDaily.setJKWI(clientActivePortListResourceLogOriginal.getJKWI());
			clientActivePortListResourceLogDaily.setMaxAddress(clientActivePortListResourceLogOriginal.getMaxAddress());
			clientActivePortListResourceLogDaily.setFirstWork(clientActivePortListResourceLogOriginal.getFirstWork());

			clientActivePortListResourceLogDaily.setProtocalType(clientDefragAnalysisResourceData.getProtocalType());
			clientActivePortListResourceLogDaily.setLocalAddress(clientDefragAnalysisResourceData.getLocalAddress());
			clientActivePortListResourceLogDaily.setLocalPort(clientDefragAnalysisResourceData.getLocalPort());
			clientActivePortListResourceLogDaily.setRemoteAddress(clientDefragAnalysisResourceData.getRemoteAddress());
			clientActivePortListResourceLogDaily.setRemotePort(clientDefragAnalysisResourceData.getRemotePort());
			clientActivePortListResourceLogDaily.setNetState(clientDefragAnalysisResourceData.getNetState());
			clientActivePortListResourceLogDaily.setProcId(clientDefragAnalysisResourceData.getProcId());
			clientActivePortListResourceLogDaily.setProcName(clientDefragAnalysisResourceData.getProcName());

			clientActivePortListResourceLogDaily.setTimeCurrent(clientDefragAnalysisResourceData.getTimeCreated());
			clientActivePortListResourceLogDaily.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientDefragAnalysisResourceData.getTimeCreated()));

			clientActivePortListResourceLogDailies.add(clientActivePortListResourceLogDaily);
		}

		return clientActivePortListResourceLogDailies;
	}

	public List<ClientActivePortListResourceLog> reconstructDocument(ClientActivePortListResourceLogOriginal clientActivePortListResourceLogOriginal) {

		List<ClientActivePortListResourceLog> clientActivePortListResourceLogs = new ArrayList<ClientActivePortListResourceLog>();

		for( ClientActivePortListResourceLogData clientDefragAnalysisResourceData: clientActivePortListResourceLogOriginal.getClientActivePortListResourceData() ) {

			ClientActivePortListResourceLog clientActivePortListResourceLog = new ClientActivePortListResourceLog();
			clientActivePortListResourceLog.setService(clientActivePortListResourceLogOriginal.getService());
			clientActivePortListResourceLog.setAppId(clientActivePortListResourceLogOriginal.getAppId());
			clientActivePortListResourceLog.setOsType(clientActivePortListResourceLogOriginal.getOsType());
			clientActivePortListResourceLog.setSource(clientActivePortListResourceLogOriginal.getSource());
			clientActivePortListResourceLog.setDeviceId(clientActivePortListResourceLogOriginal.getDeviceId());
			clientActivePortListResourceLog.setIp(clientActivePortListResourceLogOriginal.getIp());
			clientActivePortListResourceLog.setHostName(clientActivePortListResourceLogOriginal.getHostName());
			clientActivePortListResourceLog.setTimeCreated(clientActivePortListResourceLogOriginal.getTimeCreated());

			clientActivePortListResourceLog.setUserId(clientActivePortListResourceLogOriginal.getUserId());
			clientActivePortListResourceLog.setTermNo(clientActivePortListResourceLogOriginal.getTermNo());
			clientActivePortListResourceLog.setSsoBrNo(clientActivePortListResourceLogOriginal.getSsoBrNo());
			clientActivePortListResourceLog.setBrNo(clientActivePortListResourceLogOriginal.getBrNo());
			clientActivePortListResourceLog.setDeptName(clientActivePortListResourceLogOriginal.getDeptName());
			clientActivePortListResourceLog.setHwnNo(clientActivePortListResourceLogOriginal.getHwnNo());
			clientActivePortListResourceLog.setUserName(clientActivePortListResourceLogOriginal.getUserName());
			clientActivePortListResourceLog.setSsoType(clientActivePortListResourceLogOriginal.getSsoType());
			clientActivePortListResourceLog.setPcName(clientActivePortListResourceLogOriginal.getPcName());
			clientActivePortListResourceLog.setPhoneNo(clientActivePortListResourceLogOriginal.getPhoneNo());
			clientActivePortListResourceLog.setJKGP(clientActivePortListResourceLogOriginal.getJKGP());
			clientActivePortListResourceLog.setJKWI(clientActivePortListResourceLogOriginal.getJKWI());
			clientActivePortListResourceLog.setMaxAddress(clientActivePortListResourceLogOriginal.getMaxAddress());
			clientActivePortListResourceLog.setFirstWork(clientActivePortListResourceLogOriginal.getFirstWork());

			clientActivePortListResourceLog.setProtocalType(clientDefragAnalysisResourceData.getProtocalType());
			clientActivePortListResourceLog.setLocalAddress(clientDefragAnalysisResourceData.getLocalAddress());
			clientActivePortListResourceLog.setLocalPort(clientDefragAnalysisResourceData.getLocalPort());
			clientActivePortListResourceLog.setRemoteAddress(clientDefragAnalysisResourceData.getRemoteAddress());
			clientActivePortListResourceLog.setRemotePort(clientDefragAnalysisResourceData.getRemotePort());
			clientActivePortListResourceLog.setNetState(clientDefragAnalysisResourceData.getNetState());
			clientActivePortListResourceLog.setProcId(clientDefragAnalysisResourceData.getProcId());
			clientActivePortListResourceLog.setProcName(clientDefragAnalysisResourceData.getProcName());

			clientActivePortListResourceLog.setTimeCurrent(clientDefragAnalysisResourceData.getTimeCreated());
			clientActivePortListResourceLog.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientDefragAnalysisResourceData.getTimeCreated()));

			clientActivePortListResourceLogs.add(clientActivePortListResourceLog);
		}

		return clientActivePortListResourceLogs;
	}

}
