package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.transaver.ConstantsTranSaver;
import org.elasticsearch.action.index.IndexRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.concurrent.ConcurrentHashMap;

public class DocumentRepositoryImpl implements DocumentRepository<com.inswave.appplatform.transaver.elasticsearch.domain.Document> {

    private final ElasticsearchOperations elasticsearchOperations;
    private final ConcurrentHashMap<String, IndexCoordinates> knownIndexCoordinates = new ConcurrentHashMap<>();

    @Nullable
    private Document document;


    @SuppressWarnings("unused")
    public DocumentRepositoryImpl(ElasticsearchOperations operations) {
        this.elasticsearchOperations = operations;
    }

    @Override
    public <S extends com.inswave.appplatform.transaver.elasticsearch.domain.Document> S save2(S document) {
        IndexCoordinates indexCoordinates = getIndexCoordinates(document);
        S saved = elasticsearchOperations.save(document, indexCoordinates);
        elasticsearchOperations.indexOps(indexCoordinates).refresh();
        return saved;
    }

    @Override
    public Document getDocument() {
        return this.document;
    }

    @Override
    public ElasticsearchOperations getElasticsearchOperations() {
        return this.elasticsearchOperations;
    }

    @NonNull
    private <S extends com.inswave.appplatform.transaver.elasticsearch.domain.Document> IndexCoordinates getIndexCoordinates(S domainDocument) {
        return knownIndexCoordinates.computeIfAbsent(domainDocument.getIndexName(), i -> {
                IndexCoordinates indexCoordinates = IndexCoordinates.of(i);
                IndexOperations indexOps = elasticsearchOperations.indexOps(indexCoordinates);
                if (!indexOps.exists()) {
                    //indexOps.create();
                    Document settings = Document.create();
                    settings.put(ConstantsTranSaver.KEY_OF_INDEX_NUMBER_OF_REPLICAS, Config.getInstance().getLog().getIndexNumberOfReplicas());
                    settings.put(ConstantsTranSaver.KEY_OF_INDEX_NUMBER_OF_SHARDS, Config.getInstance().getLog().getIndexNumberOfShards());
                    indexOps.create(settings);
                    if (this.document == null) {
                        this.document = indexOps.createMapping(com.inswave.appplatform.transaver.elasticsearch.domain.Document.class);
                    }
                    indexOps.putMapping(this.document);
                }
                return indexCoordinates;
            }
        );
    }

        @Override
    public void setIndexName(String indexName) {
        Object obj = knownIndexCoordinates.get(indexName);
        IndexRequest indexRequest = new IndexRequest(indexName);
    }

    public IndexCoordinates getIndexCoordinates(Class clazz) {
        String indexName = clazz.getSimpleName().toLowerCase();
        final IndexOperations indexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of(indexName));
        if (!indexOperations.exists()) {
            indexOperations.create();
            indexOperations.createMapping(clazz);
        }
        return IndexCoordinates.of(indexName);
    }

}