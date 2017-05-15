/**
 * Author		: bsh
 * Date   		: 2017. 3. 30.
 * Description 	: 상수 정의 클래스입니다.
 */

package egovframework.airyami.cmm.util;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	public final static String DBIO_I = "I";     //입력모드
	public final static String DBIO_U = "U";     //수정모드
    
	
	public final static String fn_JS_PATH  = "/phishing/js/";          //js PATH
	
	public final static String GS_ADMIN_URL = "http://localhost:8080/dWise/";     	// 관리 시스템
	public final static String GS_USER_URL  = "http://localhost:8080/eship/";			// 사용자 시스템
	
	public static final String GS_CONTENTS_TYPE = "Content-Type";
	public static final String GS_ACCEPT = "Accept";
	public static final String GS_APPJSON = "application/json";
	public static final String GS_UTF_8 = "UTF-8";
	
	public static Map GM_TRAN_URL = null;         // 인터페이스 명 정보
	
	public static final String SYSTEM_ADMIN_GROUP = "system";
	public static final String USER_GROUP = "user";
	public static final String USER_TYPE  = "user_type";
	public static final String USER_COMPANY_GROUP = "company";
	
	
	// thumbnail 관련
	public static final String thumbnailPrefix = "thumbnail-";
	public static final int thumbnailWidth 	= 293;
	public static final int thumbnailHeight = 186;
	
	// RSS 관련
	public static final String rssTitle = "드림와이즈 표준 프레임워크 RSS : ";
	public static final String rssLink = "http://www.entrepreneurship.or.kr";
	public static final String rssDescription = "";	// board name으로 대체
	public static final String rssCopyright = "2015 entrepreneurship ALL RIGHTS RESERVED";
	public static final String rssLanguage = "ko";
	public static final String rssLinkViewURL = "/board/ArticleView.do";
	
	
	public static void initTransferInfo(){
    	setTranUrlInfo();
    }
	
	public static void setTranUrlInfo(){
		if(GM_TRAN_URL == null){
			GM_TRAN_URL = new HashMap();
		}
	}
    
}
