package com.inswave.appplatform.log.service;

import com.inswave.appplatform.log.processor.MessageRuleProcessorManager;
import com.inswave.appplatform.service.InternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RuleInitializeBatch implements InternalService {

    private static final Logger logger = LoggerFactory.getLogger(Class.class);

    @Override
    public Object excute() {
        MessageRuleProcessorManager.getInstance().initialRuleInfo();
        return null;
    }

    @Override
    public Object sendLog() {
        return null;
    }

}
