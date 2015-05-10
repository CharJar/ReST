package com.charjar.util;

import com.google.gson.Gson;

public class JsonTransformer {
	private static Gson gson;
	
	static {
		gson = new Gson();
	}
	
	public static String toJson(Object o) {
		if( o instanceof Exception )
			return ((Exception) o).getMessage();
		
		if( o instanceof CharJarException )
			return ((CharJarException) o).getMessage();
		
		return gson.toJson(o);
	}
	
	public static Object fromJson(String json, Class clazz) {
		return gson.fromJson(json, clazz);		
	}
	
//	public static String toXML(Object o) {
//		return xstream.toXML(o).replace("<null/>", "");
//	}
//	
//	public static Object fromXML(String xml) {
//		return xstream.fromXML(xml);
//	}
}
