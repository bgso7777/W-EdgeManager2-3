package com.inswave.appplatform.transaver.elasticsearch;

import com.inswave.appplatform.Config;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.inswave.appplatform.transaver.elasticsearch.dao")
@ComponentScan(basePackages = {"com.inswave.appplatform.transaver.elasticsearch.domain"})
public class ElasticsearchClientConfig extends AbstractElasticsearchConfiguration {

	@Value("${elasticsearch.search.server}")
	private String elasticsearchSearchServer;
	@Value("${elasticsearch.data.server}")
	private String elasticsearchDataServer;

	@Override
	public RestHighLevelClient elasticsearchClient() {
		if(elasticsearchSearchServer==null)
			elasticsearchSearchServer = Config.getInstance().getLog().getElasticsearchSearchServerRandom();
		if(elasticsearchDataServer==null)
			elasticsearchDataServer = Config.getInstance().getLog().getElasticsearchDataServerRandom();
		Config.getInstance().getLog().setElasticsearchSearchServer(elasticsearchSearchServer);
		Config.getInstance().getLog().setElasticsearchDataServer(elasticsearchDataServer);
		ClientConfiguration clientConfiguration = ClientConfiguration.builder()
				//.connectedTo(Config.getInstance().getLog().getElasticsearchDataServer())
				.connectedTo(Config.getInstance().getLog().getElasticsearchDataServer())
				.build();
		return RestClients.create(clientConfiguration).rest();
	}

}