package com.lexken.framework.config;

import java.io.IOException;

// Referenced classes of package com.newmulticampus.front.common.config:
//            ConfigProperties

public class CommonConfig extends ConfigProperties
{
	
	public static String SMTP_ADMIN_EMAIL = "";		// 관리자 메일
//	public static String SMTP_ADMIN_NAME = "";		// 관리자 이름
	public static String G_CONF_SITE_URL = "";		// SITE_URL
	public static String SMTP_ROOT_PATH = "";		// ROOT_PATH
	public static String SMTP_HOST = "";			// Send mail IP address
	public static String SMTP_USER_ID= "";			// Send mail User 
	public static String SMTP_USER_PWD = "";		// Send mail password
	public static String REAL_NICE_ID = "";			// 실명인증 가맹점ID
	public static String TERM_REG_NO = "";			// 실명인증 가맹점ID
	
	
	public static String KNARU_SMS_NUM = "";		// 발신전용 SMS 전화번호
	public static String RDA_NAME        = "";	   	// RDA한글 이름
	public static String KNARU_ADMIN_NAME = "";	   	// 대표관리자명
	public static String KNARU_SEND_EMAIL = "";		//발신전용 관리자메일
	public static String REFERER_URL = "";		    //접근경로 체크용 url 정보
	public static String CODE_UTIL_RELOAD_URL = ""; //공통코드 리로드 url 정보
	
	public static String SMS_URL = ""; 					//sms url 정보
	public static String SMS_ID = ""; 					//sms id 정보
	public static String SMS_PWD = ""; 					//sms PWD 정보
	public static String SMS_DEFAULT_SEND_NUMBER = ""; 	//sms 초기 발신 번호 정보
	
	public static String HTTP_DOMAIN_URL = ""; 		//http 도메인 url 정보
	public static String SSL_DOMAIN_URL = ""; 		//ssl 도메인 url 정보
	public static String LATIS_DOMAIN_URL = ""; 	//latis 도메인 url 정보

	public static String HUMAN_ORG_GB = "";		    	//운영 기관 코드
	public static String HUMAN_CRYPT_CHARACTOR = "";	//운영 기관 암호 코드 설정
	public static String HUMAN_RTN_URL = "";		    //RETURN BRIDGE URL

	private static CommonConfig fileInstance = new CommonConfig();
    
    public CommonConfig()
    {
        setProperties();
    }

    public static CommonConfig getInstance()
    {
        return fileInstance;
    }

    public static void reset()
        throws IOException
    {
        fileInstance = new CommonConfig();
    }

    public static void loadProperties()
    {
        setProperties();
    }

    private static void setProperties()
    { 
        String fs = System.getProperty("common.separator");
        boolean loaded = false;
        java.io.InputStream props = com.lexken.framework.config.CommonConfig.class.getResourceAsStream("common.properties");
        if(props != null)
            try
            {
                properties.load(props);

                //사이트 정보 관련
                G_CONF_SITE_URL = properties.getProperty("G_CONF_SITE_URL");
                SMTP_ROOT_PATH = properties.getProperty("SMTP_ROOT_PATH");

                //메일발송관련
            	SMTP_HOST = properties.getProperty("SMTP_HOST");
            	SMTP_USER_ID= properties.getProperty("SMTP_USER_ID");
            	SMTP_USER_PWD = properties.getProperty("SMTP_USER_PWD");
            	SMTP_ADMIN_EMAIL = properties.getProperty("SMTP_ADMIN_EMAIL");
//            	SMTP_ADMIN_NAME = properties.getProperty("SMTP_ADMIN_NAME");
            	KNARU_SMS_NUM	= properties.getProperty("KNARU_SMS_NUM");
            	RDA_NAME	        = properties.getProperty("RDA_NAME");
            	KNARU_ADMIN_NAME	= properties.getProperty("KNARU_ADMIN_NAME");
            	KNARU_SEND_EMAIL	= properties.getProperty("KNARU_SEND_EMAIL");                  	
            	
                //사이트 정보 관련
            	
                //실명인증 관련
            	REAL_NICE_ID = properties.getProperty("REAL_NICE_ID");
                //RDA 사업자등록번호 관련
            	TERM_REG_NO = properties.getProperty("TERM_REG_NO");
            	
            	//접근경로 체크용 url 정보
            	REFERER_URL = properties.getProperty("REFERER_URL");
            	
            	//공통코드 리로드 url 정보
            	CODE_UTIL_RELOAD_URL = properties.getProperty("CODE_UTIL_RELOAD_URL");
            	
            	//sms 정보
            	SMS_URL = properties.getProperty("SMS_URL"); 									//sms url 정보
            	SMS_ID = properties.getProperty("SMS_ID"); 										//sms id 정보
            	SMS_PWD = properties.getProperty("SMS_PWD"); 									//sms PWD 정보
            	SMS_DEFAULT_SEND_NUMBER = properties.getProperty("SMS_DEFAULT_SEND_NUMBER"); 	//sms 초기 발신 번호 정보
            	
            	//SSL 적용 관련 도메인 정보
            	HTTP_DOMAIN_URL = properties.getProperty("HTTP_DOMAIN_URL"); 	//http 도메인 url 정보
            	SSL_DOMAIN_URL = properties.getProperty("SSL_DOMAIN_URL"); 		//ssl 도메인 url 정보
            	LATIS_DOMAIN_URL = properties.getProperty("LATIS_DOMAIN_URL"); 	//latis 도메인 url 정보
            	
            	//인력API 정보
            	HUMAN_ORG_GB = properties.getProperty("HUMAN_ORG_GB");						//운영 기관 코드
            	HUMAN_CRYPT_CHARACTOR = properties.getProperty("HUMAN_CRYPT_CHARACTOR");	//운영 기관 암호 코드 설정
            	HUMAN_RTN_URL = properties.getProperty("HUMAN_RTN_URL");					//RETURN BRIDGE URL
            	
                loaded = true;
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            }
        if(!loaded)
            System.err.println("kr.co.lexken CommonConfig load Error!!");
    }
}
