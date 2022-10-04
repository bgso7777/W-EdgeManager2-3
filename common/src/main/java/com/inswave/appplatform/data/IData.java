package com.inswave.appplatform.data;


public interface IData {

	public static final int DATATYPE_SIMPLE = 0;
	public static final int DATATYPE_NODE = 1;
	public static final int DATATYPE_ARRAY = 2;

	public int getDataType();

	public void put(String key, IData data);

    public Object getHeaderValue(String key);
    public String getHeaderValueString(String key);
    public Boolean getHeaderValueBoolean(String key);
    public Integer getHeaderValueInteger(String key);
    public Long getHeaderValueLong(String key);

	public Object getBodyValue(String key);
	public String getBodyValueString(String key);
	public Boolean getBodyValueBoolean(String key);
	public Integer getBodyValueInteger(String key);
	public Long getBodyValueLong(String key);

	public IData get(String key);

	public IData getAt(int index);

	public void setObject(Object obj);

	public Object getObject();

	public void remove(String key);

	public int size();

	public IData clone();

	public String[] getKeyNames();

	public void prettyPrint();

	public String toRawString();

	public String toString();

	public String toString(int depth);

	public String toPrettyString();

	public String getValueByKey(String key);


}