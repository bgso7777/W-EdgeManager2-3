package com.inswave.appplatform.log.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.LanguagePack;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.data.NodeData;
import com.inswave.appplatform.data.Parse;
import com.inswave.appplatform.data.SimpleData;
import com.inswave.appplatform.log.ConstantsLog;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.transaver.elasticsearch.dao.*;
import com.inswave.appplatform.transaver.elasticsearch.domain.*;
import com.inswave.appplatform.transaver.saver.ElasticsearchService;
import com.inswave.appplatform.util.DateUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.*;

public class LogDataDaoService implements ElasticsearchService {

    @Override
    public IData select(IData reqIData, IData resIData) {

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
                Page<Document2> pageObjectLogs = null;
                if((fromDate != null) && (!fromDate.equals("")) || (toDate != null) && (!toDate.equals(""))) {
                    Date fDate = DateUtil.getDate(Constants.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z,fromDate);
                    Date tDate = DateUtil.getDate(Constants.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z,toDate);
//                    pageObjectLogs = getPageDocument(documentName, elasticsearchRepository, fromDate, toDate, isSetNullNestedData, ip, pageable);
                    pageObjectLogs = getPageDocument(documentName, elasticsearchRepository, fDate, tDate, isSetNullNestedData, ip, pageable);
                } else {
                    pageObjectLogs = getPageDocument(elasticsearchRepository, pageable);
                }

                int totalPage = pageObjectLogs.getTotalPages();
                long totalElements = pageObjectLogs.getTotalElements();

                jSONObject.put("page", new SimpleData(page));
                jSONObject.put("size", new SimpleData(pageObjectLogs.getContent().size()));
                jSONObject.put("totalPage", new SimpleData(totalPage));
                jSONObject.put("totalSize", new SimpleData(totalElements));

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

    private Page<Document2> getPageDocument(ElasticsearchRepository elasticsearchRepository, Pageable pageable) {
        return elasticsearchRepository.findAll(pageable);
    }

    private Page<Document2> getPageDocument(String documentName, ElasticsearchRepository elasticsearchRepository, Date fromDate, Date toDate, Boolean isSetNullNestedData, String ip, Pageable pageable) {

        Page<Document2> documents = null;
        StringBuffer dailyIndexNames = new StringBuffer("");
        try {
            ArrayList<String> indexes = new ArrayList<>();
//            ElasticsearchHighLevelClient elasticsearchHighLevelClient = new ElasticsearchHighLevelClient("192.168.79.100",9200);
//            elasticsearchHighLevelClient.openHighLevelClient();
//            indexes = elasticsearchHighLevelClient.getIndexNames(documentName.toLowerCase(),fromDate,toDate);
//            elasticsearchHighLevelClient.closeRestHighLevelClient();
            for(String str : indexes)
                dailyIndexNames.append(str+",");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if( documentName.equals(ServerResourceLog.class.getSimpleName()) ) {
            DynamicIndexBean.setIndexName(dailyIndexNames.toString());
            ServerResourceLogRepository serverResourceLogRepository = (ServerResourceLogRepository) elasticsearchRepository;
            documents = serverResourceLogRepository.findByTimeCreatedGreaterThanAndTimeCreatedLessThan(fromDate, toDate, pageable);
        } else if( documentName.equals(GeneratorLog.class.getSimpleName()) ) {
            GenratorLogRepository genratorLogRepository = (GenratorLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(GeneratorLog.class.getSimpleName());
            genratorLogRepository.setIndexName(GeneratorLog.class.getSimpleName().toLowerCase());
            DynamicIndexBean.setIndexName(GeneratorLog.class.getSimpleName().toLowerCase());
//            documents = genratorLogRepository.findByCurrentDateGreaterThanAndCurrentDateLessThan(fromDate, toDate, pageable);
        }
//        else if( documentName.equals(ClientPerformanceResourceLog.class.getSimpleName()) ) {
//            ClientPerformanceResourceLogRepository clientPerformanceResourceLogRepository = (ClientPerformanceResourceLogRepository) elasticsearchRepository;
//            if(ip.equals(""))
//                documents = clientPerformanceResourceLogRepository.findByTimeCurrentGreaterThanAndTimeCurrentLessThan(fromDate, toDate, pageable);
//            else
//                documents = clientPerformanceResourceLogRepository.findByTimeCurrentGreaterThanAndTimeCurrentLessThanAndIp(fromDate, toDate, ip, pageable);
//        } else if( documentName.equals(ClientProcessResourceLog.class.getSimpleName()) ) {
//            ClientProcessResourceLogRepository clientProcessResourceLogRepository = (ClientProcessResourceLogRepository) elasticsearchRepository;
//            if(ip.equals(""))
//                documents = clientProcessResourceLogRepository.findByTimeCurrentGreaterThanAndTimeCurrentLessThan(fromDate, toDate, pageable);
//            else
//                documents = clientProcessResourceLogRepository.findByTimeCurrentGreaterThanAndTimeCurrentLessThanAndIp(fromDate, toDate, ip, pageable);
//        } else if( documentName.equals(WindowsEventSystemErrorAllLog.class.getSimpleName()) ) {
//            WindowsEventSystemErrorAllLogRepository windowsEventSystemErrorAllLogRepository = (WindowsEventSystemErrorAllLogRepository) elasticsearchRepository;
//            if(ip.equals(""))
//                documents = windowsEventSystemErrorAllLogRepository.findByTimeCurrentGreaterThanAndTimeCurrentLessThan(fromDate, toDate, pageable);
//            else
//                documents = windowsEventSystemErrorAllLogRepository.findByTimeCurrentGreaterThanAndTimeCurrentLessThanAndIp(fromDate, toDate, ip, pageable);
////            if(isSetNullNestedData)
////                documents.forEach(document->{
////                    WindowsEventSystemErrorAllLog windowsEventSystemErrorAllLog = (WindowsEventSystemErrorAllLog) document;
////                    windowsEventSystemErrorAllLog.setCollectWindowsEventLogData(null);
////                });
//        }
        return documents;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
        return null;
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
