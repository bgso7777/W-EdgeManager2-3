package com.inswave.appplatform.wedgemanager.service.scm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.data.NodeData;
import com.inswave.appplatform.data.Parse;
import com.inswave.appplatform.data.SimpleData;
import com.inswave.appplatform.svn.SvnManager;
import com.inswave.appplatform.wedgemanager.dao.TerminalDistDao;
import com.inswave.appplatform.wedgemanager.dao.TerminalDistScheduleDao;
import com.inswave.appplatform.wedgemanager.dao.TerminalDistScheduleTargetDao;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalDist;
import com.inswave.appplatform.wedgemanager.domain.terminal.wrapper.DistributeTaskStatusVO;
import com.inswave.appplatform.wedgemanager.service.scm.distribute.DistributeManager;
import com.inswave.appplatform.wedgemanager.service.scm.distribute.DistributeTask;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.tigris.subversion.javahl.ClientException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class RepositoryDistributeService {

    private DistributeManager             distributeManager;
    private TerminalDistDao               terminalDistDao;
    private TerminalDistScheduleDao       terminalDistScheduleDao;
    private TerminalDistScheduleTargetDao terminalDistScheduleTargetDao;
    private SvnManager                    svnManager;

    public RepositoryDistributeService(DistributeManager distributeManager,
                                       TerminalDistDao terminalDistDao,
                                       TerminalDistScheduleDao terminalDistScheduleDao,
                                       TerminalDistScheduleTargetDao terminalDistScheduleTargetDao) {
        this.distributeManager = distributeManager;
        this.terminalDistDao = terminalDistDao;
        this.terminalDistScheduleDao = terminalDistScheduleDao;
        this.terminalDistScheduleTargetDao = terminalDistScheduleTargetDao;
    }

    @PostConstruct
    public void postConstruct() throws IOException, ClientException {
        svnManager = SvnManager.getInstance();
        //        if (terminalDistDao.count() == 0) {
        //            generateDistPreset();
        //        }

        //        for (int i = 0; i < 20; i++) {
        //            List<TerminalDist> terminalDists = terminalDistDao.findByRepositoryName("KPIC", Sort.by("distName"));
        //            distributeManager.addTask("KPIC", terminalDists, true);
        //        }
    }

    public IData getDistributeTaskStatus(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        try {
            JSONObject jsonObj = new JSONObject();
            DistributeTaskStatusVO distributeTaskStatusVO = distributeManager.getDistributeTaskStatus();
            jsonObj.put("data", new Parse().parseList(Collections.singletonList(distributeTaskStatusVO)));
            body.setObject(jsonObj);
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        }
        return resIData;
    }

    public IData addTask(IData reqIData, IData resIData) throws IOException {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        String repositoryName = reqIData.getBodyValueString("repositoryName");
        boolean beforeClean = reqIData.getBodyValueBoolean("beforeClean");
        List<Long> distributeIds = (ArrayList) reqIData.getBodyValue("distIds");
        List<TerminalDist> terminalDists = terminalDistDao.findAllById(distributeIds);

        DistributeTask distributeTask = distributeManager.addTask(repositoryName, terminalDists, beforeClean);
        if (distributeTask.getId() != null) {
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
            body.put(Constants.TAG_RESULT_MSG, new SimpleData("Distribute task has been added."));
        } else {
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData("Configure 'solutionPath' and 'distributePath' before distribute."));
        }

        return resIData;
    }
}
