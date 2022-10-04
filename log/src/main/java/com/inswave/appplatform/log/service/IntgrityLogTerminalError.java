package com.inswave.appplatform.log.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.data.NodeData;
import com.inswave.appplatform.data.SimpleData;
import com.inswave.appplatform.transaver.elasticsearch.ElasticsearchHighLevelClient;
import com.inswave.appplatform.transaver.elasticsearch.dao.ElasticsearchDaoPackage;
import com.inswave.appplatform.transaver.elasticsearch.dao.IntegrityLogRepository;
import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import com.inswave.appplatform.transaver.elasticsearch.domain.IntegrityLog;
import com.inswave.appplatform.transaver.saver.ElasticsearchService;
import com.inswave.appplatform.transaver.util.BeanUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class IntgrityLogTerminalError implements ElasticsearchService {

    @Override
    public IData select(IData reqIData, IData resIData) {
        return null;
    }

    @Override
    public IData insert(IData reqIData, IData resIData) {
        return null;
    }

    @Override
    public IData update(IData reqIData, IData resIData) {
        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String netclient5 = (String)reqIData.getBodyValue("NetClient5");
        String NetClient5 = "*abcd1234*";

        String indexName="integritylog";
        String _id="";
        String field="integrity.info.NetClient5";
        int result=1;

        String selectNestedField = "integrity.info";
        String selectField = "integrity.info.NetClient5";
        try {
            ElasticsearchHighLevelClient elasticsearchHighLevelClient = BeanUtils.getBean(ElasticsearchHighLevelClient.class);
//            elasticsearchHighLevelClient.updateDocument(indexName, _id, field, result);

            try {
                SearchResponse searchResponse = elasticsearchHighLevelClient.getDocument(indexName,selectNestedField,selectField,NetClient5);
                SearchHit[] searchHits = searchResponse.getHits().getHits();
                if( searchHits.length<=0 ) {
System.out.println("not found!  NetClient5-->>" + NetClient5);
                } else {
                    String updateDocumentId = "";//""uuyBtoABAWqYaq1xFVRd";
                    for (SearchHit hit : searchHits) {
                        String source = hit.getSourceAsString();
System.out.println("source-->>" + source);

//                        Parse parse = new Parse();
//                        JSONObject jSONObject = parse.getJSONObject(new StringBuffer(source));
//                        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
//                        IntegrityLog integrityLog = objectMapper.convertValue(jSONObject, IntegrityLog.class);

                        updateDocumentId = hit.getId();
//                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                        System.out.println("updateDocumentId-->>" + updateDocumentId);
                    }

                    IntegrityLogRepository integrityLogRepository = (IntegrityLogRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao("IntegrityLog");
                    IntegrityLog integrityLog = integrityLogRepository.findByDeviceId(updateDocumentId);
                    if(integrityLog==null) {
System.out.println();
                    } else {
System.out.println();
                    }

//                HashMap<String,Object> hashMap = new HashMap();
//                hashMap.put("result",sucessValue);
////                hashMap.put("integrity.result",sucessValue); // 안먹힘.
////                hashMap.put("integrity.integrityData.result",sucessValue); // 안먹힘.
//
//                elasticsearchHighLevelClient.updateDocument(indexName,updateDocumentId,hashMap);


//                    int sucessValue = 2000;
//                    String updateField = "result"; // integrity.result // integrity.integrityData.result
//                    elasticsearchHighLevelClient.updateDocument(indexName, updateDocumentId, updateField, sucessValue);
//                    elasticsearchHighLevelClient.updateDocument(indexName, updateDocumentId, "","integrity[0].result", sucessValue); // 안먹힘.
//                    elasticsearchHighLevelClient.updateDocument(indexName, updateDocumentId, "","integrity[0].integrityData.result", sucessValue); // 안먹힘.


                }
            } catch (ElasticsearchException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



        } catch(Exception e) {
            e.printStackTrace();
        }


        body.put("NetClient5",new SimpleData(netclient5));

        resIData.put(Constants.TAG_BODY, body);
        return resIData;
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
