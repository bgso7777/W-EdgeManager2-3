package com.inswave.appplatform.transaver.elasticsearch;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.transaver.elasticsearch.domain.Document2;
import io.searchbox.action.BulkableAction;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElasticsearchJestClient {

    private JestClient jestClient = null;

    List<String> servers = new ArrayList<>();

    private String ip;
    private int port;

    public ElasticsearchJestClient() {
        String[] tempservers = Config.getInstance().getLog().getElasticsearchDataServer();
        if (tempservers.length > 0) {
            for (String server : tempservers) {
                String[] ipport = server.split(":");
                ip = ipport[0];
                port = Integer.parseInt(ipport[1]);
                String serverStr = "http://"+ip+":"+port;
                servers.add(serverStr);
            }
        } else {
        }
    }

    public ElasticsearchJestClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        String server = "http://"+ip+":"+port;
        servers.add(server);
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    private JestClientFactory jestClientFactory = null;

    public void openJestClient() {
        jestClientFactory = new JestClientFactory();
        jestClientFactory.setHttpClientConfig(new HttpClientConfig
                .Builder(servers)
//                .gson(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:sss").create())
                .multiThreaded(true)
//                .readTimeout(10000)
                .build());
        //Object object = factory.getObject();
        jestClient = jestClientFactory.getObject();
    }

    private ArrayList<BulkableAction> insertDocuments = null;
    public BulkResponse writeDocument2s(List<Document2> document2s) throws IOException {
        insertDocuments = new ArrayList<>();
        String indexName="";
        for( Document2 document2 : document2s ) {
            indexName = document2.getIndexName();
            insertDocuments.add(new Index.Builder(Document2.getAsMap(document2)).build());
        }
        Bulk bulk = new Bulk.Builder().defaultIndex(indexName).addAction(insertDocuments).build();
        jestClient.execute(bulk);
        insertDocuments.clear();
        insertDocuments=null;
        bulk = null;
        return null;
    }

    public void closeJestClient() throws IOException {
        jestClient.close();
        jestClient=null;
        System.gc();
    }

}
