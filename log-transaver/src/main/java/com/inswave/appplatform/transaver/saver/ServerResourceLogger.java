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
import com.inswave.appplatform.log.translate.ServerResource;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.transaver.elasticsearch.ElasticsearchHighLevelClient;
import com.inswave.appplatform.transaver.elasticsearch.ElasticsearchJestClient;
import com.inswave.appplatform.transaver.elasticsearch.dao.ElasticsearchDaoPackage;
import com.inswave.appplatform.transaver.elasticsearch.dao.ServerResourceLogDailyRepository;
import com.inswave.appplatform.transaver.elasticsearch.dao.ServerResourceLogRepository;
import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean;
import com.inswave.appplatform.transaver.elasticsearch.domain.ServerResourceLog;
import com.inswave.appplatform.transaver.elasticsearch.domain.ServerResourceLogDaily;
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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ServerResourceLogger extends LoggerMonitor implements ElasticsearchService {

	@Autowired
	private static ElasticsearchHighLevelClient elasticsearchHighLevelClient;

	private static final Logger logger = LoggerFactory.getLogger(Class.class);

	public static String className = "ServerResourceLogger";
	public static String currentIndexName = "ServerResourceLog";
	public static String dailyIndexName = "ServerResourceLogDaily";

	@Override
	public IData select(IData reqIData,IData resIData) {
		// TODO Auto-generated method stub

		String service = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_SERVICE);

		IData iData = new NodeData();
		iData.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
		iData.put(Constants.TAG_ERROR, new SimpleData(""));
		iData.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(""));

		return resIData;
	}

	@Override
	public IData insert(IData reqIData,IData resIData) {
		// TODO Auto-generated method stub
		IData iData = new NodeData();
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
		return iData;
	}

	private ZonedDateTime convertToZoneDateTime(Date date, ZoneId zoneId) {
		return Instant.ofEpochMilli(date.getTime())
				.atZone(zoneId);
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
			ServerResource serverResource = new ServerResource();
			ServerResourceLog serverResourceLog = serverResource.getServerResourceObject((LinkedHashMap)object);
			ServerResourceLogDaily serverResourceLogDaily = serverResource.getServerResourceDailyObject((LinkedHashMap)object);
			serverResourceLog.setTimeCurrent(serverResourceLog.getTimeCreated());
			super.setRowSize(super.getRowSize()+1);
			super.endTimeObjectConvert();

			// deviceId별 current 로그 저장
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			super.beginTimeElasticsearchCurrentInsert();
			ServerResourceLogRepository serverResourceLogRepository = (ServerResourceLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(currentIndexName);
			DynamicIndexBean.setIndexName(currentIndexName.toLowerCase());
			serverResourceLogRepository.setIndexName(currentIndexName.toLowerCase());
			serverResourceLog.setIndexName(currentIndexName.toLowerCase());
			serverResourceLog.setStatisticsValue(DateTimeConvertor.getStatisticsValue(serverResourceLog.getTimeCurrent()));
			serverResourceLog.setTimeRegistered(DateTimeConvertor.getTimeRegistered());
			if (!elasticsearchHighLevelClient.existIndices(currentIndexName.toLowerCase())) {
				do {
					try {
						serverResourceLogRepository.setIndexName(currentIndexName.toLowerCase());
						serverResourceLogRepository.createIndex(serverResourceLog);
					} catch (UncategorizedElasticsearchException e) { // already exist (다른 서버에서 생성한 경우)
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
				elasticsearchHighLevelClient.deleteDocument(currentIndexName.toLowerCase(), "deviceId", serverResourceLog.getDeviceId());
//				if(!isCurrentIndexDelete) {
//					elasticsearchHighLevelClient.deleteCurrentIndex(currentIndexName.toLowerCase(),serverResourceLog);
//					isCurrentIndexDelete = true;
//				}
			} catch (Exception e) {
				e.printStackTrace();
				super.setExceptionCount(super.getExceptionCount()+1);
			}
			List<Document2> document2s = new ArrayList<>();
			super.setCurrentRowSize(super.getCurrentRowSize()+1);
			try {
				document2s.add(serverResourceLog);
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
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if(Config.getInstance().getLog().getIndexDailylogServerResourceLogSavedDay()>0) {
				super.beginTimeElasticsearchDailyInsert();
				serverResourceLogDaily.setTimeCurrent(serverResourceLogDaily.getTimeCreated());
				ServerResourceLogDailyRepository serverResourceLogDailyRepository = (ServerResourceLogDailyRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(dailyIndexName);
				indexName = DateTimeConvertor.getIndexName(dailyIndexName.toLowerCase(), serverResourceLog.getTimeCurrent());
				DynamicIndexBean.setIndexName(indexName);
				serverResourceLogDaily.setIndexName(indexName);
				serverResourceLogDaily.setStatisticsValue(DateTimeConvertor.getStatisticsValue(serverResourceLogDaily.getTimeCurrent()));
				serverResourceLogDaily.setTimeRegistered(DateTimeConvertor.getTimeRegistered());
				super.setDailyRowSize(super.getDailyRowSize() + 1);
				document2s = new ArrayList<>();
				try {
					document2s.add(serverResourceLogDaily);
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
					JavaMelodyMonitor.printInfo(className, "timeCreated-->>" + serverResourceLog.getTimeCreated());
					JavaMelodyMonitor.printInfo(className, "source-->>" + serverResourceLogDaily.getSource());
					JavaMelodyMonitor.printInfo(className, "timeCreated-->>" + serverResourceLog.getTimeCreated() + " source-->>" + serverResourceLogDaily.getSource());
				}
				JavaMelodyMonitor.printInfo(className, "daily process end!");
			}
		}
		super.end();

		JavaMelodyMonitor.printInfo(className,"end rowSize-->"+super.getRowSize()+" currentRowSize-->"+super.getCurrentRowSize()+" dailyRowSize-->"+super.getDailyRowSize()+" exceptionCount-->"+super.getExceptionCount());
		JavaMelodyMonitor.printInfo(className,"end runningTime->"+super.getRunningTime()+" objectConvertTime-->"+super.getObjectConvertTime()+" elasticsearchCurrentInsert-->"+super.getElasticsearchCurrentInsert()+" elasticsearchDailyInsert-->"+super.getElasticsearchDailyInsert());

		return iData;
	}

	@Override
	public IData insert(String indexName, List<Document2> document2s) throws Exception {
		return null;
	}

	public static void main(String[] argv) {
		ServerResourceLogger serverResourceLogger = new ServerResourceLogger();
		StringBuffer messageFile = new StringBuffer();
		try	{
			messageFile = FileUtil.getFileToStringBuffer("C:\\Temp\\ServerResourceLogger.message"); //
			String[] messages = messageFile.toString().split("\n");
			for (int i = 0; i < messages.length; i++) {
				StringBuffer message = new StringBuffer(messages[i]);
				System.out.println(message.toString());

				Parse parse = new Parse();
				Object objectNode = parse.getObjectNode(message.toString());
//				ObjectMapper objectMapper = new ObjectMapper();
				ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
//				objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
//				DateFormat dateFormat = new SimpleDateFormat(Constants.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z);
//				objectMapper.setDateFormat(dateFormat);
				ServerResourceLog serverResourceLog = objectMapper.readValue(objectNode.toString(), ServerResourceLog.class);

//				Calendar cal = Calendar.getInstance();
//				cal.setTime(serverResourceLog.getTimeCreated());
//				cal.add(Calendar.HOUR, -9);
//				Date d = new Date(cal.getTimeInMillis());
//				serverResourceLog.setTimeCreated(d);


				//System.out.println(DateUtil.getDate(serverResourceLog.getTimeCreated(),Constants.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z));
				//System.out.println(DateUtil.getDate(DateUtil.getCurrentDate2(Constants.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z),Constants.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z));

//				String columnName = "cpuUsage";
//				String columnType = "Double";
//				String condition = ">";
//				String value = "80";
//
//				Object object = ObjectUtil.getFieldValue(serverResourceLog,columnName);
//				if(object instanceof Double) {
//					System.out.println((Double)object);
//				}

				// bulk insert test
				////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				List<ServerResourceLog> serverResourceLogs = new ArrayList<>();
				List<Document2> document2s = new ArrayList<>();
				for (int j=0; j<1; j++) {
					ServerResourceLog tempServerResourceLog = objectMapper.readValue(objectNode.toString(), ServerResourceLog.class);
					tempServerResourceLog.setIndexName("serverresourcelogbulkdaily_"+j);
					serverResourceLogs.add(tempServerResourceLog);
					document2s.add(tempServerResourceLog);
				}
//				ElasticsearchHighLevelClient elasticsearchHighLevelClient = new ElasticsearchHighLevelClient("52.79.253.187",9203);
//				elasticsearchHighLevelClient.openHighLevelClient();
//				BulkResponse bulkResponse = elasticsearchHighLevelClient.writeDocument2s(document2s);
//				elasticsearchHighLevelClient.closeRestHighLevelClient();
				////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				ElasticsearchJestClient elasticsearchJestClient = new ElasticsearchJestClient("192.168.79.100",9203);
				List<String> servers = new ArrayList<>();
				servers.add("http://192.168.79.100:9202");
				servers.add("http://192.168.79.100:9203");
				elasticsearchJestClient.setServers(servers);
				elasticsearchJestClient.openJestClient();
				elasticsearchJestClient.writeDocument2s(document2s);
				elasticsearchJestClient.closeJestClient();

				System.out.println();
				break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

}