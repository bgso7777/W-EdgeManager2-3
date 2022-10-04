package com.inswave.appplatform.log.processor;

import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.transaver.elasticsearch.ElasticsearchHighLevelClient;
import com.inswave.appplatform.transaver.elasticsearch.dao.ElasticsearchDaoPackage;
import com.inswave.appplatform.transaver.elasticsearch.dao.RuleAlertLogRepository;
import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean;
import com.inswave.appplatform.transaver.elasticsearch.domain.RuleAlertLog;
import com.inswave.appplatform.transaver.util.BeanUtils;
import org.elasticsearch.action.bulk.BulkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.UncategorizedElasticsearchException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 연관분석을 위한 클래스
 * associationRuleAlertLog에 대한 life cycle ????
 *
 */
public class AssociationAnalysis extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(Class.class);

    ElasticsearchHighLevelClient elasticsearchHighLevelClient = BeanUtils.getBean(ElasticsearchHighLevelClient.class);

    private List<RuleAlertLog> associationRuleAlertLogs = new ArrayList<RuleAlertLog>();
    private RuleAlertLogRepository ruleAlertLogRepository = null;

    public AssociationAnalysis() {
        this.ruleAlertLogRepository = (RuleAlertLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(RuleAlertLog.class.getSimpleName());
    }

    public void addAssociation(List<RuleAlertLog> ruleAlertLogs) throws CloneNotSupportedException {
        for (RuleAlertLog ruleAlertLog : ruleAlertLogs) {
            if( ruleAlertLog.getAssociationAnalysisRuleIds().size()>0 ) {
                associationRuleAlertLogs.add((RuleAlertLog) ruleAlertLog.clone());
            }
        }
    }

    public int size() {
        return associationRuleAlertLogs.size();
    }

    public void analysisAssociation(List<RuleAlertLog> ruleAlertLogs) throws CloneNotSupportedException {
        // daily save
        List<Document2> document2s = new ArrayList<>();
        HashSet<String> indexNames = new HashSet<>();

        for (RuleAlertLog associationRuleAlertLog : associationRuleAlertLogs) {
            for (RuleAlertLog ruleAlertLog : ruleAlertLogs) {
                for (Long id : associationRuleAlertLog.getAssociationAnalysisRuleIds() ) {
                    if (id.equals(ruleAlertLog.getRuleId())) {
                        associationRuleAlertLog.getCurrentAssociationAnalysisDocumentIds().add(ruleAlertLog.getDocumentId());
                        // ????????????????? 테스트 필요
//                        ruleAlertLogRepository.save2(associationRuleAlertLog);
                        String indexName = DateTimeConvertor.getIndexName(RuleAlertLog.class.getSimpleName().toLowerCase()+"_daily", ruleAlertLog.getTimeRegistered());
                        indexNames.add(indexName);
                        ruleAlertLog.setIndexName(indexName);
                        document2s.add(associationRuleAlertLog);
                    }
                }
            }
        }

        int createSize = 0, loopCount = 0;
        for (String indexNam : indexNames) {
            do {
                if (!elasticsearchHighLevelClient.existIndices(indexNam)) {
                    try {
                        for (Document2 document2 : document2s) {
                            if (document2.getIndexName().equals(indexNam)) {
                                DynamicIndexBean.setIndexName(indexNam);
                                ruleAlertLogRepository.setIndexName(indexNam);
                                ruleAlertLogRepository.createIndex(document2);
                                break;
                            }
                        }
                    } catch (UncategorizedElasticsearchException e) { // already exist (다른 서버에서 생성한 경우)
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (elasticsearchHighLevelClient.existIndices(indexNam)) {
                    createSize++;
                    if (createSize >= indexNames.size())
                        break;
                } else {
                    JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "not exist index (" + loopCount + ") : " + indexNam);
                }
                loopCount++;
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                if (loopCount >= 10) {
                    Exception exception = new Exception("index create exception" + "{" + indexNam + "}");
                    exception.printStackTrace();
                }
            } while (true);
        }
        try {
            BulkResponse bulkResponse = elasticsearchHighLevelClient.writeDocument2s(document2s);
            if (bulkResponse.hasFailures())
                logger.error(bulkResponse.buildFailureMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        do{
            try{
                for (RuleAlertLog associationRuleAlertLog : associationRuleAlertLogs) {
                    // ????????????????? 테스트 필요
                    if( DateTimeConvertor.replaceDate( associationRuleAlertLog.getTimeAssociationAnalysis() ).compareTo(DateTimeConvertor.getDateCurrentDate()) ==-1 )
                        associationRuleAlertLogs.remove(associationRuleAlertLog);
                }
            } catch(Exception e) {}

            try{
                sleep(1);
            } catch(Exception e) {}

        }while (true);
    }

}
