package com.inswave.appplatform.log.translate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.log.util.LogDataObjectMapper;
import com.inswave.appplatform.transaver.elasticsearch.domain.ServerResourceLog;
import com.inswave.appplatform.transaver.elasticsearch.domain.ServerResourceLogDaily;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class ServerResource {

	public ServerResource () {
	}

	public ServerResourceLog getOriginalObject(StringBuffer message) {
		ServerResourceLog serverResourceLog = null;
		return serverResourceLog;
	}

	public ServerResourceLog getServerResourceObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, ServerResourceLog.class);
	}

	public ServerResourceLogDaily getServerResourceDailyObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, ServerResourceLogDaily.class);
	}

	public static void main(String[] argv){

	}

}