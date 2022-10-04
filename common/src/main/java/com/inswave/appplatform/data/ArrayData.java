package com.inswave.appplatform.data;

import org.json.simple.JSONArray;

public class ArrayData implements IData, Cloneable {

	private JSONArray arr = new JSONArray();
	public static int dataType = DATATYPE_ARRAY;

	public int getDataType() {
		return this.dataType;
	}

	public void put(String key, IData data) {
		if (key == null) {
			arr.add(data);
		} else {
			int idx = -1;
			try {
				idx = Integer.valueOf(key);
			} catch (NumberFormatException e) {
				return;
			}
			arr.set(idx, data);
		}
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
	public Boolean getBodyValueBoolean(String key) {return null;}

	@Override
	public Integer getBodyValueInteger(String key) {return null;}

	@Override
	public Long getBodyValueLong(String key) {return null;}

	public IData get(String key) {
		int idx = -1;
		try {
			idx = Integer.valueOf(key);
		} catch (NumberFormatException e) {
			return null;
		}

		Object ob = arr.get(idx);
		if (ob instanceof NodeData)
			return (NodeData) ob;
		else if (ob instanceof ArrayData)
			return (ArrayData) ob;
		return (SimpleData) ob;
	}

	public IData getAt(int index) {
		Object ob = arr.get(index);
		if (ob instanceof NodeData)
			return (NodeData) ob;
		else if (ob instanceof ArrayData)
			return (ArrayData) ob;
		return (SimpleData) ob;
	}

	public void setObject(Object obj) {
		this.arr = (JSONArray) obj;
	}

	public Object getObject() {
		return this.arr;
	}

	public void remove(String key) {
		if (key == null) {
			return;
		} else {
			int idx = -1;
			try {
				idx = Integer.valueOf(key);
			} catch (NumberFormatException e) {
				return;
			}
			if (idx < this.size())
				arr.remove(idx);
		}
	}

	public int size() {
		return arr.size();
	}

	public IData clone() {
		ArrayData data = new ArrayData();

		int len = this.size();

		for (int i = 0; i < len; i++) {
			IData d = this.getAt(i);
			data.put(null, d.clone());
		}
		return data;
	}

	public String[] getKeyNames() {
		return null;
	}

	@Override
	public void prettyPrint() {
	}

	public String toRawString() {
		return arr.toJSONString();
	}

	public String toString() {
		return arr.toJSONString();
	}

	public String toString(int depth) {
		String depth1 = null;
		String depth2 = null;
		StringBuffer debuf = new StringBuffer();
		for (int j = 0; j < depth; j++)
			debuf.append("\t");
		depth1 = debuf.toString();
		depth2 = depth1 + "\t";
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("[\n");

		if (this.arr != null) {
			for (int i = 0; i < this.size(); i++) {
				if (i != 0)
					sbuf.append(",\n");

				IData bizData = this.getAt(i);
				if (bizData != null)
					sbuf.append(depth2).append(bizData.toString(depth + 1));
			}
			sbuf.append("\n").append(depth1).append("]");
		}
		return sbuf.toString();
	}

	public String toPrettyString() {
		return this.toString(0);
	}

	public String getValueByKey(String key) {
		IData data = null;
		String subRtn = null;

		if (this.arr != null) {
			for (int i = 0; i < this.size(); i++) {
				data = getAt(i);
				if (data instanceof ArrayData || data instanceof NodeData) {
					subRtn = data.getValueByKey(key);
					if (subRtn != null)
						return subRtn;
				}
			}
		}
		return null;
	}

}
