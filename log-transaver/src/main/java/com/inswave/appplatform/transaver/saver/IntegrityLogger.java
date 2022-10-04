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
import com.inswave.appplatform.log.translate.Integrity;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.transaver.elasticsearch.ElasticsearchHighLevelClient;
import com.inswave.appplatform.transaver.elasticsearch.dao.ElasticsearchDaoPackage;
import com.inswave.appplatform.transaver.elasticsearch.dao.IntegrityLogDailyRepository;
import com.inswave.appplatform.transaver.elasticsearch.dao.IntegrityLogRepository;
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
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class IntegrityLogger extends LoggerMonitor implements ElasticsearchService {

	@Autowired
	private static ElasticsearchHighLevelClient elasticsearchHighLevelClient;

	private static final Logger logger = LoggerFactory.getLogger(Class.class);

	public static String className = "IntegrityLogger";
	public static String currentIndexName = "IntegrityLog";
	public static String dailyIndexName = "IntegrityLogDaily";

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
		message = message.replace("\uFEFF", "");
		Parse parse = new Parse();
//		Object objectNode = parse.getObjectNode(message);
		JSONArray jSONArray = parse.getJSONArray(new StringBuffer(message));
		boolean isCurrentIndexDelete = false;
		for (Object object: jSONArray) {
			super.beginTimeObjectConvert();
			Integrity integrity = new Integrity();
			IntegrityLog integrityLog = integrity.getIntegrityLogObject((LinkedHashMap)object);
			integrityLog.setResult(integrityLog.getIntegrity().getResult());
			IntegrityLogDaily integrityLogDaily = integrity.getIntegrityLogDailyObject((LinkedHashMap)object);
			super.setRowSize(super.getRowSize() + integrityLog.getIntegrity().getIntegrityData().length);
			super.endTimeObjectConvert();

			// deviceId별 current 로그 저장
			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			super.beginTimeElasticsearchCurrentInsert();
			IntegrityLogRepository integrityLogRepository = (IntegrityLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(currentIndexName);
			DynamicIndexBean.setIndexName(currentIndexName.toLowerCase());
			integrityLog.setIndexName(currentIndexName.toLowerCase());
			if (!elasticsearchHighLevelClient.existIndices(currentIndexName.toLowerCase())) {
				do {
					try {
						integrityLogRepository.setIndexName(currentIndexName.toLowerCase());
						integrityLogRepository.createIndex(integrityLog);
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
//				elasticsearchHighLevelClient.deleteDocument(currentIndexName.toLowerCase(), "deviceId", integrityLog.getDeviceId());
				if(!isCurrentIndexDelete) {
					IntegrityLogOriginal integrityLogOriginal = integrity.getOriginalObject((LinkedHashMap)object);
					elasticsearchHighLevelClient.deleteCurrentIndex(currentIndexName.toLowerCase(),integrityLogOriginal);
					isCurrentIndexDelete = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.setCurrentRowSize(super.getCurrentRowSize() + integrityLog.getIntegrity().getIntegrityData().length);
			List<Document2> document2s = new ArrayList<>();
			try {
				try {
					integrityLog.setTimeCurrent(integrityLog.getIntegrity().getStartTime());
				} catch (Exception e) {
				}
				integrityLog.setIndexName(currentIndexName.toLowerCase());
				integrityLog.setTimeRegistered(DateTimeConvertor.getTimeRegistered());
				document2s.add(integrityLog);
				BulkResponse bulkResponse = elasticsearchHighLevelClient.writeDocument2s(document2s);
				if (bulkResponse.hasFailures())
					logger.error(bulkResponse.buildFailureMessage());
			} catch (Exception e) {
				e.printStackTrace();
				super.setExceptionCount(super.getExceptionCount() + 1);
			}
			JavaMelodyMonitor.printInfo(className, "current process end!");
			super.endTimeElasticsearchCurrentInsert();

			// 일자별 로그 저장 cunnent와 같이 간다.
			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (Config.getInstance().getLog().getIndexDailylogIntegrityLogSavedDay()>0) {
				super.beginTimeElasticsearchDailyInsert();
				IntegrityLogDailyRepository integrityLogDailyRepository = (IntegrityLogDailyRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(dailyIndexName);
				String ip = "", deviceId = "", currentDate = "", timeCreated = "", regDate = "";
				document2s = new ArrayList<>();
				//for(IntegrityLogDaily integrityLogDaily : integrityLogDailies) {
				integrityLogDaily.setResult(integrityLogDaily.getResult());
				try {
					integrityLogDaily.setTimeCurrent(integrityLogDaily.getIntegrity().getStartTime());
					integrityLogDaily.setStatisticsValue(DateTimeConvertor.getStatisticsValue(integrityLogDaily.getTimeCurrent()));
					indexName = DateTimeConvertor.getIndexName(dailyIndexName.toLowerCase(), integrityLogDaily.getTimeCurrent());
					integrityLogDaily.setIndexName(indexName);
					integrityLogDaily.setTimeRegistered(DateTimeConvertor.getTimeRegistered());
					ip = integrityLogDaily.getIp();
					deviceId = integrityLogDaily.getDeviceId();
					currentDate = DateTimeConvertor.getPrintDate(integrityLogDaily.getTimeCurrent());
					timeCreated = DateTimeConvertor.getPrintDate(integrityLogDaily.getTimeCreated());
					regDate = DateTimeConvertor.getPrintDate(integrityLogDaily.getTimeRegistered());
				} catch (Exception e) {
					e.printStackTrace();
					super.setExceptionCount(super.getExceptionCount() + 1);
				}
				super.setDailyRowSize(super.getDailyRowSize() + integrityLogDaily.getIntegrity().getIntegrityData().length);
				try {
					document2s.add(integrityLogDaily);
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
					JavaMelodyMonitor.printInfo("IntegrityLogger", "ip-->>" + ip + " deviceId-->>" + deviceId);
					JavaMelodyMonitor.printInfo("IntegrityLogger", "ip-->>" + ip + " deviceId-->>" + deviceId + " regDate-->>" + regDate);
					JavaMelodyMonitor.printInfo("IntegrityLogger", "ip-->>" + ip + " deviceId-->>" + deviceId + " regDate-->>" + regDate + " timeCreated-->>" + timeCreated);
					JavaMelodyMonitor.printInfo("IntegrityLogger", "ip-->>" + ip + " deviceId-->>" + deviceId + " regDate-->>" + regDate + " currentDate-->>" + currentDate);
					JavaMelodyMonitor.printInfo("IntegrityLogger", "ip-->>" + ip + " deviceId-->>" + deviceId + " regDate-->>" + regDate + " timeCreated-->>" + timeCreated + " currentDate-->>" + currentDate);
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

		IntegrityLogger integrityLogger = new IntegrityLogger();

		StringBuffer messageFile = new StringBuffer();
		try {
			messageFile = FileUtil.getFileToStringBuffer("C:\\Temp\\IntegrityLogger.message"); // 정합성 결과
			String[] messages = messageFile.toString().split("\n");
			for (int i = 0; i < messages.length; i++) {
				StringBuffer message = new StringBuffer( messages[i] );
				System.out.println(message.toString());

				Parse parse = new Parse();
				JSONArray jSONArray = parse.getJSONArray(message);
				LinkedHashMap linkedHashMap = (LinkedHashMap)jSONArray.get(0);
				//ObjectMapper objectMapper = new ObjectMapper();
				ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
				IntegrityLog integrityLog = objectMapper.convertValue(linkedHashMap, IntegrityLog.class);
				integrityLog.setResult(integrityLog.getIntegrity().getResult());
				integrityLog.setIndexName(currentIndexName.toLowerCase());

//				ElasticsearchHighLevelClient elasticsearchHighLevelClient = new ElasticsearchHighLevelClient("52.79.253.187",9203);
//				elasticsearchHighLevelClient.openHighLevelClient();
//				List<Document2> document2s = new ArrayList<>();
//				document2s.add(integrityLog);
//
////				elasticsearchHighLevelClient.indexRemoveQuery(integrityLog.getIndexName());
////				elasticsearchHighLevelClient.indexRemoveQuery(integrityLog.getIndexName());
//
//				BulkResponse bulkResponse = elasticsearchHighLevelClient.writeDocument2s(document2s);
//				if(bulkResponse.hasFailures())
//					System.out.println(bulkResponse.buildFailureMessage());
//				else
//					System.out.println("sucess!");
//
//				elasticsearchHighLevelClient.closeRestHighLevelClient();

//				String fieldValue = ObjectUtil.getFieldValueMethod(integrityLog, "result");
//
//				System.out.println();

//				Parse parse = new Parse();
//				Object objectNode = parse.getObjectNode(messages[i]);
//				ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
//				IntegrityLog integrityLog = objectMapper.readValue(objectNode.toString(), IntegrityLog.class);
//				integrityLog.setResult(integrityLog.getIntegrity().getResult());

//				IntegrityLogDaily integrityLogDaily = objectMapper.convertValue(linkedHashMap, IntegrityLogDaily.class);
//				IntegrityLogOriginal integrityLogOriginal = objectMapper.convertValue(linkedHashMap, IntegrityLogOriginal.class);
//				integrityLogOriginal.setResult(integrityLogOriginal.getIntegrity().getResult());


//				String fieldValue = ObjectUtil.getFieldValueMethod(integrityLogOriginal, "integrity().getResult");
//
//				List<IntegrityLog1> integrityLog1s = integrityLogger.reconstructDocument(integrityLogOriginal);
//				for (IntegrityLog1 integrityLog1 : integrityLog1s) {
//				}
//
//				List<IntegrityLogDaily1> integrityLogDaily1s = integrityLogger.reconstructDailyDocument(integrityLogOriginal);
//				for(IntegrityLogDaily1 integrityLogDaily1 : integrityLogDaily1s) {
//				}
//
//				if(integrityLogOriginal.getIntegrity()!=null)
//					System.out.println();

				System.out.println();
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
