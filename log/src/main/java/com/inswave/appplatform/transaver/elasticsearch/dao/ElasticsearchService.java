package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.data.IData;

public interface ElasticsearchService {
    public IData select(IData reqIData,IData resIData);
    public IData insert(IData reqIData,IData resIData);
    public IData update(IData reqIData,IData resIData);
    public IData delete(IData reqIData,IData resIData);
    public void insert(String indexName, String message);
}
