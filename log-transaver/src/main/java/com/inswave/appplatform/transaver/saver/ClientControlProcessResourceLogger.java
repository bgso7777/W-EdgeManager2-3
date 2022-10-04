package com.inswave.appplatform.transaver.saver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.data.NodeData;
import com.inswave.appplatform.data.Parse;
import com.inswave.appplatform.data.SimpleData;
import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.log.monitor.LoggerMonitor;
import com.inswave.appplatform.log.translate.ClientControlProcessResource;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.transaver.ConstantsTranSaver;
import com.inswave.appplatform.transaver.elasticsearch.ElasticsearchHighLevelClient;
import com.inswave.appplatform.transaver.elasticsearch.dao.ClientControlProcessResourceLogDailyRepository;
import com.inswave.appplatform.transaver.elasticsearch.dao.ClientControlProcessResourceLogRepository;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ClientControlProcessResourceLogger extends LoggerMonitor implements ElasticsearchService {

	@Autowired
	private static ElasticsearchHighLevelClient elasticsearchHighLevelClient;

	private static final Logger logger = LoggerFactory.getLogger(Class.class);

	public static String className = "ClientControlProcessResourceLogger";
	public static String currentIndexName = "ClientControlProcessResourceLog";
	public static String dailyIndexName = "ClientControlProcessResourceLogDaily";

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
			ClientControlProcessResource clientControlProcessResource = new ClientControlProcessResource();
			ClientControlProcessResourceLogOriginal clientControlProcessResourceLogOriginal = clientControlProcessResource.getOriginalObject((LinkedHashMap)object);
			super.setRowSize(super.getRowSize() + clientControlProcessResourceLogOriginal.getClientControlProcessResourceData().size());
			List<ClientControlProcessResourceLog> clientControlProcessResourceLogs = clientControlProcessResource.reconstructDocument(clientControlProcessResourceLogOriginal);
			List<ClientControlProcessResourceLogDaily> clientControlProcessResourceLogDailies = clientControlProcessResource.reconstructDailyDocument(clientControlProcessResourceLogOriginal);
			super.endTimeObjectConvert();

			// deviceId별 current 로그 저장
			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			super.beginTimeElasticsearchCurrentInsert();
			ClientControlProcessResourceLogRepository clientControlProcessResourceLogRepository = (ClientControlProcessResourceLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(currentIndexName);
			DynamicIndexBean.setIndexName(currentIndexName.toLowerCase());
			clientControlProcessResourceLogRepository.setIndexName(currentIndexName.toLowerCase());
			if (!elasticsearchHighLevelClient.existIndices(currentIndexName.toLowerCase())) {
				do {
					try {
						clientControlProcessResourceLogs.get(0).setIndexName(currentIndexName.toLowerCase());
						clientControlProcessResourceLogRepository.setIndexName(currentIndexName.toLowerCase());
						clientControlProcessResourceLogRepository.createIndex(clientControlProcessResourceLogs.get(0));
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
//				elasticsearchHighLevelClient.deleteDocument(currentIndexName.toLowerCase(), "deviceId", clientControlProcessResourceLogOriginal.getDeviceId());
				if(!isCurrentIndexDelete) {
					elasticsearchHighLevelClient.deleteCurrentIndex(currentIndexName.toLowerCase(),clientControlProcessResourceLogOriginal);
					isCurrentIndexDelete = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				super.setExceptionCount(super.getExceptionCount() + 1);
			}
			super.setCurrentRowSize(super.getCurrentRowSize() + clientControlProcessResourceLogs.size());
			List<Document2> document2s = new ArrayList<>();
			for (ClientControlProcessResourceLog clientControlProcessResourceLog : clientControlProcessResourceLogs) {
				clientControlProcessResourceLog.setIndexName(currentIndexName.toLowerCase());
				document2s.add(clientControlProcessResourceLog);
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
			if (Config.getInstance().getLog().getIndexDailylogClientControlProcessResourceLogSavedDay()>0) {
				super.beginTimeElasticsearchDailyInsert();
				ClientControlProcessResourceLogDailyRepository clientControlProcessResourceLogDailyRepository = (ClientControlProcessResourceLogDailyRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(dailyIndexName);
				String ip = "", deviceId = "", currentDate = "", timeCreated = "", regDate = "";
				document2s = new ArrayList<>();
				HashSet<String> indexNames = new HashSet<>();
				for (ClientControlProcessResourceLogDaily clientControlProcessResourceLogDaily : clientControlProcessResourceLogDailies) {
					try {
						indexName = DateTimeConvertor.getIndexName(dailyIndexName.toLowerCase(), clientControlProcessResourceLogDaily.getTimeCurrent());
						indexNames.add(indexName);
						clientControlProcessResourceLogDaily.setTimeRegistered(DateTimeConvertor.getTimeRegistered());
						clientControlProcessResourceLogDaily.setIndexName(indexName);
						ip = clientControlProcessResourceLogDaily.getIp();
						deviceId = clientControlProcessResourceLogDaily.getDeviceId();
						timeCreated = DateTimeConvertor.getPrintDate(clientControlProcessResourceLogDaily.getTimeCreated());
						currentDate = DateTimeConvertor.getPrintDate(clientControlProcessResourceLogDaily.getTimeCurrent());
						regDate = DateTimeConvertor.getPrintDate(clientControlProcessResourceLogDaily.getTimeRegistered());
						document2s.add(clientControlProcessResourceLogDaily);
					} catch (Exception e) {
						e.printStackTrace();
						super.setExceptionCount(super.getExceptionCount() + 1);
					}
				}
				super.setDailyRowSize(super.getDailyRowSize() + clientControlProcessResourceLogDailies.size());
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
			messageFile = FileUtil.getFileToStringBuffer("C:\\Temp\\ClientControlProcessResourceLogger.message"); //
			String[] messages = messageFile.toString().split("\n");

			for (int i = 0; i < messages.length; i++) {
				StringBuffer message = new StringBuffer(messages[i]);
				Parse parse = new Parse();
				JSONArray jSONArray = parse.getJSONArray(new StringBuffer(message));
				for (Object object : jSONArray) {
					LinkedHashMap linkedHashMap = (LinkedHashMap) object;
					ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					SimpleDateFormat format = new SimpleDateFormat(ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z);
					objectMapper.setDateFormat(format);
					objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
					JavaTimeModule javaTimeModule = new JavaTimeModule();
					objectMapper.registerModule(javaTimeModule);

					ClientControlProcessResource clientControlProcessResource = new ClientControlProcessResource();
					ClientControlProcessResourceLogOriginal clientControlProcessResourceLogOriginal = clientControlProcessResource.getOriginalObject((LinkedHashMap)object);
					List<ClientControlProcessResourceLog> clientControlProcessResourceLogs = clientControlProcessResource.reconstructDocument(clientControlProcessResourceLogOriginal);
					List<ClientControlProcessResourceLogDaily> clientControlProcessResourceLogDailies = clientControlProcessResource.reconstructDailyDocument(clientControlProcessResourceLogOriginal);

					System.out.println();
				}
			}

//			String ip = "52.79.253.187";
//			int port = 9200;
//			ElasticsearchHighLevelClient elasticsearchHighLevelClient = new ElasticsearchHighLevelClient(ip,port);
//			elasticsearchHighLevelClient.openHighLevelClient();
//			if( elasticsearchHighLevelClient.existIndices("serverresourcelog2") )
//				System.out.println("exist");
//			else
//				System.out.println("not exist");

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
