package egovframework.airyami.cmm.util;

import java.io.IOException;
import java.io.Writer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * JSON 유틸리티
 * @author  박성희
 */
public class JsonUtil {

	static Object toJsonObject(Object obj) {
		Object json = null;
		if (obj != null) {
			Class clazz = obj.getClass();
			if (boolean.class.equals(clazz) ||   Boolean.class.equals(clazz) ||
				   byte.class.equals(clazz) ||      Byte.class.equals(clazz) ||
				   char.class.equals(clazz) || Character.class.equals(clazz) ||
				  short.class.equals(clazz) ||     Short.class.equals(clazz) ||
				    int.class.equals(clazz) ||   Integer.class.equals(clazz) ||
				   long.class.equals(clazz) ||      Long.class.equals(clazz) ||
				  float.class.equals(clazz) ||     Float.class.equals(clazz) ||
				 double.class.equals(clazz) ||    Double.class.equals(clazz)) {
				json = obj;
			} else if (obj instanceof Number) {
			    json = obj;
			} else if (obj instanceof String) {
				json = obj;
			} else if (obj instanceof Map) {
				JSONObject jsonObject = new JSONObject();
				
				Map<String,Object> map = (Map)obj;
				for (Map.Entry<String,Object> entry : map.entrySet()) {
					Object key   = entry.getKey();
					Object value = entry.getValue();
					try {
						jsonObject.put(String.valueOf(key), toJsonObject(value));
					} catch (JSONException e) {
						throw new RuntimeException(e);
					}
				}
				
				json = jsonObject;
			} else if (obj instanceof Collection) {
				JSONArray jsonArray = new JSONArray();
				for (Object x : (Collection)obj) {
					jsonArray.put(toJsonObject(x));
				}
				json = jsonArray;
			} else if (clazz.isArray()) {
				JSONArray jsonArray = new JSONArray();
				for (Object x : (Object[])obj) {
					jsonArray.put(toJsonObject(x));
				}
				json = jsonArray;
			} else {
				json = new JSONObject(obj);
			}
		}
		return json;
	}

	public static String toString(String str) {
		if (str == null) {
			return "null";
		}
		return "\""+str.replaceAll("\"","\\\\\\\"")+"\"";
	}

	public static String toString(Object obj) {
		if (obj instanceof String) {
			return toString((String)obj);
		} else {
			Object jsonObject = toJsonObject(obj);
			return String.valueOf(jsonObject);
		}
	}

	public static void write(Object obj, Writer writer) {
	    try {
	        if (obj instanceof String) {
	            writer.write(toString((String)obj));
	        } else {
	            Object jsonObject = toJsonObject(obj);
	            if (jsonObject instanceof JSONObject) {
	                ((JSONObject)jsonObject).write(writer);
	            } else if (jsonObject instanceof JSONArray) {
	                ((JSONArray)jsonObject).write(writer);
	            } else {
	                writer.write(String.valueOf(jsonObject));
	            }
	        }
	    } catch (JSONException e) {
	        throw new RuntimeException(e);
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}

	private static Object toPlainObject(Object json) {
		Object obj = json;
		if (json != null && !(json instanceof String)) {
			if (json instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject)json;
				if (jsonObject.names() != null) {
					obj = toMap(jsonObject);
				} else {
					obj = String.valueOf(json);
					if ("null".equals(obj)) {
						obj = null;
					}
				}
			} else if (json instanceof JSONArray) {
				obj = toList((JSONArray)json);
			}
		}
		return obj;
	}

	public static Map<String,Object> toMap(JSONObject jsonObject) {
		Map<String,Object> map = null;
		try {
			if (jsonObject != null) {
				map = new HashMap<String,Object>();

				JSONArray names = jsonObject.names();
				if (names != null) {
					for (int i=0; i<names.length(); i++) {
						String name = names.getString(i);
						Object obj = null;
						if (!jsonObject.isNull(name)) {
							obj = toPlainObject(jsonObject.get(name));
						}
						map.put(name, obj);
					}
				}
			}
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return map;
	}

	public static List<Object> toList(JSONArray jsonArray) {
		List<Object> list = null;
		try {
			if (jsonArray != null) {
				list = new ArrayList<Object>();

				for (int i=0; i<jsonArray.length(); i++) {
					Object obj = null;
					if (!jsonArray.isNull(i)) {
						obj = toPlainObject(jsonArray.get(i));
					}
					list.add(obj);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return list;
	}

	public static Map<String,Object> parseToMap(String source) {
		try {
			JSONObject jsonObject = new JSONObject(source);
			return toMap(jsonObject);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<Object> parseToList(String source) {
		try {
			JSONArray jsonArray = new JSONArray(source);
			return toList(jsonArray);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
}
