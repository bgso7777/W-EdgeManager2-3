package com.inswave.appplatform.log.translate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientHWInfoResourceLog;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientHWInfoResourceLogDaily;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientHWInfoResourceLogData;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientHWInfoResourceLogOriginal;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.log.util.LogDataObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ClientHWInfoResource {

	public ClientHWInfoResource () {
	}

	public ClientHWInfoResourceLogOriginal getOriginalObject(StringBuffer message) {
		ClientHWInfoResourceLogOriginal clientHWInfoResourceLogOriginal = null;
		return clientHWInfoResourceLogOriginal;
	}

	public ClientHWInfoResourceLogOriginal getOriginalObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, ClientHWInfoResourceLogOriginal.class);
	}


	public List<ClientHWInfoResourceLogDaily> reconstructDailyDocument(ClientHWInfoResourceLogOriginal clientHWInfoResourceLogOriginal) {

		List<ClientHWInfoResourceLogDaily> applicationErrorLogDailies = new ArrayList<ClientHWInfoResourceLogDaily>();

		for( ClientHWInfoResourceLogData clientHWInfoResourceLogData: clientHWInfoResourceLogOriginal.getClientHWInfoResourceData() ) {

			ClientHWInfoResourceLogDaily clientHWInfoResourceLogDaily = new ClientHWInfoResourceLogDaily();
			clientHWInfoResourceLogDaily.setService(clientHWInfoResourceLogOriginal.getService());
			clientHWInfoResourceLogDaily.setAppId(clientHWInfoResourceLogOriginal.getAppId());
			clientHWInfoResourceLogDaily.setOsType(clientHWInfoResourceLogOriginal.getOsType());
			clientHWInfoResourceLogDaily.setSource(clientHWInfoResourceLogOriginal.getSource());
			clientHWInfoResourceLogDaily.setDeviceId(clientHWInfoResourceLogOriginal.getDeviceId());
			clientHWInfoResourceLogDaily.setIp(clientHWInfoResourceLogOriginal.getIp());
			clientHWInfoResourceLogDaily.setHostName(clientHWInfoResourceLogOriginal.getHostName());
			clientHWInfoResourceLogDaily.setTimeCreated(clientHWInfoResourceLogOriginal.getTimeCreated());

			clientHWInfoResourceLogDaily.setUserId(clientHWInfoResourceLogOriginal.getUserId());
			clientHWInfoResourceLogDaily.setTermNo(clientHWInfoResourceLogOriginal.getTermNo());
			clientHWInfoResourceLogDaily.setSsoBrNo(clientHWInfoResourceLogOriginal.getSsoBrNo());
			clientHWInfoResourceLogDaily.setBrNo(clientHWInfoResourceLogOriginal.getBrNo());
			clientHWInfoResourceLogDaily.setDeptName(clientHWInfoResourceLogOriginal.getDeptName());
			clientHWInfoResourceLogDaily.setHwnNo(clientHWInfoResourceLogOriginal.getHwnNo());
			clientHWInfoResourceLogDaily.setUserName(clientHWInfoResourceLogOriginal.getUserName());
			clientHWInfoResourceLogDaily.setSsoType(clientHWInfoResourceLogOriginal.getSsoType());
			clientHWInfoResourceLogDaily.setPcName(clientHWInfoResourceLogOriginal.getPcName());
			clientHWInfoResourceLogDaily.setPhoneNo(clientHWInfoResourceLogOriginal.getPhoneNo());
			clientHWInfoResourceLogDaily.setJKGP(clientHWInfoResourceLogOriginal.getJKGP());
			clientHWInfoResourceLogDaily.setJKWI(clientHWInfoResourceLogOriginal.getJKWI());
			clientHWInfoResourceLogDaily.setMaxAddress(clientHWInfoResourceLogOriginal.getMaxAddress());
			clientHWInfoResourceLogDaily.setFirstWork(clientHWInfoResourceLogOriginal.getFirstWork());

			clientHWInfoResourceLogDaily.setOsBuildNumber(clientHWInfoResourceLogData.getOsBuildNumber());
			clientHWInfoResourceLogDaily.setOsServicePackVersion(clientHWInfoResourceLogData.getOsServicePackVersion());
			clientHWInfoResourceLogDaily.setOsVersion(clientHWInfoResourceLogData.getOsVersion());
			clientHWInfoResourceLogDaily.setOsName(clientHWInfoResourceLogData.getOsName());
			clientHWInfoResourceLogDaily.setOsArchitecture(clientHWInfoResourceLogData.getOsArchitecture());
			clientHWInfoResourceLogDaily.setOsSerialNumber(clientHWInfoResourceLogData.getOsSerialNumber());
			clientHWInfoResourceLogDaily.setOsCaption(clientHWInfoResourceLogData.getOsCaption());
			clientHWInfoResourceLogDaily.setOsStatus(clientHWInfoResourceLogData.getOsStatus());
			clientHWInfoResourceLogDaily.setPcName(clientHWInfoResourceLogData.getPcName());
			clientHWInfoResourceLogDaily.setPcModel(clientHWInfoResourceLogData.getPcModel());
			clientHWInfoResourceLogDaily.setPcManufacturer(clientHWInfoResourceLogData.getPcManufacturer());
			clientHWInfoResourceLogDaily.setPcCaption(clientHWInfoResourceLogData.getPcCaption());
			clientHWInfoResourceLogDaily.setPcSystemType(clientHWInfoResourceLogData.getPcSystemType());
			clientHWInfoResourceLogDaily.setPcStatus(clientHWInfoResourceLogData.getPcStatus());
			clientHWInfoResourceLogDaily.setCpuName(clientHWInfoResourceLogData.getCpuName());
			clientHWInfoResourceLogDaily.setCpuArchitecture(clientHWInfoResourceLogData.getCpuArchitecture());
			clientHWInfoResourceLogDaily.setCpuManufacturer(clientHWInfoResourceLogData.getCpuManufacturer());
			clientHWInfoResourceLogDaily.setCpuNumberOfCores(clientHWInfoResourceLogData.getCpuNumberOfCores());
			clientHWInfoResourceLogDaily.setCpuThreadCount(clientHWInfoResourceLogData.getCpuThreadCount());
			clientHWInfoResourceLogDaily.setCpuMaxClockSpeed(clientHWInfoResourceLogData.getCpuMaxClockSpeed());
			clientHWInfoResourceLogDaily.setCpuStatus(clientHWInfoResourceLogData.getCpuStatus());
			clientHWInfoResourceLogDaily.setCpuCaption(clientHWInfoResourceLogData.getCpuCaption());
			clientHWInfoResourceLogDaily.setMemName(clientHWInfoResourceLogData.getMemName());
			clientHWInfoResourceLogDaily.setMemModel(clientHWInfoResourceLogData.getMemModel());
			clientHWInfoResourceLogDaily.setMemManufacturer(clientHWInfoResourceLogData.getMemManufacturer());
			clientHWInfoResourceLogDaily.setMemBankLabel(clientHWInfoResourceLogData.getMemBankLabel());
			clientHWInfoResourceLogDaily.setMemDeviceLocator(clientHWInfoResourceLogData.getMemDeviceLocator());
			clientHWInfoResourceLogDaily.setMemSerialNumber(clientHWInfoResourceLogData.getMemSerialNumber());
			clientHWInfoResourceLogDaily.setMemSpeed(clientHWInfoResourceLogData.getMemSpeed());
			clientHWInfoResourceLogDaily.setMemMemoryType(clientHWInfoResourceLogData.getMemMemoryType());
			clientHWInfoResourceLogDaily.setMemTotalSize(clientHWInfoResourceLogData.getMemTotalSize());
			clientHWInfoResourceLogDaily.setMemVirtualTotalSize(clientHWInfoResourceLogData.getMemVirtualTotalSize());
			clientHWInfoResourceLogDaily.setMemStatus(clientHWInfoResourceLogData.getMemStatus());
			clientHWInfoResourceLogDaily.setMemCaption(clientHWInfoResourceLogData.getMemCaption());
			clientHWInfoResourceLogDaily.setMemName_2(clientHWInfoResourceLogData.getMemName_2());
			clientHWInfoResourceLogDaily.setMemModel_2(clientHWInfoResourceLogData.getMemModel_2());
			clientHWInfoResourceLogDaily.setMemManufacturer_2(clientHWInfoResourceLogData.getMemManufacturer_2());
			clientHWInfoResourceLogDaily.setMemBankLabel_2(clientHWInfoResourceLogData.getMemBankLabel_2());
			clientHWInfoResourceLogDaily.setMemDeviceLocator_2(clientHWInfoResourceLogData.getMemDeviceLocator_2());
			clientHWInfoResourceLogDaily.setMemSerialNumber_2(clientHWInfoResourceLogData.getMemSerialNumber_2());
			clientHWInfoResourceLogDaily.setMemSpeed_2(clientHWInfoResourceLogData.getMemSpeed_2());
			clientHWInfoResourceLogDaily.setMemMemoryType_2(clientHWInfoResourceLogData.getMemMemoryType_2());
			clientHWInfoResourceLogDaily.setMemStatus_2(clientHWInfoResourceLogData.getMemStatus_2());
			clientHWInfoResourceLogDaily.setMemCaption_2(clientHWInfoResourceLogData.getMemCaption_2());
			clientHWInfoResourceLogDaily.setDiskName(clientHWInfoResourceLogData.getDiskName());
			clientHWInfoResourceLogDaily.setDiskModel(clientHWInfoResourceLogData.getDiskModel());
			clientHWInfoResourceLogDaily.setDiskManufacturer(clientHWInfoResourceLogData.getDiskManufacturer());
			clientHWInfoResourceLogDaily.setDiskSerialNumber(clientHWInfoResourceLogData.getDiskSerialNumber());
			clientHWInfoResourceLogDaily.setDiskCaption(clientHWInfoResourceLogData.getDiskCaption());
			clientHWInfoResourceLogDaily.setDiskInterfaceType(clientHWInfoResourceLogData.getDiskInterfaceType());
			clientHWInfoResourceLogDaily.setDiskMediaType(clientHWInfoResourceLogData.getDiskMediaType());
			clientHWInfoResourceLogDaily.setDiskKBSize(clientHWInfoResourceLogData.getDiskKBSize());
			clientHWInfoResourceLogDaily.setDiskStatus(clientHWInfoResourceLogData.getDiskStatus());

			clientHWInfoResourceLogDaily.setDiskName_2(clientHWInfoResourceLogData.getDiskName_2());
			clientHWInfoResourceLogDaily.setDiskModel_2(clientHWInfoResourceLogData.getDiskModel_2());
			clientHWInfoResourceLogDaily.setDiskManufacturer_2(clientHWInfoResourceLogData.getDiskManufacturer_2());
			clientHWInfoResourceLogDaily.setDiskSerialNumber_2(clientHWInfoResourceLogData.getDiskSerialNumber_2());
			clientHWInfoResourceLogDaily.setDiskCaption_2(clientHWInfoResourceLogData.getDiskCaption_2());
			clientHWInfoResourceLogDaily.setDiskInterfaceType_2(clientHWInfoResourceLogData.getDiskInterfaceType_2());
			clientHWInfoResourceLogDaily.setDiskMediaType_2(clientHWInfoResourceLogData.getDiskMediaType_2());
			clientHWInfoResourceLogDaily.setDiskKBSize_2(clientHWInfoResourceLogData.getDiskKBSize_2());
			clientHWInfoResourceLogDaily.setDiskStatus_2(clientHWInfoResourceLogData.getDiskStatus_2());

			clientHWInfoResourceLogDaily.setTimeCurrent(clientHWInfoResourceLogData.getTimeCreated());
			clientHWInfoResourceLogDaily.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientHWInfoResourceLogData.getTimeCreated()));

			clientHWInfoResourceLogDaily.setDiskDrive(clientHWInfoResourceLogData.getDiskDrive());
			clientHWInfoResourceLogDaily.setPhysicalMemory(clientHWInfoResourceLogData.getPhysicalMemory());

			applicationErrorLogDailies.add(clientHWInfoResourceLogDaily);
		}

		return applicationErrorLogDailies;
	}


	public List<ClientHWInfoResourceLog> reconstructDocument(ClientHWInfoResourceLogOriginal clientHWInfoResourceLogOriginal) {

		List<ClientHWInfoResourceLog> clientHWInfoResourceLogs = new ArrayList<ClientHWInfoResourceLog>();

		for( ClientHWInfoResourceLogData clientHWInfoResourceLogData: clientHWInfoResourceLogOriginal.getClientHWInfoResourceData() ) {

			ClientHWInfoResourceLog clientHWInfoResourceLog = new ClientHWInfoResourceLog();
			clientHWInfoResourceLog.setService(clientHWInfoResourceLogOriginal.getService());
			clientHWInfoResourceLog.setAppId(clientHWInfoResourceLogOriginal.getAppId());
			clientHWInfoResourceLog.setOsType(clientHWInfoResourceLogOriginal.getOsType());
			clientHWInfoResourceLog.setSource(clientHWInfoResourceLogOriginal.getSource());
			clientHWInfoResourceLog.setDeviceId(clientHWInfoResourceLogOriginal.getDeviceId());
			clientHWInfoResourceLog.setIp(clientHWInfoResourceLogOriginal.getIp());
			clientHWInfoResourceLog.setHostName(clientHWInfoResourceLogOriginal.getHostName());
			clientHWInfoResourceLog.setTimeCreated(clientHWInfoResourceLogOriginal.getTimeCreated());

			clientHWInfoResourceLog.setUserId(clientHWInfoResourceLogOriginal.getUserId());
			clientHWInfoResourceLog.setTermNo(clientHWInfoResourceLogOriginal.getTermNo());
			clientHWInfoResourceLog.setSsoBrNo(clientHWInfoResourceLogOriginal.getSsoBrNo());
			clientHWInfoResourceLog.setBrNo(clientHWInfoResourceLogOriginal.getBrNo());
			clientHWInfoResourceLog.setDeptName(clientHWInfoResourceLogOriginal.getDeptName());
			clientHWInfoResourceLog.setHwnNo(clientHWInfoResourceLogOriginal.getHwnNo());
			clientHWInfoResourceLog.setUserName(clientHWInfoResourceLogOriginal.getUserName());
			clientHWInfoResourceLog.setSsoType(clientHWInfoResourceLogOriginal.getSsoType());
			clientHWInfoResourceLog.setPcName(clientHWInfoResourceLogOriginal.getPcName());
			clientHWInfoResourceLog.setPhoneNo(clientHWInfoResourceLogOriginal.getPhoneNo());
			clientHWInfoResourceLog.setJKGP(clientHWInfoResourceLogOriginal.getJKGP());
			clientHWInfoResourceLog.setJKWI(clientHWInfoResourceLogOriginal.getJKWI());
			clientHWInfoResourceLog.setMaxAddress(clientHWInfoResourceLogOriginal.getMaxAddress());
			clientHWInfoResourceLog.setFirstWork(clientHWInfoResourceLogOriginal.getFirstWork());

			clientHWInfoResourceLog.setOsBuildNumber(clientHWInfoResourceLogData.getOsBuildNumber());
			clientHWInfoResourceLog.setOsServicePackVersion(clientHWInfoResourceLogData.getOsServicePackVersion());
			clientHWInfoResourceLog.setOsVersion(clientHWInfoResourceLogData.getOsVersion());
			clientHWInfoResourceLog.setOsName(clientHWInfoResourceLogData.getOsName());
			clientHWInfoResourceLog.setOsArchitecture(clientHWInfoResourceLogData.getOsArchitecture());
			clientHWInfoResourceLog.setOsSerialNumber(clientHWInfoResourceLogData.getOsSerialNumber());
			clientHWInfoResourceLog.setOsCaption(clientHWInfoResourceLogData.getOsCaption());
			clientHWInfoResourceLog.setOsStatus(clientHWInfoResourceLogData.getOsStatus());
			clientHWInfoResourceLog.setPcName(clientHWInfoResourceLogData.getPcName());
			clientHWInfoResourceLog.setPcModel(clientHWInfoResourceLogData.getPcModel());
			clientHWInfoResourceLog.setPcManufacturer(clientHWInfoResourceLogData.getPcManufacturer());
			clientHWInfoResourceLog.setPcCaption(clientHWInfoResourceLogData.getPcCaption());
			clientHWInfoResourceLog.setPcSystemType(clientHWInfoResourceLogData.getPcSystemType());
			clientHWInfoResourceLog.setPcStatus(clientHWInfoResourceLogData.getPcStatus());
			clientHWInfoResourceLog.setCpuName(clientHWInfoResourceLogData.getCpuName());
			clientHWInfoResourceLog.setCpuArchitecture(clientHWInfoResourceLogData.getCpuArchitecture());
			clientHWInfoResourceLog.setCpuManufacturer(clientHWInfoResourceLogData.getCpuManufacturer());
			clientHWInfoResourceLog.setCpuNumberOfCores(clientHWInfoResourceLogData.getCpuNumberOfCores());
			clientHWInfoResourceLog.setCpuThreadCount(clientHWInfoResourceLogData.getCpuThreadCount());
			clientHWInfoResourceLog.setCpuMaxClockSpeed(clientHWInfoResourceLogData.getCpuMaxClockSpeed());
			clientHWInfoResourceLog.setCpuStatus(clientHWInfoResourceLogData.getCpuStatus());
			clientHWInfoResourceLog.setCpuCaption(clientHWInfoResourceLogData.getCpuCaption());
			clientHWInfoResourceLog.setMemName(clientHWInfoResourceLogData.getMemName());
			clientHWInfoResourceLog.setMemModel(clientHWInfoResourceLogData.getMemModel());
			clientHWInfoResourceLog.setMemManufacturer(clientHWInfoResourceLogData.getMemManufacturer());
			clientHWInfoResourceLog.setMemBankLabel(clientHWInfoResourceLogData.getMemBankLabel());
			clientHWInfoResourceLog.setMemDeviceLocator(clientHWInfoResourceLogData.getMemDeviceLocator());
			clientHWInfoResourceLog.setMemSerialNumber(clientHWInfoResourceLogData.getMemSerialNumber());
			clientHWInfoResourceLog.setMemSpeed(clientHWInfoResourceLogData.getMemSpeed());
			clientHWInfoResourceLog.setMemMemoryType(clientHWInfoResourceLogData.getMemMemoryType());
			clientHWInfoResourceLog.setMemTotalSize(clientHWInfoResourceLogData.getMemTotalSize());
			clientHWInfoResourceLog.setMemVirtualTotalSize(clientHWInfoResourceLogData.getMemVirtualTotalSize());
			clientHWInfoResourceLog.setMemStatus(clientHWInfoResourceLogData.getMemStatus());
			clientHWInfoResourceLog.setMemCaption(clientHWInfoResourceLogData.getMemCaption());
			clientHWInfoResourceLog.setMemName_2(clientHWInfoResourceLogData.getMemName_2());
			clientHWInfoResourceLog.setMemModel_2(clientHWInfoResourceLogData.getMemModel_2());
			clientHWInfoResourceLog.setMemManufacturer_2(clientHWInfoResourceLogData.getMemManufacturer_2());
			clientHWInfoResourceLog.setMemBankLabel_2(clientHWInfoResourceLogData.getMemBankLabel_2());
			clientHWInfoResourceLog.setMemDeviceLocator_2(clientHWInfoResourceLogData.getMemDeviceLocator_2());
			clientHWInfoResourceLog.setMemSerialNumber_2(clientHWInfoResourceLogData.getMemSerialNumber_2());
			clientHWInfoResourceLog.setMemSpeed_2(clientHWInfoResourceLogData.getMemSpeed_2());
			clientHWInfoResourceLog.setMemMemoryType_2(clientHWInfoResourceLogData.getMemMemoryType_2());
			clientHWInfoResourceLog.setMemStatus_2(clientHWInfoResourceLogData.getMemStatus_2());
			clientHWInfoResourceLog.setMemCaption_2(clientHWInfoResourceLogData.getMemCaption_2());
			clientHWInfoResourceLog.setDiskName(clientHWInfoResourceLogData.getDiskName());
			clientHWInfoResourceLog.setDiskModel(clientHWInfoResourceLogData.getDiskModel());
			clientHWInfoResourceLog.setDiskManufacturer(clientHWInfoResourceLogData.getDiskManufacturer());
			clientHWInfoResourceLog.setDiskSerialNumber(clientHWInfoResourceLogData.getDiskSerialNumber());
			clientHWInfoResourceLog.setDiskCaption(clientHWInfoResourceLogData.getDiskCaption());
			clientHWInfoResourceLog.setDiskInterfaceType(clientHWInfoResourceLogData.getDiskInterfaceType());
			clientHWInfoResourceLog.setDiskMediaType(clientHWInfoResourceLogData.getDiskMediaType());
			clientHWInfoResourceLog.setDiskKBSize(clientHWInfoResourceLogData.getDiskKBSize());
			clientHWInfoResourceLog.setDiskStatus(clientHWInfoResourceLogData.getDiskStatus());

			clientHWInfoResourceLog.setDiskName_2(clientHWInfoResourceLogData.getDiskName_2());
			clientHWInfoResourceLog.setDiskModel_2(clientHWInfoResourceLogData.getDiskModel_2());
			clientHWInfoResourceLog.setDiskManufacturer_2(clientHWInfoResourceLogData.getDiskManufacturer_2());
			clientHWInfoResourceLog.setDiskSerialNumber_2(clientHWInfoResourceLogData.getDiskSerialNumber_2());
			clientHWInfoResourceLog.setDiskCaption_2(clientHWInfoResourceLogData.getDiskCaption_2());
			clientHWInfoResourceLog.setDiskInterfaceType_2(clientHWInfoResourceLogData.getDiskInterfaceType_2());
			clientHWInfoResourceLog.setDiskMediaType_2(clientHWInfoResourceLogData.getDiskMediaType_2());
			clientHWInfoResourceLog.setDiskKBSize_2(clientHWInfoResourceLogData.getDiskKBSize_2());
			clientHWInfoResourceLog.setDiskStatus_2(clientHWInfoResourceLogData.getDiskStatus_2());
			clientHWInfoResourceLog.setDiskDrive(clientHWInfoResourceLogData.getDiskDrive());
			clientHWInfoResourceLog.setPhysicalMemory(clientHWInfoResourceLogData.getPhysicalMemory());

			clientHWInfoResourceLog.setTimeCurrent(clientHWInfoResourceLogData.getTimeCreated());
			clientHWInfoResourceLog.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientHWInfoResourceLogData.getTimeCreated()));

			clientHWInfoResourceLogs.add(clientHWInfoResourceLog);
		}

		return clientHWInfoResourceLogs;
	}

}
