package com.inswave.appplatform.log.translate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.log.util.LogDataObjectMapper;
import com.inswave.appplatform.transaver.elasticsearch.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClientPerformanceResource {

	public ClientPerformanceResource () {
	}

	public ClientPerformanceResourceLogOriginal getOriginalObject(StringBuffer message) {
		ClientPerformanceResourceLogOriginal clientPerformanceResourceLogOriginal = null;
		return clientPerformanceResourceLogOriginal;
	}

	public ClientPerformanceResourceLogOriginal getOriginalObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, ClientPerformanceResourceLogOriginal.class);
	}


	public List<ClientPerformanceResourceLogStatistics> reconstructStatisticsDocument(ClientPerformanceResourceLogOriginal clientPerformanceResourceLogOriginal) {

		List<ClientPerformanceResourceLogStatistics> clientPerformanceResourceLogMinutelies = new ArrayList<ClientPerformanceResourceLogStatistics>();

		for ( ClientPerformanceResourceLogData clientPerformanceResourceLogData : clientPerformanceResourceLogOriginal.getClientPerfResourceData() ) {
			ClientPerformanceResourceLogStatistics clientPerformanceResourceLogMinutely = new ClientPerformanceResourceLogStatistics();
			clientPerformanceResourceLogMinutely.setService(clientPerformanceResourceLogOriginal.getService());
			clientPerformanceResourceLogMinutely.setAppId(clientPerformanceResourceLogOriginal.getAppId());
			clientPerformanceResourceLogMinutely.setOsType(clientPerformanceResourceLogOriginal.getOsType());
			clientPerformanceResourceLogMinutely.setSource(clientPerformanceResourceLogOriginal.getSource());
			clientPerformanceResourceLogMinutely.setDeviceId(clientPerformanceResourceLogOriginal.getDeviceId());
			clientPerformanceResourceLogMinutely.setIp(clientPerformanceResourceLogOriginal.getIp());
			clientPerformanceResourceLogMinutely.setHostName(clientPerformanceResourceLogOriginal.getHostName());
			clientPerformanceResourceLogMinutely.setTimeCreated(clientPerformanceResourceLogOriginal.getTimeCreated());

			clientPerformanceResourceLogMinutely.setUserId(clientPerformanceResourceLogOriginal.getUserId());
			clientPerformanceResourceLogMinutely.setTermNo(clientPerformanceResourceLogOriginal.getTermNo());
			clientPerformanceResourceLogMinutely.setSsoBrNo(clientPerformanceResourceLogOriginal.getSsoBrNo());
			clientPerformanceResourceLogMinutely.setBrNo(clientPerformanceResourceLogOriginal.getBrNo());
			clientPerformanceResourceLogMinutely.setDeptName(clientPerformanceResourceLogOriginal.getDeptName());
			clientPerformanceResourceLogMinutely.setHwnNo(clientPerformanceResourceLogOriginal.getHwnNo());
			clientPerformanceResourceLogMinutely.setUserName(clientPerformanceResourceLogOriginal.getUserName());
			clientPerformanceResourceLogMinutely.setSsoType(clientPerformanceResourceLogOriginal.getSsoType());
			clientPerformanceResourceLogMinutely.setPcName(clientPerformanceResourceLogOriginal.getPcName());
			clientPerformanceResourceLogMinutely.setPhoneNo(clientPerformanceResourceLogOriginal.getPhoneNo());
			clientPerformanceResourceLogMinutely.setJKGP(clientPerformanceResourceLogOriginal.getJKGP());
			clientPerformanceResourceLogMinutely.setJKWI(clientPerformanceResourceLogOriginal.getJKWI());
			clientPerformanceResourceLogMinutely.setMaxAddress(clientPerformanceResourceLogOriginal.getMaxAddress());
			clientPerformanceResourceLogMinutely.setFirstWork(clientPerformanceResourceLogOriginal.getFirstWork());

			clientPerformanceResourceLogMinutely.setCpuUsage(clientPerformanceResourceLogData.getCpuUsage());
			clientPerformanceResourceLogMinutely.setThreadCount(clientPerformanceResourceLogData.getThreadCount());
			clientPerformanceResourceLogMinutely.setHandleCount(clientPerformanceResourceLogData.getHandleCount());
			clientPerformanceResourceLogMinutely.setMemoryUsed(clientPerformanceResourceLogData.getMemoryUsed());
			clientPerformanceResourceLogMinutely.setMemoryTotal(clientPerformanceResourceLogData.getMemoryTotal());
			clientPerformanceResourceLogMinutely.setMemoryUsage(clientPerformanceResourceLogData.getMemoryUsage());
			//clientPerformanceResourceLogMinutely.setPoolPaged(clientPerformanceResourceLogData.getPoolPaged());
			clientPerformanceResourceLogMinutely.setPoolNonpaged(clientPerformanceResourceLogData.getPoolNonpaged());
			clientPerformanceResourceLogMinutely.setDiskUsage(clientPerformanceResourceLogData.getDiskUsage());
			//clientPerformanceResourceLogMinutely.setDiskTotal(clientPerformanceResourceLogData.getDiskTotal());
			clientPerformanceResourceLogMinutely.setDiskUsed(clientPerformanceResourceLogData.getDiskUsed());
			//clientPerformanceResourceLogMinutely.setDiskTime(clientPerformanceResourceLogData.getDiskTime());
			clientPerformanceResourceLogMinutely.setDiskRead(clientPerformanceResourceLogData.getDiskRead());
			clientPerformanceResourceLogMinutely.setDiskWrite(clientPerformanceResourceLogData.getDiskWrite());
			clientPerformanceResourceLogMinutely.setNetworkSent(clientPerformanceResourceLogData.getNetworkSent());
			clientPerformanceResourceLogMinutely.setNetworkReceived(clientPerformanceResourceLogData.getNetworkReceived());
			//clientPerformanceResourceLogMinutely.setNetworkUsed(clientPerformanceResourceLogData.getNetworkUsed());
			clientPerformanceResourceLogMinutely.setCpuTemperature(clientPerformanceResourceLogData.getCpuTemperature());
			clientPerformanceResourceLogMinutely.setMemoryVirtualTotal(clientPerformanceResourceLogData.getMemoryVirtualTotal());

			clientPerformanceResourceLogMinutely.setTimeCurrent(clientPerformanceResourceLogData.getTimeCreated());
			clientPerformanceResourceLogMinutely.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientPerformanceResourceLogData.getTimeCreated()));

			clientPerformanceResourceLogMinutelies.add(clientPerformanceResourceLogMinutely);
		}

		return clientPerformanceResourceLogMinutelies;
	}

	public List<ClientPerformanceResourceLogDaily> reconstructDailyDocument(ClientPerformanceResourceLogOriginal clientPerformanceResourceLogOriginal) {

		List<ClientPerformanceResourceLogDaily> clientPerformanceResourceLogDailies = new ArrayList<ClientPerformanceResourceLogDaily>();

		for ( ClientPerformanceResourceLogData clientPerformanceResourceLogData : clientPerformanceResourceLogOriginal.getClientPerfResourceData() ) {
			ClientPerformanceResourceLogDaily clientPerformanceResourceLogDaily = new ClientPerformanceResourceLogDaily();
			clientPerformanceResourceLogDaily.setService(clientPerformanceResourceLogOriginal.getService());
			clientPerformanceResourceLogDaily.setAppId(clientPerformanceResourceLogOriginal.getAppId());
			clientPerformanceResourceLogDaily.setOsType(clientPerformanceResourceLogOriginal.getOsType());
			clientPerformanceResourceLogDaily.setSource(clientPerformanceResourceLogOriginal.getSource());
			clientPerformanceResourceLogDaily.setDeviceId(clientPerformanceResourceLogOriginal.getDeviceId());
			clientPerformanceResourceLogDaily.setIp(clientPerformanceResourceLogOriginal.getIp());
			clientPerformanceResourceLogDaily.setHostName(clientPerformanceResourceLogOriginal.getHostName());
			clientPerformanceResourceLogDaily.setTimeCreated(clientPerformanceResourceLogOriginal.getTimeCreated());

			clientPerformanceResourceLogDaily.setUserId(clientPerformanceResourceLogOriginal.getUserId());
			clientPerformanceResourceLogDaily.setTermNo(clientPerformanceResourceLogOriginal.getTermNo());
			clientPerformanceResourceLogDaily.setSsoBrNo(clientPerformanceResourceLogOriginal.getSsoBrNo());
			clientPerformanceResourceLogDaily.setBrNo(clientPerformanceResourceLogOriginal.getBrNo());
			clientPerformanceResourceLogDaily.setDeptName(clientPerformanceResourceLogOriginal.getDeptName());
			clientPerformanceResourceLogDaily.setHwnNo(clientPerformanceResourceLogOriginal.getHwnNo());
			clientPerformanceResourceLogDaily.setUserName(clientPerformanceResourceLogOriginal.getUserName());
			clientPerformanceResourceLogDaily.setSsoType(clientPerformanceResourceLogOriginal.getSsoType());
			clientPerformanceResourceLogDaily.setPcName(clientPerformanceResourceLogOriginal.getPcName());
			clientPerformanceResourceLogDaily.setPhoneNo(clientPerformanceResourceLogOriginal.getPhoneNo());
			clientPerformanceResourceLogDaily.setJKGP(clientPerformanceResourceLogOriginal.getJKGP());
			clientPerformanceResourceLogDaily.setJKWI(clientPerformanceResourceLogOriginal.getJKWI());
			clientPerformanceResourceLogDaily.setMaxAddress(clientPerformanceResourceLogOriginal.getMaxAddress());
			clientPerformanceResourceLogDaily.setFirstWork(clientPerformanceResourceLogOriginal.getFirstWork());

			clientPerformanceResourceLogDaily.setCpuUsage(clientPerformanceResourceLogData.getCpuUsage());
			clientPerformanceResourceLogDaily.setThreadCount(clientPerformanceResourceLogData.getThreadCount());
			clientPerformanceResourceLogDaily.setHandleCount(clientPerformanceResourceLogData.getHandleCount());
			clientPerformanceResourceLogDaily.setMemoryUsed(clientPerformanceResourceLogData.getMemoryUsed());
			clientPerformanceResourceLogDaily.setMemoryTotal(clientPerformanceResourceLogData.getMemoryTotal());
			clientPerformanceResourceLogDaily.setMemoryUsage(clientPerformanceResourceLogData.getMemoryUsage());
			clientPerformanceResourceLogDaily.setPoolPaged(clientPerformanceResourceLogData.getPoolPaged());
			clientPerformanceResourceLogDaily.setPoolNonpaged(clientPerformanceResourceLogData.getPoolNonpaged());
			clientPerformanceResourceLogDaily.setDiskUsage(clientPerformanceResourceLogData.getDiskUsage());
			clientPerformanceResourceLogDaily.setDiskTotal(clientPerformanceResourceLogData.getDiskTotal());
			clientPerformanceResourceLogDaily.setDiskUsed(clientPerformanceResourceLogData.getDiskUsed());
			clientPerformanceResourceLogDaily.setDiskTime(clientPerformanceResourceLogData.getDiskTime());
			clientPerformanceResourceLogDaily.setDiskRead(clientPerformanceResourceLogData.getDiskRead());
			clientPerformanceResourceLogDaily.setDiskWrite(clientPerformanceResourceLogData.getDiskWrite());
			clientPerformanceResourceLogDaily.setNetworkSent(clientPerformanceResourceLogData.getNetworkSent());
			clientPerformanceResourceLogDaily.setNetworkReceived(clientPerformanceResourceLogData.getNetworkReceived());
			clientPerformanceResourceLogDaily.setNetworkUsed(clientPerformanceResourceLogData.getNetworkUsed());
			clientPerformanceResourceLogDaily.setCpuTemperature(clientPerformanceResourceLogData.getCpuTemperature());
			clientPerformanceResourceLogDaily.setMemoryVirtualTotal(clientPerformanceResourceLogData.getMemoryVirtualTotal());
			clientPerformanceResourceLogDaily.setDiskInfo(clientPerformanceResourceLogData.getDiskInfo());

			clientPerformanceResourceLogDaily.setTimeCurrent(clientPerformanceResourceLogData.getTimeCreated());
			clientPerformanceResourceLogDaily.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientPerformanceResourceLogData.getTimeCreated()));

			clientPerformanceResourceLogDailies.add(clientPerformanceResourceLogDaily);
		}

		return clientPerformanceResourceLogDailies;
	}

	public List<ClientPerformanceResourceLog> reconstructDocument(ClientPerformanceResourceLogOriginal clientPerformanceResourceLogOriginal) {

		List<ClientPerformanceResourceLog> clientPerformanceResourceLogs = new ArrayList<ClientPerformanceResourceLog>();

		for ( ClientPerformanceResourceLogData clientPerformanceResourceLogData : clientPerformanceResourceLogOriginal.getClientPerfResourceData() ) {
			ClientPerformanceResourceLog clientPerformanceResourceLog = new ClientPerformanceResourceLog();
			clientPerformanceResourceLog.setService(clientPerformanceResourceLogOriginal.getService());
			clientPerformanceResourceLog.setAppId(clientPerformanceResourceLogOriginal.getAppId());
			clientPerformanceResourceLog.setOsType(clientPerformanceResourceLogOriginal.getOsType());
			clientPerformanceResourceLog.setSource(clientPerformanceResourceLogOriginal.getSource());
			clientPerformanceResourceLog.setDeviceId(clientPerformanceResourceLogOriginal.getDeviceId());
			clientPerformanceResourceLog.setIp(clientPerformanceResourceLogOriginal.getIp());
			clientPerformanceResourceLog.setHostName(clientPerformanceResourceLogOriginal.getHostName());
			clientPerformanceResourceLog.setTimeCreated(clientPerformanceResourceLogOriginal.getTimeCreated());

			clientPerformanceResourceLog.setUserId(clientPerformanceResourceLogOriginal.getUserId());
			clientPerformanceResourceLog.setTermNo(clientPerformanceResourceLogOriginal.getTermNo());
			clientPerformanceResourceLog.setSsoBrNo(clientPerformanceResourceLogOriginal.getSsoBrNo());
			clientPerformanceResourceLog.setBrNo(clientPerformanceResourceLogOriginal.getBrNo());
			clientPerformanceResourceLog.setDeptName(clientPerformanceResourceLogOriginal.getDeptName());
			clientPerformanceResourceLog.setHwnNo(clientPerformanceResourceLogOriginal.getHwnNo());
			clientPerformanceResourceLog.setUserName(clientPerformanceResourceLogOriginal.getUserName());
			clientPerformanceResourceLog.setSsoType(clientPerformanceResourceLogOriginal.getSsoType());
			clientPerformanceResourceLog.setPcName(clientPerformanceResourceLogOriginal.getPcName());
			clientPerformanceResourceLog.setPhoneNo(clientPerformanceResourceLogOriginal.getPhoneNo());
			clientPerformanceResourceLog.setJKGP(clientPerformanceResourceLogOriginal.getJKGP());
			clientPerformanceResourceLog.setJKWI(clientPerformanceResourceLogOriginal.getJKWI());
			clientPerformanceResourceLog.setMaxAddress(clientPerformanceResourceLogOriginal.getMaxAddress());
			clientPerformanceResourceLog.setFirstWork(clientPerformanceResourceLogOriginal.getFirstWork());

			clientPerformanceResourceLog.setCpuUsage(clientPerformanceResourceLogData.getCpuUsage());
			clientPerformanceResourceLog.setThreadCount(clientPerformanceResourceLogData.getThreadCount());
			clientPerformanceResourceLog.setHandleCount(clientPerformanceResourceLogData.getHandleCount());
			clientPerformanceResourceLog.setMemoryUsed(clientPerformanceResourceLogData.getMemoryUsed());
			clientPerformanceResourceLog.setMemoryTotal(clientPerformanceResourceLogData.getMemoryTotal());
			clientPerformanceResourceLog.setMemoryUsage(clientPerformanceResourceLogData.getMemoryUsage());
			clientPerformanceResourceLog.setPoolPaged(clientPerformanceResourceLogData.getPoolPaged());
			clientPerformanceResourceLog.setPoolNonpaged(clientPerformanceResourceLogData.getPoolNonpaged());
			clientPerformanceResourceLog.setDiskUsage(clientPerformanceResourceLogData.getDiskUsage());
			clientPerformanceResourceLog.setDiskTotal(clientPerformanceResourceLogData.getDiskTotal());
			clientPerformanceResourceLog.setDiskUsed(clientPerformanceResourceLogData.getDiskUsed());
			clientPerformanceResourceLog.setDiskTime(clientPerformanceResourceLogData.getDiskTime());
			clientPerformanceResourceLog.setDiskRead(clientPerformanceResourceLogData.getDiskRead());
			clientPerformanceResourceLog.setDiskWrite(clientPerformanceResourceLogData.getDiskWrite());
			clientPerformanceResourceLog.setNetworkSent(clientPerformanceResourceLogData.getNetworkSent());
			clientPerformanceResourceLog.setNetworkReceived(clientPerformanceResourceLogData.getNetworkReceived());
			clientPerformanceResourceLog.setNetworkUsed(clientPerformanceResourceLogData.getNetworkUsed());
			clientPerformanceResourceLog.setCpuTemperature(clientPerformanceResourceLogData.getCpuTemperature());
			clientPerformanceResourceLog.setMemoryVirtualTotal(clientPerformanceResourceLogData.getMemoryVirtualTotal());
			clientPerformanceResourceLog.setDiskInfo(clientPerformanceResourceLogData.getDiskInfo());

			clientPerformanceResourceLog.setTimeCurrent(clientPerformanceResourceLogData.getTimeCreated());
			clientPerformanceResourceLog.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientPerformanceResourceLogData.getTimeCreated()));

			clientPerformanceResourceLogs.add(clientPerformanceResourceLog);
		}

		return clientPerformanceResourceLogs;
	}

}
