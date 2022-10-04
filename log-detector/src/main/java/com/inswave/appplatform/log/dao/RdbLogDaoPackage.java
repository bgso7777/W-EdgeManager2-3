package com.inswave.appplatform.log.dao;

import com.inswave.appplatform.dao.RdbDaoPackage;
import com.inswave.appplatform.log.domain.Rule;
import com.inswave.appplatform.log.domain.RuleLevel;
import com.inswave.appplatform.log.domain.RuleReceiver;
import com.inswave.appplatform.log.domain.RuleTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RdbLogDaoPackage implements CommandLineRunner {

    private static RdbLogDaoPackage rdbLogDaoPackage = null;

    @Autowired private RuleDao ruleDao;
    @Autowired private RuleLevelDao ruleLevelDao;
    @Autowired private RuleTargetDao ruleTargetDao;
    @Autowired private RuleReceiverDao ruleReceiverDao;

    public static RdbLogDaoPackage getInstance() {
        if( rdbLogDaoPackage==null )
            rdbLogDaoPackage = new RdbLogDaoPackage();
        return rdbLogDaoPackage;
    }

    @Override
    public void run(String... args) throws Exception {
        RdbDaoPackage.getInstance().putDao(Rule.class.getSimpleName(),ruleDao, Rule.class);
        RdbDaoPackage.getInstance().putDao(RuleLevel.class.getSimpleName(),ruleLevelDao, RuleLevel.class);
        RdbDaoPackage.getInstance().putDao(RuleTarget.class.getSimpleName(),ruleTargetDao, RuleTarget.class);
        RdbDaoPackage.getInstance().putDao(RuleReceiver.class.getSimpleName(),ruleReceiverDao, RuleReceiver.class);
    }
}