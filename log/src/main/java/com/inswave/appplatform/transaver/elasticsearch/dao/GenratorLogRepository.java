package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.transaver.elasticsearch.ElasticsearchHighLevelClient;

import com.inswave.appplatform.transaver.elasticsearch.domain.GeneratorLog;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.*;
import java.util.stream.Collectors;

public interface GenratorLogRepository extends ElasticsearchRepository<GeneratorLog, String>,Document2Repository<GeneratorLog> {

    List<GeneratorLog> findAll();
    List<GeneratorLog> findByDeviceId(String deviceId);
    List<GeneratorLog> findByDeviceIdAndCurrentHHAndCurrentMm(String deviceId, int currentHH, int currentMm);

    /**
     // GET generatorlog/_search
     //{
     //  "aggs": {
     //    "aggsTermsDeviceId": {
     //      "terms": {
     //        "field": "deviceId.keyword",
     //        "size": maxSIze
     //      }
     //    }
     //  }
     //}
     * @return
     */
    default List<GeneratorLog> findAggsTermsDeviceId(int maxSize){
        List<GeneratorLog> ret = new ArrayList<>();

        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("aggsTermsDeviceId").field("deviceId.keyword").size(maxSize);
        termsAggregationBuilder.order();

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.addAggregation(termsAggregationBuilder);
//        nativeSearchQueryBuilder.withPageable(PageRequest.of(page, maxSize));

        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
        ElasticsearchOperations elasticsearchOperations = getElasticsearchOperations();
        SearchHits<GeneratorLog> hits = elasticsearchOperations.search(nativeSearchQuery, GeneratorLog.class);

//        for (SearchHit<GeneratorLog> searchHit : hits.getSearchHits())
//            ret.add(searchHit.getContent());

        ParsedStringTerms parsedStringTerms = hits.getAggregations().get("aggsTermsDeviceId");
        for (Terms.Bucket bucket : parsedStringTerms.getBuckets()) {
            GeneratorLog generatorLog = new GeneratorLog();
            generatorLog.setDeviceId(bucket.getKeyAsString()); // bucket.getDocCount()
            ret.add(generatorLog);
        }

        return ret;
    };


    default List<GeneratorLog> findPageble(int page, int size){

        List<GeneratorLog> ret = new ArrayList<>();














//        PageRequest pageable = PageRequest.of(page, size);
////        ElasticsearchOperations elasticsearchRestTemplate = getElasticsearchOperations();
//        ;
//
//        BoolQueryBuilder aggregateQueryBuilder = QueryBuilders.boolQuery( );
//        // ... when the code to add some conditions to the aggregateQueryBuilder is written here
//
//        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder( );
//        NativeSearchQueryBuilder pagingSearchQueryBuilder = new NativeSearchQueryBuilder( );
//
//        if ( aggregateQueryBuilder.hasClauses( ) ) {
//            searchQueryBuilder.withQuery( aggregateQueryBuilder );
//            pagingSearchQueryBuilder.withQuery( aggregateQueryBuilder );
//        }
//
//        // paging needs
//        SearchScrollHits< GeneratorLog > pagingSearchScrollHits = elasticsearchRestTemplate.searchScrollStart(
//                1,
////                pagingSearchQueryBuilder.withMaxResults( 1 ).build( ),
//                pagingSearchQueryBuilder.build(),
//                GeneratorLog.class,
//                IndexCoordinates.of( GeneratorLog.class.getAnnotation( Document.class ).indexName( ) ) );
//        SearchPage< GeneratorLog > searchPage = SearchHitSupport.searchPageFor( pagingSearchScrollHits, pageable );
//
//        // Actual search results
//        SearchHits< GeneratorLog > searchHits = elasticsearchRestTemplate.search( searchQueryBuilder
//                        .withPageable( pageable ).build( ),
//                GeneratorLog.class );
//
////        Collection< ServiceLogRepresentation > logs = searchHits.getSearchHits( ).stream( ).map( SearchHit::getContent )
////                .map( ServiceLog::representation ).collect( Collectors.toList( ) );
////
////        // PagedRepresentations.of( int page, int pages, long total, Collection< T > representations );
////        return PagedRepresentations.of( searchPage.getNumber( ) + 1, searchPage.getTotalPages( ),
////                pagingSearchScrollHits.getTotalHits( ), logs );



//        Elasticsearch-data 4.4
//        List<Object> searchAfter = null;
//        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//        nativeSearchQueryBuilder.withSorts(SortBuilders.fieldSort("deviceId").order(SortOrder.ASC));
//        nativeSearchQueryBuilder.withPageable(PageRequest.of(page, size));
//        if(searchAfter!=null)
//            nativeSearchQueryBuilder.withSearchAfter(searchAfter);
//
//        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
//        ElasticsearchOperations elasticsearchOperations = getElasticsearchOperations();
//        SearchHits<GeneratorLog> hits = elasticsearchOperations.search(nativeSearchQuery, GeneratorLog.class);
//
//        for (SearchHit<GeneratorLog> searchHit : hits.getSearchHits())
//            ret.add(searchHit.getContent());
//
//        SearchHit lastHitDocument = hits.getSearchHits().get(hits.getSearchHits().size()-1);
//        searchAfter = lastHitDocument.getSortValues();
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


//        SearchRequest searchRequest = new SearchRequest(GeneratorLog.class.getSimpleName().toLowerCase());
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.size(20000);
//        sourceBuilder.from(0);
//
//        QueryBuilder scoreQuery = QueryBuilders.queryStringQuery("text:엘라스틱서치").boost(5);
//        QueryBuilder filterQuery = QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery("date", today));
//        sourceBuilder.query(QueryBuilders.boolQuery().must(scoreQuery).filter(filterQuery));
//
//        searchRequest.source(sourceBuilder);
//        SearchResponse result = client.search(searchRequest);
//        System.out.println(result.toString());
//
//        ElasticsearchOperations elasticsearchOperations = getElasticsearchOperations();
//        SearchHits<GeneratorLog> hits = elasticsearchOperations.search(nativeSearchQuery, GeneratorLog.class);
//        elasticsearchOperations.indexOps()

//        QueryBuilder qb = getDailyQueryBuilder(statDate);
//        String[] includeFields = new String[]{"@timestamp", "usn", "j.room"};
//        Object[] searchAfter = null;
//        while (true) {
//            SearchSourceBuilder searchSourceBuilder =
//                    new SearchSourceBuilder()
//                            .size(1000)
//                            .query(qb)
//                            .sort(SortBuilders.fieldSort("@timestamp")) //defautl ASC
//                            .sort(SortBuilders.fieldSort("usn"))
//                            .fetchSource(includeFields, null)
//                            .timeout(new TimeValue(60, TimeUnit.SECONDS));
//            if (searchAfter != null) {
//                // 바로 직전 쿼리의 마지막 Docuemnt의 sort_value를 셋팅한다.
//                // 동일한 쿼리에 'sort_value' 의 조건식이 더 추가된다.
//                searchSourceBuilder.searchAfter(searchAfter);
//            }
//
//            SearchRequest searchRequest =
//                    new SearchRequest()
//                            .indices(ElasticConstant.Indices.INDEX_NAME)
//                            .source(searchSourceBuilder);
//
//            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//            SearchHit[] hits = searchResponse.getHits().getHits();
//
//            if (hits.length > 0) {
//                // foreach 각 문서 처리
//                SearchHit lastHitDocument = hits[hits.length - 1];
//                searchAfter = lastHitDocument.getSortValues();
//            } else {
//                return ret;
//            }
//        }


//        Object[] searchAfter = null;
//
//        while (true) {
//            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
//                    .query(queryBuilder)
//                    .from(pageNumber * pageSize)
//                    .size(pageSize)
//                    .sort(SortBuilders.fieldSort("@timestamp").order(SortOrder.ASC))
//                    .trackTotalHits(true)
//                    .pointInTimeBuilder(new PointInTimeBuilder("storyparks-examples"))
//                    .timeout(new TimeValue(searchSourceBuilderTimeout, TimeUnit.SECONDS));
//
//            if (searchAfter != null) {
//                searchSourceBuilder.searchAfter(searchAfter);
//            }
//
//            SearchRequest searchRequest = new SearchRequest(indexPattern);
//            searchRequest.source(searchSourceBuilder);
//
//            SearchResponse searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//            SearchHits[] hits = searchResponse.getHits().getHits();
//
//            if (hits.length > 0) {
//                SearchHit lastHitDocument = hits[hits.length - 1];
//                searchAfter = lastHitDocument.getSortValues();
//            } else {
//                break;
//            }
//        }

//        QueryBuilder qb = getDailyQueryBuilder(statDate);
//        String[] includeFields = new String[]{"@timestamp", "usn", "j.room"};
//        Object[] searchAfter = null;
//        while (true) {
//            SearchSourceBuilder searchSourceBuilder =
//                    new SearchSourceBuilder()
//                            .size(1000)
//                            .query(qb)
//                            .sort(SortBuilders.fieldSort("@timestamp")) //defautl ASC
//                            .sort(SortBuilders.fieldSort("usn"))
//                            .fetchSource(includeFields, null)
//                            .timeout(new TimeValue(60, TimeUnit.SECONDS));
//            if (searchAfter != null) {
//                // 바로 직전 쿼리의 마지막 Docuemnt의 sort_value를 셋팅한다.
//                // 동일한 쿼리에 'sort_value' 의 조건식이 더 추가된다.
//                searchSourceBuilder.searchAfter(searchAfter);
//            }
//
//            SearchRequest searchRequest =
//                    new SearchRequest()
//                            .indices(ElasticConstant.Indices.INDEX_NAME)
//                            .source(searchSourceBuilder);
//
//            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//            SearchHit[] hits = searchResponse.getHits().getHits();
//
//            if (hits.length > 0) {
//                // foreach 각 문서 처리
//                SearchHit lastHitDocument = hits[hits.length - 1];
//                searchAfter = lastHitDocument.getSortValues();
//            } else {
//                return ret;
//            }
//        }


//        // 10,000건 이상 조회를 위해 searchAfter를 적용합니다.
//        Object[] searchAfter = null;
//        String SEARCH_FIELD_NAME = "Name";
//        String RESULT_FIELD_NAME = "ID";
//
//        // Like 검색을 위한 queryString
//        String QUERY_KEYWORD = "*" + Name + "*";
//
//        List<Long> result = new ArrayList<>();
//
//        while(true) {
//            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
//                    .size(1000)
//                    .query(QueryBuilders.queryStringQuery(QUERY_KEYWORD).field(SEARCH_FIELD_NAME))
//                    .sort(SortBuilders.fieldSort(RESULT_FIELD_NAME)) // default ASC
//                    .fetchSource(RESULT_FIELD_NAME, null)
//                    .timeout(new TimeValue(60, TimeUnit.SECONDS));
//
//            // searchAfter: 다음 페이지의 검색에 이전 페이지의 결과(sort values)를 사용합니다.
//            if(searchAfter != null)
//                sourceBuilder.searchAfter(searchAfter);
//
//            SearchRequest searchRequest = new SearchRequest("es_test").source(sourceBuilder);
//            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//
//            // result
//            org.elasticsearch.search.SearchHit[] hits = searchResponse.getHits().getHits();
//
//            for(org.elasticsearch.search.SearchHit hit : hits) {
//                Map<String, Object> map = hit.getSourceAsMap();
//
//                Number id = (Number)map.get("ID");
//                if(id != null) result.add(id.longValue());
//            }
//
//            if(hits.length > 0) {
//                // searchAfter: 마지막 Document의 sort values를 설정합니다.
//                SearchHit lastHitDocument = hits[hits.length - 1];
//                searchAfter = lastHitDocument.getSortValues();
//            } else {
//                // 검색이 없을 경우 EXIT
//                break;
//            }
//        }




//        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//        nativeSearchQueryBuilder.withPageable(PageRequest.of(page, size));
//        nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("deviceId.keyword").order(SortOrder.ASC));
//
//        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
//        ElasticsearchOperations elasticsearchOperations = getElasticsearchOperations();
//        SearchHits<GeneratorLog> hits = elasticsearchOperations.search(nativeSearchQuery, GeneratorLog.class);
//
//        for (SearchHit<GeneratorLog> searchHit : hits.getSearchHits())
//            ret.add(searchHit.getContent());


        return ret;
    };

    @org.springframework.data.elasticsearch.annotations.Query("{\"bool\": {\"must\": [{\"match\": {\"userName\": \"?0\"}}]}}")
    Page<GeneratorLog> findByUserNameNameUsingCustomQuery(String userName, Pageable pageable);

//    Page<Document2> findByCurrentDateGreaterThanAndCurrentDateLessThan(Date from, Date to, Pageable pageable); // x
//    Page<Document2> findByTimeRegisteredGreaterThanAndTimeRegisteredLessThan(Date from, Date to, Pageable pageable); // x

//    @Query("{\"aggs\":{\"group_by_deviceId\":{\"terms\":{\"field\":\"deviceId.keyword\",\"size\":30000}}}}")
//    Object groupByDeviceId(); // x
//@Query("{\"match\":{\"name\":\"?0\"}}")
//List<GeneratorLog> findAllByNameUsingAnnotations(String name);
}