package com.inswave.appplatform.service.batch;

import com.inswave.appplatform.Constants;
import com.inswave.appplatform.dao.*;
import com.inswave.appplatform.domain.*;
import com.inswave.appplatform.log.entity.LogInfomation;
import com.inswave.appplatform.service.InternalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ConfigInitial implements InternalService {
    @Override
    public Object excute() {

        log.debug("start ConfigInitial");

        Integer result = Constants.RESULT_SUCESS;

        try{
            if(!com.inswave.appplatform.Config.getInstance().getRunMode2().equals(Constants.RUN_MODE2_TRANSAVER))
                initConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.debug("end ConfigInitial");

        return result;
    }

    private void initConfig() {
        ConfigDao configDao = (ConfigDao) RdbDaoPackage.getInstance().getDao(Config.class.getSimpleName());
        List<Config> configs = configDao.findAll();
        for (Config config : configs) {
            try {
                if (config.getKey_().equals(Constants.TAG_MANAGER_SERVER_URLS)) {
                    com.inswave.appplatform.Config.getInstance().setManagerServerUrls(config.getValue().split(Constants.URL_SPLIT_STRING));
                } else if (config.getKey_().equals(Constants.TAG_ADMIN_SERVER_URLS)) {
                    com.inswave.appplatform.Config.getInstance().setAdminServerUrls(config.getValue().split(Constants.URL_SPLIT_STRING));
                } else if (config.getKey_().equals(Constants.TAG_ELASTICSEARCH_SERVER_URLS)) {
                    com.inswave.appplatform.Config.getInstance().setElasticsearchServerUrls(config.getValue().split(Constants.URL_SPLIT_STRING));
                } else if (config.getKey_().equals(Constants.TAG_LOG_MANAGER_SERVER_URLS)) {
                    com.inswave.appplatform.Config.getInstance().setLogmanagerServerUrls(config.getValue().split(Constants.URL_SPLIT_STRING));

                } else if (config.getKey_().equals(Constants.TAG_SMTP_HOST)) {
                    com.inswave.appplatform.Config.getInstance().setSmtpHost(config.getValue());
                } else if (config.getKey_().equals(Constants.TAG_SMTP_PORT)) {
                    com.inswave.appplatform.Config.getInstance().setSmtpPort(config.getValue());
                } else if (config.getKey_().equals(Constants.TAG_SMTP_ID)) {
                    com.inswave.appplatform.Config.getInstance().setSmtpId(config.getValue());
                } else if (config.getKey_().equals(Constants.TAG_SMTP_PASSWORD)) {
                    com.inswave.appplatform.Config.getInstance().setSmtpPassword(config.getValue());
                } else if (config.getKey_().equals(Constants.TAG_SMTP_NAME)) {
                    com.inswave.appplatform.Config.getInstance().setSmtpName(config.getValue());
                } else if (config.getKey_().equals(Constants.TAG_URL)) {
                    com.inswave.appplatform.Config.getInstance().setUrl(config.getValue());
                    com.inswave.appplatform.Config.getInstance().setUrlData(config.getValue2());
                } else if (config.getKey_().equals(Constants.SHINHANBANKATOP_INTEGRITY_ALERT_URL)) {
                    if( com.inswave.appplatform.Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP) ) {
                        com.inswave.appplatform.Config.getInstance().setShinhanbankatopIntegrityAlertUrl(config.getValue());
                        com.inswave.appplatform.Config.getInstance().setShinhanbankatopIntegrityAlertUrlData(config.getValue2());
                    }
                } else if (config.getKey_().equals(Constants.SHINHANBANKATOP_LOGIN_URL)) {
                    if( com.inswave.appplatform.Config.getInstance().getRunMode3().equals(Constants.RUN_MODE3_SHINHANBANKATOP) ) {
                        com.inswave.appplatform.Config.getInstance().setShinhanbankatopLoginUrl(config.getValue());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        initLanguage();
        if(Constants.RUN_MODE2_MANAGER.equals(com.inswave.appplatform.Config.getInstance().getRunMode2())) {
			initConfigManager();
			// manager에서 로그 저장을 하는 경우의 사이트들..
			if(Constants.RUN_MODE3_SHINHANBANKATOP.equals(com.inswave.appplatform.Config.getInstance().getRunMode3()) ||
				Constants.RUN_MODE3_INSWAVE.equals(com.inswave.appplatform.Config.getInstance().getRunMode3()) ) {
				initConfigLog();
			}
		} else if(Constants.RUN_MODE2_LOG.equals(com.inswave.appplatform.Config.getInstance().getRunMode2())) {
			initConfigLog();
		}
    }

    /**
     *
     */
    public void initLanguage() {
        LanguageDao languageDao = (LanguageDao) RdbDaoPackage.getInstance().getDao(Language.class.getSimpleName());
        List<Language> languages = languageDao.findAll();
        for (Language language : languages) {
            com.inswave.appplatform.LanguagePack.getInstance().put(language.getCountryId(),language.getKey_(),language.getValue());
        }

        CountryDao countryDao = (CountryDao) RdbDaoPackage.getInstance().getDao(Country.class.getSimpleName());
        List<Country> countries = countryDao.findAll();
        for (Country country : countries) {
            com.inswave.appplatform.LanguagePack.getInstance().put(Constants.LANGUAGEPACK_COUNTRY_ID,country.getName(),Long.toString(country.getCountryId()));
        }
    }

    /**
     * log 서버에서 작동하기 위한 값 설정.
     */
    public void initConfigLog() {

        ConfigLogDao configLogDao = (ConfigLogDao) RdbDaoPackage.getInstance().getDao(ConfigLog.class.getSimpleName());
        List<ConfigLog> configLogs = configLogDao.findAll();

        for (ConfigLog configLog : configLogs) {

            if (configLog.getKey_().equals(Constants.TAG_DAY_SAVE_LOG)) {
                try {
                    com.inswave.appplatform.Config.getInstance().getLog().setDaySaveLog(Integer.parseInt(configLog.getValue()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (configLog.getKey_().equals(Constants.TAG_IS_SEND_LOG_TO_COLLECTOR)) {
                if (configLog.getValue().toLowerCase().equals("true"))
                    com.inswave.appplatform.Config.getInstance().getLog().setSendLogToCollector(true);
                else
                    com.inswave.appplatform.Config.getInstance().getLog().setSendLogToCollector(false);
            } else if (configLog.getKey_().indexOf(Constants.TAG_LOG_INFOMATION_KEY)!=-1) {
                try {
                    LogInfomation logInfomations = new LogInfomation();
                    String[] keys = configLog.getKey_().split(Constants.TAG_LOG_INFOMATION_KEY);
                    logInfomations.setKey(keys[1]);
                    logInfomations.setUrl(configLog.getValue());
                    logInfomations.setLogSavedDay(configLog.getLogSavedDay());
                    logInfomations.setIndexNames(configLog.getValue2());
                    logInfomations.setDescription(configLog.getDescription());
                    com.inswave.appplatform.Config.getInstance().getLog().getLogInfomation().put(logInfomations.getKey(),logInfomations);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * manager 서버에서 작동하기 위한 값 설정.
     */
    public void initConfigManager() {
        ConfigManagerDao configManagerDao = (ConfigManagerDao) RdbDaoPackage.getInstance().getDao(ConfigManager.class.getSimpleName());
        List<ConfigManager> configManagers = configManagerDao.findAll();
            for (ConfigManager configManager : configManagers) {
                try{
                    if(configManager.getKey_().equals(Constants.TAG_COUNT_LOGIN_FAIL_LOCK)) {
                        try{ com.inswave.appplatform.Config.getInstance().getManager().setCountLoginFailLock(Integer.parseInt(configManager.getValue())); } catch(Exception e) {}
                    } else if(configManager.getKey_().equals(Constants.TAG_MINUTE_LOGIN_FAIL_LOCK)) {
                        try{ com.inswave.appplatform.Config.getInstance().getManager().setMinuteLoginFailLock(Integer.parseInt(configManager.getValue())); } catch(Exception e) {}
                    } else if(configManager.getKey_().equals(Constants.TAG_USER_PASSWORD_DAY)) {
                        try{ com.inswave.appplatform.Config.getInstance().getManager().setUserPasswordDay(Integer.parseInt(configManager.getValue())); } catch(Exception e) {}
                    } else if(configManager.getKey_().equals(Constants.TAG_IS_SAVE_UI_LOG)) {
                        if(configManager.getValue().toLowerCase().equals("true"))
                            com.inswave.appplatform.Config.getInstance().getManager().setSaveUiLog(true);
                        else
                            com.inswave.appplatform.Config.getInstance().getManager().setSaveUiLog(false);
                    } else if(configManager.getKey_().equals(Constants.TAG_IS_CHECK_IP)) {
                        if(configManager.getValue().toLowerCase().equals("true"))
                            com.inswave.appplatform.Config.getInstance().getManager().setCheckIp(true);
                        else
                            com.inswave.appplatform.Config.getInstance().getManager().setCheckIp(false);
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object sendLog() {
        return null;
    }

}
