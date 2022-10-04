package com.inswave.appplatform.log.translate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientDefragAnalysisResourceLog;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientDefragAnalysisResourceLogDaily;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientDefragAnalysisResourceLogData;
import com.inswave.appplatform.transaver.elasticsearch.domain.ClientDefragAnalysisResourceLogOriginal;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.log.util.LogDataObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ClientDefragAnalysisResource {

	public ClientDefragAnalysisResource () {
	}

	public ClientDefragAnalysisResourceLogOriginal getOriginalObject(StringBuffer message) {
		ClientDefragAnalysisResourceLogOriginal clientDefragAnalysisResourceLogOriginal = null;
		return clientDefragAnalysisResourceLogOriginal;
	}

	public ClientDefragAnalysisResourceLogOriginal getOriginalObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, ClientDefragAnalysisResourceLogOriginal.class);
	}


	public List<ClientDefragAnalysisResourceLogDaily> reconstructDailyDocument(ClientDefragAnalysisResourceLogOriginal clientDefragAnalysisResourceLogOriginal) {

		List<ClientDefragAnalysisResourceLogDaily> clientDefragAnalysisResourceLogDailies = new ArrayList<ClientDefragAnalysisResourceLogDaily>();

		for( ClientDefragAnalysisResourceLogData clientDefragAnalysisResourceData: clientDefragAnalysisResourceLogOriginal.getClientDefragAnalysisResourceData() ) {

			ClientDefragAnalysisResourceLogDaily clientDefragAnalysisResourceLogDaily = new ClientDefragAnalysisResourceLogDaily();
			clientDefragAnalysisResourceLogDaily.setService(clientDefragAnalysisResourceLogOriginal.getService());
			clientDefragAnalysisResourceLogDaily.setAppId(clientDefragAnalysisResourceLogOriginal.getAppId());
			clientDefragAnalysisResourceLogDaily.setOsType(clientDefragAnalysisResourceLogOriginal.getOsType());
			clientDefragAnalysisResourceLogDaily.setSource(clientDefragAnalysisResourceLogOriginal.getSource());
			clientDefragAnalysisResourceLogDaily.setDeviceId(clientDefragAnalysisResourceLogOriginal.getDeviceId());
			clientDefragAnalysisResourceLogDaily.setIp(clientDefragAnalysisResourceLogOriginal.getIp());
			clientDefragAnalysisResourceLogDaily.setHostName(clientDefragAnalysisResourceLogOriginal.getHostName());
			clientDefragAnalysisResourceLogDaily.setTimeCreated(clientDefragAnalysisResourceLogOriginal.getTimeCreated());

			clientDefragAnalysisResourceLogDaily.setUserId(clientDefragAnalysisResourceLogOriginal.getUserId());
			clientDefragAnalysisResourceLogDaily.setTermNo(clientDefragAnalysisResourceLogOriginal.getTermNo());
			clientDefragAnalysisResourceLogDaily.setSsoBrNo(clientDefragAnalysisResourceLogOriginal.getSsoBrNo());
			clientDefragAnalysisResourceLogDaily.setBrNo(clientDefragAnalysisResourceLogOriginal.getBrNo());
			clientDefragAnalysisResourceLogDaily.setDeptName(clientDefragAnalysisResourceLogOriginal.getDeptName());
			clientDefragAnalysisResourceLogDaily.setHwnNo(clientDefragAnalysisResourceLogOriginal.getHwnNo());
			clientDefragAnalysisResourceLogDaily.setUserName(clientDefragAnalysisResourceLogOriginal.getUserName());
			clientDefragAnalysisResourceLogDaily.setSsoType(clientDefragAnalysisResourceLogOriginal.getSsoType());
			clientDefragAnalysisResourceLogDaily.setPcName(clientDefragAnalysisResourceLogOriginal.getPcName());
			clientDefragAnalysisResourceLogDaily.setPhoneNo(clientDefragAnalysisResourceLogOriginal.getPhoneNo());
			clientDefragAnalysisResourceLogDaily.setJKGP(clientDefragAnalysisResourceLogOriginal.getJKGP());
			clientDefragAnalysisResourceLogDaily.setJKWI(clientDefragAnalysisResourceLogOriginal.getJKWI());
			clientDefragAnalysisResourceLogDaily.setMaxAddress(clientDefragAnalysisResourceLogOriginal.getMaxAddress());
			clientDefragAnalysisResourceLogDaily.setFirstWork(clientDefragAnalysisResourceLogOriginal.getFirstWork());

			clientDefragAnalysisResourceLogDaily.setDriveLetter(clientDefragAnalysisResourceData.getDriveLetter());
			clientDefragAnalysisResourceLogDaily.setAverageFileSize(clientDefragAnalysisResourceData.getAverageFileSize());
			clientDefragAnalysisResourceLogDaily.setAverageFragmentsPerFile(clientDefragAnalysisResourceData.getAverageFragmentsPerFile());
			clientDefragAnalysisResourceLogDaily.setAverageFreeSpacePerExtent(clientDefragAnalysisResourceData.getAverageFreeSpacePerExtent());
			clientDefragAnalysisResourceLogDaily.setClusterSize(clientDefragAnalysisResourceData.getClusterSize());
			clientDefragAnalysisResourceLogDaily.setExcessFolderFragments(clientDefragAnalysisResourceData.getExcessFolderFragments());
			clientDefragAnalysisResourceLogDaily.setFilePercentFragmentation(clientDefragAnalysisResourceData.getFilePercentFragmentation());
			clientDefragAnalysisResourceLogDaily.setFragmentedFolders(clientDefragAnalysisResourceData.getFragmentedFolders());
			clientDefragAnalysisResourceLogDaily.setFreeSpace(clientDefragAnalysisResourceData.getFreeSpace());
			clientDefragAnalysisResourceLogDaily.setFreeSpacePercent(clientDefragAnalysisResourceData.getFreeSpacePercent());
			clientDefragAnalysisResourceLogDaily.setLargestFreeSpaceExtent(clientDefragAnalysisResourceData.getLargestFreeSpaceExtent());
			clientDefragAnalysisResourceLogDaily.setMFTPercentInUse(clientDefragAnalysisResourceData.getMFTPercentInUse());
			clientDefragAnalysisResourceLogDaily.setMFTRecordCount(clientDefragAnalysisResourceData.getMFTRecordCount());
			clientDefragAnalysisResourceLogDaily.setTotalExcessFragments(clientDefragAnalysisResourceData.getTotalExcessFragments());
			clientDefragAnalysisResourceLogDaily.setTotalFiles(clientDefragAnalysisResourceData.getTotalFiles());
			clientDefragAnalysisResourceLogDaily.setTotalFolders(clientDefragAnalysisResourceData.getTotalFolders());
			clientDefragAnalysisResourceLogDaily.setTotalFragmentedFiles(clientDefragAnalysisResourceData.getTotalFragmentedFiles());
			clientDefragAnalysisResourceLogDaily.setTotalFreeSpaceExtents(clientDefragAnalysisResourceData.getTotalFreeSpaceExtents());
			clientDefragAnalysisResourceLogDaily.setTotalMFTFragments(clientDefragAnalysisResourceData.getTotalMFTFragments());
			clientDefragAnalysisResourceLogDaily.setUsedSpace(clientDefragAnalysisResourceData.getUsedSpace());
			clientDefragAnalysisResourceLogDaily.setVolumeName(clientDefragAnalysisResourceData.getVolumeName());
			clientDefragAnalysisResourceLogDaily.setVolumeSize(clientDefragAnalysisResourceData.getVolumeSize());

			clientDefragAnalysisResourceLogDaily.setTimeCurrent(clientDefragAnalysisResourceData.getTimeCreated());
			clientDefragAnalysisResourceLogDaily.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientDefragAnalysisResourceData.getTimeCreated()));

			clientDefragAnalysisResourceLogDailies.add(clientDefragAnalysisResourceLogDaily);
		}

		return clientDefragAnalysisResourceLogDailies;
	}

	public List<ClientDefragAnalysisResourceLog> reconstructDocument(ClientDefragAnalysisResourceLogOriginal clientDefragAnalysisResourceLogOriginal) {

		List<ClientDefragAnalysisResourceLog> clientDefragAnalysisResourceLogs = new ArrayList<ClientDefragAnalysisResourceLog>();

		for( ClientDefragAnalysisResourceLogData clientDefragAnalysisResourceData: clientDefragAnalysisResourceLogOriginal.getClientDefragAnalysisResourceData() ) {

			ClientDefragAnalysisResourceLog clientDefragAnalysisResourceLog = new ClientDefragAnalysisResourceLog();
			clientDefragAnalysisResourceLog.setService(clientDefragAnalysisResourceLogOriginal.getService());
			clientDefragAnalysisResourceLog.setAppId(clientDefragAnalysisResourceLogOriginal.getAppId());
			clientDefragAnalysisResourceLog.setOsType(clientDefragAnalysisResourceLogOriginal.getOsType());
			clientDefragAnalysisResourceLog.setSource(clientDefragAnalysisResourceLogOriginal.getSource());
			clientDefragAnalysisResourceLog.setDeviceId(clientDefragAnalysisResourceLogOriginal.getDeviceId());
			clientDefragAnalysisResourceLog.setIp(clientDefragAnalysisResourceLogOriginal.getIp());
			clientDefragAnalysisResourceLog.setHostName(clientDefragAnalysisResourceLogOriginal.getHostName());
			clientDefragAnalysisResourceLog.setTimeCreated(clientDefragAnalysisResourceLogOriginal.getTimeCreated());

			clientDefragAnalysisResourceLog.setUserId(clientDefragAnalysisResourceLogOriginal.getUserId());
			clientDefragAnalysisResourceLog.setTermNo(clientDefragAnalysisResourceLogOriginal.getTermNo());
			clientDefragAnalysisResourceLog.setSsoBrNo(clientDefragAnalysisResourceLogOriginal.getSsoBrNo());
			clientDefragAnalysisResourceLog.setBrNo(clientDefragAnalysisResourceLogOriginal.getBrNo());
			clientDefragAnalysisResourceLog.setDeptName(clientDefragAnalysisResourceLogOriginal.getDeptName());
			clientDefragAnalysisResourceLog.setHwnNo(clientDefragAnalysisResourceLogOriginal.getHwnNo());
			clientDefragAnalysisResourceLog.setUserName(clientDefragAnalysisResourceLogOriginal.getUserName());
			clientDefragAnalysisResourceLog.setSsoType(clientDefragAnalysisResourceLogOriginal.getSsoType());
			clientDefragAnalysisResourceLog.setPcName(clientDefragAnalysisResourceLogOriginal.getPcName());
			clientDefragAnalysisResourceLog.setPhoneNo(clientDefragAnalysisResourceLogOriginal.getPhoneNo());
			clientDefragAnalysisResourceLog.setJKGP(clientDefragAnalysisResourceLogOriginal.getJKGP());
			clientDefragAnalysisResourceLog.setJKWI(clientDefragAnalysisResourceLogOriginal.getJKWI());
			clientDefragAnalysisResourceLog.setMaxAddress(clientDefragAnalysisResourceLogOriginal.getMaxAddress());
			clientDefragAnalysisResourceLog.setFirstWork(clientDefragAnalysisResourceLogOriginal.getFirstWork());

			clientDefragAnalysisResourceLog.setDriveLetter(clientDefragAnalysisResourceData.getDriveLetter());
			clientDefragAnalysisResourceLog.setAverageFileSize(clientDefragAnalysisResourceData.getAverageFileSize());
			clientDefragAnalysisResourceLog.setAverageFragmentsPerFile(clientDefragAnalysisResourceData.getAverageFragmentsPerFile());
			clientDefragAnalysisResourceLog.setAverageFreeSpacePerExtent(clientDefragAnalysisResourceData.getAverageFreeSpacePerExtent());
			clientDefragAnalysisResourceLog.setClusterSize(clientDefragAnalysisResourceData.getClusterSize());
			clientDefragAnalysisResourceLog.setExcessFolderFragments(clientDefragAnalysisResourceData.getExcessFolderFragments());
			clientDefragAnalysisResourceLog.setFilePercentFragmentation(clientDefragAnalysisResourceData.getFilePercentFragmentation());
			clientDefragAnalysisResourceLog.setFragmentedFolders(clientDefragAnalysisResourceData.getFragmentedFolders());
			clientDefragAnalysisResourceLog.setFreeSpace(clientDefragAnalysisResourceData.getFreeSpace());
			clientDefragAnalysisResourceLog.setFreeSpacePercent(clientDefragAnalysisResourceData.getFreeSpacePercent());
			clientDefragAnalysisResourceLog.setLargestFreeSpaceExtent(clientDefragAnalysisResourceData.getLargestFreeSpaceExtent());
			clientDefragAnalysisResourceLog.setMFTPercentInUse(clientDefragAnalysisResourceData.getMFTPercentInUse());
			clientDefragAnalysisResourceLog.setMFTRecordCount(clientDefragAnalysisResourceData.getMFTRecordCount());
			clientDefragAnalysisResourceLog.setTotalExcessFragments(clientDefragAnalysisResourceData.getTotalExcessFragments());
			clientDefragAnalysisResourceLog.setTotalFiles(clientDefragAnalysisResourceData.getTotalFiles());
			clientDefragAnalysisResourceLog.setTotalFolders(clientDefragAnalysisResourceData.getTotalFolders());
			clientDefragAnalysisResourceLog.setTotalFragmentedFiles(clientDefragAnalysisResourceData.getTotalFragmentedFiles());
			clientDefragAnalysisResourceLog.setTotalFreeSpaceExtents(clientDefragAnalysisResourceData.getTotalFreeSpaceExtents());
			clientDefragAnalysisResourceLog.setTotalMFTFragments(clientDefragAnalysisResourceData.getTotalMFTFragments());
			clientDefragAnalysisResourceLog.setUsedSpace(clientDefragAnalysisResourceData.getUsedSpace());
			clientDefragAnalysisResourceLog.setVolumeName(clientDefragAnalysisResourceData.getVolumeName());
			clientDefragAnalysisResourceLog.setVolumeSize(clientDefragAnalysisResourceData.getVolumeSize());

			clientDefragAnalysisResourceLog.setTimeCurrent(clientDefragAnalysisResourceData.getTimeCreated());
			clientDefragAnalysisResourceLog.setStatisticsValue(DateTimeConvertor.getStatisticsValue(clientDefragAnalysisResourceData.getTimeCreated()));

			clientDefragAnalysisResourceLogs.add(clientDefragAnalysisResourceLog);
		}

		return clientDefragAnalysisResourceLogs;
	}

}
