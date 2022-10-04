package com.inswave.appplatform.transaver.elasticsearch.dao;

import java.util.Hashtable;

public class ElasticsearchDaoPackage {

    private static ElasticsearchDaoPackage allDaoPackage;
    private static Hashtable elasticsearchPackage_ = null;

    public static ElasticsearchDaoPackage getInstance() {
        if(allDaoPackage==null)
            allDaoPackage = new ElasticsearchDaoPackage();
        return allDaoPackage;
    }

    public ElasticsearchDaoPackage() {
        elasticsearchPackage_ = new Hashtable();
    }

    public Object getElasticsearchDao(String documentName) {
        return elasticsearchPackage_.get(documentName);
    }

    public void putElasticsearchDao(String documentName, Object daoServiceObject) {
        elasticsearchPackage_.put(documentName,daoServiceObject);
    }
}
