package com.inswave.appplatform.log.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.data.NodeData;
import com.inswave.appplatform.data.SimpleData;
import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import com.inswave.appplatform.transaver.saver.ElasticsearchService;

import java.util.List;

public class IntegrityLogTerminalError implements ElasticsearchService {

    @Override
    public IData select(IData reqIData, IData resIData) {
        return null;
    }

    @Override
    public IData insert(IData reqIData, IData resIData) {
        return null;
    }

    @Override
    public IData update(IData reqIData, IData resIData) {
        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        resIData.put(Constants.TAG_BODY,body);
        return resIData;
    }

    @Override
    public IData delete(IData reqIData, IData resIData) {
        return null;
    }

    @Override
    public IData insert(String indexName, String message) throws JsonProcessingException, Exception {
        return null;
    }

    @Override
    public IData insert(String indexName, List<Document2> document2s) throws Exception {
        return null;
    }
}
