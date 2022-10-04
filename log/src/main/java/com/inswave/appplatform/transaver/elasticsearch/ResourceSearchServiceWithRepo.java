package com.inswave.appplatform.transaver.elasticsearch;

import com.inswave.appplatform.transaver.elasticsearch.dao.ServerResourceLogRepository;
import com.inswave.appplatform.transaver.elasticsearch.domain.ServerResourceLog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceSearchServiceWithRepo {

  private ServerResourceLogRepository resourceRepository;

  public void createProductIndexBulk(final List<ServerResourceLog> resources) {
	  resourceRepository.saveAll(resources);
  }

  public void createProductIndex(final ServerResourceLog resource) {
	  resourceRepository.save(resource);
  }
}