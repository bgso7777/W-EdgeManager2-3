package com.inswave.appplatform.log.service;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.log.entity.LogInfomation;
import com.inswave.appplatform.service.InternalService;
import com.inswave.appplatform.transaver.elasticsearch.ElasticsearchHighLevelClient;
import com.inswave.appplatform.transaver.util.BeanUtils;
import com.inswave.appplatform.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DocumentCheckBatch implements InternalService {

    private static final Logger logger = LoggerFactory.getLogger(Class.class);

    private String[] allIndices = new String[0];

    @Override
    public Object excute() {

        Integer result = Constants.RESULT_SUCESS;

        deleteCheckSize();

        try{ Thread.sleep(5000); } catch(Exception e) {}

        deleteCheckDate();

        return result;
    }

    /**
     * 일자로 체크 후 삭제
     */
    private void deleteCheckDate() {
        try{
            ElasticsearchHighLevelClient elasticsearchHighLevelClient = BeanUtils.getBean(ElasticsearchHighLevelClient.class);
            allIndices = elasticsearchHighLevelClient.getIndices();

            HashMap logInfomations = Config.getInstance().getLog().getLogInfomation();
            Iterator<String> keys =  logInfomations.keySet().iterator();
            while ( keys.hasNext() ) {
                LogInfomation logInfomation = (LogInfomation) logInfomations.get(keys.next());
                List<String> deleteIndices = new ArrayList<>();
                for (int i = 0; i < allIndices.length; i++) {
                    if(allIndices[i].indexOf(logInfomation.getIndexNames())!=-1 && allIndices[i].indexOf("_")!=-1 ) {
                        JavaMelodyMonitor.printInfo("DocumentCheck", logInfomation.getIndexNames()+" : findIndice-->>"+allIndices[i]+" savedDay-->>"+logInfomation.getLogSavedDay() );
                        String elasticDate = "";
                        try{ elasticDate = allIndices[i].substring(allIndices[i].indexOf("_")+1,allIndices[i].length()); } catch(Exception e) {}
                        if(!elasticDate.equals("")) {
                            if( DateUtil.isSavedElasticDate(elasticDate, logInfomation.getLogSavedDay()) )
                                ;
                            else
                                deleteIndices.add(allIndices[i]);
                        }
                    }
                }
                for (String indice : deleteIndices) {
                    try{
                        elasticsearchHighLevelClient.indexRemoveQuery(indice);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * count로 체크 후 삭제
     */
    private void deleteCheckSize() {

        try{
            ElasticsearchHighLevelClient elasticsearchHighLevelClient = BeanUtils.getBean(ElasticsearchHighLevelClient.class);
            allIndices = elasticsearchHighLevelClient.getIndices();

            HashMap logInfomations = Config.getInstance().getLog().getLogInfomation();
            Iterator<String> keys =  logInfomations.keySet().iterator();
            while ( keys.hasNext() ) {
                LogInfomation logInfomation = (LogInfomation) logInfomations.get(keys.next());
                ArrayList<String> logInfomationIndexNames = new ArrayList<>();
                for (int i = 0; i < allIndices.length; i++) {
                    if(allIndices[i].indexOf(logInfomation.getIndexNames())!=-1 && allIndices[i].indexOf("_")!=-1 ) {
                        logInfomationIndexNames.add(allIndices[i]);
                    }
                }
                Collections.sort(logInfomationIndexNames, Collections.reverseOrder());
                for (int i = 0; i < logInfomationIndexNames.size(); i++) {
                    if((logInfomation.getLogSavedDay()-1)<i) {
                        try {
                            elasticsearchHighLevelClient.indexRemoveQuery(logInfomationIndexNames.get(i));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object sendLog() {
        return null;
    }

    public DocumentCheckBatch () {}
    public static void main(String argv[]) {

        try {
            DocumentCheckBatch documentCheckBatch = new DocumentCheckBatch();

            LogInfomation logInfomation = new LogInfomation();
            logInfomation.setIndexNames("modulemonitoringlogdaily");
            logInfomation.setLogSavedDay(5);

            ElasticsearchHighLevelClient elasticsearchHighLevelClient = new ElasticsearchHighLevelClient("192.168.79.100",9200);
            documentCheckBatch.allIndices = elasticsearchHighLevelClient.getIndices();
            List<String> deleteIndices = new ArrayList<>();
            for (int i = 0; i < documentCheckBatch.allIndices.length; i++) {
                if(documentCheckBatch.allIndices[i].indexOf(logInfomation.getIndexNames())!=-1 && documentCheckBatch.allIndices[i].indexOf("_")!=-1 ) {
System.out.println("target indice -> "+documentCheckBatch.allIndices[i]);
                     String elasticDate = "";
                    try{ elasticDate = documentCheckBatch.allIndices[i].substring(documentCheckBatch.allIndices[i].indexOf("_")+1,documentCheckBatch.allIndices[i].length()); } catch(Exception e) {}
                    if(!elasticDate.equals("")) {
                        if( DateUtil.isSavedElasticDate(elasticDate, logInfomation.getLogSavedDay()) )
                            ;
                        else
                            deleteIndices.add(documentCheckBatch.allIndices[i]);
                    }
                }
            }
            for (String indice : deleteIndices) {
System.out.println("    step1 delete indice -> "+indice);
            }

            try{ Thread.sleep(5000); } catch(Exception e) {}
            try{
                documentCheckBatch.allIndices = elasticsearchHighLevelClient.getIndices();

                ArrayList<String> logInfomationIndexNames = new ArrayList<>();
                for (int i = 0; i < documentCheckBatch.allIndices.length; i++) {
                    if(documentCheckBatch.allIndices[i].indexOf(logInfomation.getIndexNames())!=-1 && documentCheckBatch.allIndices[i].indexOf("_")!=-1 ) {
                        logInfomationIndexNames.add(documentCheckBatch.allIndices[i]);
                    }
                }
                Collections.sort(logInfomationIndexNames,Collections.reverseOrder());
                for (int i = 0; i < logInfomationIndexNames.size(); i++) {
                    if((logInfomation.getLogSavedDay()-1)<i) {
System.out.println("    step2 delete indice -> "+logInfomationIndexNames.get(i));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
//            elasticsearchHighLevelClient.closeRestHighLevelClient();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}
