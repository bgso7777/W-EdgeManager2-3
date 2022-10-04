package com.inswave.appplatform.transaver.elasticsearch.dao;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.transaver.ConstantsTranSaver;
import com.inswave.appplatform.transaver.elasticsearch.domain.*;
import org.elasticsearch.action.index.IndexRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.concurrent.ConcurrentHashMap;

public class Document2RepositoryImpl implements Document2Repository<Document2> {

    private final ElasticsearchOperations elasticsearchOperations;
    private final ConcurrentHashMap<String, IndexCoordinates> knownIndexCoordinates = new ConcurrentHashMap<>();

    @Nullable
    private Document document;


    @SuppressWarnings("unused")
    public Document2RepositoryImpl(ElasticsearchOperations operations) {
        this.elasticsearchOperations = operations;
    }

    @Override
    public <S extends Document2> S save2(S document) {
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
    private <S extends Document2> IndexCoordinates getIndexCoordinates(S domainDocument) {
        return knownIndexCoordinates.computeIfAbsent(domainDocument.getIndexName(), i -> {
                IndexCoordinates indexCoordinates = IndexCoordinates.of(i);
                IndexOperations indexOperations = elasticsearchOperations.indexOps(indexCoordinates);
                if (!indexOperations.exists()) {
                    //indexOps.create();
                    Document settings = Document.create();
                    settings.put(ConstantsTranSaver.KEY_OF_INDEX_NUMBER_OF_REPLICAS, Config.getInstance().getLog().getIndexNumberOfReplicas());
                    settings.put(ConstantsTranSaver.KEY_OF_INDEX_NUMBER_OF_SHARDS, Config.getInstance().getLog().getIndexNumberOfShards());
                    indexOperations.create(settings);
                    if (this.document == null) {
                        this.document = indexOperations.createMapping(Document2.class);
                    }
                    indexOperations.putMapping(this.document);
                }
                return indexCoordinates;
            }
        );
    }

    @Override
    public void createIndex(Document2 document2) {

        IndexCoordinates indexCoordinates = IndexCoordinates.of(document2.getIndexName());
        IndexOperations indexOperations = elasticsearchOperations.indexOps(indexCoordinates);
        Document settings = Document.create();
        settings.put(ConstantsTranSaver.KEY_OF_INDEX_NUMBER_OF_REPLICAS, Config.getInstance().getLog().getIndexNumberOfReplicas()); // 1
        settings.put(ConstantsTranSaver.KEY_OF_INDEX_NUMBER_OF_SHARDS, Config.getInstance().getLog().getIndexNumberOfShards());     // 2
        indexOperations.create(settings);

        if (document2 instanceof ApplicationErrorLog)
            this.document = indexOperations.createMapping(ApplicationErrorLog.class);
        else if (document2 instanceof ApplicationErrorLogDaily)
            this.document = indexOperations.createMapping(ApplicationErrorLogDaily.class);

        else if (document2 instanceof ClientActivePortListResourceLog)
            this.document = indexOperations.createMapping(ClientActivePortListResourceLog.class);
        else if (document2 instanceof ClientActivePortListResourceLogDaily)
            this.document = indexOperations.createMapping(ClientActivePortListResourceLogDaily.class);

        else if (document2 instanceof ClientDefragAnalysisResourceLog)
            this.document = indexOperations.createMapping(ClientDefragAnalysisResourceLog.class);
        else if (document2 instanceof ClientDefragAnalysisResourceLogDaily)
            this.document = indexOperations.createMapping(ClientDefragAnalysisResourceLogDaily.class);

        else if (document2 instanceof ClientHWInfoResourceLog)
            this.document = indexOperations.createMapping(ClientHWInfoResourceLog.class);
        else if (document2 instanceof ClientHWInfoResourceLogDaily)
            this.document = indexOperations.createMapping(ClientHWInfoResourceLogDaily.class);

        else if (document2 instanceof ClientMBRResourceLog)
            this.document = indexOperations.createMapping(ClientMBRResourceLog.class);
        else if (document2 instanceof ClientMBRResourceLogDaily)
            this.document = indexOperations.createMapping(ClientMBRResourceLogDaily.class);

        else if (document2 instanceof ClientPerformanceResourceLog)
            this.document = indexOperations.createMapping(ClientPerformanceResourceLog.class);
        else if (document2 instanceof ClientPerformanceResourceLogDaily)
            this.document = indexOperations.createMapping(ClientPerformanceResourceLogDaily.class);

        else if (document2 instanceof ClientProcessCreationLog)
            this.document = indexOperations.createMapping(ClientProcessCreationLog.class);
        else if (document2 instanceof ClientProcessCreationLogDaily)
            this.document = indexOperations.createMapping(ClientProcessCreationLogDaily.class);

        else if (document2 instanceof ClientProcessResourceLog)
            this.document = indexOperations.createMapping(ClientProcessResourceLog.class);
        else if (document2 instanceof ClientProcessResourceLogDaily)
            this.document = indexOperations.createMapping(ClientProcessResourceLogDaily.class);

        else if (document2 instanceof ClientProgramListResourceLog)
            this.document = indexOperations.createMapping(ClientProgramListResourceLog.class);
        else if (document2 instanceof ClientProgramListResourceLogDaily)
            this.document = indexOperations.createMapping(ClientProgramListResourceLogDaily.class);

        else if (document2 instanceof ClientUserTermMonitorLog)
            this.document = indexOperations.createMapping(ClientUserTermMonitorLog.class);
        else if (document2 instanceof ClientUserTermMonitorLogDaily)
            this.document = indexOperations.createMapping(ClientUserTermMonitorLogDaily.class);

        else if (document2 instanceof ClientWindowsUpdateListResourceLog)
            this.document = indexOperations.createMapping(ClientWindowsUpdateListResourceLog.class);
        else if (document2 instanceof ClientWindowsUpdateListResourceLogDaily)
            this.document = indexOperations.createMapping(ClientWindowsUpdateListResourceLogDaily.class);

        else if (document2 instanceof DeviceErrorLog)
            this.document = indexOperations.createMapping(DeviceErrorLog.class);
        else if (document2 instanceof DeviceErrorLogDaily)
            this.document = indexOperations.createMapping(DeviceErrorLogDaily.class);

        else if (document2 instanceof HWErrorLog)
            this.document = indexOperations.createMapping(HWErrorLog.class);
        else if (document2 instanceof HWErrorLogDaily)
            this.document = indexOperations.createMapping(HWErrorLogDaily.class);

        else if (document2 instanceof IntegrityLog)
            this.document = indexOperations.createMapping(IntegrityLog.class);
        else if (document2 instanceof IntegrityLogDaily)
            this.document = indexOperations.createMapping(IntegrityLogDaily.class);

        else if (document2 instanceof PcOnOffEventLog)
            this.document = indexOperations.createMapping(PcOnOffEventLog.class);
        else if (document2 instanceof PcOnOffEventLogDaily)
            this.document = indexOperations.createMapping(PcOnOffEventLogDaily.class);

        else if (document2 instanceof ServerResourceLog)
            this.document = indexOperations.createMapping(ServerResourceLog.class);
        else if (document2 instanceof ServerResourceLogDaily)
            this.document = indexOperations.createMapping(ServerResourceLogDaily.class);

        else if (document2 instanceof WindowsBlueScreenLog)
            this.document = indexOperations.createMapping(WindowsBlueScreenLog.class);
        else if (document2 instanceof WindowsBlueScreenLogDaily)
            this.document = indexOperations.createMapping(WindowsBlueScreenLogDaily.class);

        else if (document2 instanceof WindowsEventSystemErrorAllLog)
            this.document = indexOperations.createMapping(WindowsEventSystemErrorAllLog.class);
        else if (document2 instanceof WindowsEventSystemErrorAllLogDaily)
            this.document = indexOperations.createMapping(WindowsEventSystemErrorAllLogDaily.class);

        else if (document2 instanceof ClientControlProcessResourceLog)
            this.document = indexOperations.createMapping(ClientControlProcessResourceLog.class);
        else if (document2 instanceof ClientControlProcessResourceLogDaily)
            this.document = indexOperations.createMapping(ClientControlProcessResourceLogDaily.class);

        else if (document2 instanceof ModuleMonitoringLog)
            this.document = indexOperations.createMapping(ModuleMonitoringLog.class);

        else if (document2 instanceof RuleAlertLog)
            this.document = indexOperations.createMapping(RuleAlertLog.class);
        else if (document2 instanceof RuleAlertSendLog)
            this.document = indexOperations.createMapping(RuleAlertSendLog.class);

        else
            this.document = indexOperations.createMapping(Document2.class);

        indexOperations.putMapping(this.document);
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