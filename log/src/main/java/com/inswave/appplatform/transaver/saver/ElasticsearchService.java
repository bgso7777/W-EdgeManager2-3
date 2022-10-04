package com.inswave.appplatform.transaver.saver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;

import java.util.List;

public interface ElasticsearchService {
    public IData select(IData reqIData,IData resIData);
    public IData insert(IData reqIData,IData resIData);
    public IData update(IData reqIData,IData resIData);
    public IData delete(IData reqIData,IData resIData);
    public IData insert(String indexName, String message) throws JsonProcessingException, Exception;
    public IData insert(String indexName, List<Document2> document2s) throws Exception;
}
