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
import com.inswave.appplatform.log.translate.ClientProcessCreation;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.transaver.elasticsearch.ElasticsearchHighLevelClient;
import com.inswave.appplatform.transaver.elasticsearch.dao.ClientProcessCreationLogDailyRepository;
import com.inswave.appplatform.transaver.elasticsearch.dao.ClientProcessCreationLogRepository;
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
public class ClientProcessCreationLogger extends LoggerMonitor implements ElasticsearchService {

	@Autowired
	private static ElasticsearchHighLevelClient elasticsearchHighLevelClient;

	private static final Logger logger = LoggerFactory.getLogger(Class.class);

	public static String className = "ClientProcessCreationLogger";
	public static String currentIndexName = "ClientProcessCreationLog";
	public static String dailyIndexName = "ClientProcessCreationLogDaily";

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

	public IData insert(String indexName, String message) throws JsonProcessingException , Exception {

		super.begin();

		IData iData = new NodeData();
		iData.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

		JavaMelodyMonitor.printInfo( "Log Messssage Listener Counter","");
		JavaMelodyMonitor.printInfo(className,"begin");

		if(elasticsearchHighLevelClient==null)
			elasticsearchHighLevelClient = new ElasticsearchHighLevelClient();

		// ?????? ?????? ?????? ??????
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		super.setRecodevalue(message);
		try {
			if(Config.getInstance().getLog().isSaveRequestLogData()) {
				String fileName = Config.getInstance().getLog().getSaveRequestLogDataDir()+className+"_"+DateUtil.getCurrentDate(Config.getInstance().getLog().getLogFileDatePattern());
				FileUtil.saveStringBufferToFileAppend(fileName,new StringBuffer(message+"\n"));

				// min mix size????????? ?????? ?????? ??????
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

		// ?????? ??????
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		message = message.toString().replace("\uFEFF", "");
		Parse parse = new Parse();
//		Object objectNode = parse.getObjectNode(message);
		JSONArray jSONArray = parse.getJSONArray(new StringBuffer(message));
		boolean isCurrentIndexDelete = false;
		for (Object object: jSONArray) {
			super.beginTimeObjectConvert();
			ClientProcessCreation clientProcessCreation = new ClientProcessCreation();
			ClientProcessCreationLogOriginal clientProcessCreationLogOriginal = clientProcessCreation.getOriginalObject((LinkedHashMap)object);
			super.setRowSize(super.getRowSize()+clientProcessCreationLogOriginal.getProcessCreationLogData().size());
			List<ClientProcessCreationLogDaily> clientProcessCreationLogDailies = clientProcessCreation.reconstructDailyDocument(clientProcessCreationLogOriginal);
			List<ClientProcessCreationLog> clientProcessCreationLogs = clientProcessCreation.reconstructDocument(clientProcessCreationLogOriginal);
			super.endTimeObjectConvert();

			// deviceId??? current ?????? ??????
			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			super.beginTimeElasticsearchCurrentInsert();
			ClientProcessCreationLogRepository clientProcessCreationLogRepository = (ClientProcessCreationLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(currentIndexName);
			DynamicIndexBean.setIndexName(currentIndexName.toLowerCase());
			clientProcessCreationLogRepository.setIndexName(currentIndexName.toLowerCase());
			if (!elasticsearchHighLevelClient.existIndices(currentIndexName.toLowerCase())) {
				do {
					try {
						clientProcessCreationLogs.get(0).setIndexName(currentIndexName.toLowerCase());
						clientProcessCreationLogRepository.setIndexName(currentIndexName.toLowerCase());
						clientProcessCreationLogRepository.createIndex(clientProcessCreationLogs.get(0));
					} catch (UncategorizedElasticsearchException e) { // already exist (?????? ???????????? ????????? ??????)
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
						super.setExceptionCount(super.getExceptionCount()+1);
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
//				elasticsearchHighLevelClient.deleteDocument(currentIndexName.toLowerCase(), "deviceId", clientProcessCreationLogOriginal.getDeviceId());
				if(!isCurrentIndexDelete) {
					elasticsearchHighLevelClient.deleteCurrentIndex(currentIndexName.toLowerCase(),clientProcessCreationLogOriginal);
					isCurrentIndexDelete = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				super.setExceptionCount(super.getExceptionCount()+1);
			}
			super.setCurrentRowSize(super.getCurrentRowSize()+clientProcessCreationLogs.size());
			List<Document2> document2s = new ArrayList<>();
			for (ClientProcessCreationLog clientProcessCreationLog : clientProcessCreationLogs) {
				clientProcessCreationLog.setIndexName(currentIndexName.toLowerCase());
				document2s.add(clientProcessCreationLog);
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

			// ????????? ?????? ??????
			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (Config.getInstance().getLog().getIndexDailylogClientProcessCreationLogSavedDay()>0) {
				super.beginTimeElasticsearchDailyInsert();
				ClientProcessCreationLogDailyRepository clientProcessCreationLogDailyRepository = (ClientProcessCreationLogDailyRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(dailyIndexName);
				String ip = "", deviceId = "", currentDate = "", timeCreated = "", regDate = "";
				document2s = new ArrayList<>();
				HashSet<String> indexNames = new HashSet<>();
				for (ClientProcessCreationLogDaily clientProcessCreationLogDaily : clientProcessCreationLogDailies) {
					try {
						indexName = DateTimeConvertor.getIndexName(dailyIndexName.toLowerCase(), clientProcessCreationLogDaily.getTimeCurrent());
						indexNames.add(indexName);
						clientProcessCreationLogDaily.setIndexName(indexName);
						clientProcessCreationLogDaily.setTimeRegistered(DateTimeConvertor.getTimeRegistered());
						ip = clientProcessCreationLogDaily.getIp();
						deviceId = clientProcessCreationLogDaily.getDeviceId();
						currentDate = DateTimeConvertor.getPrintDate(clientProcessCreationLogDaily.getTimeCurrent());
						timeCreated = DateTimeConvertor.getPrintDate(clientProcessCreationLogDaily.getTimeCreated());
						regDate = DateTimeConvertor.getPrintDate(clientProcessCreationLogDaily.getTimeRegistered());
						document2s.add(clientProcessCreationLogDaily);
					} catch (Exception e) {
						e.printStackTrace();
						super.setExceptionCount(super.getExceptionCount() + 1);
					}
				}
				super.setDailyRowSize(super.getDailyRowSize() + clientProcessCreationLogDailies.size());
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

				if (Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_TEST) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_INSWAVE) ||
						Config.getInstance().getRunMode1().equals(Constants.RUN_MODE1_DEV) && Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP)
				) {
					JavaMelodyMonitor.printInfo(className, "ip==>>" + ip + " deviceId==>>" + deviceId);
					JavaMelodyMonitor.printInfo(className, "ip==>>" + ip + " deviceId==>>" + deviceId + " regDate==>>" + regDate);
				}
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
		ClientProcessCreationLogger clientProcessCreationLogger = new ClientProcessCreationLogger();
		StringBuffer messageFile = new StringBuffer();
		try	{
			messageFile = FileUtil.getFileToStringBuffer("C:\\Temp\\ClientProcessCreationLog.message"); // ok
			String[] messages = messageFile.toString().split("\n");
			for (int i = 0; i < messages.length; i++) {
				StringBuffer message = new StringBuffer(messages[i]);
				System.out.println(message.toString());

				message = new StringBuffer( message.toString().replace("\uFEFF", ""));

				Parse parse = new Parse();
				Object objectNode = parse.getObjectNode(message.toString());
				ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

				ClientProcessCreation clientProcessCreation = new ClientProcessCreation();
				ClientProcessCreationLogOriginal clientProcessCreationLogOriginal = clientProcessCreation.getOriginalObject((LinkedHashMap)objectNode);

				List<ClientProcessCreationLog> clientProcessCreationLogs = clientProcessCreation.reconstructDocument(clientProcessCreationLogOriginal);
				List<ClientProcessCreationLogDaily> clientProcessCreationLogDailies = clientProcessCreation.reconstructDailyDocument(clientProcessCreationLogOriginal);

				System.out.println();

			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
}
