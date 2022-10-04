package com.inswave.appplatform.log.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inswave.appplatform.dao.Domain;

import java.util.List;

public class TableEntityProvider {

    public Object[] getTableEntity (String tableEntityName, List<Domain> reqTableEntities) {
        ObjectMapper objectMapper = new ObjectMapper();
        Object[] ret = new Object[0];
        if( tableEntityName.equals(Rule.class.getSimpleName()) )
            ret = (objectMapper.convertValue(reqTableEntities, new TypeReference<List<Rule>>(){})).toArray();
        else if( tableEntityName.equals(RuleLevel.class.getSimpleName()) )
            ret = (objectMapper.convertValue(reqTableEntities, new TypeReference<List<RuleLevel>>(){})).toArray();
        else if( tableEntityName.equals(RuleReceiver.class.getSimpleName()) )
            ret = (objectMapper.convertValue(reqTableEntities, new TypeReference<List<RuleReceiver>>(){})).toArray();
        else if( tableEntityName.equals(RuleTarget.class.getSimpleName()) )
            ret = (objectMapper.convertValue(reqTableEntities, new TypeReference<List<RuleTarget>>(){})).toArray();
        else {
        }
        return ret;
    }
}
