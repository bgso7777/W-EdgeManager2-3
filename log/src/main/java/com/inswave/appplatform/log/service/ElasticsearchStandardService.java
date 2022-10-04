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
import com.inswave.appplatform.transaver.elasticsearch.dao.RuleAlertExclusionRepository;
import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean;
import com.inswave.appplatform.transaver.elasticsearch.domain.RuleAlertExclusion;
import com.inswave.appplatform.transaver.saver.ElasticsearchService;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.*;
import java.util.stream.StreamSupport;

public class ElasticsearchStandardService implements ElasticsearchService {

    @Override
    public IData select(IData reqIData, IData resIData) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String _id = (String) reqIData.getBodyValue("_id");

        String documentName = reqIData.getHeaderValueString(Constants.TAG_HEADER_DESTINATION);
        ElasticsearchRepository elasticsearchRepository = (ElasticsearchRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(documentName);

        JSONObject jSONObject = new JSONObject();
        if(elasticsearchRepository==null) {
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_REPOSITORY_NOT_FOUND));
        } else {
            if(_id!=null&&!_id.equals("")) {

            } else {
                Integer page = (Integer) reqIData.getBodyValue("page");
                Integer size = (Integer) reqIData.getBodyValue("size");
                if(page==null) page=0;
                if(size==null) size=0;

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

                if(size>0) {

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

    // 페이징 처리 참조 소스
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*public IData select2(IData reqIData, IData resIData) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String id = (String)reqIData.getBodyValue(ConstantsLog.TAG_ID);
        if(id==null)
            id="";
        String ip = (String)reqIData.getBodyValue(ConstantsLog.TAG_IP);
        if(ip==null)
            ip="";

        String fromDate = (String) reqIData.getBodyValue(ConstantsLog.TAG_FROM_DATE);
        String toDate = (String) reqIData.getBodyValue(ConstantsLog.TAG_TO_DATE);

        Integer page = (Integer)reqIData.getBodyValue(ConstantsLog.TAG_PAGE);
        Integer size = (Integer)reqIData.getBodyValue(ConstantsLog.TAG_SIZE);
        String dateColumn = (String) reqIData.getBodyValue(ConstantsLog.TAG_DATE_COLUMN);

        Boolean isSetNullNestedData = (Boolean) reqIData.getBodyValue(ConstantsLog.TAG_IS_SET_NULL_NESTED_DATA);
        if(isSetNullNestedData==null)
            isSetNullNestedData = false;

        String documentName = "";
        ElasticsearchRepository elasticsearchRepository = null;
        List<Object> selectResult = null;
        Long countryId=null;
        try {
            documentName = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_DESTINATION);
            elasticsearchRepository = (ElasticsearchRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(documentName);
        } catch(Exception e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_DOMAIN_FORMAT_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId,Constants.ERROR_DOMAIN_FORMAT)));
            resIData.put(Constants.TAG_BODY,body);
            return resIData;
        }

        JSONObject jSONObject = new JSONObject();
        try {
            if((id!=null)&&(!id.equals(""))) {
                Optional document = elasticsearchRepository.findById(id);
                Object object = document.get();
                Parse parse = new Parse();
                ObjectMapper mapper = new ObjectMapper();
                String info = mapper.writeValueAsString(object);
                JSONObject iJSONObject = mapper.readValue(info, JSONObject.class);
                jSONObject.put(documentName,iJSONObject);
            } else {
                Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, dateColumn);
                Page<Document> pageObjectLogs = null;
                if((fromDate != null) && (!fromDate.equals("")) || (toDate != null) && (!toDate.equals(""))) {
//                    Date fDate = DateUtil.getDate(Constants.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z,fromDate);
//                    Date tDate = DateUtil.getDate(Constants.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z,toDate);
                    pageObjectLogs = getPageDocument(documentName, elasticsearchRepository, fromDate, toDate, isSetNullNestedData, ip, pageable);
                } else {
                    pageObjectLogs = getPageDocument(elasticsearchRepository, pageable);
                }
                List<Object> object = Lists.newArrayList(pageObjectLogs);
                Parse parse = new Parse();
                String info = parse.toString(object);
                ObjectMapper mapper = new ObjectMapper();
                JSONArray jSONArray = mapper.readValue(info, JSONArray.class);
                jSONObject.put(documentName+Constants.TAG_DOCUMENT_ENTITY_ROWS,jSONArray);
            }
        } catch(Exception e) {
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_RDB_QUERY_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId,Constants.ERROR_RDB_QUERY)));
        }
        body.setObject(jSONObject);
        resIData.put(Constants.TAG_BODY, body);
        return resIData;
    }

    private Page<Document> getPageDocument(ElasticsearchRepository elasticsearchRepository, Pageable pageable) {
        return elasticsearchRepository.findAll(pageable);
    }

    private Page<Document> getPageDocument(String documentName, ElasticsearchRepository elasticsearchRepository, String fromDate, String toDate, Boolean isSetNullNestedData, String ip, Pageable pageable) {
        Page<Document> documents = null;
        if( documentName.equals(ServerResourceLog.class.getSimpleName()) ) {
            ServerResourceLogRepository serverResourceLogRepository = (ServerResourceLogRepository) elasticsearchRepository;
            documents = serverResourceLogRepository.findByTimeCreatedGreaterThanAndTimeCreatedLessThan(fromDate, toDate, pageable);

        } else if( documentName.equals(ServerResourceLog2.class.getSimpleName()) ) {
            ServerResourceLog2Repository serverResourceLog2Repository = (ServerResourceLog2Repository) elasticsearchRepository;

            // 일자별 조회 어떻게 처리할 것인가 ??????????????
            com.inswave.appplatform.log.elasticsearch.domain.DynamicIndexBean.setIndexName("serverresourcelog2_20210631");
            documents = serverResourceLog2Repository.findByTimeCreatedGreaterThanAndTimeCreatedLessThan(fromDate, toDate, pageable);

            Logger.getLogger("");

        } else if( documentName.equals(ClientPerformanceResourceLog.class.getSimpleName()) ) {
            ClientPerformanceLogRepository clientPerformanceLogRepository = (ClientPerformanceLogRepository) elasticsearchRepository;
            if(ip.equals(""))
                documents = clientPerformanceLogRepository.findByTimeCreatedGreaterThanAndTimeCreatedLessThan(fromDate, toDate, pageable);
            else
                documents = clientPerformanceLogRepository.findByTimeCreatedGreaterThanAndTimeCreatedLessThanAndIp(fromDate, toDate, ip, pageable);
            if(isSetNullNestedData)
                documents.forEach(document->{
                    ClientPerformanceResourceLog clientPerformanceResourceLog = (ClientPerformanceResourceLog) document;
                    clientPerformanceResourceLog.setClientPerfResourceData(null);
                });
        } else if( documentName.equals(ClientProcessResourceLog.class.getSimpleName()) ) {
            ClientProcessResourceLogRepository clientProcessResourceLogRepository = (ClientProcessResourceLogRepository) elasticsearchRepository;
            if(ip.equals(""))
                documents = clientProcessResourceLogRepository.findByTimeCreatedGreaterThanAndTimeCreatedLessThan(fromDate, toDate, pageable);
            else
                documents = clientProcessResourceLogRepository.findByTimeCreatedGreaterThanAndTimeCreatedLessThanAndIp(fromDate, toDate, ip, pageable);

            if(isSetNullNestedData)
                documents.forEach(document->{
                    ClientProcessResourceLog clientProcessResourceLog = (ClientProcessResourceLog)document;
                    clientProcessResourceLog.setClientProcessResourceData(null);
                });
        } else if( documentName.equals(WindowsEventSystemErrorAllLog.class.getSimpleName()) ) {
            WindowsEventSystemErrorAllLogRepository windowsEventSystemErrorAllLogRepository = (WindowsEventSystemErrorAllLogRepository) elasticsearchRepository;
            if(ip.equals(""))
                documents = windowsEventSystemErrorAllLogRepository.findByTimeCreatedGreaterThanAndTimeCreatedLessThan(fromDate, toDate, pageable);
            else
                documents = windowsEventSystemErrorAllLogRepository.findByTimeCreatedGreaterThanAndTimeCreatedLessThanAndIp(fromDate, toDate, ip, pageable);
            if(isSetNullNestedData)
                documents.forEach(document->{
                    WindowsEventSystemErrorAllLog windowsEventSystemErrorAllLog = (WindowsEventSystemErrorAllLog) document;
                    windowsEventSystemErrorAllLog.setCollectWindowsEventLogData(null);
                });
        }
        return documents;
    }*/
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    public IData select2(IData reqIData, IData resIData) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String daoRepositoryName = reqIData.getHeaderValueString(Constants.TAG_HEADER_DESTINATION);
        ElasticsearchRepository elasticsearchRepository = (ElasticsearchRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(daoRepositoryName);

        JSONObject jSONObject = new JSONObject();
        if (elasticsearchRepository == null) {
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_REPOSITORY_NOT_FOUND));
        } else {
            String fromDate = (String) reqIData.getBodyValue(ConstantsLog.TAG_FROM_DATE);
            String toDate = (String) reqIData.getBodyValue(ConstantsLog.TAG_TO_DATE);

            Integer page = (Integer)reqIData.getBodyValue(ConstantsLog.TAG_PAGE);
            Integer size = (Integer)reqIData.getBodyValue(ConstantsLog.TAG_SIZE);
            String dateColumn = (String) reqIData.getBodyValue(ConstantsLog.TAG_DATE_COLUMN);

            Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, dateColumn);
            Page<Document> pageObjectLogs = null;
            List<RuleAlertLog> documents = null;
            if((fromDate!=null)&&(!fromDate.equals("")) || (toDate!=null)&&(!toDate.equals(""))) {
////                    Date fDate = DateUtil.getDate(Constants.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z,fromDate);
////                    Date tDate = DateUtil.getDate(Constants.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z,toDate);
//                pageObjectLogs = getPageDocument(documentName, elasticsearchRepository, fromDate, toDate, isSetNullNestedData, ip, pageable);

                RuleAlertLogRepository ruleAlertLogRepository = (RuleAlertLogRepository) elasticsearchRepository;
                DynamicIndexBean.setIndexName(daoRepositoryName.toLowerCase());
                ruleAlertLogRepository.setIndexName(daoRepositoryName.toLowerCase());
                documents = ruleAlertLogRepository.findByTimeRegisteredGreaterThanAndTimeRegisteredLessThan(fromDate,toDate);

                JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(), "documents.size()-->>"+documents.size());

            } else {
//                pageObjectLogs = getPageDocument(elasticsearchRepository, pageable);
            }

            try {
//                List<Object> object = Lists.newArrayList(pageObjectLogs);
                Parse parse = new Parse();
                String info = parse.toString(documents);
                ObjectMapper mapper = new ObjectMapper();
                JSONArray jSONArray = mapper.readValue(info, JSONArray.class);
                jSONObject.put(daoRepositoryName+Constants.TAG_DOCUMENT_ENTITY_ROWS, jSONArray);
            } catch(Exception e) {
                e.printStackTrace();
                body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
                body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_RDB_QUERY_CODE));
                body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData());
            }
        }

        body.setObject(jSONObject);
        resIData.put(Constants.TAG_BODY,body);
        return resIData;
    }*/

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
                //document는 늘어나나 데이터가 안들어감 1 row씩 inseart시 대량 들어올 경우 문제 생길 수 있음
//                DynamicIndexBean.setIndexName(documentName.toLowerCase());
//                ruleAlertExclusionRepository.save(objects);
                for (Object object : objects) {
                    DynamicIndexBean.setIndexName(documentName.toLowerCase());
                    elasticsearchRepository.save(object);
                    if(documentName.equals(RuleAlertExclusion.class.getSimpleName())) {
                        RuleAlertExclusion ruleAlertExclusion = (RuleAlertExclusion) object;
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

        JSONObject jSONObject = new JSONObject();
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

        JSONObject jSONObject = new JSONObject();
        if(elasticsearchRepository==null) {
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_REPOSITORY_NOT_FOUND));
        } else {
            ArrayList<HashMap> arrayList = (ArrayList) reqIData.getBodyValue(documentName+Constants.TAG_TABLE_ENTITY_ROWS);
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
