package com.inswave.appplatform.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonUtil {

    public static String toString(List<Object> arrayList) {
        String ret = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ret = objectMapper.writeValueAsString(arrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static JsonNode toJsonNode(List<Object> arrayList) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayNode arr = objectMapper.valueToTree(arrayList);
            if (arr.size() > 0) return arr.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonNode toJsonNode(String value) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rtn = mapper.createObjectNode();
        try {
            rtn = mapper.readTree(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return rtn;
    }

    public static List toList(String value, Class clazz) {
        return toList(toJsonNode(value), clazz);
    }

    public static List toList(JsonNode value, Class clazz) {
        ObjectMapper mapper = new ObjectMapper();
        List rtn = new ArrayList();
        try {
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            rtn = mapper.readValue(value.toString(), listType);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return rtn;
    }

    public static Map toMap(Object value) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(value, Map.class);
    }
}
