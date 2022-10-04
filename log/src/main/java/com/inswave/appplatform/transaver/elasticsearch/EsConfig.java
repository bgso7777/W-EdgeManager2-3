package com.inswave.appplatform.transaver.elasticsearch;

import org.springframework.stereotype.Component;

@Component("esConfig")
public class EsConfig {

  //@Value("${es.index.name}")
  private String indexName="";

  public String getIndexName() {
    return indexName;
  }

  public void setIndexName(String indexName) {
    this.indexName = indexName;
  }

  // serverresourcelog

}
