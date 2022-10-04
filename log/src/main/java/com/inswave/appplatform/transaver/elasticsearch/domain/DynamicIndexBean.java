package com.inswave.appplatform.transaver.elasticsearch.domain;

import org.springframework.stereotype.Component;

/**
 * Dynamic Index Name 처리로 검색 시 indexName 셋 후 조회
 */
@Component
public class DynamicIndexBean {

    private static String indexName = "dummy"; // index name셋팅 후 shards=2,  replicas 가 바뀜 ??

    public static final String getIndexName() {
        return indexName;
    }
    public static void setIndexName(String indexNamee) {
        indexName = indexNamee;
    }

}