package egovframework.airyami.cmm.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import egovframework.airyami.cmm.util.*;


/**
 * VO로 사용하기 위한 Map<String,Object> 클래스
 */
public class ValueMap extends LinkedHashMap<String,Object> {
    /**
     * 인스턴스를 생성한다.
     */
    public ValueMap() {
        super();
    }
    
    /**
     * 인스턴스를 생성한다.
     * @param map  복사할 맵
     */
    public ValueMap(Map<String,Object> map) {
        super(map);
    }

    /**
     * 키의 값을 가져온다.
     * @param key           키
     * @param defaultValue  기본값
     * @return 값
     */
    public Object get(String key, Object defaultValue) {
        return ObjectUtil.nvl(get(key), defaultValue);
    }
    
    /**
     * 키의 값을 가져온뒤 없앤다.
     * @param key           키
     * @param defaultValue  기본값
     * @return 값
     */
    public Object remove(String key, Object defaultValue) {
        return ObjectUtil.nvl(remove(key), defaultValue);
    }
    
    /**
     * 키의 값을 문자열로 가져온다. (기본값: null)
     * @param key  키
     * @return 문자열 값
     */
    public String getString(String key) {
        return getString(key, null);
    }
    
    /**
     * 키의 값을 문자열로 가져온다.
     * @param key           키
     * @param defaultValue  기본값
     * @return 문자열 값
     */
    public String getString(String key, String defaultValue) {
        String value = defaultValue;
        Object valueObject = get(key);
        if (valueObject != null) {
			value = String.valueOf(valueObject);
        }
        return value;
    }
    
    /**
     * 키의 값을 문자열로 가져온뒤 없앤다. (기본값: null)
     * @param key  키
     * @return 문자열 값
     */
    public String removeString(String key) {
        return removeString(key, null);
    }
    
    /**
     * 키의 값을 문자열로 가져온뒤 없앤다.
     * @param key           키
     * @param defaultValue  기본값
     * @return 문자열 값
     */
    public String removeString(String key, String defaultValue) {
        String value = getString(key, defaultValue);
        remove(key);
        return value;
    }
    
    /**
     * 키의 값을 정수로 가져온다. (기본값: null)
     * @param key  키
     * @return 정수 값
     */
    public Integer getInteger(String key) {
        return getInteger(key, null);
    }
    
    /**
     * 키의 값을 정수로 가져온다.
     * @param key           키
     * @param defaultValue  기본값
     * @return 정수 값
     */
    public Integer getInteger(String key, Integer defaultValue) {
        Integer value = defaultValue;
        try {
			value = Integer.parseInt(getString(key));
        } catch (Exception e) {
        }
        return value;
    }
    
    /**
     * 키의 값을 정수로 가져온뒤 없앤다. (기본값: null)
     * @param key  키
     * @return 정수 값
     */
    public Integer removeInteger(String key) {
        return removeInteger(key, null);
    }
    
    /**
     * 키의 값을 정수로 가져온뒤 없앤다.
     * @param key           키
     * @param defaultValue  기본값
     * @return 정수 값
     */
    public Integer removeInteger(String key, Integer defaultValue) {
        Integer value = getInteger(key, defaultValue);
        remove(key);
        return value;
    }
    
    /**
     * 키의 값을  Boolean으로 가져온다. (기본값: null)
     * @param key  키
     * @return  Boolean
     */
    public Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }
    
    /**
     * 키의 값을  Boolean으로 가져온다.
     * @param key           키
     * @param defaultValue  기본값
     * @return  Boolean
     */
    public Boolean getBoolean(String key, Boolean defaultValue) {
        Boolean value = defaultValue;
        try {
                value = Boolean.parseBoolean(getString(key));
        } catch (Exception e) {
        }
        return value;
    }
    
    /**
     * 키의 값을  Boolean으로 가져온뒤 없앤다. (기본값: null)
     * @param key  키
     * @return  Boolean
     */
    public Boolean removeBoolean(String key) {
        return removeBoolean(key, null);
    }
    
    /**
     * 키의 값을 Boolean으로 가져온뒤 없앤다.
     * @param key           키
     * @param defaultValue  기본값
     * @return  Boolean
     */
    public Boolean removeBoolean(String key, Boolean defaultValue) {
        Boolean value = getBoolean(key, defaultValue);
        remove(key);
        return value;
    }
    
    /**
     * Boolean 값을 Y/N 값으로 변환하여 키에 넣는다.
     * @param key    키
     * @param value  값
     */
    public void putYnFromBoolean(String key, boolean value) {
        put(key, value ? "Y" : "N");
    }
    
    /**
     * 키의 Y/N 값을 Boolean으로 변환하여 가져온다.
     * @param key           키
     * @param defaultValue  기본값
     * @return  Boolean
     */
    public Boolean getBooleanFromYn(String key, Boolean defaultValue) {
        String yn = getString(key);
        if (yn == null) return defaultValue;
        return "Y".equals(yn);
    }
    
    /**
     * 키의 값을 ","로 나눈 문자열 배열을 얻는다.
     * @param key  키
     * @return  문자열 배열
     */
    public String[] getStringArray(String key) {
        return getStringArray(key, ",");
    }
    
    /**
     * 키의 값을 구분자로 나눈 문자열 배열을 얻는다.
     * @param key        키
     * @param delimiter  구분자
     * @return  문자열 배열
     */
    public String[] getStringArray(String key, String delimiter) {
        String[] arr = null;
        String value = getString(key);
        if (value != null && value.trim().length() > 0) {
                arr = value.split(delimiter);
                for (int i=0; i<arr.length; i++) {
                        arr[i] = arr[i].trim();
                }
        }
        return arr;
    }
    
    /**
     * 키의 값이 null이면 기본값으로 대체한다. 
     * @param key           키
     * @param defaultValue  기본값
     */
    public void replaceNull(String key, Object defaultValue) {
        put(key, get(key, defaultValue));
    }
    
    /**
     * HTTP 요청 파라미터 맵을 넣는다.
     * @param parameterMap  HTTP 요청의 파라미터 맵
     */
    @SuppressWarnings({"rawtypes","unchecked"})
    public void putParameterMap(Map parameterMap) {
        Iterator<Map.Entry<String,String[]>> it = parameterMap.entrySet().iterator();
        while (it.hasNext()) {
                Map.Entry<String,String[]> entry = it.next();
                String key = entry.getKey();
                String[] valueArr = entry.getValue();
                String value = "";
                if (valueArr != null) {
                        for (int i=0; i<valueArr.length; i++) {
                                if (i>0) value += ",";
                                value += valueArr[i];
                        }
                }
                put(key, value);
        }
    }
}
