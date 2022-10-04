package com.inswave.appplatform.log.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.data.NodeData;
import com.inswave.appplatform.data.Parse;
import com.inswave.appplatform.data.SimpleData;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.transaver.elasticsearch.dao.ElasticsearchDaoPackage;
import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean;
import com.inswave.appplatform.transaver.elasticsearch.domain.RuleAlertExclusion;
import com.inswave.appplatform.transaver.saver.ElasticsearchService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.*;
import java.util.stream.StreamSupport;

public class RelationDaoService implements ElasticsearchService {

    @Override
    public IData select(IData reqIData, IData resIData) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String documentId = (String) reqIData.getBodyValue("documentId");

        String documentName = reqIData.getHeaderValueString(Constants.TAG_HEADER_DESTINATION);
        ElasticsearchRepository elasticsearchRepository = (ElasticsearchRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(documentName);

        JSONObject jSONObject = new JSONObject();
        if(elasticsearchRepository==null) {
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_REPOSITORY_NOT_FOUND));
        } else {
            if(documentId!=null&&!documentId.equals("")) {

            } else {
                Integer page = (Integer) reqIData.getBodyValue("page");
                Integer size = (Integer) reqIData.getBodyValue("size");
                if(page==null) page=0;
                if(size==null) size=0;

                if(size>0) {

//                try {
//                    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "timeRegistered");
//
//                    DynamicIndexBean.setIndexName(documentName.toLowerCase());
//                    RuleAlertExclusionRepository tempRuleAlertExclusionepository = (RuleAlertExclusionRepository) elasticsearchRepository;
//                    tempRuleAlertExclusionepository.setIndexName(documentName.toLowerCase());
//
//                    Page<RuleAlertExclusion> pageObjectLogs = elasticsearchRepository.findAll(pageable);
//
//                    List<RuleAlertExclusion> object = Lists.newArrayList(pageObjectLogs);
//                    Parse parse = new Parse();
//                    String info = parse.toString(object);
//                    ObjectMapper mapper = new ObjectMapper();
//                    JSONArray jSONArray = mapper.readValue(info, JSONArray.class);
//                    jSONObject.put(documentName+Constants.TAG_DOCUMENT_ENTITY_ROWS,jSONArray);
//                } catch(Exception e) {
//                    body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
//                    body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_RDB_QUERY_CODE));
//                }

                } else { // 전체조회

                    try{
                        Iterable<Object> objects = null;
                        try{
                            DynamicIndexBean.setIndexName(documentName.toLowerCase());
                            objects = Collections.singleton(elasticsearchRepository.findAll());
                        } catch(Exception e) {
                            e.printStackTrace();
                            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
                            body.put(Constants.TAG_RESULT_MSG, new SimpleData(e.getMessage()));
                        }
                        if(objects==null) {
                            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_DATA_NOT_FOUND));
                        } else {
                            Long documentsSize = StreamSupport.stream(objects.spliterator(),false).count();
                            if(documentsSize<=0) {
                                body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_DATA_NOT_FOUND));
                            } else {
                                List<Object> listObject = Lists.newArrayList(objects);
                                Parse parse = new Parse();
                                String info = parse.toString(listObject);
                                ObjectMapper objectMapper = new ObjectMapper();
                                JSONArray jSONArray = (JSONArray) objectMapper.readValue(info, JSONArray.class);
                                jSONObject.put(documentName+"Rows",jSONArray);
                            }
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
                        body.put(Constants.TAG_RESULT_MSG, new SimpleData(e.getMessage()));
                    }
                }
            }
        }
        body.setObject(jSONObject);
        resIData.put(Constants.TAG_BODY,body);
        return resIData;
    }

    @Override
    public IData insert(IData reqIData, IData resIData) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String documentName = reqIData.getHeaderValueString(Constants.TAG_HEADER_DESTINATION);
        ElasticsearchRepository elasticsearchRepository = (ElasticsearchRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(documentName);

        JSONObject jSONObject = new JSONObject();
        if(elasticsearchRepository==null) {
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_REPOSITORY_NOT_FOUND));
        } else {
            List<Object> objects = new ArrayList<>();
            try{
                ArrayList<HashMap> arrayList = (ArrayList) reqIData.getBodyValue(documentName+Constants.TAG_TABLE_ENTITY_ROWS);
                for (HashMap hashMap : arrayList) {
                    if(documentName.equals(RuleAlertExclusion.class.getSimpleName())) {
                        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        RuleAlertExclusion ruleAlertExclusion = objectMapper.convertValue(hashMap, RuleAlertExclusion.class);
                        ruleAlertExclusion.setIndexName(documentName.toLowerCase());
                        objects.add(ruleAlertExclusion);
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            try{
                //document는 늘어나나 데이터가 안들어감  1 row씩 insert 속도에 문제 생길 수 있음.
//                DynamicIndexBean.setIndexName(documentName.toLowerCase());
//                ruleAlertExclusionRepository.save(documents);
                for (Object object : objects) {
                    DynamicIndexBean.setIndexName(documentName.toLowerCase());
                    elasticsearchRepository.save(object);
                    if(documentName.equals(RuleAlertExclusion.class.getSimpleName())) {
                        RuleAlertExclusion ruleAlertExclusion = (RuleAlertExclusion)object;
                        ruleAlertExclusion.setIndexName(documentName.toLowerCase());
                        ruleAlertExclusion.setTimeRegistered(DateTimeConvertor.changeElasticsearchDate(9,new Date()));
                        DynamicIndexBean.setIndexName(documentName.toLowerCase());
                        elasticsearchRepository.save(object);
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
                body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
                body.put(Constants.TAG_RESULT_MSG, new SimpleData(e.getMessage()));
            }
        }
        resIData.put(Constants.TAG_BODY,body);
        return resIData;
    }

    @Override
    public IData update(IData reqIData, IData resIData) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String documentName = reqIData.getHeaderValueString(Constants.TAG_HEADER_DESTINATION);
        ElasticsearchRepository elasticsearchRepository = (ElasticsearchRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(documentName);
//        ElasticsearchTemplate elasticsearchTemplate = (ElasticsearchTemplate) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(ElasticsearchTemplate.class.getSimpleName());

        if(elasticsearchRepository==null) {
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_REPOSITORY_NOT_FOUND));
        } else {
            ArrayList<HashMap> arrayList = (ArrayList) reqIData.getBodyValue(documentName+Constants.TAG_TABLE_ENTITY_ROWS);
            for (HashMap hashMap : arrayList) {
                if(documentName.equals(RuleAlertExclusion.class.getSimpleName())) {
                    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    RuleAlertExclusion ruleAlertExclusion = objectMapper.convertValue(hashMap, RuleAlertExclusion.class);
                    ruleAlertExclusion.setIndexName(documentName.toLowerCase());
                    DynamicIndexBean.setIndexName(documentName.toLowerCase());
                    if(elasticsearchRepository.existsById(ruleAlertExclusion.getDocumentId())) {
                        ruleAlertExclusion.setTimeUpdated(DateTimeConvertor.changeElasticsearchDate(9,new Date()));
                        elasticsearchRepository.save(ruleAlertExclusion);
                    } else {
                        // insert ??
                        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_DATA_NOT_FOUND));
                    }
                }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                String idFieldName = "documentId";
//                String idFieldValue = "";
//                for(Object key : hashMap.keySet()) {
//                    if(idFieldName.equals(key)) {
//                        Object value = hashMap.get(key);
//                        idFieldValue = value.toString();
////System.out.println("documentId->"+key+" idFieldValue->"+value.toString());
//                    }
//                }
//                for(Object objectKey : hashMap.keySet()) {
//                    if(!idFieldName.equals(objectKey)) {
//                        String key = (String) objectKey;
//                        Object value = hashMap.get(key);
////System.out.println("update key->"+key+" value->"+value.toString());
//                        ElasticsearchHighLevelClient elasticsearchHighLevelClient = new ElasticsearchHighLevelClient();
//                        try {
//                            elasticsearchHighLevelClient.openHighLevelClient();
//                            elasticsearchHighLevelClient.updateDocument(documentName.toLowerCase(), idFieldValue, key, value);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        } finally {
//                            try {
//                            elasticsearchHighLevelClient.closeRestHighLevelClient();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                ElasticsearchHighLevelClient elasticsearchHighLevelClient = new ElasticsearchHighLevelClient();
//                try {
//                    elasticsearchHighLevelClient.openHighLevelClient();
//                    elasticsearchHighLevelClient.updateDocument(documentName.toLowerCase(), idFieldValue, hashMap);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                    elasticsearchHighLevelClient.closeRestHighLevelClient();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        }

        resIData.put(Constants.TAG_BODY,body);
        return resIData;
    }

    @Override
    public IData delete(IData reqIData, IData resIData) {
        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String documentName = reqIData.getHeaderValueString(Constants.TAG_HEADER_DESTINATION);
        ElasticsearchRepository elasticsearchRepository = (ElasticsearchRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(documentName);

        if(elasticsearchRepository==null) {
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_REPOSITORY_NOT_FOUND));
        } else {
            ArrayList<HashMap> arrayList = (ArrayList) reqIData.getBodyValue(documentName + Constants.TAG_TABLE_ENTITY_ROWS);
            for (HashMap hashMap : arrayList) {
                String idFieldName = "documentId";
                String idFieldValue = "";
                for(Object key : hashMap.keySet()) {
                    if(idFieldName.equals(key)) {
                        Object value = hashMap.get(key);
                        idFieldValue = value.toString();
                        DynamicIndexBean.setIndexName(documentName.toLowerCase());
                        elasticsearchRepository.deleteById(idFieldValue);
                    }
                }
            }
        }
        resIData.put(Constants.TAG_BODY,body);
        return resIData;
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
