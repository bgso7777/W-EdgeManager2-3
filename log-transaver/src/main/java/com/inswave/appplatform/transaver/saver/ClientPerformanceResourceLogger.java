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
import com.inswave.appplatform.log.translate.ClientPerformanceResource;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.transaver.ConstantsTranSaver;
import com.inswave.appplatform.transaver.elasticsearch.ElasticsearchHighLevelClient;
import com.inswave.appplatform.transaver.elasticsearch.dao.ClientPerformanceResourceLogDailyRepository;
import com.inswave.appplatform.transaver.elasticsearch.dao.ClientPerformanceResourceLogRepository;
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
import java.util.*;

@Service
public class ClientPerformanceResourceLogger extends LoggerMonitor implements ElasticsearchService {

	@Autowired
	private static ElasticsearchHighLevelClient elasticsearchHighLevelClient;

	private static final Logger logger = LoggerFactory.getLogger(Class.class);

	public static String className = "ClientPerformanceResourceLogger";
	public static String currentIndexName = "ClientPerformanceResourceLog";
	public static String dailyIndexName = "ClientPerformanceResourceLogDaily";

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
	public IData insert(String indexName, String message) throws JsonProcessingException , Exception {

		super.begin();

		IData iData = new NodeData();
		iData.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

		JavaMelodyMonitor.printInfo( "Log Messssage Listener Counter","");
		JavaMelodyMonitor.printInfo(className,"begin");

		if(elasticsearchHighLevelClient==null)
			elasticsearchHighLevelClient = new ElasticsearchHighLevelClient();

		// 로그 파일 저장 저장
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		super.setRecodevalue(message);
		try {
			if(Config.getInstance().getLog().isSaveRequestLogData()) {
				String fileName = Config.getInstance().getLog().getSaveRequestLogDataDir()+className+"_"+DateUtil.getCurrentDate(Config.getInstance().getLog().getLogFileDatePattern());
				FileUtil.saveStringBufferToFileAppend(fileName,new StringBuffer(message+"\n"));

				// min mix size구분을 위한 임시 코드
				////////////////////////////////////////////////////////////////////////////////////////////////////////
				String fileNameMin = fileName+".min";
				File minFile = new File(fileNameMin);
				if(minFile.exists()) {
					if(minFile.length()>message.length())
						FileUtil.saveStringBufferToFile2(fileNameMin, new StringBuffer(message));
				} else {
					FileUtil.saveStringBufferToFile2(fileNameMin, new StringBuffer(message));
				}

				String fileNameMax = fileName+".max";
				File maxFile = new File(fileNameMax);
				if(maxFile.exists()) {
					if(maxFile.length()<message.length())
						FileUtil.saveStringBufferToFile2(fileNameMax, new StringBuffer(message));
				} else {
					FileUtil.saveStringBufferToFile2(fileNameMax, new StringBuffer(message));
				}
				////////////////////////////////////////////////////////////////////////////////////////////////////////
			}
		}catch(Exception e) {
			e.printStackTrace();
			super.setExceptionCount(super.getExceptionCount()+1);
		}

		// 객체 변환
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		message = message.replaceAll("\uFEFF", "");
		Date startTimeElasticsearchInsert = new Date();
		Parse parse = new Parse();
		JSONArray jSONArray = parse.getJSONArray(new StringBuffer(message));
		boolean isCurrentIndexDelete = false;
		for (Object object: jSONArray) {
			super.beginTimeObjectConvert();
			ClientPerformanceResource clientPerformanceResource = new ClientPerformanceResource();
			ClientPerformanceResourceLogOriginal clientPerformanceResourceLogOriginal = clientPerformanceResource.getOriginalObject((LinkedHashMap)object);
			super.setRowSize(super.getRowSize()+clientPerformanceResourceLogOriginal.getClientPerfResourceData().size());
			List<ClientPerformanceResourceLog> clientPerformanceResourceLogs = clientPerformanceResource.reconstructDocument(clientPerformanceResourceLogOriginal);
			List<ClientPerformanceResourceLogDaily> clientPerformanceResourceLogDailies = clientPerformanceResource.reconstructDailyDocument(clientPerformanceResourceLogOriginal);
			super.endTimeObjectConvert();

			// deviceId별 current 로그 저장
			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			super.beginTimeElasticsearchCurrentInsert();
			ClientPerformanceResourceLogRepository clientPerformanceResourceLogRepository = (ClientPerformanceResourceLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(currentIndexName);
			DynamicIndexBean.setIndexName(currentIndexName.toLowerCase());
			clientPerformanceResourceLogRepository.setIndexName(currentIndexName.toLowerCase());
			if(!elasticsearchHighLevelClient.existIndices(currentIndexName.toLowerCase())) {
				do{
					try{
						clientPerformanceResourceLogs.get(0).setIndexName(currentIndexName.toLowerCase());
						clientPerformanceResourceLogRepository.setIndexName(currentIndexName.toLowerCase());
						clientPerformanceResourceLogRepository.createIndex(clientPerformanceResourceLogs.get(0));
					} catch (UncategorizedElasticsearchException e) { // already exist (다른 서버에서 생성한 경우)
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
						super.setExceptionCount(super.getExceptionCount()+1);
					}
					if( elasticsearchHighLevelClient.existIndices(currentIndexName.toLowerCase()) )
						break;
					else
						JavaMelodyMonitor.printInfo(className,"not create index : "+currentIndexName.toLowerCase());
					try{Thread.sleep(1000);} catch(Exception e) {}
				}while(true);
			}
			try{
//				elasticsearchHighLevelClient.deleteDocument(currentIndexName.toLowerCase(), "deviceId", clientPerformanceResourceLogOriginal.getDeviceId());
				if(!isCurrentIndexDelete) {
					elasticsearchHighLevelClient.deleteCurrentIndex(currentIndexName.toLowerCase(),clientPerformanceResourceLogOriginal);
					isCurrentIndexDelete = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				super.setExceptionCount(super.getExceptionCount()+1);
			}
			super.setCurrentRowSize(super.getCurrentRowSize()+clientPerformanceResourceLogs.size());
			List<Document2> document2s = new ArrayList<>();
			for (ClientPerformanceResourceLog clientPerformanceResourceLog : clientPerformanceResourceLogs) {
				clientPerformanceResourceLog.setIndexName(currentIndexName.toLowerCase());
				document2s.add(clientPerformanceResourceLog);
			}
			try {
				BulkResponse bulkResponse = elasticsearchHighLevelClient.writeDocument2s(document2s);
				if (bulkResponse.hasFailures())
					logger.error(bulkResponse.buildFailureMessage());
			} catch (Exception e) {
				e.printStackTrace();
				super.setExceptionCount(super.getExceptionCount()+1);
			}
			JavaMelodyMonitor.printInfo(className, "current process end!");
			super.endTimeElasticsearchCurrentInsert();

			// 일자별 로그 저장
			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (Config.getInstance().getLog().getIndexDailylogClientPerformanceLogSavedDay()>0) {
				super.beginTimeElasticsearchDailyInsert();
				ClientPerformanceResourceLogDailyRepository clientPerformanceResourceLogDailyRepository = (ClientPerformanceResourceLogDailyRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(dailyIndexName);
				String ip = "", deviceId = "", currentDate = "", timeCreated = "", regDate = "";
				document2s = new ArrayList<>();
				HashSet<String> indexNames = new HashSet<>();
				for (ClientPerformanceResourceLogDaily clientPerformanceResourceLogDaily : clientPerformanceResourceLogDailies) {
					try {
						indexName = DateTimeConvertor.getIndexName(dailyIndexName.toLowerCase(), clientPerformanceResourceLogDaily.getTimeCurrent());
						indexNames.add(indexName);
						clientPerformanceResourceLogDaily.setIndexName(indexName);
						clientPerformanceResourceLogDaily.setTimeRegistered(DateTimeConvertor.getTimeRegistered());
						ip = clientPerformanceResourceLogDaily.getIp();
						deviceId = clientPerformanceResourceLogDaily.getDeviceId();
						currentDate = DateTimeConvertor.getPrintDate(clientPerformanceResourceLogDaily.getTimeCurrent());
						timeCreated = DateTimeConvertor.getPrintDate(clientPerformanceResourceLogDaily.getTimeCreated());
						regDate = DateTimeConvertor.getPrintDate(clientPerformanceResourceLogDaily.getTimeRegistered());
						document2s.add(clientPerformanceResourceLogDaily);
					} catch (Exception e) {
						e.printStackTrace();
						super.setExceptionCount(super.getExceptionCount() + 1);
					}
				}
				super.setDailyRowSize(super.getDailyRowSize() + clientPerformanceResourceLogDailies.size());
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
		try {
			StringBuffer messageFile = new StringBuffer();
			messageFile = FileUtil.getFileToStringBuffer("C:\\Temp\\ClientPerformanceResourceLog.message"); // 각 시스템의 리소스 사용량
			String[] messages = messageFile.toString().split("\n");
			for (int i = 0; i < messages.length; i++) {
				StringBuffer message = new StringBuffer( messages[i] );
				System.out.println(message.toString());

				Parse parse = new Parse();
				Object objectNode = parse.getObjectNode(message.toString());
				ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
				SimpleDateFormat format = new SimpleDateFormat(ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z);
				objectMapper.setDateFormat(format);
				objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
				JavaTimeModule javaTimeModule = new JavaTimeModule();
				objectMapper.registerModule(javaTimeModule);

				ClientPerformanceResource clientPerformanceResource = new ClientPerformanceResource();
				ClientPerformanceResourceLogOriginal clientPerformanceResourceLogOriginal = clientPerformanceResource.getOriginalObject((LinkedHashMap)objectNode);
				List<ClientPerformanceResourceLog> clientPerformanceResourceLogs = clientPerformanceResource.reconstructDocument(clientPerformanceResourceLogOriginal);
				List<ClientPerformanceResourceLogDaily> clientPerformanceResourceLogDailies = clientPerformanceResource.reconstructDailyDocument(clientPerformanceResourceLogOriginal);

				System.out.println();
			}


		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
