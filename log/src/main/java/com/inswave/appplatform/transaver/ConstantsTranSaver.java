package com.inswave.appplatform.transaver;

public class ConstantsTranSaver {

    public static final String TAG_DATE_PATTERN_OF_ELASTICSEARCH_INDEX = "yyyyMMdd";
    public static final int TAG_MAX_TERMINAL_COUNT = 33300;

    public static final String KEY_OF_INDEX_NUMBER_OF_REPLICAS="index.number_of_replicas";
    public static final String KEY_OF_INDEX_NUMBER_OF_SHARDS="index.number_of_shards";

    public static final String TAG_DATE_PATTERN_YYYYMMDD = "yyyyMMdd";
    public static final String TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String CLASS_OF_DOCUMENT_CHECK_BATCH_SERVICE = "com.inswave.appplatform.log.service.DocumentCheckBatch";
    public static final String CLASS_OF_KAFKA_LISTENER_CONTROLLER_SERVICE = "com.inswave.appplatform.log.service.KafkaListenerController";
    public static final String CLASS_OF_MODULE_MONITORING = "com.inswave.appplatform.transaver.saver.ModuleMonitoringLogger";
    public static final String CLASS_OF_ELASTICSEARCH_TEMPLATE_CREATE_SERVICE = "com.inswave.appplatform.log.service.ElasticsearchTemplateCreator";
    public static final String CLASS_OF_LOG_GENERATOR_SERVICE = "com.inswave.appplatform.log.service.LogGenerator";

    public static final String TAG_PROPERTIES = "properties";
    public static final String TAG_INDEX_NUMBER_OF_SHARDS = "index.number_of_shards";
    public static final String TAG_INDEX_NUMBER_OF_REPLICAS = "index.number_of_replicas";
    public static final String PATH_TEMPLATE_FILE = "/com/inswave/appplatform/transaver/elasticsearch/domain";
    public static final String FILE_TEMPLATE = "Template.json";

    public static final String TAG_DATE_PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String TAG_DATE_PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
}