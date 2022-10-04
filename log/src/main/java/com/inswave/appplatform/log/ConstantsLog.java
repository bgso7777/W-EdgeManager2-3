package com.inswave.appplatform.log;

public class ConstantsLog {

    public static final String SORT_DESENDING = "desc";
    public static final String SORT_ASCENDING = "asc";
    public static final Integer TYPE_OF_COLUMN_CPU = 1;
    public static final Integer TYPE_OF_COLUMN_MEMORY = 2;
    public static final Integer TYPE_OF_COLUMN_THREAD = 3;
    public static final Integer TYPE_OF_COLUMN_HANDLE = 4;

    public static final String TAG_SERVICE = "service";
    public static final String TAG_URL = "url";
    public static final String TAG_SEND_TYPE = "sendType";
    public static final String TAG_DESCRIPTION = "description";

    public static final String TAG_INFOMATION = "infomation";
    public static final String TAG_DATA = "data";

    public static final Integer TYPE_OF_SCATTER_CPU_MEMORY = 1;
    public static final Integer TYPE_OF_SCATTER_CPU_DISK = 2;
    public static final Integer TYPE_OF_SCATTER_CPU_TEMPERATURE = 3;
    public static final Integer TYPE_OF_SCATTER_CPU_THREAD = 4;
    public static final Integer TYPE_OF_SCATTER_CPU_HANDLE = 5;

    public static final String TAG_TITLE = "title";
    public static final String TAG_SERVER_NAME = "serverName";
    public static final String TAG_CPU_USAGE = "cpuUsage";
    public static final String TAG_DISK_USAGE = "diskUsage";
    public static final String TAG_MEMORY_USAGE = "memoryUsage";
    public static final String TAG_NETWORK_RECEIVED = "networkReceived";
    public static final String TAG_NETWORK_SENT = "networkSent";
    public static final String TAG_IS_ON = "isOn";
    public static final String TAG_TIME_CREATED = "timeCreated";
    public static final String TAG_SOURCE = "source";
    public static final String TAG_INDEX_NAME = "indexName";
    public static final String TAG_PAGE = "page";
    public static final String TAG_SIZE = "size";
    public static final String TAG_DATE_COLUMN = "dateColumn";
    public static final String TAG_FROM_DATE = "fromDate";
    public static final String TAG_TO_DATE = "toDate";
    public static final String TAG_CPU_TEMPERATURE = "cpuTemperature";
    public static final String TAG_THREAD_COUNT = "threadCount";
    public static final String TAG_HANDLE_COUNT = "handleCount";

    public static final String TAG_WINDOWS_EVENT_LEVEL_DISPLAY_NAME = "windowsEventLevelLevelDisplayName";
    public static final String TAG_WINDOWS_EVENT_LEVEL_DISPLAY_NAME_COUNT = "windowsEventLevelLevelDisplayNameCount";
    public static final Integer TYPE_OF_WINDOW_EVENT_LEVEL_CATEGORY = 1;

    public static final String TAG_ID = "id";
    public static final String TAG_IS_SET_NULL_NESTED_DATA = "isSetNullNestedData";
    public static final String TAG_IP = "ip";
    public static final String TAG_DATE_PATTERN_OF_ELASTICSEARCH_INDEX = "yyyyMMdd";
    public static final String CLASS_OF_JVM_MEMORY_CHECK_BATCH_SERVICE = "com.inswave.appplatform.log.service.JvmMemoryCheck";
    public static final int TAG_MAX_TERMINAL_COUNT = 33300;

    public static final String KEY_OF_INDEX_NUMBER_OF_REPLICAS="index.number_of_replicas";
    public static final String KEY_OF_INDEX_NUMBER_OF_SHARDS="index.number_of_shards";

    public static final String CLASS_OF_RULE_INITIAL_BATCH_SERVICE = "com.inswave.appplatform.log.service.RuleInitializeBatch";
    public static final String METHOD_OF_INTERNAL_EXCUTE_SERVICE = "excute";
    public static final String TAG_LOG_SAVED_DAY = "logSavedDay";
    public static final String TAG_GENERATOR_DATA_DIRS = "generatorDataDirs";
    public static final String TAG_CURRENT_DEVICE_COUNT = "currentDeviceCount";
    public static final String TAG_CHECK_SEND_LOG_SLEEP_MS = "checkSendLogSleepMs";
    public static final String TAG_GENERATOR_SENDER_STATUS = "generatorSenderStatus";

    public static final Integer TAG_ALREADY_RUN = 2;
    public static final String TAG_IS_STARTED_SENDER_THREAD = "isStartedSenderThread";
    public static final String TAG_ALL_DEVICE_SIZE = "allDeviceSize";
    public static final String TAG_CURRENT_DEVICE_SIZE = "currentDeviceSize";
    public static final String TAG_INSTANCE_IDX = "instanceIdx";
}