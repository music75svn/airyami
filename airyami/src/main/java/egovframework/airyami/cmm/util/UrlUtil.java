package egovframework.airyami.cmm.util;

import javax.servlet.http.HttpServletRequest;

public class UrlUtil {
    
	/**
	 * 액션 루트 URL을 얻는다.
	 * @param request
	 * @return
	 */
	public static String getUrlPath(HttpServletRequest request) {
		String requestURL = request.getRequestURL().toString();
		
		String[] arrs = requestURL.split( "/" );
		int len = requestURL.indexOf(arrs[3]);
		String homeDomain = requestURL.substring(0, len-1)+ request.getContextPath();
		
		return homeDomain;
	}
	
	/**
	 * 액션 루트 URL을 얻는다.
	 * @param request
	 * @return
	 */
	public static String getMenuID(HttpServletRequest request) {
		String requestURL = request.getRequestURL().toString();
		String[] arrs = requestURL.split( "/" );
		String naviMenu = "/" + arrs[arrs.length -2] + "/" + arrs[arrs.length -1].replace( ".do", "" );
		
		return naviMenu;
	}
	
	
	
	/**
	 * 이전 URL을 얻는다.
	 * @param request
	 * @return
	 */
	public static String getRefPath(HttpServletRequest request) {
		String refPath = request.getHeader("referer");
		
//		System.out.println("refPath :: " + refPath);
//		System.out.println("getUrlPath :: " + getUrlPath(request));
//		System.out.println("getRequestURI :: " + request.getRequestURI());
//		System.out.println("getContextPath :: " + request.getContextPath());
		
		if(CommonUtils.isNull(refPath))
			return "";
		
		refPath.replace(getUrlPath(request), "");
//		System.out.println("refPath :: " + refPath);
		
		return refPath;
	}
	
    /**
     * 액션 루트 URL을 얻는다.
     * @param request
     * @return
     */
    public static String getActionRoot(HttpServletRequest request) {
        return request.getContextPath();
    }
    
    /**
     * 웹 컨텐츠 루트 URL을 얻는다.
     * @param request
     * @return
     */
    public static String getWebContentRoot(HttpServletRequest request) {
        return request.getContextPath()+"/htdocs";
    }
    
    /**
     * 스타일 CSS 파일의 URL을 얻는다.
     * @param request
     * @return
     */
    public static String getStyleUrl(HttpServletRequest request) {
        return getWebContentRoot(request)+"/css/style.css";
    }
    
    /**
     * JQuery 라이브러리의 URL을 얻는다.
     * @param request
     * @return
     */
    public static String getJQueryUrl(HttpServletRequest request) {
        return getWebContentRoot(request)+"/js/jquery-1.8.1.min.js";
    }
	
	/**
	 * 공통 액션 루트 URL을 얻는다.
	 * @param request
	 * @return
	 */
	public static String getCommonActionRoot() {
		return "/HP_Common";
	}
	
	/**
	 * 공통 프로젝트의 웹 컨텐츠 루트 URL을 얻는다.
	 * @return
	 */
	public static String getCommonWebContentRoot() {
		return "http://172.6.15.50/portal";
	}
	
	/**
	 * 공통 프로젝트의 이미지 루트 URL을 얻는다.
	 * @return
	 */
	public static String getCommonImagesRoot() {
		return getCommonWebContentRoot()+"/images";
	}
	
	/**
	 * 공통 프로젝트의 스타일 CSS 파일의 URL을 얻는다.
	 * @return
	 */
	public static String getCommonStyleUrl() {
		return getCommonWebContentRoot()+"/css/style.css";
	}
	
	/**
	 * 공통 프로젝트의 유틸리티 라이브러리의 URL을 얻는다.
	 * @return
	 */
	public static String getCommonUtilUrl() {
		return getCommonWebContentRoot()+"/js/util.js";
	}
	
	/**
	 * 공통 프로젝트의 JQuery 라이브러리의 URL을 얻는다.
	 * @return
	 */
	public static String getCommonJQueryUrl() {
		return getCommonWebContentRoot()+"/js/jquery-1.8.1.min.js";
	}
	
	/**
	 * 공통 프로젝트의 JQuery UI 라이브러리의 URL을 얻는다.
	 * @return
	 */
	public static String getCommonJQueryUiUrl() {
		return getCommonWebContentRoot()+"/js/jquery-ui-1.9.2.custom.min.js";
	}
	
	/**
	 * 공통 프로젝트의 JQuery UI CSS의 URL을 얻는다.
	 * @return
	 */
	public static String getCommonJQueryUiCssUrl() {
		return getCommonWebContentRoot()+"/css/jquery-ui/smoothness/jquery-ui-1.9.2.custom.min.css";
	}
	
	/**
	 * 첨부파일 루트 URL을 얻는다.
	 * @return
	 */
	public static String getAttachRoot() {
		return "/hiportal";
	}
}
