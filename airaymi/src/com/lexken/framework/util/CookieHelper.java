package com.lexken.framework.util;

public class CookieHelper {		
    /**
     * 쿠키 생성
     * @param response
     * @param name 쿠키명
     * @param value 쿠키값
     */
    public static void setCookie(javax.servlet.http.HttpServletResponse response, String name, String value) 
            throws Exception
    {
        value = java.net.URLEncoder.encode(value,"MS949");
        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(name, value);
        cookie.setMaxAge(60*60*24*3000);
        response.addCookie(cookie);
    }
    
    /**
     * 쿠키 생성
     * @param response
     * @param name 쿠키명
     * @param value 쿠키값
     * @param uri 경로 지정
     */
    public static void setCookie(javax.servlet.http.HttpServletResponse response, String name, String value,String uri) 
            throws Exception
    {
        value = java.net.URLEncoder.encode(value,"MS949");
        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(name, value);
        cookie.setPath(uri);
        cookie.setMaxAge(60*60*24*3000);
        response.addCookie(cookie);
    }
        
    /**
     * 쿠키 생성(무한)
     * @param response
     * @param name 쿠키명
     * @param value 쿠키값
     */
    public static void setCookieInfinity(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, String name, String value) 
            throws Exception
    {
    	javax.servlet.http.Cookie cookie = null;
    	
    	int cnt = 0;
    	String url = "/";
    	javax.servlet.http.Cookie [] cookies = request.getCookies();
    	
        for(int i=0;i<cookies.length;i++) {
        	if(name.equals(cookies[i].getName())) {
        		cnt++;
        		cookie = cookies[i];
        		cookie.setValue(value);
        		break;
            }
        }
        
        if (cnt == 0){
        	cookie = new javax.servlet.http.Cookie(name, value);
        }
        
    	value = java.net.URLEncoder.encode(value,"MS949");
    	cookie.setPath(url);
    	cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }
    
    /**
     * 쿠키 가져오기
     * @param request
     * @param cookieName 쿠키명
     * @return String 쿠키값
     */
    public static String getCookie(javax.servlet.http.HttpServletRequest request, String cookieName) 
            throws Exception
    {
        javax.servlet.http.Cookie [] cookies = request.getCookies();
        String value = "";
        for(int i=0;i<cookies.length;i++) {
        	if(cookieName.equals(cookies[i].getName())) {
                value = java.net.URLDecoder.decode(cookies[i].getValue(),"MS949");
                break;
            }
        }
        return value;
    }
}
