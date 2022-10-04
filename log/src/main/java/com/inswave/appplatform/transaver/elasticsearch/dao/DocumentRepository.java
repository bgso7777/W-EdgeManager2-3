package com.inswave.appplatform.transaver.elasticsearch.dao;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;

public interface DocumentRepository<T> {
    <S extends com.inswave.appplatform.transaver.elasticsearch.domain.Document> S save2(S entity);
    Document getDocument();
    ElasticsearchOperations getElasticsearchOperations();
    void setIndexName(String indexName);
}