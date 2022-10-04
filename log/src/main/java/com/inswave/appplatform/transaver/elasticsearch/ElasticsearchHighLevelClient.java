package com.inswave.appplatform.transaver.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.log.util.DateTimeConvertor;
import com.inswave.appplatform.transaver.ConstantsTranSaver;
import com.inswave.appplatform.transaver.elasticsearch.dao.DeviceRepository;
import com.inswave.appplatform.transaver.elasticsearch.dao.ElasticsearchDaoPackage;
import com.inswave.appplatform.transaver.elasticsearch.domain.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpHost;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.indices.PutIndexTemplateRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

@Service
public class ElasticsearchHighLevelClient {

    private static final Logger logger = LoggerFactory.getLogger(Class.class);

    @Autowired
    RestHighLevelClient restHighLevelClient;
    private static ElasticsearchRestTemplate elasticsearchRestTemplate;

    private String[] servers = {};

    private String ip;
    private int port;

    public static String[] indices = {};

    public ElasticsearchHighLevelClient() {
        servers = Config.getInstance().getLog().getElasticsearchDataServer();
        openHighLevelClient();
        initElasticsearchRestTemplate();
    }

    public ElasticsearchHighLevelClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        openHighLevelClient();
        initElasticsearchRestTemplate();
    }

    private RestHighLevelClient openHighLevelClient() {
        RestClientBuilder builder = RestClient.builder(getOpenHttpHost().toArray(new HttpHost[getOpenHttpHost().size()]));
        restHighLevelClient = new RestHighLevelClient(builder);
        return restHighLevelClient;
    }

    private void initElasticsearchRestTemplate() {
        elasticsearchRestTemplate = new ElasticsearchRestTemplate(restHighLevelClient);
    }

    private List<HttpHost> getOpenHttpHost() {
        List<HttpHost> hostList = new ArrayList<>();
        if(servers.length>0) {
            for (String server : servers) {
                String[] ipport = server.split(":");
                String tempIp = ipport[0];
                Integer tempPort = Integer.parseInt(ipport[1]);
                HttpHost httpHost = new HttpHost(tempIp, tempPort, "http");
                hostList.add(httpHost);
            }
        } else {
            hostList.add(new HttpHost(ip, port, "http"));
        }
        return hostList;
    }

//    public void createIndex(String indexName) {
//        try {
////            openHighLevelClient();
//            CreateIndexRequest request = new CreateIndexRequest(indexName);
//            request.settings(Settings.builder()
//                    .put(ConstantsTranSaver.KEY_OF_INDEX_NUMBER_OF_SHARDS, Config.getInstance().getLog().getIndexNumberOfShards())
//                    .put(ConstantsTranSaver.KEY_OF_INDEX_NUMBER_OF_REPLICAS, Config.getInstance().getLog().getIndexNumberOfReplicas())
//            );
//            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////        finally {
////            try {
////                restHighLevelClient.close();
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        }
//    }

//  ssl 통신 시 참조
//    private RestHighLevelClient initClient(List<HttpHost> httpHosts, boolean useSsl, ClusterMode mode, String token) {
//        RestHighLevelClient restHighLevelClient;
//        RestClientBuilder restClientBuilder = RestClient.builder(httpHosts.toArray(new HttpHost[0]))
//                .setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS)
//                .setRequestConfigCallback(builder -> builder.setConnectTimeout(50000)
//                        .setSocketTimeout(600000));
//        if (useSsl) {
//            restClientBuilder
//                    .setHttpClientConfigCallback(httpAsyncClientBuilder -> {
//                        httpAsyncClientBuilder.setSSLHostnameVerifier((s, sslSession) -> true);
//                        SSLContext sslContext;
//                        try {
//                            sslContext = SSLContext.getInstance("TLS");
//                            sslContext.init(null, TRUST_ALL_CERTS, new java.security.SecureRandom());
//                        } catch (NoSuchAlgorithmException | KeyManagementException e) {
//                            throw new RuntimeException(e.getMessage());
//                        }
//                        httpAsyncClientBuilder.setSSLContext(sslContext);
//                        String basicToken = Base64.getEncoder().encodeToString(token.getBytes());
//                        httpAsyncClientBuilder.setDefaultHeaders(Collections.singletonList(new BasicHeader("Authorization", "Basic " + basicToken)));
//                        return httpAsyncClientBuilder;
//                    });
//        }
//        if (ClusterMode.CLUSTER.equals(mode)) {
//            SniffOnFailureListener sniffOnFailureListener = new SniffOnFailureListener();
//            restClientBuilder.setFailureListener(sniffOnFailureListener);
//            restHighLevelClient = new RestHighLevelClient(restClientBuilder);
//            NodesSniffer nodesSniffer = new ElasticsearchNodesSniffer(restHighLevelClient.getLowLevelClient(),
//                    ElasticsearchNodesSniffer.DEFAULT_SNIFF_REQUEST_TIMEOUT,
//                    useSsl ? ElasticsearchNodesSniffer.Scheme.HTTPS : ElasticsearchNodesSniffer.Scheme.HTTP);
//            Sniffer sniffer = Sniffer.builder(restHighLevelClient.getLowLevelClient()).setSniffIntervalMillis(5000).setNodesSniffer(nodesSniffer).build();
//            sniffOnFailureListener.setSniffer(sniffer);
//        } else {
//            restHighLevelClient = new RestHighLevelClient(restClientBuilder);
//        }
//        return restHighLevelClient;
//    }

    public void bulkInsertData(String index, String sendData) throws IOException {

        BulkProcessor bulkProcessor = getBulkProcessor(restHighLevelClient);

        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.source(sendData.toString(), XContentType.JSON);

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(indexRequest);

        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        bulkProcessor.add(indexRequest);
        bulkProcessor.flush();

    }

    /**
     * Create a bulkProcessor and initialize it
     * @param client
     * @return
     */
    public BulkProcessor getBulkProcessor(RestHighLevelClient client) {

        BulkProcessor bulkProcessor = null;
        try {

            BulkProcessor.Listener listener = new BulkProcessor.Listener() {
                @Override
                public void beforeBulk(long executionId, BulkRequest request) {
                    //System.out.println("Try to insert data number : " + request.numberOfActions());
                }
                @Override
                public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                    //System.out.println("************** Success insert data number : " + request.numberOfActions() + " , id: "
                    //        + executionId);
                }
                @Override
                public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                    //System.out.println("Bulk is unsuccess : " + failure + ", executionId: " + executionId);
                }
            };

            BiConsumer<BulkRequest, ActionListener<BulkResponse>> bulkConsumer = (request, bulkListener) -> client
                    .bulkAsync(request, RequestOptions.DEFAULT, bulkListener);

            //	bulkProcessor = BulkProcessor.builder(bulkConsumer, listener).build();
            BulkProcessor.Builder builder = BulkProcessor.builder(bulkConsumer, listener);
            builder.setBulkActions(5000);
            builder.setBulkSize(new ByteSizeValue(100L, ByteSizeUnit.MB));
            builder.setConcurrentRequests(10);
            builder.setFlushInterval(TimeValue.timeValueSeconds(100L));
            builder.setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3));
            // Note: I feel a bit pit here, the official website sample does not have this step, and the author did not pay attention to it because of carelessness. When I watched it during debugging, I found that the above properties set by the builder did not take effect.
            bulkProcessor = builder.build();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                bulkProcessor.awaitClose(100L, TimeUnit.SECONDS);
                client.close();
            } catch (Exception e1) {
                e.printStackTrace();
            }
        }
        return bulkProcessor;
    }

    public String[] getIndices() throws Exception {
        GetIndexRequest request = new GetIndexRequest().indices("*");
        GetIndexResponse response = null;
        response = restHighLevelClient.indices().get(request, RequestOptions.DEFAULT);
        indices = response.getIndices();
        return indices;
    }

    public synchronized boolean existIndices(String indicesName) {

        indicesName = indicesName.toLowerCase();

        for (int i = 0; indices != null && i < indices.length; i++)
            if (indicesName.equals(indices[i]))
                return true;

        boolean isExist = false;
        try {
            //GetIndexRequest request = new GetIndexRequest().indices(indicesName.toLowerCase());
            GetIndexRequest request = new GetIndexRequest().indices("*");
            GetIndexResponse response = null;
            response = restHighLevelClient.indices().get(request, RequestOptions.DEFAULT);
            indices = response.getIndices();
            for (int i = 0; indices != null && i < indices.length; i++) {
                if (indicesName.equals(indices[i])) {
                    isExist = true;
                    break;
                }
            }
        } catch (ElasticsearchStatusException e) { // not found 시 발생함.
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public synchronized void indexRemoveQuery(String targetIndex) {
        try{
            JavaMelodyMonitor.printInfo("ElasticsearchHighLevelClient","delete indice -->>"+targetIndex);
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(targetIndex);
            restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized BulkResponse writeDocument2s(List<Document2> document2s) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        document2s.forEach(document2 -> {
            IndexRequest indexRequest = new IndexRequest(document2.getIndexName());
            document2.setTimeRegistered(DateTimeConvertor.getTimeRegistered());
            indexRequest.source(Document2.getAsMap(document2));
            bulkRequest.add(indexRequest);
        });
        return restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }


    public BulkByScrollResponse updateDocument(String indexName, String _id, String field, Object value) throws IOException {

        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest();
        updateByQueryRequest.indices(indexName);

        updateByQueryRequest.setQuery(new TermQueryBuilder("_id", _id));
        if (value instanceof String)
            updateByQueryRequest.setScript(new Script(ScriptType.INLINE,"painless","ctx._source."+field+"='"+value+"'", Collections.emptyMap()));
        else
            updateByQueryRequest.setScript(new Script(ScriptType.INLINE,"painless","ctx._source."+field+"="+value.toString()+"", Collections.emptyMap()));

        return restHighLevelClient.updateByQuery(updateByQueryRequest,RequestOptions.DEFAULT);
    }

    public BulkByScrollResponse updateDocument(String indexName, String _id, String nestedField, String field, Object value) throws IOException {

        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest();
        updateByQueryRequest.indices(indexName);

        updateByQueryRequest.setQuery(new TermQueryBuilder("_id", _id));
        if (value instanceof String)
            updateByQueryRequest.setScript(new Script(ScriptType.INLINE,"painless","ctx._source.nested."+field+"='"+value+"'", Collections.emptyMap()));
        else
            updateByQueryRequest.setScript(new Script(ScriptType.INLINE,"painless","ctx._source.nested."+field+"="+value.toString()+"", Collections.emptyMap()));

        return restHighLevelClient.updateByQuery(updateByQueryRequest,RequestOptions.DEFAULT);
    }

    public BulkByScrollResponse updateDocument(String indexName, String _id, HashMap hashMap) throws IOException {

        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest();
        updateByQueryRequest.indices(indexName);
        updateByQueryRequest.setQuery(new TermQueryBuilder("_id", _id));

        StringBuilder script = new StringBuilder();
        for(Object key : hashMap.keySet()) {
            String appendValue = "";
            String field = (String) key;
            Object value = hashMap.get(key);
            if(!field.equals("documentId")) {
                if (value instanceof Number) {
                    appendValue = value.toString();
                } else if (value instanceof String) {
                    appendValue = "'" + value.toString() + "'";
                } else if (value instanceof List) {
//                appendValue = JsonUtils.toJson(value);
                } else {
                    appendValue = value.toString();
                }
            }
            script.append("ctx._source.").append(field).append("=").append(appendValue).append(";");
        }
        updateByQueryRequest.setScript(new Script(script.toString()));
        return restHighLevelClient.updateByQuery(updateByQueryRequest,RequestOptions.DEFAULT);
    }

//    public Response updateDocument(String index, String queryField, String queryValue) {
//
//        RestClient restClient = restHighLevelClient.getLowLevelClient();
//        Request request = new Request(
//                "POST",
//                "/" + index + "/_`Update_By_Query`");
//        request.addParameter("wait_for_completion", "false");
//        request.addParameter("scroll_size", "100");
//        request.addParameter("timeout", "3m");
//        request.addParameter("slices", "10");
//        String entity = "{\n" +
//                "  \"query\": {\n" +
//                "    \"match\": {\n" +
//                "      \""+ queryField+ "\": \"" + queryValue + "\"\n" +
//                "    }\n" +
//                "  }, \n" +
//                "  \"script\": {\n" +
//                "    \"source\": \"ctx._source." + targetField + "=params."+ targetField + "\"\n,"+
//                "    \"lang\": \"painless\",\n" +
//                "    \"params\": {\n" +
//                "      \""+ targetField + "\": \"" + targetValue + "\"\n" +
//                "    }\n" +
//                "  }\n" +
//                "}";
//        request.setJsonEntity(entity);
//        Response response = restClient.performRequest(request);
//        return response;
//    }

    public synchronized BulkByScrollResponse deleteDocument(String indexName, String fieldName, String value) throws IOException {
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest();
        deleteByQueryRequest.indices(indexName);
        deleteByQueryRequest.setQuery(new TermQueryBuilder(fieldName, value));
        BulkByScrollResponse bulkResponse = restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        return bulkResponse;
    }

    public synchronized void deleteCurrentIndex_before(String currentIndexname, String deviceId, String ip, String hostName) throws IOException {

        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest();
        deleteByQueryRequest.indices(currentIndexname);
        deleteByQueryRequest.setQuery(new TermQueryBuilder(Constants.TAG_HEADER_DEVICEID, deviceId));
        BulkByScrollResponse bulkByScrollResponse = restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);

        if(bulkByScrollResponse.getDeleted()<=0L) { // 없던 deviceId
            Map<String, Object> searchIpHostname = new HashMap<>();
            searchIpHostname.put(Constants.TAG_HEADER_IP,ip);
            searchIpHostname.put(Constants.TAG_HEADER_HOST_NAME,hostName);
            SearchResponse searchResponse = getDocument(currentIndexname.toLowerCase(),searchIpHostname); // 기존 내역 조회
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            if( searchHits.length>0 ) {
                Map<String, Device> deviceHashMap = new HashMap<>();
                boolean isCurrentIndexDelete = false;
                for (SearchHit searchHit : searchHits) {
                    Map<String, Object> sources = searchHit.getSourceAsMap();
                    Device device = new Device();
                    device.setDeviceId(deviceId);
                    device.setIp(ip);
                    device.setHostName(hostName);
                    device.setService(currentIndexname);
                    for(String fieldName : sources.keySet()) {
                        Object value = sources.get(fieldName);
                        if(fieldName.equals(Constants.TAG_HEADER_DEVICEID))
                            device.setBeforeDeviceId((String) value);
                        else if(fieldName.equals(Constants.TAG_HEADER_IP))
                            device.setBeforeIp((String)value);
                        else if(fieldName.equals(Constants.TAG_HEADER_HOST_NAME))
                            device.setBeforeHostName((String)value);
                        else if(fieldName.equals(Constants.TAG_HEADER_USER_ID))
                            device.setUserId((String)value);
                        else if(fieldName.equals(Constants.TAG_HEADER_TERM_NO))
                            device.setTermNo((String)value);
                        else if(fieldName.equals(Constants.TAG_HEADER_SSO_BR_NO))
                            device.setSsoBrNo((String)value);
                        else if(fieldName.equals(Constants.TAG_HEADER_BR_NO))
                            device.setBrNo((String)value);
                        else if(fieldName.equals(Constants.TAG_HEADER_DEPT_NAME))
                            device.setDeptName((String)value);
                        else if(fieldName.equals(Constants.TAG_HEADER_HWN_NO))
                            device.setHwnNo((String)value);
                        else if(fieldName.equals(Constants.TAG_HEADER_USER_NAME))
                            device.setUserName((String)value);
                        else if(fieldName.equals(Constants.TAG_HEADER_PHONE_NO))
                            device.setPhoneNo((String)value);
                    }
                    if(!isCurrentIndexDelete) {
                        DeleteByQueryRequest deleteByQueryRequest2 = new DeleteByQueryRequest();
                        deleteByQueryRequest2.indices(currentIndexname.toLowerCase());
                        deleteByQueryRequest2.setQuery(new TermsQueryBuilder(Constants.TAG_HEADER_DEVICEID, device.getDeviceId()));
                        BulkByScrollResponse bulkByScrollResponse2 = restHighLevelClient.deleteByQuery(deleteByQueryRequest2, RequestOptions.DEFAULT); // 기존 것 삭제
                        isCurrentIndexDelete = true;
                    }
                    deviceHashMap.put(device.getDeviceId(),device);
                }
                if(deviceHashMap.size()>0) {
                    DeviceRepository deviceRepository = (DeviceRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(Device.class.getSimpleName());
                    for (String key : deviceHashMap.keySet() ) {
                        Device  device = deviceHashMap.get(key);
                        DynamicIndexBean.setIndexName(Device.class.getSimpleName().toLowerCase());
                        device.setIndexName(Device.class.getSimpleName().toLowerCase());
                        device.setTimeRegistered(DateTimeConvertor.getTimeRegistered2());
                        deviceRepository.save(device);
                    }
                }
            }
        }
    }

    public synchronized void deleteCurrentIndex(String currentIndexname, Document document) throws IOException {

        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest();
        deleteByQueryRequest.indices(currentIndexname);
        deleteByQueryRequest.setQuery(new TermQueryBuilder(Constants.TAG_HEADER_DEVICEID, document.getDeviceId()));
        BulkByScrollResponse bulkByScrollResponse = restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);

        JavaMelodyMonitor.printInfo("deleteCurrentIndex", "step000 bulkByScrollResponse.getDeleted()-->>" + bulkByScrollResponse.getDeleted() + " currentIndexname-->>" + currentIndexname + " deviceId-->>" + document.getDeviceId() + " ip-->>" + document.getIp() + " hostName-->>" + document.getHostName());
        JavaMelodyMonitor.printInfo("deleteCurrentIndex", "step000 bulkByScrollResponse.getDeleted()-->>" + bulkByScrollResponse.getDeleted() + " currentIndexname-->>" + currentIndexname + " deviceId-->>" + document.getDeviceId() + " ip-->>" + document.getIp() + " hostName-->>" + document.getHostName());

        if (bulkByScrollResponse.getDeleted() == 0L) { // 신규 deviceId

            JavaMelodyMonitor.printInfo("deleteCurrentIndex", "step020 not exist {" + bulkByScrollResponse.getDeleted() + "} currentIndexname-->>" + currentIndexname + " deviceId-->>" + document.getDeviceId() + " ip-->>" + document.getIp() + " hostName-->>" + document.getHostName());

            Map<String, Object> searchIpHostname = new HashMap<>(); // ip hostName으로 기존 내역 조회
            searchIpHostname.put(Constants.TAG_HEADER_IP, ip);
            searchIpHostname.put(Constants.TAG_HEADER_HOST_NAME, document.getHostName());
            SearchResponse searchResponse = getDocument(currentIndexname.toLowerCase(), searchIpHostname); // 기존 내역 조회
            SearchHit[] ipAndHostNameSearchHits = searchResponse.getHits().getHits();

            Map<String, Device> deviceHashMap = new HashMap<>();
            JavaMelodyMonitor.printInfo("deleteCurrentIndex", "step021 ipAndHostNameSearchHits.length-->>" + ipAndHostNameSearchHits.length);
            if (ipAndHostNameSearchHits.length == 0) {
                JavaMelodyMonitor.printInfo("deleteCurrentIndex", "step022 first insert!"); // end
                Device device = new Device();
                device.setService(currentIndexname);
                device.setDeviceId(document.getDeviceId());
                device.setIp(document.getIp());
                device.setHostName(document.getHostName());
                device.setUserId(document.getUserId());
                device.setTermNo(document.getTermNo());
                device.setSsoBrNo(document.getSsoBrNo());
                device.setBrNo(document.getBrNo());
                device.setDeptName(document.getDeptName());
                device.setHwnNo(document.getHwnNo());
                device.setUserName(document.getUserName());
                device.setPhoneNo(document.getPhoneNo());
                device.setIsBeforeDeviceIdDeleted(false);
                deviceHashMap.put(device.getBeforeDeviceId(), device);
            } else {
                for (SearchHit ipHostNameSearchHit : ipAndHostNameSearchHits) {
                    JavaMelodyMonitor.printInfo("deleteCurrentIndex", "step023 new Device();");
                    Map<String, Object> ipHostnameSearchSources = ipHostNameSearchHit.getSourceAsMap();
                    Device device = new Device();
                    device.setService(currentIndexname);
                    device.setDeviceId(document.getDeviceId());
                    device.setIp(document.getIp());
                    device.setHostName(document.getHostName());
                    device.setUserId(document.getUserId());
                    device.setTermNo(document.getTermNo());
                    device.setSsoBrNo(document.getSsoBrNo());
                    device.setBrNo(document.getBrNo());
                    device.setDeptName(document.getDeptName());
                    device.setHwnNo(document.getHwnNo());
                    device.setUserName(document.getUserName());
                    device.setPhoneNo(document.getPhoneNo());
                    device.setIsBeforeDeviceIdDeleted(false);
                    device.setBeforeDeviceId( (String) ipHostnameSearchSources.get(Constants.TAG_HEADER_DEVICEID) );
                    device.setBeforeIp( (String) ipHostnameSearchSources.get(Constants.TAG_HEADER_IP) );
                    device.setBeforeHostName( (String) ipHostnameSearchSources.get(Constants.TAG_HEADER_HOST_NAME) );

                    JavaMelodyMonitor.printInfo("deleteCurrentIndex", "step024 device.getDeviceId()-->>" + device.getDeviceId());
                    JavaMelodyMonitor.printInfo("deleteCurrentIndex", "step025 device.getBeforeDeviceId()-->>" + device.getBeforeDeviceId());
                    JavaMelodyMonitor.printInfo("deleteCurrentIndex", "step026 device.getBeforeIp()-->>" + device.getBeforeIp());
                    JavaMelodyMonitor.printInfo("deleteCurrentIndex", "step027 device.getBeforeHostName()-->>" + device.getBeforeHostName());

                    deviceHashMap.put(device.getBeforeDeviceId(), device);
                    JavaMelodyMonitor.printInfo("deleteCurrentIndex", "step028 put device.getDeviceId()-->>" + device.getDeviceId());
                }
                JavaMelodyMonitor.printInfo("deleteCurrentIndex", "step029 deviceHashMap.size()-->>" + deviceHashMap.size());
            }

            DeviceRepository deviceRepository = (DeviceRepository) ElasticsearchDaoPackage.getInstance().getElasticsearchDao(Device.class.getSimpleName());
            for (String key : deviceHashMap.keySet()) {
                Device device = deviceHashMap.get(key);
                if(device.getDeviceId().equals(device.getBeforeDeviceId())) { //  같은 경우가 상당 수   무시..
                    ;
                } else { // 신규와 조회 후 다른 것만 처리.
                    DeleteByQueryRequest deleteByQueryRequest2 = new DeleteByQueryRequest();
                    deleteByQueryRequest2.indices(currentIndexname.toLowerCase());
                    deleteByQueryRequest2.setQuery(new TermsQueryBuilder(Constants.TAG_HEADER_DEVICEID, device.getBeforeDeviceId()));
                    BulkByScrollResponse bulkByScrollResponse2 = restHighLevelClient.deleteByQuery(deleteByQueryRequest2, RequestOptions.DEFAULT); // 기존 것 삭제
                    JavaMelodyMonitor.printInfo("deleteCurrentIndex", "step0291 delete {" + bulkByScrollResponse2.getDeleted() + "} currentIndexname-->>" + currentIndexname + " device.getDeviceId()-->>" + device.getDeviceId());
                    device.setIsBeforeDeviceIdDeleted(true);

                    DynamicIndexBean.setIndexName(Device.class.getSimpleName().toLowerCase());
                    device.setIndexName(Device.class.getSimpleName().toLowerCase());
                    device.setTimeRegistered(DateTimeConvertor.getTimeRegistered2());
                    deviceRepository.save(device);
                    JavaMelodyMonitor.printInfo("deleteCurrentIndex", "step0292 save device.getIndexName()-->>" + device.getIndexName() + " device.getDeviceId()-->>" + device.getDeviceId() + " device.getBeforeDeviceId()-->>" + device.getBeforeDeviceId());
                }
            }

        } else { // 존재하는 deviceId 이면 end
            JavaMelodyMonitor.printInfo("deleteCurrentIndex", "step010 exist true {" + bulkByScrollResponse.getDeleted() + "} currentIndexname-->>" + currentIndexname + " deviceId-->>" + document.getDeviceId() + " ip-->>" + document.getIp() + " hostName-->>" + document.getHostName());
        }
    }

    private SearchResponse getDocument(String indexName, Map<String, Object> matchKeyValue) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        for(String key : matchKeyValue.keySet()){
            Object object = matchKeyValue.get(key);
            MatchQueryBuilder matchQueryBuilder = null;
            if(object!=null) {
                if (object instanceof String) {
                    matchQueryBuilder = new MatchQueryBuilder(key, (String) object);
                } else if (object instanceof Boolean) {
                    matchQueryBuilder = new MatchQueryBuilder(key, (Boolean) object);
                } else if (object instanceof Long) {
                    matchQueryBuilder = new MatchQueryBuilder(key, (Long) object);
                } else if (object instanceof Double) {
                    matchQueryBuilder = new MatchQueryBuilder(key, (Double) object);
                } else if (object instanceof Integer) {
                    matchQueryBuilder = new MatchQueryBuilder(key, (Integer) object);
                } else if (object instanceof Float) {
                    matchQueryBuilder = new MatchQueryBuilder(key, (Float) object);
                } else {
                    if (object != null)
                        matchQueryBuilder = new MatchQueryBuilder(key, object);
                }
            }
            if(matchQueryBuilder!=null)
                boolQueryBuilder.should(matchQueryBuilder);
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        return restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
    }

    public SearchResponse getDocument(String indexName, String fieldName, String fieldValue) throws IOException {

        SearchRequest searchRequest = new SearchRequest(indexName);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(fieldName,fieldValue);
        searchSourceBuilder.query(matchQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        return restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
    }

    public SearchResponse getDocument(String indexName, String nestedFieldName, String fieldName, String fieldValue) throws IOException {

        SearchRequest searchRequest = new SearchRequest(indexName);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        NestedQueryBuilder nestedQueryBuilder = new NestedQueryBuilder(nestedFieldName, new MatchQueryBuilder(fieldName,fieldValue), ScoreMode.None);
        searchSourceBuilder.query(nestedQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        return restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
    }

    public AcknowledgedResponse createTemplate(String templateName, String indexName, Map<String, Object> properties, int shards, int replicas) throws IOException {

        if(shards<=0)
            shards = Config.getInstance().getLog().getIndexNumberOfShards();
        if(replicas<=0)
            replicas = Config.getInstance().getLog().getIndexNumberOfReplicas();

        List<String> indexPatterns = new ArrayList<>();
        PutIndexTemplateRequest request = new PutIndexTemplateRequest(templateName);
        indexPatterns.add(indexName);
        request.patterns(indexPatterns);
        request.settings(Settings.builder()
                .put(ConstantsTranSaver.TAG_INDEX_NUMBER_OF_SHARDS, shards)
                .put(ConstantsTranSaver.TAG_INDEX_NUMBER_OF_REPLICAS, replicas)
        );

        Map<String, Object> jsonMap = new HashMap<>(); {
            jsonMap.put(ConstantsTranSaver.TAG_PROPERTIES, properties.get(ConstantsTranSaver.TAG_PROPERTIES));
        }
        request.mapping(jsonMap);

//        request.alias(new Alias("twitter_alias").filter(QueryBuilders.termQuery("user", "kimchy")));
//        request.alias(new Alias("{index}_alias").searchRouting("xyz"));

//        request.order(20);
//        request.version(4);
//        request.create(true);
//        request.masterNodeTimeout(TimeValue.timeValueMinutes(1));
//        request.masterNodeTimeout("1m");

        ActionListener<AcknowledgedResponse> listener =
            new ActionListener<AcknowledgedResponse>() {
                @Override
                public void onResponse(AcknowledgedResponse acknowledgedResponse) {
                }

                @Override
                public void onFailure(Exception e) {
                }
        };

        return restHighLevelClient.indices().putTemplate(request, RequestOptions.DEFAULT);
    }

    public SearchResponse getDocuments(String index, String sortField, Object[] searchAfter, int size) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        Map<String, SortOrder> sorts = new HashMap<String, SortOrder>() {
            {
                put(sortField, SortOrder.ASC);
            }
        };
        SearchSourceBuilder builder = new SearchSourceBuilder();
        sorts.forEach((field, sort) -> {
            builder.sort(field, sort);
        });
        builder.size(size);
        builder.query(QueryBuilders.matchAllQuery());

        if (ArrayUtils.isNotEmpty(searchAfter))
            builder.searchAfter(searchAfter);

        searchRequest.source(builder);
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    public static void main(String[] argv) {

        ElasticsearchHighLevelClient elasticsearchHighLevelClient = new ElasticsearchHighLevelClient("192.168.79.100", 9202);
//        ElasticsearchHighLevelClient elasticsearchHighLevelClient = new ElasticsearchHighLevelClient("3.38.208.73", 9202);

        SearchResponse searchResponse = null;
        String sortField = "deviceId.keyword";
        int count=0;
        try {
            searchResponse = elasticsearchHighLevelClient.getDocuments("generatorlog", sortField, null,10);
            SearchHits searchHits = searchResponse.getHits();
            SearchHit[] hits = searchHits.getHits();

            if (hits.length == 0) {
            } else {
                for (SearchHit searchHit : hits) {
                    count++;
                    ObjectMapper objectMapper = new ObjectMapper();
                    GeneratorLog generatorLog =  objectMapper.readValue(searchHit.getSourceAsString(),GeneratorLog.class);
System.out.println(generatorLog.getIndexName()+ " count-->>" + count + " id-->>"+searchHit.getId() + " generatorLog.getDeviceId()-->>"+generatorLog.getDeviceId());
                }
                SearchHit lastHitDocument = hits[hits.length - 1];
                Object[] sortValues = lastHitDocument.getSortValues();
                searchResponse = elasticsearchHighLevelClient.getDocuments("generatorlog", sortField, sortValues,10);
System.out.println();
            }

System.out.println();
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    public static void main_(String[] argv) {

//        ElasticsearchHighLevelClient elasticsearchHighLevelClient = new ElasticsearchHighLevelClient("52.79.253.187", 9201);
////        String indexName="serverresourcelogdaily_20220617";
//        String indexName = "integritylogdaily_20220623";
//        String newDeviceIdFieldName="_id";
//        String newDeviceId="2aQwb4EBvjefz3dmD-eV\n";
////        String value="3aQwb4EBvjefz3dmH-fC";
//        String ip="192.168.42.116",hostName="INSWAVE-COLLIN";
//        try {
//            BulkByScrollResponse bulkByScrollResponse = elasticsearchHighLevelClient.deleteDocument(indexName, newDeviceIdFieldName, newDeviceId);
//            if(bulkByScrollResponse.getDeleted()<=0L) {
//                Map<String, Object> fieldNameValue = new HashMap<>();
//                fieldNameValue.put("ip",ip);
//                fieldNameValue.put("hostName",hostName);
//                SearchResponse searchResponse = null;//elasticsearchHighLevelClient.getDocument(indexName,fieldNameValue);
//                SearchHit[] searchHits = searchResponse.getHits().getHits();
//                if( searchHits.length>0 ) {
//
//                    Map<String,Device> deviceHashSet = new HashMap<>();
//                    Device device = new Device();
//
//                    ; // ip, hostname 으로 delete and insert
//                    for (SearchHit searchHit : searchHits) {
////                        String source = searchHit.getSourceAsString();
////System.out.println("source-->>" + source);
//                        String documentId = searchHit.getId();
//System.out.println("documentId-->>" + documentId);
//
//                        String deviceId = "";
//                        Map<String, Object> sources = searchHit.getSourceAsMap();
//                        for(String fieldName : sources.keySet()) {
//                            Object value = sources.get(fieldName);
//                            if(fieldName.equals(Constants.TAG_HEADER_DEVICEID)) {
//                                device.setDeviceId((String) value);
//                                elasticsearchHighLevelClient.deleteDocument(indexName,"deviceId",device.getDeviceId());
//                            }
//                            device.setNewDeviceId(newDeviceId);
//                            deviceHashSet.put(newDeviceId,device);
//                        }
//                    }
//                } else {
//                    // next
//                }
//            } else {
//                // next
//            }
//
//            // current insert
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        }


    }


}
