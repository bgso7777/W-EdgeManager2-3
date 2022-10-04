package com.inswave.appplatform.transaver.elasticsearch;

import com.inswave.appplatform.transaver.elasticsearch.domain.ServerResourceLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResourceSearchService {

	  private static final String PRODUCT_INDEX = "serverresourcelog";

	  private ElasticsearchOperations elasticsearchOperations;

	  public String createProductIndexBulk (ServerResourceLog product) {

//			IndexQuery indexQuery = new IndexQueryBuilder()
//					.withId(product.getServerResourceLogId().toString())
//					.withObject(product).build();
//
//			   String documentId = elasticsearchOperations
//				.index(indexQuery, IndexCoordinates.of(PRODUCT_INDEX));
//
//			   return documentId;
		  return "";
	  }

}