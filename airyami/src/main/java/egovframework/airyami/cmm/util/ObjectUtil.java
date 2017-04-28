package egovframework.airyami.cmm.util;

public class ObjectUtil {

    /**
     * 두 값이 동일한가?
     * @param a  값1
     * @param b  값2
     * @return  true/false
     */
    public static boolean isEqual(Object a, Object b) {
        if (a == b) {
                return true;
        }
        if (a != null && a.equals(b)) {
                return true;
        }
        return false;
    }
    
    /**
     * 값이 비어있는가?
     * (null 또는 값을 문자열로 변환시 ""이면 비어있는 것이다.)
     * @param value  값
     * @return  true/false
     */
    public static boolean isEmpty(Object value) {
        String str = null;
        if (value != null) {
            str = String.valueOf(value);
        }
        return str == null || "".equals(str);
    }
    
    /**
     * 값이 null이면 기본값을 리턴한다.
     * @param value         값
     * @param defaultValue  기본값
     * @return 값 또는 기본값
     */
    public static <T> T replaceNull(T value, T defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
    
    /**
     * 값이 비어있으면 기본값을 리턴한다.
     * (null 또는 ""이면 비어있는 것이다.)
     * @param value         값
     * @param defaultValue  기본값
     * @return 값 또는 기본값
     */
    public static <T> T replaceEmpty(T value, T defaultValue) {
        if (isEmpty(value)) {
            return defaultValue;
        }
        return value;
    }
    
    /**
     * 오라클의 nvl() 함수와 동일한 기능을 한다.
     * 값이 비어있으면 기본값을 리턴한다.
     * (null 또는 ""이면 비어있는 것이다.)
     * @param value         값
     * @param defaultValue  기본값
     * @return 값 또는 기본값
     */
    public static <T> T nvl(T value, T defaultValue) {
        return replaceEmpty(value, defaultValue);
    }
    
    /**
     * 주어진 값을 순서대로 비교값i와 비교하여 일치(equals())하면 결과값i가 리턴된다.<br>
     * 기본값이 있는 경우, 어떤 것과도 같지 않으면 기본값이 리턴된다.
     * @param value  값
     * @param objects = 비교값1, 결과값1, 비교값2, 결과값2, ..., 비교값n, 결과값n [,기본값]
     * @return  결과값
     */
    public static Object decode(Object value, Object... objects) {
        Object result = null;
        boolean matched = false;
        for (int i=0; i+1<objects.length; i+=2) {
            if (isEqual(value, objects[i])) {
                result = objects[i+1];
                matched = true;
                break;
            }
        }
        if (!matched && objects.length%2==1) {
            result = objects[objects.length-1];
        }
        return result;
    }
    
    /**
     * 값이 비교값 배열에 포함되는가? (equals()로 비교) 
     * @param value          값
     * @param compareValues  비교값 배열
     * @return  true/false
     */
    public static boolean in(Object value, Object[] compareValues) {
        if (value != null && compareValues != null) {
            for (Object compareValue : compareValues) {
                if (value.equals(compareValue)) {
                    return true;
                }
            }
        }
        return false;
    }
}