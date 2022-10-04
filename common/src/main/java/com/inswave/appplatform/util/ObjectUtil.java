package com.inswave.appplatform.util;

import sun.misc.Unsafe;

import java.io.*;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ObjectUtil {

	public static byte[] serialize(Object obj) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(obj);
	    return out.toByteArray();
	}
	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
	    ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return is.readObject();
	}

	public static final Unsafe us = getUnsafe();
	public static boolean useCompressedOops = true;
	public static int retainedSize(Object obj) {
		return retainedSize(obj, new HashMap<Object, Object>());
	}

	private static int retainedSize(Object obj, HashMap<Object, Object> calculated) {
		try {
			if (obj == null)
				throw new NullPointerException();
			calculated.put(obj, obj);
			Class<?> cls = obj.getClass();
			if (cls.isArray()) {
				int arraysize = us.arrayBaseOffset(cls) + us.arrayIndexScale(cls) * Array.getLength(obj);
				if (!cls.getComponentType().isPrimitive()) {
					Object[] arr = (Object[]) obj;
					for (Object comp : arr) {
						if (comp != null && !isCalculated(calculated, comp))
							arraysize += retainedSize(comp, calculated);
					}
				}
				return arraysize;
			} else {
				int objectsize = sizeof(cls);
				for (Field f : getAllNonStaticFields(obj.getClass())) {
					Class<?> fcls = f.getType();
					if (fcls.isPrimitive())
						continue;
					f.setAccessible(true);
					Object ref = f.get(obj);
					if (ref != null && !isCalculated(calculated, ref)) {
						int referentSize = retainedSize(ref, calculated);
						objectsize += referentSize;
					}
				}
				return objectsize;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static int sizeof(Class<?> cls) {

		if (cls == null)
			throw new NullPointerException();

		if (cls.isArray())
			throw new IllegalArgumentException();

		if (cls.isPrimitive())
			return primsize(cls);

		int lastOffset = Integer.MIN_VALUE;
		Class<?> lastClass = null;

		for (Field f : getAllNonStaticFields(cls)) {
			if (Modifier.isStatic(f.getModifiers()))
				continue;

			int offset = (int) us.objectFieldOffset(f);
			if (offset > lastOffset) {
				lastOffset = offset;
				lastClass = f.getClass();
			}
		}
		if (lastOffset > 0)
			return modulo8(lastOffset + primsize(lastClass));
		else
			return 16;
	}

	private static Field[] getAllNonStaticFields(Class<?> cls) {
		if (cls == null)
			throw new NullPointerException();

		List<Field> fieldList = new ArrayList<Field>();
		while (cls != Object.class) {
			for (Field f : cls.getDeclaredFields()) {
				if (!Modifier.isStatic(f.getModifiers()))
					fieldList.add(f);
			}
			cls = cls.getSuperclass();
		}
		Field[] fs = new Field[fieldList.size()];
		fieldList.toArray(fs);
		return fs;
	}

	private static boolean isCalculated(HashMap<Object, Object> calculated, Object test) {
		Object that = calculated.get(test);
		return that != null && that == test;
	}

	private static int primsize(Class<?> cls) {
		if (cls == byte.class)
			return 1;
		if (cls == boolean.class)
			return 1;
		if (cls == char.class)
			return 2;
		if (cls == short.class)
			return 2;
		if (cls == int.class)
			return 4;
		if (cls == float.class)
			return 4;
		if (cls == long.class)
			return 8;
		if (cls == double.class)
			return 8;
		else
			return useCompressedOops ? 4 : 8;
	}

	private static int modulo8(int value) {
		return (value & 0x7) > 0 ? (value & ~0x7) + 8 : value;
	}

	private static Unsafe getUnsafe() {
		try {
			Field f = Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			return (Unsafe) f.get(Unsafe.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object getFieldValue(Object object, String name) throws NoSuchFieldException, IllegalAccessException {
		Class<?> c = object.getClass();
		Field field = c.getDeclaredField(name);
		return field.get(object);
	}

	public static String getFieldValueMethod(Object object, String name) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Class<?> c = object.getClass();
		Method method = c.getMethod("get"+upperCaseFirst(name));
		Object retObjtct = method.invoke(object);
		String fieldValue = "";
		if (retObjtct instanceof Long) {
			fieldValue = ((Long)retObjtct).toString();
		} else if (retObjtct instanceof Double) {
			fieldValue = ((Double)retObjtct).toString();
		} else if (retObjtct instanceof String) {
			fieldValue = ((String)retObjtct);
		}  else if (retObjtct instanceof Integer) {
			fieldValue = ((Integer) retObjtct).toString();
		}
		return fieldValue;
	}

	public static String upperCaseFirst(String val) {
		char[] arr = val.toCharArray();
		arr[0] = Character.toUpperCase(arr[0]);
		return new String(arr);
	}

	public static Field[] getFields(Object object) throws NoSuchFieldException, IllegalAccessException {
		Class<?> c = object.getClass();
		return c.getDeclaredFields();
	}
}
