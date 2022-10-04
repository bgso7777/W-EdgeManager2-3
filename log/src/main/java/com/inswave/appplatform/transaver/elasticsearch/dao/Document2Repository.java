package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;

public interface Document2Repository<T> {
    <S extends Document2> S save2(S entity);
    Document getDocument();
    ElasticsearchOperations getElasticsearchOperations();
    void setIndexName(String indexName);
    void createIndex(Document2 document2);
}