package com.inswave.appplatform.log.translate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.log.util.LogDataObjectMapper;
import com.inswave.appplatform.transaver.elasticsearch.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClientProcessResource {

	public ClientProcessResource () {
	}

	public ClientProcessResourceLogOriginal getOriginalObject(StringBuffer message) {
		ClientProcessResourceLogOriginal clientProcessResourceLogOriginal = null;
		return clientProcessResourceLogOriginal;
	}

	public ClientProcessResourceLogOriginal getOriginalObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, ClientProcessResourceLogOriginal.class);
	}


	public List<ClientProcessResourceLogStatistics> reconstructMinutelyDocument(ClientProcessResourceLogOriginal clientProcessResourceLogOriginal) {

		List<ClientProcessResourceLogStatistics> clientProcessResourceLogMinutelies = new ArrayList<ClientProcessResourceLogStatistics>();

		for( ClientProcessResourceLogData collectWindowsEventLogData: clientProcessResourceLogOriginal.getClientProcessResourceData() ) {

			ClientProcessResourceLogStatistics clientProcessResourceLogMinutely = new ClientProcessResourceLogStatistics();
			clientProcessResourceLogMinutely.setService(clientProcessResourceLogOriginal.getService());
			clientProcessResourceLogMinutely.setAppId(clientProcessResourceLogOriginal.getAppId());
			clientProcessResourceLogMinutely.setOsType(clientProcessResourceLogOriginal.getOsType());
			clientProcessResourceLogMinutely.setSource(clientProcessResourceLogOriginal.getSource());
			clientProcessResourceLogMinutely.setDeviceId(clientProcessResourceLogOriginal.getDeviceId());
			clientProcessResourceLogMinutely.setIp(clientProcessResourceLogOriginal.getIp());
			clientProcessResourceLogMinutely.setHostName(clientProcessResourceLogOriginal.getHostName());
			clientProcessResourceLogMinutely.setTimeCreated(clientProcessResourceLogOriginal.getTimeCreated());

			clientProcessResourceLogMinutely.setUserId(clientProcessResourceLogOriginal.getUserId());
			clientProcessResourceLogMinutely.setTermNo(clientProcessResourceLogOriginal.getTermNo());
			clientProcessResourceLogMinutely.setSsoBrNo(clientProcessResourceLogOriginal.getSsoBrNo());
			clientProcessResourceLogMinutely.setBrNo(clientProcessResourceLogOriginal.getBrNo());
			clientProcessResourceLogMinutely.setDeptName(clientProcessResourceLogOriginal.getDeptName());
			clientProcessResourceLogMinutely.setHwnNo(clientProcessResourceLogOriginal.getHwnNo());
			clientProcessResourceLogMinutely.setUserName(clientProcessResourceLogOriginal.getUserName());
			clientProcessResourceLogMinutely.setSsoType(clientProcessResourceLogOriginal.getSsoType());
			clientProcessResourceLogMinutely.setPcName(clientProcessResourceLogOriginal.getPcName());
			clientProcessResourceLogMinutely.setPhoneNo(clientProcessResourceLogOriginal.getPhoneNo());
			clientProcessResourceLogMinutely.setJKGP(clientProcessResourceLogOriginal.getJKGP());
			clientProcessResourceLogMinutely.setJKWI(clientProcessResourceLogOriginal.getJKWI());
			clientProcessResourceLogMinutely.setMaxAddress(clientProcessResourceLogOriginal.getMaxAddress());
			clientProcessResourceLogMinutely.setFirstWork(clientProcessResourceLogOriginal.getFirstWork());

			clientProcessResourceLogMinutely.setCount(clientProcessResourceLogMinutely.getCount()+1);

			//clientProcessResourceLogMinutely.setPid(collectWindowsEventLogData.getPid());
			clientProcessResourceLogMinutely.setProcName(collectWindowsEventLogData.getProcName());
			clientProcessResourceLogMinutely.setProcCpuUsage(collectWindowsEventLogData.getProcCpuUsage());
			clientProcessResourceLogMinutely.setProcThreadCount(collectWindowsEventLogData.getProcThreadCount());
			clientProcessResourceLogMinutely.setProcHandleCount(collectWindowsEventLogData.getProcHandleCount());
			clientProcessResourceLogMinutely.setProcWorkingSet(collectWindowsEventLogData.getProcWorkingSet());
			clientProcessResourceLogMinutely.setProcPoolPage(collectWindowsEventLogData.getProcPoolPage());
			clientProcessResourceLogMinutely.setProcPoolNonpaged(collectWindowsEventLogData.getProcPoolNonpaged());
			clientProcessResourceLogMinutely.setProcIoRead(collectWindowsEventLogData.getProcIoRead());
			clientProcessResourceLogMinutely.setProcIoWrite(collectWindowsEventLogData.getProcIoWrite());
			clientProcessResourceLogMinutely.setProcMemoryUsed(collectWindowsEventLogData.getProcMemoryUsed());
			clientProcessResourceLogMinutely.setMemroyTotal(collectWindowsEventLogData.getMemroyTotal());
			clientProcessResourceLogMinutely.setProcFileDescription(collectWindowsEventLogData.getProcFileDescription());
			clientProcessResourceLogMinutely.setProcMemoryUsage(collectWindowsEventLogData.getProcMemoryUsage());

			clientProcessResourceLogMinutely.setTimeCurrent(collectWindowsEventLogData.getTimeCreated());
			clientProcessResourceLogMinutely.setStatisticsValue(DateTimeConvertor.getStatisticsValue(collectWindowsEventLogData.getTimeCreated()));

			clientProcessResourceLogMinutelies.add(clientProcessResourceLogMinutely);
		}
		return clientProcessResourceLogMinutelies;
	}

	public List<ClientProcessResourceLogDaily> reconstructDailyDocument(ClientProcessResourceLogOriginal clientProcessResourceLogOriginal) {

		List<ClientProcessResourceLogDaily> clientProcessResourceLogDailies = new ArrayList<ClientProcessResourceLogDaily>();

		for( ClientProcessResourceLogData collectWindowsEventLogData: clientProcessResourceLogOriginal.getClientProcessResourceData() ) {

			ClientProcessResourceLogDaily clientProcessResourceLogDaily = new ClientProcessResourceLogDaily();
			clientProcessResourceLogDaily.setService(clientProcessResourceLogOriginal.getService());
			clientProcessResourceLogDaily.setAppId(clientProcessResourceLogOriginal.getAppId());
			clientProcessResourceLogDaily.setOsType(clientProcessResourceLogOriginal.getOsType());
			clientProcessResourceLogDaily.setSource(clientProcessResourceLogOriginal.getSource());
			clientProcessResourceLogDaily.setDeviceId(clientProcessResourceLogOriginal.getDeviceId());
			clientProcessResourceLogDaily.setIp(clientProcessResourceLogOriginal.getIp());
			clientProcessResourceLogDaily.setHostName(clientProcessResourceLogOriginal.getHostName());
			clientProcessResourceLogDaily.setTimeCreated(clientProcessResourceLogOriginal.getTimeCreated());

			clientProcessResourceLogDaily.setUserId(clientProcessResourceLogOriginal.getUserId());
			clientProcessResourceLogDaily.setTermNo(clientProcessResourceLogOriginal.getTermNo());
			clientProcessResourceLogDaily.setSsoBrNo(clientProcessResourceLogOriginal.getSsoBrNo());
			clientProcessResourceLogDaily.setBrNo(clientProcessResourceLogOriginal.getBrNo());
			clientProcessResourceLogDaily.setDeptName(clientProcessResourceLogOriginal.getDeptName());
			clientProcessResourceLogDaily.setHwnNo(clientProcessResourceLogOriginal.getHwnNo());
			clientProcessResourceLogDaily.setUserName(clientProcessResourceLogOriginal.getUserName());
			clientProcessResourceLogDaily.setSsoType(clientProcessResourceLogOriginal.getSsoType());
			clientProcessResourceLogDaily.setPcName(clientProcessResourceLogOriginal.getPcName());
			clientProcessResourceLogDaily.setPhoneNo(clientProcessResourceLogOriginal.getPhoneNo());
			clientProcessResourceLogDaily.setJKGP(clientProcessResourceLogOriginal.getJKGP());
			clientProcessResourceLogDaily.setJKWI(clientProcessResourceLogOriginal.getJKWI());
			clientProcessResourceLogDaily.setMaxAddress(clientProcessResourceLogOriginal.getMaxAddress());
			clientProcessResourceLogDaily.setFirstWork(clientProcessResourceLogOriginal.getFirstWork());

			clientProcessResourceLogDaily.setPid(collectWindowsEventLogData.getPid());
			clientProcessResourceLogDaily.setProcName(collectWindowsEventLogData.getProcName());
			clientProcessResourceLogDaily.setProcCpuUsage(collectWindowsEventLogData.getProcCpuUsage());
			clientProcessResourceLogDaily.setProcThreadCount(collectWindowsEventLogData.getProcThreadCount());
			clientProcessResourceLogDaily.setProcHandleCount(collectWindowsEventLogData.getProcHandleCount());
			clientProcessResourceLogDaily.setProcWorkingSet(collectWindowsEventLogData.getProcWorkingSet());
			clientProcessResourceLogDaily.setProcPoolPage(collectWindowsEventLogData.getProcPoolPage());
			clientProcessResourceLogDaily.setProcPoolNonpaged(collectWindowsEventLogData.getProcPoolNonpaged());
			clientProcessResourceLogDaily.setProcIoRead(collectWindowsEventLogData.getProcIoRead());
			clientProcessResourceLogDaily.setProcIoWrite(collectWindowsEventLogData.getProcIoWrite());
			clientProcessResourceLogDaily.setProcMemoryUsed(collectWindowsEventLogData.getProcMemoryUsed());
			clientProcessResourceLogDaily.setMemroyTotal(collectWindowsEventLogData.getMemroyTotal());
			clientProcessResourceLogDaily.setProcNetworkReceived(collectWindowsEventLogData.getProcNetworkReceived());
			clientProcessResourceLogDaily.setProcNetworkSent(collectWindowsEventLogData.getProcNetworkSent());
			clientProcessResourceLogDaily.setProcFileDescription(collectWindowsEventLogData.getProcFileDescription());
			clientProcessResourceLogDaily.setProcIntegrityLevel(collectWindowsEventLogData.getProcIntegrityLevel());
			clientProcessResourceLogDaily.setProcMemoryUsage(collectWindowsEventLogData.getProcMemoryUsage());

			clientProcessResourceLogDaily.setTimeCurrent(collectWindowsEventLogData.getTimeCreated());
			clientProcessResourceLogDaily.setStatisticsValue(DateTimeConvertor.getStatisticsValue(collectWindowsEventLogData.getTimeCreated()));

			clientProcessResourceLogDailies.add(clientProcessResourceLogDaily);
		}
		return clientProcessResourceLogDailies;
	}

	public List<ClientProcessResourceLog> reconstructDocument(ClientProcessResourceLogOriginal clientProcessResourceLogOriginal) {

		List<ClientProcessResourceLog> clientProcessResourceLogs = new ArrayList<ClientProcessResourceLog>();

		for( ClientProcessResourceLogData collectWindowsEventLogData: clientProcessResourceLogOriginal.getClientProcessResourceData() ) {

			ClientProcessResourceLog clientProcessResourceLog = new ClientProcessResourceLog();
			clientProcessResourceLog.setService(clientProcessResourceLogOriginal.getService());
			clientProcessResourceLog.setAppId(clientProcessResourceLogOriginal.getAppId());
			clientProcessResourceLog.setOsType(clientProcessResourceLogOriginal.getOsType());
			clientProcessResourceLog.setSource(clientProcessResourceLogOriginal.getSource());
			clientProcessResourceLog.setDeviceId(clientProcessResourceLogOriginal.getDeviceId());
			clientProcessResourceLog.setIp(clientProcessResourceLogOriginal.getIp());
			clientProcessResourceLog.setHostName(clientProcessResourceLogOriginal.getHostName());
			clientProcessResourceLog.setTimeCreated(clientProcessResourceLogOriginal.getTimeCreated());

			clientProcessResourceLog.setUserId(clientProcessResourceLogOriginal.getUserId());
			clientProcessResourceLog.setTermNo(clientProcessResourceLogOriginal.getTermNo());
			clientProcessResourceLog.setSsoBrNo(clientProcessResourceLogOriginal.getSsoBrNo());
			clientProcessResourceLog.setBrNo(clientProcessResourceLogOriginal.getBrNo());
			clientProcessResourceLog.setDeptName(clientProcessResourceLogOriginal.getDeptName());
			clientProcessResourceLog.setHwnNo(clientProcessResourceLogOriginal.getHwnNo());
			clientProcessResourceLog.setUserName(clientProcessResourceLogOriginal.getUserName());
			clientProcessResourceLog.setSsoType(clientProcessResourceLogOriginal.getSsoType());
			clientProcessResourceLog.setPcName(clientProcessResourceLogOriginal.getPcName());
			clientProcessResourceLog.setPhoneNo(clientProcessResourceLogOriginal.getPhoneNo());
			clientProcessResourceLog.setJKGP(clientProcessResourceLogOriginal.getJKGP());
			clientProcessResourceLog.setJKWI(clientProcessResourceLogOriginal.getJKWI());
			clientProcessResourceLog.setMaxAddress(clientProcessResourceLogOriginal.getMaxAddress());
			clientProcessResourceLog.setFirstWork(clientProcessResourceLogOriginal.getFirstWork());

			clientProcessResourceLog.setPid(collectWindowsEventLogData.getPid());
			clientProcessResourceLog.setProcName(collectWindowsEventLogData.getProcName());
			clientProcessResourceLog.setProcCpuUsage(collectWindowsEventLogData.getProcCpuUsage());
			clientProcessResourceLog.setProcThreadCount(collectWindowsEventLogData.getProcThreadCount());
			clientProcessResourceLog.setProcHandleCount(collectWindowsEventLogData.getProcHandleCount());
			clientProcessResourceLog.setProcWorkingSet(collectWindowsEventLogData.getProcWorkingSet());
			clientProcessResourceLog.setProcPoolPage(collectWindowsEventLogData.getProcPoolPage());
			clientProcessResourceLog.setProcPoolNonpaged(collectWindowsEventLogData.getProcPoolNonpaged());
			clientProcessResourceLog.setProcIoRead(collectWindowsEventLogData.getProcIoRead());
			clientProcessResourceLog.setProcIoWrite(collectWindowsEventLogData.getProcIoWrite());
			clientProcessResourceLog.setProcMemoryUsed(collectWindowsEventLogData.getProcMemoryUsed());
			clientProcessResourceLog.setMemroyTotal(collectWindowsEventLogData.getMemroyTotal());
			clientProcessResourceLog.setProcFileDescription(collectWindowsEventLogData.getProcFileDescription());
			clientProcessResourceLog.setProcIntegrityLevel(collectWindowsEventLogData.getProcIntegrityLevel());
			clientProcessResourceLog.setProcMemoryUsage(collectWindowsEventLogData.getProcMemoryUsage());

			clientProcessResourceLog.setTimeCurrent(collectWindowsEventLogData.getTimeCreated());
			clientProcessResourceLog.setStatisticsValue(DateTimeConvertor.getStatisticsValue(collectWindowsEventLogData.getTimeCreated()));

			clientProcessResourceLogs.add(clientProcessResourceLog);
		}
		return clientProcessResourceLogs;
	}
}
