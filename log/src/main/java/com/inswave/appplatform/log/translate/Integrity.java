package com.inswave.appplatform.log.translate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inswave.appplatform.log.util.LogDataObjectMapper;
import com.inswave.appplatform.transaver.elasticsearch.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class Integrity {

	public Integrity () {
	}

	public IntegrityLog getIntegrityLogObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, IntegrityLog.class);
	}

	public IntegrityLogDaily getIntegrityLogDailyObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, IntegrityLogDaily.class);
	}

	public IntegrityLogOriginal getOriginalObject(StringBuffer message) {
		IntegrityLogOriginal integrityLogOriginal = null;
		return integrityLogOriginal;
	}

	public IntegrityLogOriginal getOriginalObject(LinkedHashMap linkedHashMap) throws JsonProcessingException, Exception {
		return LogDataObjectMapper.getObjectMapper().convertValue(linkedHashMap, IntegrityLogOriginal.class);
	}

}
