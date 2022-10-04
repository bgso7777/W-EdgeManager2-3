package com.inswave.appplatform.transaver.saver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.data.NodeData;
import com.inswave.appplatform.data.Parse;
import com.inswave.appplatform.data.SimpleData;
import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.log.monitor.LoggerMonitor;
import com.inswave.appplatform.log.translate.ClientUserTermMonitor;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.transaver.elasticsearch.ElasticsearchHighLevelClient;
import com.inswave.appplatform.transaver.elasticsearch.dao.ClientUserTermMonitorLogDailyRepository;
import com.inswave.appplatform.transaver.elasticsearch.dao.ClientUserTermMonitorLogRepository;
import com.inswave.appplatform.transaver.elasticsearch.dao.ElasticsearchDaoPackage;
import com.inswave.appplatform.transaver.elasticsearch.domain.*;
import com.inswave.appplatform.util.DateUtil;
import com.inswave.appplatform.util.FileUtil;
import org.elasticsearch.action.bulk.BulkResponse;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.UncategorizedElasticsearchException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ClientUserTermMonitorLogger extends LoggerMonitor implements ElasticsearchService {

	@Autowired
	private static ElasticsearchHighLevelClient elasticsearchHighLevelClient;

	private static final Logger logger = LoggerFactory.getLogger(Class.class);

	public static String className = "ClientUserTermMonitorLogger";
	public static String currentIndexName = "ClientUserTermMonitorLog";
	public static String dailyIndexName = "ClientUserTermMonitorLogDaily";

	@Override
	public IData select(IData reqIData,IData resIData) {
		// TODO Auto-generated method stub
		
		IData iData = new NodeData();
		iData.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

		return iData;
	}
    
	@Override
	public IData insert(IData reqIData,IData resIData) {
		// TODO Auto-generated method stub

		IData iData = new NodeData();
		iData.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
		return iData;
	}
	
	@Override
	public IData update(IData reqIData,IData resIData) {
		// TODO Auto-generated method stub
		IData iData = new NodeData();
		iData.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
		return iData;
	}
	
	@Override
	public IData delete(IData reqIData,IData resIData) {
		// TODO Auto-generated method stub
		
		IData iData = new NodeData();
		iData.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
		return iData;
	}

	@Override
	public IData insert(String indexName, String message) throws JsonProcessingException, Exception {

		super.begin();

		IData iData = new NodeData();
		iData.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

		JavaMelodyMonitor.printInfo("Log Messssage Listener Counter", "");
		JavaMelodyMonitor.printInfo(className, "begin");

		if(elasticsearchHighLevelClient==null)
			elasticsearchHighLevelClient = new ElasticsearchHighLevelClient();

		// 로그 파일 저장 저장
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		super.setRecodevalue(message);
		try {
			if (Config.getInstance().getLog().isSaveRequestLogData()) {
				String fileName = Config.getInstance().getLog().getSaveRequestLogDataDir() + className + "_" + DateUtil.getCurrentDate(Config.getInstance().getLog().getLogFileDatePattern());
				FileUtil.saveStringBufferToFileAppend(fileName, new StringBuffer(message + "\n"));

				// min mix size구분을 위한 임시 코드
				////////////////////////////////////////////////////////////////////////////////////////////////////////
				String fileNameMin = fileName + ".min";
				File minFile = new File(fileNameMin);
				if (minFile.exists()) {
					if (minFile.length() > message.length())
						FileUtil.saveStringBufferToFile2(fileNameMin, new StringBuffer(message));
				} else {
					FileUtil.saveStringBufferToFile2(fileNameMin, new StringBuffer(message));
				}

				String fileNameMax = fileName + ".max";
				File maxFile = new File(fileNameMax);
				if (maxFile.exists()) {
					if (maxFile.length() < message.length())
						FileUtil.saveStringBufferToFile2(fileNameMax, new StringBuffer(message));
				} else {
					FileUtil.saveStringBufferToFile2(fileNameMax, new StringBuffer(message));
				}
				////////////////////////////////////////////////////////////////////////////////////////////////////////
			}
		} catch (Exception e) {
			e.printStackTrace();
			super.setExceptionCount(super.getExceptionCount() + 1);
		}

		// 객체 변환
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		message = message.replaceAll("\uFEFF", "");
		Parse parse = new Parse();
//		Object objectNode = parse.getObjectNode(message);
		JSONArray jSONArray = parse.getJSONArray(new StringBuffer(message));
		boolean isCurrentIndexDelete = false;
		for (Object object : jSONArray) {
			super.beginTimeObjectConvert();
			ClientUserTermMonitor clientUserTermMonitor = new ClientUserTermMonitor();
			ClientUserTermMonitorLogOriginal clientUserTermMonitorLogOriginal = clientUserTermMonitor.getOriginalObject((LinkedHashMap)object);
			super.setRowSize(super.getRowSize() + clientUserTermMonitorLogOriginal.getUserTermMonitorLogData().size());
			List<ClientUserTermMonitorLogDaily> clientUserTermMonitorLogDailies = clientUserTermMonitor.reconstructDailyDocument(clientUserTermMonitorLogOriginal);
			List<ClientUserTermMonitorLog> clientUserTermMonitorLogs = clientUserTermMonitor.reconstructDocument(clientUserTermMonitorLogOriginal);
			super.endTimeObjectConvert();

			// deviceId별 current 로그 저장
			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			super.beginTimeElasticsearchCurrentInsert();
			ClientUserTermMonitorLogRepository clientUserTermMonitorLogRepository = (ClientUserTermMonitorLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(currentIndexName);
			DynamicIndexBean.setIndexName(currentIndexName.toLowerCase());
			clientUserTermMonitorLogRepository.setIndexName(currentIndexName.toLowerCase());
			if (!elasticsearchHighLevelClient.existIndices(currentIndexName.toLowerCase())) {
				do {
					try {
						clientUserTermMonitorLogs.get(0).setIndexName(currentIndexName.toLowerCase());
						clientUserTermMonitorLogRepository.setIndexName(currentIndexName.toLowerCase());
						clientUserTermMonitorLogRepository.createIndex(clientUserTermMonitorLogs.get(0));
					} catch (UncategorizedElasticsearchException e) { // already exist (다른 서버에서 생성한 경우)
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
						super.setExceptionCount(super.getExceptionCount() + 1);
					}
					if (elasticsearchHighLevelClient.existIndices(currentIndexName.toLowerCase()))
						break;
					else
						JavaMelodyMonitor.printInfo(className, "not create index : " + currentIndexName.toLowerCase());
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
				} while (true);
			}
			try {
//				elasticsearchHighLevelClient.deleteDocument(currentIndexName.toLowerCase(), "deviceId", clientUserTermMonitorLogOriginal.getDeviceId());
				if(!isCurrentIndexDelete) {
					elasticsearchHighLevelClient.deleteCurrentIndex(currentIndexName.toLowerCase(),clientUserTermMonitorLogOriginal);
					isCurrentIndexDelete = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				super.setExceptionCount(super.getExceptionCount() + 1);
			}
			super.setCurrentRowSize(super.getCurrentRowSize() + clientUserTermMonitorLogs.size());
			List<Document2> document2s = new ArrayList<>();
			for (ClientUserTermMonitorLog clientUserTermMonitorLog : clientUserTermMonitorLogs) {
				clientUserTermMonitorLog.setIndexName(currentIndexName.toLowerCase());
				document2s.add(clientUserTermMonitorLog);
			}
			try {
				BulkResponse bulkResponse = elasticsearchHighLevelClient.writeDocument2s(document2s);
				if (bulkResponse.hasFailures())
					logger.error(bulkResponse.buildFailureMessage());
			} catch (Exception e) {
				e.printStackTrace();
				super.setExceptionCount(super.getExceptionCount() + 1);
			}
			JavaMelodyMonitor.printInfo(className, "current process end!");
			super.endTimeElasticsearchCurrentInsert();

			// 일자별 로그 저장
			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (Config.getInstance().getLog().getIndexDailylogClientUserTermMonitorLogSavedDay()>0) {
				super.beginTimeElasticsearchDailyInsert();
				ClientUserTermMonitorLogDailyRepository windowsEventSystemErrorAllLogDailyRepository = (ClientUserTermMonitorLogDailyRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(dailyIndexName);
				String ip = "", deviceId = "", currentDate = "", timeCreated = "", regDate = "";
				document2s = new ArrayList<>();
				HashSet<String> indexNames = new HashSet<>();
				for (ClientUserTermMonitorLogDaily windowsEventSystemErrorAllLogDaily : clientUserTermMonitorLogDailies) {
					try {
						indexName = DateTimeConvertor.getIndexName(dailyIndexName.toLowerCase(), windowsEventSystemErrorAllLogDaily.getTimeCurrent());
						indexNames.add(indexName);
						windowsEventSystemErrorAllLogDaily.setIndexName(indexName);
						windowsEventSystemErrorAllLogDaily.setTimeRegistered(DateTimeConvertor.getTimeRegistered());
						ip = windowsEventSystemErrorAllLogDaily.getIp();
						deviceId = windowsEventSystemErrorAllLogDaily.getDeviceId();
						timeCreated = DateTimeConvertor.getPrintDate(windowsEventSystemErrorAllLogDaily.getTimeCreated());
						currentDate = DateTimeConvertor.getPrintDate(windowsEventSystemErrorAllLogDaily.getTimeCurrent());
						regDate = DateTimeConvertor.getPrintDate(windowsEventSystemErrorAllLogDaily.getTimeRegistered());
						document2s.add(windowsEventSystemErrorAllLogDaily);
					} catch (Exception e) {
						e.printStackTrace();
						super.setExceptionCount(super.getExceptionCount() + 1);
					}
				}
				super.setDailyRowSize(super.getDailyRowSize() + clientUserTermMonitorLogDailies.size());
				try {
					BulkResponse bulkResponse = elasticsearchHighLevelClient.writeDocument2s(document2s);
					if (bulkResponse.hasFailures())
						logger.error(bulkResponse.buildFailureMessage());
				} catch (Exception e) {
					e.printStackTrace();
					super.setExceptionCount(super.getExceptionCount() + 1);
				}
				super.endTimeElasticsearchDailyInsert();
				if (Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_TEST) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) ||
						Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) ||
						Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP)
				) {
					JavaMelodyMonitor.printInfo(className, "ip-->>" + ip + " deviceId-->>" + deviceId);
					JavaMelodyMonitor.printInfo(className, "ip-->>" + ip + " deviceId-->>" + deviceId + " regDate-->>" + regDate);
					JavaMelodyMonitor.printInfo(className, "ip-->>" + ip + " deviceId-->>" + deviceId + " regDate-->>" + regDate + " timeCreated-->>" + timeCreated);
					JavaMelodyMonitor.printInfo(className, "ip-->>" + ip + " deviceId-->>" + deviceId + " regDate-->>" + regDate + " currentDate-->>" + currentDate);
					JavaMelodyMonitor.printInfo(className, "ip-->>" + ip + " deviceId-->>" + deviceId + " regDate-->>" + regDate + " timeCreated-->>" + timeCreated + " currentDate-->>" + currentDate);
				}
				JavaMelodyMonitor.printInfo(className, "daily process end!");
			}
		}
		super.end();

		JavaMelodyMonitor.printInfo(className,"end  rowSize-->"+super.getRowSize()+" currentRowSize-->"+super.getCurrentRowSize()+" dailyRowSize-->"+super.getDailyRowSize()+" exceptionCount-->"+super.getExceptionCount());
		JavaMelodyMonitor.printInfo(className,"end  runningTime->"+super.getRunningTime()+" objectConvertTime-->"+super.getObjectConvertTime()+" elasticsearchCurrentInsert-->"+super.getElasticsearchCurrentInsert()+" elasticsearchDailyInsert-->"+super.getElasticsearchDailyInsert());

		return iData;
	}

	@Override
	public IData insert(String indexName, List<Document2> document2s) throws Exception {
		return null;
	}

	public static void main(String[] argv) {
		StringBuffer messageFile = new StringBuffer();
		try	{
			messageFile = FileUtil.getFileToStringBuffer("C:\\Temp\\WindowsEventSystemErrorAllLogger.message"); //
			String[] messages = messageFile.toString().split("\n");
			for (int i = 0; i < messages.length; i++) {
				StringBuffer message = new StringBuffer(messages[i]);
				System.out.println(message.toString());

				Parse parse = new Parse();
				JSONArray jSONArray = parse.getJSONArray(message);
				for (Object object : jSONArray) {
					//LinkedHashMap linkedHashMap = (LinkedHashMap) jSONArray.get(0);
					LinkedHashMap linkedHashMap = (LinkedHashMap) object;
					//ObjectMapper objectMapper = new ObjectMapper();
					ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
//					objectMapper.setTimeZone(TimeZone.getTimeZone(Constants.TAG_OBJECT_MAPPER_TIME_ZONE));
//					DateFormat dateFormat = new SimpleDateFormat(Constants.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z);
//					objectMapper.setDateFormat(dateFormat);

					ClientUserTermMonitor clientUserTermMonitor = new ClientUserTermMonitor();
					ClientUserTermMonitorLogOriginal clientUserTermMonitorLogOriginal = clientUserTermMonitor.getOriginalObject((LinkedHashMap)object);

					List<ClientUserTermMonitorLogDaily> windowsEventSystemErrorAllLogDailies =clientUserTermMonitor.reconstructDailyDocument(clientUserTermMonitorLogOriginal);
					System.out.println();

//					for(WindowsEventSystemErrorAllLogData windowsEventSystemErrorAllLogData :windowsEventSystemErrorAllLog.getCollectWindowsEventLogData())
//						windowsEventSystemErrorAllLogData.setMessage("");
//
//					for(int j = 0; j< ConstantsTranSaver.TAG_MAX_TERMINAL_COUNT; j++) {
//						WindowsEventSystemErrorAllLog copyWindowsEventSystemErrorAllLog = objectMapper.convertValue(linkedHashMap, WindowsEventSystemErrorAllLog.class);
//						copyWindowsEventSystemErrorAllLog.setDeviceId((copyWindowsEventSystemErrorAllLog.getDeviceId()+j));
//						DocumentStorage.getInstance().getWindowsEventSystemErrorAllLogs().put(copyWindowsEventSystemErrorAllLog.getDeviceId(),copyWindowsEventSystemErrorAllLog);
//					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
