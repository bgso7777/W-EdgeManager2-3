package com.inswave.appplatform.transaver.elasticsearch.dao;

import ch.qos.logback.classic.Logger;
import com.inswave.appplatform.transaver.elasticsearch.domain.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.inswave.appplatform.transaver.elasticsearch.domain"})
@EnableAutoConfiguration
public class ElasticsearchDaoService implements CommandLineRunner {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(Class.class);

	@Autowired
	private StandardRepository standardRepository;

	@Autowired
	private ServerResourceLogRepository serverResourceLogRepository;
	@Autowired
	private ServerResourceLogDailyRepository serverResourceLogDailyRepository;
	@Autowired
	private ServerResourceLogStatisticsRepository serverResourceLogStatisticsValueRepository;

	@Autowired
	private ClientProcessResourceLogRepository clientProcessResourceLogRepository;
	@Autowired
	private ClientProcessResourceLogDailyRepository clientProcessResourceLogDailyRepository;
	@Autowired
	private ClientProcessResourceLogStatisticsRepository clientProcessResourceLogStatisticsValueRepository;

	@Autowired
	private WindowsEventSystemErrorAllLogRepository windowsEventSystemErrorAllLogRepository;
	@Autowired
	private WindowsEventSystemErrorAllLogStatisticsRepository windowsEventSystemErrorAllLogStatisticsValueRepository;
	@Autowired
	private WindowsEventSystemErrorAllLogDailyRepository windowsEventSystemErrorAllLogDailyRepository;

	@Autowired
	private ApplicationErrorLogRepository applicationErrorLogRepository;
	@Autowired
	private ApplicationErrorLogDailyRepository applicationErrorLogDailyRepository;
	@Autowired
	private ApplicationErrorLogStatisticsRepository applicationErrorLogStatisticsValueRepository;

	@Autowired
	private ClientPerformanceResourceLogRepository clientPerformanceResourceLogRepository;
	@Autowired
	private ClientPerformanceResourceLogStatisticsRepository clientPerformanceResourceLogStatisticsValueRepository;
	@Autowired
	private ClientPerformanceResourceLogDailyRepository clientPerformanceResourceLogDailyRepository;

	@Autowired
	private DeviceErrorLogRepository deviceErrorLogRepository;
	@Autowired
	private DeviceErrorLogDailyRepository deviceErrorLogDailyRepository;
	@Autowired
	private DeviceErrorLogStatisticsRepository deviceErrorLogStatisticsValueRepository;

	@Autowired
	private HWErrorLogRepository hWErrorLogRepository;
	@Autowired
	private HWErrorLogStatisticsRepository hWErrorLogStatisticsValueRepository;
	@Autowired
	private HWErrorLogDailyRepository hWErrorLogDailyRepository;

	@Autowired
	private IntegrityLogRepository integrityLogRepository;
	@Autowired
	private IntegrityLogDailyRepository integrityLogDailyRepository;

	@Autowired
	private WindowsBlueScreenLogRepository windowsBlueScreenLogRepository;
	@Autowired
	private WindowsBlueScreenLogDailyRepository windowsBlueScreenLogDailyRepository;
	@Autowired
	private WindowsBlueScreenLogStatisticsRepository windowsBlueScreenLogStatisticsValueRepository;

	@Autowired
	private PcOnOffEventLogRepository pcOnOffEventLogRepository;
	@Autowired
	private PcOnOffEventLogDailyRepository pcOnOffEventLogDailyRepository;
	@Autowired
	private PcOnOffEventLogStatisticsRepository pcOnOffEventLogStatisticsValueRepository;

	@Autowired
	ClientHWInfoResourceLogRepository clientHWInfoResourceLogRepository;
	@Autowired
	ClientHWInfoResourceLogDailyRepository clientHWInfoResourceLogDailyRepository;

	@Autowired
	ClientWindowsUpdateListResourceLogRepository clientWindowsUpdateListResourceLogRepository;
	@Autowired
	ClientWindowsUpdateListResourceLogDailyRepository clientWindowsUpdateListResourceLogDailyRepository;

	@Autowired
	ClientMBRResourceLogRepository clientMBRResourceLogRepository;
	@Autowired
	ClientMBRResourceLogDailyRepository clientMBRResourceLogDailyRepository;

	@Autowired
	ClientProgramListResourceLogRepository clientProgramListResourceLogRepository;
	@Autowired
	ClientProgramListResourceLogDailyRepository clientProgramListResourceLogDailyRepository;

	@Autowired
	ClientDefragAnalysisResourceLogRepository clientDefragAnalysisResourceLogRepository;
	@Autowired
	ClientDefragAnalysisResourceLogDailyRepository clientDefragAnalysisResourceLogDailyRepository;

	@Autowired
	ClientUserTermMonitorLogRepository clientUserTermMonitorLogRepository;
	@Autowired
	ClientUserTermMonitorLogDailyRepository clientUserTermMonitorLogDailyRepository;
	@Autowired
	ClientUserTermMonitorLogStatisticsRepository clientUserTermMonitorLogStatisticsValueRepository;

	@Autowired
	private ClientActivePortListResourceLogRepository clientActivePortListResourceLogRepository;
	@Autowired
	private ClientActivePortListResourceLogDailyRepository clientActivePortListResourceLogDailyRepository;

	@Autowired
	private RuleAlertLogRepository ruleAlertLogRepository;
	@Autowired
	private RuleAlertSendLogRepository ruleAlertSendLogRepository;

	@Autowired
	ClientProcessCreationLogRepository clientProcessCreationLogRepository;
	@Autowired
	ClientProcessCreationLogDailyRepository clientProcessCreationLogDailyRepository;

	@Autowired
	ClientControlProcessResourceLogRepository clientControlProcessResourceLogRepository;
	@Autowired
	ClientControlProcessResourceLogDailyRepository clientControlProcessResourceLogDailyRepository;

	@Autowired
	ModuleMonitoringLogRepository moduleMonitoringLogRepository;

	@Autowired
	RuleAlertExclusionRepository ruleAlertExclusionRepository;

	@Autowired
	DeviceRepository deviceRepository;

	@Autowired
	GenratorLogRepository genratorLogRepository;

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao("StandardRepository",standardRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientActivePortListResourceLog.class.getSimpleName(),clientActivePortListResourceLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientActivePortListResourceLogDaily.class.getSimpleName(),clientActivePortListResourceLogDailyRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientUserTermMonitorLog.class.getSimpleName(),clientUserTermMonitorLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientUserTermMonitorLogDaily.class.getSimpleName(),clientUserTermMonitorLogDailyRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientUserTermMonitorLogStatistics.class.getSimpleName(),clientUserTermMonitorLogStatisticsValueRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ServerResourceLog.class.getSimpleName(),serverResourceLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ServerResourceLogDaily.class.getSimpleName(),serverResourceLogDailyRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ServerResourceLogStatistics.class.getSimpleName(),serverResourceLogStatisticsValueRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientPerformanceResourceLog.class.getSimpleName(),clientPerformanceResourceLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientPerformanceResourceLogDaily.class.getSimpleName(),clientPerformanceResourceLogDailyRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientPerformanceResourceLogStatistics.class.getSimpleName(),clientPerformanceResourceLogStatisticsValueRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(WindowsEventSystemErrorAllLog.class.getSimpleName(),windowsEventSystemErrorAllLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(WindowsEventSystemErrorAllLogStatistics.class.getSimpleName(),windowsEventSystemErrorAllLogStatisticsValueRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(WindowsEventSystemErrorAllLogDaily.class.getSimpleName(),windowsEventSystemErrorAllLogDailyRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ApplicationErrorLog.class.getSimpleName(),applicationErrorLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ApplicationErrorLogDaily.class.getSimpleName(),applicationErrorLogDailyRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ApplicationErrorLogStatistics.class.getSimpleName(),applicationErrorLogStatisticsValueRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientProcessResourceLog.class.getSimpleName(),clientProcessResourceLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientProcessResourceLogDaily.class.getSimpleName(),clientProcessResourceLogDailyRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientProcessResourceLogStatistics.class.getSimpleName(),clientProcessResourceLogStatisticsValueRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(DeviceErrorLog.class.getSimpleName(),deviceErrorLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(DeviceErrorLogDaily.class.getSimpleName(),deviceErrorLogDailyRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(DeviceErrorLogStatistics.class.getSimpleName(),deviceErrorLogStatisticsValueRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(HWErrorLog.class.getSimpleName(),hWErrorLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(HWErrorLogDaily.class.getSimpleName(),hWErrorLogDailyRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(HWErrorLogStatistics.class.getSimpleName(),hWErrorLogStatisticsValueRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(IntegrityLog.class.getSimpleName(),integrityLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(IntegrityLogDaily.class.getSimpleName(),integrityLogDailyRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(WindowsBlueScreenLog.class.getSimpleName(),windowsBlueScreenLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(WindowsBlueScreenLogDaily.class.getSimpleName(),windowsBlueScreenLogDailyRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(WindowsBlueScreenLogStatistics.class.getSimpleName(),windowsBlueScreenLogStatisticsValueRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(PcOnOffEventLog.class.getSimpleName(),pcOnOffEventLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(PcOnOffEventLogDaily.class.getSimpleName(),pcOnOffEventLogDailyRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(PcOnOffEventLogStatistics.class.getSimpleName(),pcOnOffEventLogStatisticsValueRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientHWInfoResourceLog.class.getSimpleName(),clientHWInfoResourceLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientHWInfoResourceLogDaily.class.getSimpleName(),clientHWInfoResourceLogDailyRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientWindowsUpdateListResourceLog.class.getSimpleName(),clientWindowsUpdateListResourceLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientWindowsUpdateListResourceLogDaily.class.getSimpleName(),clientWindowsUpdateListResourceLogDailyRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientMBRResourceLog.class.getSimpleName(),clientMBRResourceLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientMBRResourceLogDaily.class.getSimpleName(),clientMBRResourceLogDailyRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientProgramListResourceLog.class.getSimpleName(),clientProgramListResourceLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientProgramListResourceLogDaily.class.getSimpleName(),clientProgramListResourceLogDailyRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientDefragAnalysisResourceLog.class.getSimpleName(),clientDefragAnalysisResourceLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientDefragAnalysisResourceLogDaily.class.getSimpleName(),clientDefragAnalysisResourceLogDailyRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(RuleAlertLog.class.getSimpleName(),ruleAlertLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(RuleAlertSendLog.class.getSimpleName(),ruleAlertSendLogRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientProcessCreationLog.class.getSimpleName(),clientProcessCreationLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientProcessCreationLogDaily.class.getSimpleName(),clientProcessCreationLogDailyRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientControlProcessResourceLog.class.getSimpleName(),clientControlProcessResourceLogRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ClientControlProcessResourceLogDaily.class.getSimpleName(),clientControlProcessResourceLogDailyRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(ModuleMonitoringLog.class.getSimpleName(),moduleMonitoringLogRepository);

		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(RuleAlertExclusion.class.getSimpleName(),ruleAlertExclusionRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(Device.class.getSimpleName(),deviceRepository);
		ElasticsearchDaoPackage.getInstance().putElasticsearchDao(GeneratorLog.class.getSimpleName(),genratorLogRepository);
	}

}
