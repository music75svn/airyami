package com.lexken.framework.config;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;
 
 
public class ConfigProperties {
         
    /**
     * The common name for this class library. This is used for error messages,
     * because most of the code for this class is duplicated between Cryptix
     * and IJCE.
     */
    protected static final String PRODUCT_NAME = "MUSIUM_BUSAN_HP";
  
    /**
     * The name of the directory in which the properties files and (if
     * applicable) native libraries are found.
     */
    //static final String LIB_DIRNAME = "webzine-lib";


    /**
     * 전역 Properties 오브젝트.
     */
    protected static final Properties properties = new Properties();

    public ConfigProperties() {}

    /**
     * 기능 : property 값 얻어오기.
     * @param    String      key : 얻어올 Key 값
     * @return   String      value 값
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * 기능 : property 값을 얻어오거나 만약 property 값이 아직 설정이 안되었다면 <i>defaultValue</i>값을 리턴
     * @param    String      key : 얻어올 Key 값
     * @param    String      defaultValue : 디폴트값
     * @return   String      value 값
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }


    /**
     * 기능 : property 이름 전부 가져오기 (Enumeration 타입)
     * @return   Enumeration      value 값
     */
    public static Enumeration propertyNames() {
        return properties.propertyNames();
    }

    /**
     * Lists the properties to the PrintStream <i>out</i>.
     */
    public static void list(PrintStream out) {
        properties.list(out);
    }

    /**
     * Lists the properties to the PrintWriter <i>out</i>.
     */
    public static void list(PrintWriter out) {
        properties.list(out);
    }
}// end class
