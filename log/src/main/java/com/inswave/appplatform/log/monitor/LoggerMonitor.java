package com.inswave.appplatform.log.monitor;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.data.NodeData;
import com.inswave.appplatform.util.DateUtil;
import com.inswave.appplatform.util.FileUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LoggerMonitor implements Cloneable {

    private String instanceId;
    private String key;
    private String topicName;
    private String groupId;

    private int rowSize=0, currentRowSize=0, dailyRowSize=0;
    private Integer jsonProcessingExceptionCount=0;
    private Integer commitSyncExceptionCount=0;
    private Integer exceptionCount=0;
    private long runningTime=0L, objectConvertTime=0L, elasticsearchCurrentInsert=0L, elasticsearchDailyInsert=0L;

    private Date beginTime = null;
    private Date endTime = null;

    private Date beginTimeObjectConvert = null;
    private Date endTimeObjectConvert = null;

    private Date beginTimeElasticsearchCurrentInsert = null;
    private Date endTimeElasticsearchCurrentInsert = null;

    private Date beginTimeElasticsearchDailyInsert = null;
    private Date endTimeElasticsearchDailyInsert = null;

    private IData iData = new NodeData();
    private String exceptionFileName = "";
    private boolean isException = false;
    private boolean isJsonProcessingException = false;
    private boolean isCommitSyncException = false;

    private String recodevalue;
    
    public void begin() {
        beginTime = new Date();

        instanceId="";
        key="";
        topicName="";
        groupId="";

        rowSize=0;
        currentRowSize=0;
        dailyRowSize=0;
        jsonProcessingExceptionCount=0;
        commitSyncExceptionCount=0;
        exceptionCount=0;
        runningTime=0L;
        objectConvertTime=0L;
        elasticsearchCurrentInsert=0L;
        elasticsearchDailyInsert=0L;

        endTime = null;

        beginTimeObjectConvert = null;
        endTimeObjectConvert = null;

        beginTimeElasticsearchCurrentInsert = null;
        endTimeElasticsearchCurrentInsert = null;

        beginTimeElasticsearchDailyInsert = null;
        endTimeElasticsearchDailyInsert = null;

        iData = new NodeData();
        exceptionFileName = "";
        isException = false;
        isJsonProcessingException = false;
        isCommitSyncException = false;

        recodevalue="";
    }

    public void beginTimeObjectConvert(){
        beginTimeObjectConvert = new Date();
    }
    public void endTimeObjectConvert(){
        endTimeObjectConvert = new Date();
        objectConvertTime = objectConvertTime + (endTimeObjectConvert.getTime() - beginTimeObjectConvert.getTime());
    }

    public void end() {
        endTime = new Date();
        runningTime = endTime.getTime() - beginTime.getTime();

        if(isJsonProcessingException)
            jsonProcessingExceptionCount=1;
        if(isCommitSyncException)
            commitSyncExceptionCount=1;
        if(isException)
            exceptionCount=1;

        endJob();
    }

    public void beginTimeElasticsearchCurrentInsert() {
        beginTimeElasticsearchCurrentInsert = new Date();
    }

    public void endTimeElasticsearchCurrentInsert() {
        endTimeElasticsearchCurrentInsert = new Date();
        elasticsearchCurrentInsert = endTimeElasticsearchCurrentInsert.getTime() - beginTimeElasticsearchCurrentInsert.getTime();
    }

    public void beginTimeElasticsearchDailyInsert() {
        beginTimeElasticsearchDailyInsert = new Date();
    }

    public void endTimeElasticsearchDailyInsert() {
        endTimeElasticsearchDailyInsert = new Date();
        elasticsearchDailyInsert = endTimeElasticsearchDailyInsert.getTime() - beginTimeElasticsearchDailyInsert.getTime();
    }

    public LoggerMonitor cloneLoggerMonitor() throws CloneNotSupportedException {
        return (LoggerMonitor) this.clone();
    }

    public void endJob() {
        if(isException)
            exceptionFileName = Config.getInstance().getLog().getSaveExceptionLogDir()+this.getClass().getSimpleName()+"_Exception_"+Config.getInstance().getLog().getInstanceId()+"_"+ DateUtil.getCurrentDate(Config.getInstance().getLog().getLogFileDatePattern());

        if(isJsonProcessingException)
            exceptionFileName = Config.getInstance().getLog().getSaveExceptionLogDir()+this.getClass().getSimpleName()+"_JsonProcessingException_"+Config.getInstance().getLog().getInstanceId()+"_"+DateUtil.getCurrentDate(Config.getInstance().getLog().getLogFileDatePattern());

        if(isCommitSyncException)
            exceptionFileName = Config.getInstance().getLog().getSaveExceptionLogDir() + this.getClass().getSimpleName() + "_CommitSyncException_" + Config.getInstance().getLog().getInstanceId() + "_" + DateUtil.getCurrentDate(Config.getInstance().getLog().getLogFileDatePattern());

        try {
            if (Config.getInstance().getLog().isSaveExceptionLogData() && !exceptionFileName.equals(""))
                FileUtil.saveStringBufferToFile(exceptionFileName, new StringBuffer(recodevalue));
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }
}
