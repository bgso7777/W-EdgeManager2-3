package com.inswave.appplatform.transaver.saver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.data.NodeData;
import com.inswave.appplatform.data.SimpleData;
import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.log.monitor.LoggerMonitor;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.transaver.elasticsearch.ElasticsearchHighLevelClient;
import com.inswave.appplatform.transaver.elasticsearch.dao.ElasticsearchDaoPackage;
import com.inswave.appplatform.transaver.elasticsearch.dao.ModuleMonitoringLogRepository;
import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean;
import com.inswave.appplatform.transaver.elasticsearch.domain.ModuleMonitoringLog;
import com.inswave.appplatform.transaver.util.BeanUtils;
import org.elasticsearch.action.bulk.BulkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;


public class ModuleMonitoringLogger extends LoggerMonitor implements ElasticsearchService {

    ElasticsearchHighLevelClient elasticsearchHighLevelClient = BeanUtils.getBean(ElasticsearchHighLevelClient.class);

    private static final Logger logger = LoggerFactory.getLogger(Class.class);

    public static String className = "ModuleMonitoringLogger";
    public static String currentIndexName = "ModuleMonitoringLog";
    public static String dailyIndexName = "ModuleMonitoringLogDaily";

    @Override
    public IData select(IData reqIData, IData resIData) {
        return null;
    }

    @Override
    public IData insert(IData reqIData, IData resIData) {
        return null;
    }

    @Override
    public IData update(IData reqIData, IData resIData) {
        return null;
    }

    @Override
    public IData delete(IData reqIData, IData resIData) {
        return null;
    }

    @Override
    public IData insert(String indexName, String message) throws JsonProcessingException, Exception {
        return null;
    }

    @Override
    public IData insert(String indexName, List<Document2> document2s) throws Exception {

        IData iData = new NodeData();
        iData.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        // current
        ModuleMonitoringLogRepository moduleMonitoringLogRepository = (ModuleMonitoringLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(currentIndexName);
        if (!elasticsearchHighLevelClient.existIndices(currentIndexName.toLowerCase())) {
            Document2 document2 = document2s.get(0);
            document2.setIndexName(currentIndexName.toLowerCase());
            DynamicIndexBean.setIndexName(currentIndexName.toLowerCase());
            moduleMonitoringLogRepository.setIndexName(currentIndexName.toLowerCase());
            moduleMonitoringLogRepository.createIndex(document2);
        }

        for (Document2 document2 : document2s) {
            try {
                ModuleMonitoringLog moduleMonitoringLog = (ModuleMonitoringLog) document2;
                document2.setIndexName(currentIndexName.toLowerCase());
                DynamicIndexBean.setIndexName(currentIndexName.toLowerCase());
                moduleMonitoringLogRepository.setIndexName(currentIndexName.toLowerCase());
                moduleMonitoringLog.setTimeRegistered(DateTimeConvertor.getTimeRegistered());
//                moduleMonitoringLogRepository.deleteById(moduleMonitoringLog.getKey()); // ???????????
                List<ModuleMonitoringLog> moduleMonitoringLogs = moduleMonitoringLogRepository.findByKey(moduleMonitoringLog.getKey());
                if(moduleMonitoringLogs.size()>0) {
                    for (ModuleMonitoringLog tempModuleMonitoringLog : moduleMonitoringLogs)
                        moduleMonitoringLogRepository.deleteById(tempModuleMonitoringLog.getDocumentId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            BulkResponse bulkResponse = elasticsearchHighLevelClient.writeDocument2s(document2s);
            if (bulkResponse.hasFailures())
                JavaMelodyMonitor.printInfo(className, bulkResponse.buildFailureMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // daily
        if(Config.getInstance().getLog().getIndexDailylogWindowsEventSystemErrorAllLogSavedDay()>0) {
            HashSet<String> indexNames = new HashSet<>();
            for (Document2 document2 : document2s) {
                ModuleMonitoringLog moduleMonitoringLog = (ModuleMonitoringLog) document2;
                indexName = DateTimeConvertor.getIndexName(currentIndexName.toLowerCase() + "daily", moduleMonitoringLog.getTimeCurrent());
                document2.setIndexName(indexName);
                indexNames.add(indexName);
            }
            try {
                BulkResponse bulkResponse = elasticsearchHighLevelClient.writeDocument2s(document2s);
                if (bulkResponse.hasFailures())
                    JavaMelodyMonitor.printInfo(className, bulkResponse.buildFailureMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return iData;
    }
}
