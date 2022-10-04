package com.inswave.appplatform.log.processor;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.transaver.elasticsearch.ElasticsearchHighLevelClient;
import com.inswave.appplatform.transaver.elasticsearch.domain.ServerResourceLog;
import com.inswave.appplatform.transaver.util.BeanUtils;
import com.inswave.appplatform.util.DateUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ServerResourceObserver extends Thread {

    ElasticsearchHighLevelClient elasticsearchHighLevelClient = BeanUtils.getBean(ElasticsearchHighLevelClient.class);

    private static ConcurrentLinkedDeque<ServerResourceLog> serverResourceLogs = new ConcurrentLinkedDeque<>();
    private static String documentDelete_yyyyMMddHH = ""; // 1시간에 1번씩만 삭제 체크 (연속 삭제 방지)

    public ServerResourceObserver() {
    }

    @Override
    public void run() {
        do{
            try{
                if( serverResourceLogs.size()>0 )
                    process();
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }while (true);
    }

    private synchronized void process() {
        for(ServerResourceLog serverResourceLog : serverResourceLogs) {
            if( serverResourceLog!=null &&
                serverResourceLog.getSource() != null &&
                serverResourceLog.getSource().equals(Config.getInstance().getLog().getServerresourcelogObserverCheckSourceName())) {
                checkElasticsearchDiskCapacity(serverResourceLog.getDiskUsage());
            }
        }
    }

    private synchronized void remove(ServerResourceLog serverResourceLog) {
        try {
            serverResourceLogs.remove(serverResourceLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void add(ServerResourceLog serverResourceLog) {
        try {
            serverResourceLogs.add((ServerResourceLog) serverResourceLog.clone());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkElasticsearchDiskCapacity(Double diskUsage) {
        try {
            String[] deleteIndexNames = Config.getInstance().getLog().getServerresourcelogObserverDeleteIndexNames().split(",");
            int capacityElasticsearchDataDisk = Config.getInstance().getLog().getServerresourcelogObserverCapacityElasticsearchDataDisk();
            if(capacityElasticsearchDataDisk<=diskUsage) {
                Hashtable<String, ArrayList<String>> targetIndexNames = new Hashtable<>();
                try {
                    String[] allIndices = elasticsearchHighLevelClient.getIndices();
                    for (int i = 0; i < allIndices.length; i++) {
                        for (int j = 0; j < deleteIndexNames.length; j++) {
                            if (allIndices[i].indexOf(deleteIndexNames[j]) != -1) {
                                ArrayList<String> tempTargetIndexNames = targetIndexNames.get(deleteIndexNames[j]);
                                if (tempTargetIndexNames == null) {
                                    tempTargetIndexNames = new ArrayList<>();
                                    tempTargetIndexNames.add(allIndices[i]);
                                    targetIndexNames.put(deleteIndexNames[j], tempTargetIndexNames);
                                } else {
                                    tempTargetIndexNames.add(allIndices[i]);
                                    targetIndexNames.put(deleteIndexNames[j], tempTargetIndexNames);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Enumeration<String> keys = targetIndexNames.keys();
                while (keys.hasMoreElements()) {
                    String key = keys.nextElement();
                    ArrayList<String> tempTargetIndexNames = targetIndexNames.get(key);
                    Collections.sort(tempTargetIndexNames);
                    for (String indexname : tempTargetIndexNames) {
                        if( documentDelete_yyyyMMddHH.equals("") ||
                            (!documentDelete_yyyyMMddHH.equals(DateUtil.getCurrentDate("yyyyMMddHH"))) ) {
                            documentDelete_yyyyMMddHH = DateUtil.getCurrentDate("yyyyMMddHH");
                            JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "delete-->>" + indexname);
                            elasticsearchHighLevelClient.indexRemoveQuery(indexname);
                            break;
                        }
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
