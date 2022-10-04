package com.inswave.appplatform.log.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.data.NodeData;
import com.inswave.appplatform.data.Parse;
import com.inswave.appplatform.data.SimpleData;
import com.inswave.appplatform.transaver.elasticsearch.dao.ElasticsearchDaoPackage;
import com.inswave.appplatform.transaver.elasticsearch.dao.ElasticsearchService;
import com.inswave.appplatform.transaver.elasticsearch.domain.Document;
import com.inswave.appplatform.transaver.elasticsearch.domain.RuleAlertExclusion;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class StandardService implements ElasticsearchService {

    @Override
    public IData select(IData reqIData, IData resIData) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String id = "";
        try {
            id = (String)reqIData.getBodyValue("id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String documentName = (String)reqIData.getHeaderValue(Constants.TAG_HEADER_DESTINATION);
        ElasticsearchRepository elasticsearchRepository = (ElasticsearchRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(documentName);

        JSONObject jSONObject = new JSONObject();
        if(elasticsearchRepository==null) {

            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_REPOSITORY_NOT_FOUND));

        } else {

            try {
                // 해당 dodumentId로 조회
                if (id != null && !id.equals("")) {

                    Optional document = elasticsearchRepository.findById(id);
                    Object object = document.get();
                    new Parse();
                    ObjectMapper mapper = new ObjectMapper();
                    String info = mapper.writeValueAsString(object);
                    JSONObject iJSONObject = (JSONObject) mapper.readValue(info, JSONObject.class);
                    jSONObject.put(documentName, iJSONObject);

                } else {

                    Integer page = (Integer) reqIData.getBodyValue("page");
                    Integer size = (Integer) reqIData.getBodyValue("size");
                    // 페이지 처리
                    if (size > 0) {
//                    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, new String[]{dateColumn});
//                Page<Document> pageObjectLogs = null;
//                if ((fromDate == null || fromDate.equals("")) && (toDate == null || toDate.equals(""))) {
//                    pageObjectLogs = this.getPageDocument(elasticsearchRepository, pageable);
//                } else {
//                    pageObjectLogs = this.getPageDocument(documentName, elasticsearchRepository, fromDate, toDate, isSetNullNestedData, ip, pageable);
//                }

//                List<Object> object = Lists.newArrayList(pageObjectLogs);
//                Parse parse = new Parse();
//                info = parse.toString(object);
//                ObjectMapper mapper = new ObjectMapper();
//                JSONArray jSONArray = (JSONArray)mapper.readValue(info, JSONArray.class);
//                jSONObject.put(documentName + "Rows", jSONArray);

                    // 전체 조회
                    } else {

                        Iterable<Document> documents = null;
                        try {
                            documents = elasticsearchRepository.findAll();
                        } catch (NoSuchIndexException e) {
                            e.printStackTrace();
                            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
                            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_RDB_QUERY_CODE));
                        }
                        if(documents==null) {
                            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_DATA_NOT_FOUND));
                        } else {
                            Long documentsSize = StreamSupport.stream(documents.spliterator(), false).count();
                            if (documentsSize <= 0) {
                                body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_DATA_NOT_FOUND));
                            } else {
                                List<Object> object = Lists.newArrayList(documents);
                                Parse parse = new Parse();
                                String info = parse.toString(object);
                                ObjectMapper mapper = new ObjectMapper();
                                JSONArray jSONArray = (JSONArray) mapper.readValue(info, JSONArray.class);
                                jSONObject.put(documentName + "Rows", jSONArray);
                            }
                        }
                    }
                }

            } catch (Exception ee) {
                ee.printStackTrace();
                body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
                body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_RDB_QUERY_CODE));
            }
        }

        body.setObject(jSONObject);
        resIData.put(Constants.TAG_BODY,body);
        return resIData;
    }

    public IData insert(IData reqIData, IData resIData) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String documentName = "RuleAlertExclusion";
        try {
            documentName = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_DESTINATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ElasticsearchRepository elasticsearchRepository = (ElasticsearchRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(documentName);

        // create index ?????????????????????

        JSONObject jSONObject = new JSONObject();
        if(elasticsearchRepository==null) {

            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_REPOSITORY_NOT_FOUND));

        } else {

            List<Document> insertDocuments = null;
            try {
                insertDocuments = (List<Document>) reqIData.getBodyValue(documentName + Constants.TAG_TABLE_ENTITY_ROWS);
            } catch(Exception e) {
                e.printStackTrace();
            }
            try {
                elasticsearchRepository.save(insertDocuments);
            } catch(Exception e) {
                e.printStackTrace();
                body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
                body.put(Constants.TAG_RESULT_MSG, new SimpleData(e.toString()));
            }

        }

        resIData.put(Constants.TAG_BODY,body);
        return resIData;
    }

    public IData update(IData reqIData, IData resIData) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String documentName = (String)reqIData.getHeaderValue(Constants.TAG_HEADER_DESTINATION);
        ElasticsearchRepository elasticsearchRepository = (ElasticsearchRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(documentName);

        if(elasticsearchRepository==null) {

            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_REPOSITORY_NOT_FOUND));

        } else {

            List<Document> insertDocuments = null;
            try {
                insertDocuments = (List<Document>) reqIData.getBodyValue(documentName + Constants.TAG_TABLE_ENTITY_ROWS);
            } catch(Exception e) {
                e.printStackTrace();
            }
            try {
                elasticsearchRepository.save(insertDocuments);
            } catch(Exception e) {
                e.printStackTrace();
                body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
                body.put(Constants.TAG_RESULT_MSG, new SimpleData(e.toString()));
            }

        }

        resIData.put(Constants.TAG_BODY,body);
        return resIData;
    }

    public IData delete(IData reqIData, IData resIData) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String id = "";
        try {
            id = (String)reqIData.getBodyValue("id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String documentName = (String)reqIData.getHeaderValue(Constants.TAG_HEADER_DESTINATION);
        ElasticsearchRepository elasticsearchRepository = (ElasticsearchRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(documentName);

        if(elasticsearchRepository==null) {

            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_REPOSITORY_NOT_FOUND));

        } else {

            if (id == null || id.equals("")) {
                body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_ID_NOT_FOUND));
            } else {
                try {
                    elasticsearchRepository.delete(id);
                } catch(Exception e) {
                    e.printStackTrace();
                    body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
                    body.put(Constants.TAG_RESULT_MSG, new SimpleData(e.toString()));
                }
            }
        }

        resIData.put(Constants.TAG_BODY,body);
        return resIData;
    }

    @Override
    public void insert(String indexName, String message) {

    }

    public static void main(String[] argv) {


        StandardService standardService = new StandardService();
        try {
            RuleAlertExclusion ruleAlertExclusion1 = new RuleAlertExclusion();
            ruleAlertExclusion1.setName("name1");
            ruleAlertExclusion1.setDescription("description1");
            ruleAlertExclusion1.setIp("111.111.111.111");
            ruleAlertExclusion1.setHostName("hostname1");
            ruleAlertExclusion1.setUserId("id1");

            RuleAlertExclusion ruleAlertExclusion2 = new RuleAlertExclusion();
            ruleAlertExclusion2.setName("name2");
            ruleAlertExclusion2.setDescription("description2");
            ruleAlertExclusion2.setIp("222.222.222.222");
            ruleAlertExclusion2.setHostName("hostname2");
            ruleAlertExclusion2.setUserId("id2");

            List<RuleAlertExclusion> ruleAlertExclusions = new ArrayList<>();
            ruleAlertExclusions.add(ruleAlertExclusion1);
            ruleAlertExclusions.add(ruleAlertExclusion2);

            Parse parse = new Parse();
            String info = parse.toString(ruleAlertExclusions);

            ObjectMapper mapper = new ObjectMapper();
            JSONArray jSONArray = mapper.readValue(info, JSONArray.class);
            JSONObject jsonObj1 = new JSONObject();
            jsonObj1.put("RuleAlertExclusionRows", jSONArray);


            IData reqData = new NodeData();

            HashMap header = new HashMap();
            header.put("destination", new SimpleData("RuleAlertExclusion"));
            header.put("service", new SimpleData("StandardService"));
            reqData.put("header", (IData) header);

            IData body = new NodeData();
            body.setObject(jsonObj1);
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
            body.put(Constants.TAG_ERROR, new SimpleData(""));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(""));
            reqData.put("body", body);

            standardService.insert(reqData,new NodeData());

        } catch(Exception e) {
            e.printStackTrace();
        }


    }

}
