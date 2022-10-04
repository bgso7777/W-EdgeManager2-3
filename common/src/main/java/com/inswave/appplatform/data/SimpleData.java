package com.inswave.appplatform.data;

public class SimpleData implements IData, Cloneable {

	private Object obj = null;
	public static int dataType = DATATYPE_SIMPLE;

	public SimpleData() {
	}

	public SimpleData(Object obj) {
		this.obj = obj;
	}

	public int getDataType() {
		return this.dataType;
	}

	public void put(String key, IData data) {
		if (key == null)
			obj = data.getObject();
	}

	@Override
	public Object getHeaderValue(String key) {
		return null;
	}
	@Override
	public String getHeaderValueString(String key) { return null; }
	@Override
	public Boolean getHeaderValueBoolean(String key) { return null; }
	@Override
	public Integer getHeaderValueInteger(String key) { return null; }
	@Override
	public Long getHeaderValueLong(String key) { return null; }

	@Override
	public Object getBodyValue(String key) {
		return null;
	}

	@Override
	public String getBodyValueString(String key) {return null;}

	@Override
	public Boolean getBodyValueBoolean(String key) { return null; }

	@Override
	public Integer getBodyValueInteger(String key) {return null;}

	@Override
	public Long getBodyValueLong(String key) {return null;}

	public IData get(String key) {
		return new SimpleData(obj);
	}

	public IData getAt(int index) {
		return new SimpleData(obj);
	}

	public void setObject(Object obj) {
		this.obj = obj;
	}

	public Object getObject() {
		return this.obj;
	}

	public void remove(String key) {
		if (key == null)
			obj = null;
	}

	public int size() {
		return 0;
	}

	public IData clone() {
		SimpleData data = new SimpleData();

		Object obj = this.getObject();
		data.setObject(obj);

		return data;
	}

	public String[] getKeyNames() {
		return null;
	}

	@Override
	public void prettyPrint() {
	}

	public String toRawString() {
		if(obj == null)
			return null;
		else
			return obj.toString();
	}

	public String toString() {
		if(obj == null)
			return null;
		else if(obj instanceof String)
			return "\""+ obj.toString()+"\"";
		else
			return obj.toString();
	}

	public String toString(int depth) {
		return this.toString();
	}

	public String toPrettyString() {
		return this.toString(0);
	}

	public String getValueByKey(String key) {
		if(obj !=null) {
			return obj.toString();
		} else
			return null;
	}

}
