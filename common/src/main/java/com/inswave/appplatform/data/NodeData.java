package com.inswave.appplatform.data;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Set;

public class NodeData implements IData, Cloneable {

    private       JSONObject obj      = new JSONObject();
    public static int        dataType = IData.DATATYPE_NODE;

    private String rawString;

    public int getDataType() {
        return this.dataType;
    }

    public void put(String key, IData data) {
        obj.put(key, data);
    }

    @Override
    public Object getHeaderValue(String key) {
        HashMap linkedHashMap = (HashMap) obj.get("header");
        return linkedHashMap.get(key);
    }

    @Override
    public String getHeaderValueString(String key) {
        return (String) getHeaderValue(key);
    }

    @Override
    public Boolean getHeaderValueBoolean(String key) {
        return (Boolean) getHeaderValue(key);
    }

    @Override
    public Integer getHeaderValueInteger(String key) {
        return (Integer) getHeaderValue(key);
    }

    @Override
    public Long getHeaderValueLong(String key) {
        return (Long) getHeaderValue(key);
    }

    @Override
    public Object getBodyValue(String key) {
        Object ret = null;
        if(obj.get("body") instanceof HashMap) {
            HashMap linkedHashMap = (HashMap) obj.get("body");
            ret = linkedHashMap.get(key);
        } else if(obj.get("body") instanceof NodeData) {
            NodeData nodeData = (NodeData) obj.get("body");
            SimpleData simpleData = (SimpleData) nodeData.get(key);
            ret = simpleData.getObject();
        }
        return ret;
    }

    @Override
    public String getBodyValueString(String key) {
        return (String) getBodyValue(key);
    }

    @Override
    public Boolean getBodyValueBoolean(String key) {
        return (Boolean) getBodyValue(key);
    }

    @Override
    public Integer getBodyValueInteger(String key) {
        return (Integer) getBodyValue(key);
    }

    @Override
    public Long getBodyValueLong(String key) {
        if (getBodyValue(key).getClass().equals(Integer.class)) {
            return getBodyValueInteger(key).longValue();
        }
        return (Long) getBodyValue(key);
    }

    public IData get(String key) {
        Object ob = obj.get(key);
        if (ob instanceof NodeData)
            return (NodeData) ob;
        else if (ob instanceof ArrayData)
            return (ArrayData) ob;
        return (SimpleData) ob;
    }

    public IData getAt(int index) {
        String[] keys = this.getKeyNames();
        if (index < 0 || index >= keys.length)
            return null;
        return (IData) obj.get(keys[index]);
    }

    public void setObject(Object obj) {
        this.obj = (JSONObject) obj;
    }

    public Object getObject() {
        return this.obj;
    }

    public void remove(String key) {
        obj.remove(key);
    }

    public int size() {
        return obj.size();
    }

    public IData clone() {

        NodeData data = new NodeData();
        String[] keys = this.getKeyNames();

        for (int i = 0; i < keys.length; i++) {
            IData d = this.get(keys[i]);
            if (d != null)
                data.put(keys[i], d.clone());
        }
        return data;
    }

    public String[] getKeyNames() {
        Set<String> keyset = obj.keySet();
        return keyset.toArray(new String[0]);
    }

    @Override
    public void prettyPrint() {

    }

    public String toRawString() {
        return obj.toJSONString();
    }

    public String toString() {
        return obj.toJSONString();
    }

    public String toString(int depth) {
        String key = null;
        String depth1 = null;
        String depth2 = null;
        StringBuffer debuf = new StringBuffer();
        for (int j = 0; j < depth; j++)
            debuf.append("\t");
        depth1 = debuf.toString();
        depth2 = depth1 + "\t";
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("{\n");

        if (this.obj != null) {
            String[] keys = this.getKeyNames();

            for (int i = 0; i < keys.length; i++) {
                key = keys[i];
                if (i != 0)
                    sbuf.append(",\n");
                sbuf.append(depth2).append("\"" + key + "\" : ");
                IData bizData = this.get(key);
                if (bizData != null) {
                    sbuf.append(bizData.toString(depth + 1));
                }
            }
            sbuf.append("\n").append(depth1).append("}");
        }
        return sbuf.toString();
    }

    public String toPrettyString() {
        return this.toString(0);
    }

    public String getValueByKey(String keyName) {
        String key = null;
        IData data = null;
        String subRtn = null;

        if (this.obj != null) {
            String[] keys = this.getKeyNames();
            for (int i = 0; i < keys.length; i++) {
                key = keys[i];
                data = this.get(key);
                if (key.equals(keyName)) {
                    if (data instanceof SimpleData) {
                        return data.getValueByKey(keyName);
                    } else if (data instanceof ArrayData)
                        return "::ArrayData";
                    else if (data instanceof NodeData)
                        return "::NodeData";
                } else {
                    if (data instanceof ArrayData || data instanceof NodeData) {
                        subRtn = data.getValueByKey(keyName);
                        if (subRtn != null)
                            return subRtn;
                    }
                }
            }
        }
        return null;
    }

}