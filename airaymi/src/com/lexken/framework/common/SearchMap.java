/*************************************************************************
* CLASS 명  	: SearchMap
* 작 업 자  	: 박재현
* 작 업 일  	: 2009.07.12
* 기    능  	: 화면 submit 데이터 자동 저장
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    박재현		 2009.07.12			  최 초 작 업 
**************************************************************************/
package com.lexken.framework.common;

import java.io.File;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.config.CommonConfig;
import com.lexken.framework.config.FileConfig;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.CalendarHelper;
import com.lexken.framework.util.FileHelper;
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.FtpUtil;
import com.lexken.framework.util.StaticUtil;

public class  SearchMap extends HashMap  {

	private static final long serialVersionUID = 1L;
	protected String m_method ;	            //submit 방식
	protected String m_url ;		        //url 확인용 변수
	private HttpServletRequest req 	= null;	// request
	private HttpServletResponse res = null;	// res
	
	private final int	nBeginRow 	= 1;	// 페이지번호
	private final int 	nRowPerPage = 10;	// 한페이지 라인수
	
	public boolean 	bfileUpload = true;		// 파일 업로드 성공여부
	
	private String rsaPrivateKey = "__rsaPrivateKey__";	//RSA 개인키값이 저장될 세션키
	
	private final String NAMESPACE = "com.lexken";      // PACKAGE NAMESPACE
	
	public HashMap<String, Object> hash = new HashMap<String, Object>(); //Action result 반환
	
	// Logger
	private final Log logger = LogFactory.getLog(getClass());
	
	
	// 생성자(WSDL 에서 사용) searchmap 기본기능 사용 불가 지만.. 파라미터 세팅용으로 WSDL 에서 사용(SQL 파라미터 통일성 유지용)
	public SearchMap(){

	}
	
	// 생성자
	public SearchMap(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response){
		req = request;
		res = response;
		m_method = req.getMethod();
		m_url = req.getRequestURL().toString();
		this.put("beginRow", nBeginRow);
		this.put("currentUrl", request.getRequestURI());
		this.put("servletContext", request.getRequestURL().toString().replaceAll(request.getRequestURI(), "") + request.getContextPath());
		this.put("userAgent", req.getHeader("user-agent"));
		
		//SSL 적용을 위한 서버 명 가져오기(2010.10.24 박재현)
		CommonConfig commonConfig = CommonConfig.getInstance();
		String  http_domain_url   = commonConfig.getProperty("HTTP_DOMAIN_URL");
		String  ssl_domain_url    = commonConfig.getProperty("SSL_DOMAIN_URL");
		req.setAttribute("HTTP_DOMAIN_URL", http_domain_url);
		req.setAttribute("SSL_DOMAIN_URL", ssl_domain_url);
		
		//context 설정
		this.put("context", req.getContextPath());
		
		//상수값 설정
		this.put("METRIC_NM", StringConstants.METRIC_NM);
		this.put("METRIC_GRP_NM", StringConstants.METRIC_GRP_NM);
		this.put("SC_DEPT_NM", StringConstants.SC_DEPT_NM);
		this.put("DEPT_NM", StringConstants.DEPT_NM);
		this.put("UP_SC_DEPT_NM", StringConstants.UP_SC_DEPT_NM);
		this.put("PERSPECTIVE_NM", StringConstants.PERSPECTIVE_NM);
		this.put("DIRECTION_NM", StringConstants.DIRECTION_NM);
		this.put("STRATEGY_NM", StringConstants.STRATEGY_NM);
		this.put("UP_STRATEGY_NM", StringConstants.UP_STRATEGY_NM);
		this.put("ACTIVITY_NM", StringConstants.ACTIVITY_NM);
		this.put("YEARMON", StringConstants.YEARMON);
		this.put("YEAR", StringConstants.YEAR);
		this.put("WEIGHT", StringConstants.WEIGHT);
		
		//ORACLE ENCRYPTION KEY
		String  encryptionCode    = commonConfig.getProperty("ENCRYPTION_CODE");
		this.put("encryptionCode", encryptionCode);
		
		//접속자 아이피 추출
		String clientIp = req.getRemoteAddr();
		this.put("clientIp", clientIp);
		
		// 일반 전송일때 처리
		if(req.getContentType() == null || req.getContentType().indexOf("multipart/form-data") == -1) {

			SortedMap<String,String[]> sMap = Collections.synchronizedSortedMap
	        ( new TreeMap<String,String[]>(req.getParameterMap()));
			
			synchronized (sMap) {
				for (String key : sMap.keySet()) {
					String[] value = sMap.get(key);
					key = key.replaceAll("amp;", "");
					if(value.length == 0){
					}else if(value.length == 1){
						this.put(key, toKorean(value[0]));
						//System.out.println("[key, value] : [" + key + ","+ value[0]+"]");
						//this.put(key, value[0]);
					}else{
						for(int i=0; i<value.length; i++){
							value[i] = toKorean(value[i]);
							//value[i] = value[i];
						}
						this.put(key, value);
					}
				}
			}
		} else {  //첨부파일이 있는 경우 처리
			/**********************************
			 * FileUpload
			 **********************************/
			FileUpload fileUpload = new FileUpload(req, this);
			fileUpload.setAddPath("/temp");

			// 파일 업로드 실패
			if(!fileUpload.upload()){
				bfileUpload = false;
				HashMap returnMap = new HashMap();
				returnMap.put("ErrorNumber", fileUpload.getMessageNumber());
				returnMap.put("ErrorMessage", fileUpload.getMessageString());
				req.setAttribute("returnMap", returnMap);
				
				try{
					logger.error("첨부파일 업로드시 실패한 후에 에러 페이지로 이동");
					logger.error("  - ERROR_NUMBER: " + fileUpload.getMessageNumber());
					logger.error("    " + fileUpload.getMessageString());
					logger.error("  - URL: " + request.getRequestURL().toString().replaceAll(request.getRequestURI(), "") + request.getContextPath());
					logger.error("  - URI: " + request.getRequestURI());

					//RequestDispatcher requestDispatcher = req.getRequestDispatcher(req.getContextPath() + "/bsc/module/commModule/error.vw");
					//requestDispatcher.forward(req, res);
					//res.sendRedirect(req.getContextPath() +  "/secure/error.vw?ERROR_MESSAGE="+ fileUpload.getMessageString());
					res.sendRedirect(req.getContextPath() + "/bsc/module/commModule/errorFilePage.vw?ErrorNumber=" + fileUpload.getMessageNumber() + "&amp;ErrorMessage=" + URLEncoder.encode(fileUpload.getMessageString(), "UTF-8"));
				}catch(Exception e){
					e.printStackTrace();
				}
			} else {
				ArrayList fileList = fileUpload.getFileInfoList();
				if(fileList != null && fileList.size() > 0){
					this.put("FileInfoList", fileUpload.getFileInfoList());
					for(int i=0; i<fileList.size(); i++){
						FileInfoVO fileInfoVO = (FileInfoVO)fileList.get(i);
						this.put(fileInfoVO.getFileObjectName(), fileInfoVO);
					}
				}
			}
		}
		if(this.get("sortCol") != null && 
				this.get("sortMode") != null
				&& "java.lang.String".equals(this.get("sortCol").getClass().getName())
				&& "java.lang.String".equals(this.get("sortMode").getClass().getName())
				){
			this.put("findSortCol", "" + this.get("sortCol"));
			this.put("findSortMode", "" + this.get("sortMode"));
		}

        //-----------------------------------------------------------------------
        // 내부망사용자여부 확인(IP가 10으로 시작되는 경우)
        //-----------------------------------------------------------------------
        String temp = "";
        temp = req.getHeader("WL-Proxy-Client-IP");
        if(temp == null || "".equals(temp)){
            temp = req.getRemoteAddr();
        }
        this.put("clientIp", temp);
        
        temp = temp.substring(0, 2);
        if("10".equals(temp)){
	        this.put("inNetUserYn", "Y");
        } else {
	        this.put("inNetUserYn", "N");
        }
        
        //-----------------------------------------------------------------------
        // sql injection 방지 로직
        //-----------------------------------------------------------------------
        
        boolean injectionYn = false;
        if(this.get("findSortCol") != null &&
				this.get("findSortMode") != null
				&& "java.lang.String".equals(this.get("findSortCol").getClass().getName())
				&& "java.lang.String".equals(this.get("findSortMode").getClass().getName())
				)
        {
            injectionYn = true;
            String tmpFindSortCol =  " " + this.get("findSortCol") + " ";

            tmpFindSortCol = tmpFindSortCol.replaceAll("\r" ," " ).replaceAll("\n" ," " ).replaceAll("\t" ," " ).toLowerCase();

            // injection 에 들어올 sql 구문   
            ArrayList<String> injectionArry =   new ArrayList<String> ();
            injectionArry.add(" and ");
            injectionArry.add(" or ");
            injectionArry.add(" select ");
            injectionArry.add(" insert ");
            injectionArry.add(" update ");
            injectionArry.add(" delete ");
            injectionArry.add(" truncate ");
            injectionArry.add(" drop ");
            injectionArry.add(" where ");
            injectionArry.add(" not ");

            for ( int i =0 ; i < injectionArry.size() ; i ++ )
            {
                int cnt = tmpFindSortCol.indexOf ((String)injectionArry.get(i)) ;

                if (cnt >= 0) {
                    injectionYn = false;
                    break;
                }
            }
        }

        if (injectionYn )
        {
			String sortQueryString = "";
			if((this.get("goSub") == null && !"Y".equals("" + this.get("goSub"))) &&
					!"".equals("" + this.get("findSortCol")) &&
					!"".equals("" + this.get("findSortMode"))
					){
				if("DESC".equals(this.get("findSortMode"))){
					sortQueryString = "ORDER BY " + this.getString("findSortCol") + " " + this.getString("findSortMode") + " NULLS LAST";
				}else{
					sortQueryString = "ORDER BY " + this.getString("findSortCol") + " " + this.getString("findSortMode");
				}
			}else{
				sortQueryString = "";
			}
			
			this.put("sortQueryString", sortQueryString);
		}else{
			this.put("sortQueryString", "");
		}
		//logWrite();
        
        //이전페이지 정보를 기억한다.
        makeUrlParamFindHistory();
		
		//현재 URL에서 PACKAGE와 CLASS명을 가져온다.
	    setUrlParam();

	}
	
	
	/**
	 * toKorean
	 * @param str
	 * @return String
	 * @throws   
	*/
	public String toKorean(String str)
	{		
		if (str == null) return str;
		String result = null;
		String userCharset = req.getCharacterEncoding();

		try {
			String usrAgent = req.getHeader("user-agent");
			String[] acceptLang = req.getHeader("accept-language").split(",");
			if("GET".equals(m_method) && (usrAgent.indexOf("MSIE") > -1 || usrAgent.indexOf("Chrome") > -1) && !"ko-KR".equals(acceptLang[0])) {
				result = java.net.URLDecoder.decode(str, "UTF-8"); 
			} else {
				if ("UTF-8".equalsIgnoreCase(userCharset)) {
					result = java.net.URLDecoder.decode(str, "UTF-8");
				} else {
					result = new String(str.getBytes("8859_1"), "UTF-8");
					
				}
			}
		} catch (Exception e) {
			//dummy
		}
		return result;
	}

	
	/**
	 * toEnglish
	 * @param str
	 * @return String
	 * @throws   
	*/
	public String toEnglish(String str)
	{
		if (str == null) return str;
		String result = null;
		
		try {
			result = new String(str.getBytes("UTF-8"), "8859_1");
		} catch (Exception e) {
			//dummy
		}
		return result;
	}
	
	/**
	 * log
	 * @param str
	 * @return String
	 * @throws   
	*/
	public void logWrite()
	{
		logger.debug("==============parameter info start=================" );
		logger.debug("METHOD: " + m_method);
		logger.debug("USER-AGENT: " + req.getHeader("user-agent"));
		logger.debug("Accept-Language: " + req.getHeader("accept-language"));
		for (Object key : this.keySet()) {
			
			Object value = this.get(key);
			
			if(value != null){
			
				if("java.lang.String".equals(value.getClass().getName())){
					if("GET".equals(m_method)){
						if(m_url.indexOf("localhost") > 0)   
							logger.debug("[" + value.getClass().getName() + "]" + key + " : " + value);
						else											// 서버(제우스)
							logger.debug("[" + value.getClass().getName() + "]" + key + " : " + value);
					}else{
						if(m_url.indexOf("localhost") > 0)   
							logger.debug("[" + value.getClass().getName() + "]" + key + " : " + value);
						else											// 서버(제우스)
							logger.debug("[" + value.getClass().getName() + "]" + key + " : " + value);
					}
				}else if("[Ljava.lang.String;".equals(value.getClass().getName())){
					String[] temp = (String[])value;
					for (int i = 0; i < temp.length; i++) {
						if("GET".equals(m_method)){
							if(m_url.indexOf("localhost") > 0)   
								logger.debug("[" + value.getClass().getName() + "]" + key + " : " + temp[i]);
							else											// 서버(제우스)
								logger.debug("[" + value.getClass().getName() + "]" + key + " : " + temp[i]);    
						}else{
							if(m_url.indexOf("localhost") > 0)   
								logger.debug("[" + value.getClass().getName() + "]" + key + " : " + temp[i]);
							else											// 서버(제우스)
								logger.debug("[" + value.getClass().getName() + "]" + key + " : " + temp[i]);    
						}
						
					}
				}else if("java.util.ArrayList".equals(value.getClass().getName())){
					/*
					ArrayList temp = (ArrayList)value;
					if(temp != null && temp.size() > 0){
						if("kr.co.lexken.common.FileInfoVO".equals(temp.get(0).getClass().getName())){
							logger.debug("==============File info start=================" );
							for (int i = 0; i < temp.size(); i++) {
								FileInfoVO fileInfoVO = (FileInfoVO)temp.get(i);
								
								logger.debug("========" + i + "===========");
								if("GET".equals(m_method)){
									if(m_url.indexOf("localhost") > 0){   
										logger.debug("FileObjectName : " + fileInfoVO.getFileObjectName());
										logger.debug("FileUploadPath : " + fileInfoVO.getFileUploadPath());
										logger.debug("MaskFileName : " + fileInfoVO.getMaskFileName());
										logger.debug("RealFileName : " + fileInfoVO.getRealFileName());
										logger.debug("Width : " + fileInfoVO.getWidth());
										logger.debug("Height : " + fileInfoVO.getHeight());
										logger.debug("Size : " + fileInfoVO.getSize());
									}else{											// 서버(제우스)
										logger.debug("FileObjectName : " + fileInfoVO.getFileObjectName());
										logger.debug("FileUploadPath : " + fileInfoVO.getFileUploadPath());
										logger.debug("MaskFileName : " + fileInfoVO.getMaskFileName());
										logger.debug("RealFileName : " + fileInfoVO.getRealFileName());
										logger.debug("Width : " + fileInfoVO.getWidth());
										logger.debug("Height : " + fileInfoVO.getHeight());
										logger.debug("Size : " + fileInfoVO.getSize());
									}
								}else{
									if(m_url.indexOf("localhost") > 0){   
										logger.debug("FileObjectName : " + fileInfoVO.getFileObjectName());
										logger.debug("FileUploadPath : " + fileInfoVO.getFileUploadPath());
										logger.debug("MaskFileName : " + fileInfoVO.getMaskFileName());
										logger.debug("RealFileName : " + fileInfoVO.getRealFileName());
										logger.debug("Width : " + fileInfoVO.getWidth());
										logger.debug("Height : " + fileInfoVO.getHeight());
										logger.debug("Size : " + fileInfoVO.getSize());
									}else{											// 서버(제우스)
										logger.debug("FileObjectName : " + fileInfoVO.getFileObjectName());
										logger.debug("FileUploadPath : " + fileInfoVO.getFileUploadPath());
										logger.debug("MaskFileName : " + fileInfoVO.getMaskFileName());
										logger.debug("RealFileName : " + fileInfoVO.getRealFileName());
										logger.debug("Width : " + fileInfoVO.getWidth());
										logger.debug("Height : " + fileInfoVO.getHeight());
										logger.debug("Size : " + fileInfoVO.getSize());
									}
								}
								
							}
							logger.debug("==============File info end=================" );
						}
					}
					*/
				}else if("kr.co.lexken.common.FileInfoVO".equals(value.getClass().getName())){
					logger.debug("==============File info start=================" );
						FileInfoVO fileInfoVO = (FileInfoVO)value;
						if("GET".equals(m_method)){
							if(m_url.indexOf("localhost") > 0){   
								logger.debug("FileObjectName : " + fileInfoVO.getFileObjectName());
								logger.debug("FileUploadPath : " + fileInfoVO.getFileUploadPath());
								logger.debug("MaskFileName : " + fileInfoVO.getMaskFileName());
								logger.debug("RealFileName : " + fileInfoVO.getRealFileName());
								logger.debug("Width : " + fileInfoVO.getWidth());
								logger.debug("Height : " + fileInfoVO.getHeight());
								logger.debug("Size : " + fileInfoVO.getSize());
							}else{											// 서버(제우스)
								logger.debug("FileObjectName : " + fileInfoVO.getFileObjectName());
								logger.debug("FileUploadPath : " + fileInfoVO.getFileUploadPath());
								logger.debug("MaskFileName : " + fileInfoVO.getMaskFileName());
								logger.debug("RealFileName : " + fileInfoVO.getRealFileName());
								logger.debug("Width : " + fileInfoVO.getWidth());
								logger.debug("Height : " + fileInfoVO.getHeight());
								logger.debug("Size : " + fileInfoVO.getSize());
							}
						}else{
							if(m_url.indexOf("localhost") > 0){   
								logger.debug("FileObjectName : " + fileInfoVO.getFileObjectName());
								logger.debug("FileUploadPath : " + fileInfoVO.getFileUploadPath());
								logger.debug("MaskFileName : " + fileInfoVO.getMaskFileName());
								logger.debug("RealFileName : " + fileInfoVO.getRealFileName());
								logger.debug("Width : " + fileInfoVO.getWidth());
								logger.debug("Height : " + fileInfoVO.getHeight());
								logger.debug("Size : " + fileInfoVO.getSize());
							}else{											// 서버(제우스)
								logger.debug("FileObjectName : " + fileInfoVO.getFileObjectName());
								logger.debug("FileUploadPath : " + fileInfoVO.getFileUploadPath());
								logger.debug("MaskFileName : " + fileInfoVO.getMaskFileName());
								logger.debug("RealFileName : " + fileInfoVO.getRealFileName());
								logger.debug("Width : " + fileInfoVO.getWidth());
								logger.debug("Height : " + fileInfoVO.getHeight());
								logger.debug("Size : " + fileInfoVO.getSize());
							}
						}
					logger.debug("==============File info end=================" );
				}else if("java.lang.Integer".equals(value.getClass().getName())){
					if("GET".equals(m_method)){
						if(m_url.indexOf("localhost") > 0)   
							logger.debug("[" + value.getClass().getName() + "]" + key + " : " + value);
						else											// 서버(제우스)
							logger.debug("[" + value.getClass().getName() + "]" + key + " : " + value);
					}else{
						if(m_url.indexOf("localhost") > 0)   
							logger.debug("[" + value.getClass().getName() + "]" + key + " : " + value);
						else											// 서버(제우스)
							logger.debug("[" + value.getClass().getName() + "]" + key + " : " + value);
					}
				}else{
					try{
						logger.debug("[" + value.getClass().getName() + "]" + key + " : " + value);
					} catch (Exception e) {   
						e.printStackTrace(); 
						logger.error(e.toString());
					}
				}
			}else{
				logger.debug("[null]" + key + " : null");
			}
			
		}
		
		logger.debug("==============parameter info end===================" );
		
	}
	
	/**
	 * makeHidden
	 * @param 
	 * @return String
	 * @throws   
	*/
	public String makeHidden(){
		String resultHtml = "";

		for (Object key : this.keySet()) {
			
			Object value = this.get(key);
			
			if(value != null){
				
				if("java.lang.String".equals(value.getClass().getName())){
					if("GET".equals(m_method)){
						if(m_url.indexOf("localhost") > 0)   
							resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(value) + "' />\n";
						else											// 서버(제우스)
							resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(value) + "' />\n";
					}else{
						if(m_url.indexOf("localhost") > 0)   
							resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(value) + "' />\n";
						else											// 서버(제우스)
							resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(value) + "' />\n";
					}
				}else if("[Ljava.lang.String;".equals(value.getClass().getName())){
					String[] temp = (String[])value;
					for (int i = 0; i < temp.length; i++) {
						if("GET".equals(m_method)){
							if(m_url.indexOf("localhost") > 0)   
								resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(temp[i]) + "' />\n";
							else											// 서버(제우스)
								resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(temp[i]) + "' />\n";    
						}else{
							if(m_url.indexOf("localhost") > 0)   
								resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(temp[i]) + "' />\n";
							else											// 서버(제우스)
								resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(temp[i]) + "' />\n";    
						}
						
					}
				}else if("java.lang.Integer".equals(value.getClass().getName())){
					if("GET".equals(m_method)){
						if(m_url.indexOf("localhost") > 0)   
							resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(value) + "' />\n";
						else											// 서버(제우스)
							resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(value) + "' />\n";
					}else{
						if(m_url.indexOf("localhost") > 0)   
							resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(value) + "' />\n";
						else											// 서버(제우스)
							resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(value) + "' />\n";
					}
				}else{
					
				}
			}
			
		}
		return resultHtml;
	}
	
	/**
	 * makeUrlParam
	 * @param 
	 * @return String
	 * @throws   
	*/
	public String makeUrlParam(){
		String resultHtml = "";
		String prefix = "";

		for (Object key : this.keySet()) {
			
			Object value = this.get(key);
			
			if(value != null){
				if("".equals(resultHtml)){
					prefix = "";
				}else{
					prefix = "&amp;";
				}
				
				if("java.lang.String".equals(value.getClass().getName())){
					if("GET".equals(m_method)){
						if(m_url.indexOf("localhost") > 0)   
							resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
						else											// 서버(제우스)
							resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
					}else{
						if(m_url.indexOf("localhost") > 0)   
							resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
						else											// 서버(제우스)
							resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
					}
				}else if("[Ljava.lang.String;".equals(value.getClass().getName())){
					String[] temp = (String[])value;
					for (int i = 0; i < temp.length; i++) {
						
						if("".equals(resultHtml)){
							prefix = "";
						}else{
							prefix = "&amp;";
						}
						
						if("GET".equals(m_method)){
							if(m_url.indexOf("localhost") > 0)   
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(temp[i]);
							else											// 서버(제우스)
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(temp[i]);    
						}else{
							if(m_url.indexOf("localhost") > 0)   
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(temp[i]);
							else											// 서버(제우스)
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(temp[i]);    
						}
						
					}
				}else if("java.lang.Integer".equals(value.getClass().getName())){
					if("GET".equals(m_method)){
						if(m_url.indexOf("localhost") > 0)   
							resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
						else											// 서버(제우스)
							resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
					}else{
						if(m_url.indexOf("localhost") > 0)   
							resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
						else											// 서버(제우스)
							resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
					}
				}else{
					
				}
			}
			
		}
		return resultHtml;
	}
	
	/**
	 * makeHiddenFind
	 * @param 
	 * @return String
	 * @throws   
	*/
	public String makeHiddenFind(){
		String resultHtml = "";
		/*
		if(this.get("menuId") != null){
			resultHtml += "<input type='hidden' name='menuId' value='" + HtmlHelper.txt2html(this.get("menuId")) + "' />\n";
		}
		
		if(this.get("beginRow") != null){
			resultHtml += "<input type='hidden' name='beginRow' value='" + HtmlHelper.txt2html(this.get("beginRow")) + "' />\n";
		}
		
		if(this.get("rowPerPage") != null){
			resultHtml += "<input type='hidden' name='rowPerPage' value='" + HtmlHelper.txt2html(this.get("rowPerPage")) + "' />\n";
		}
		*/
		for (Object key : this.keySet()) {
			
			Object value = this.get(key);
			
			if(value != null){
			
				if((((String)key).toUpperCase()).indexOf("FIND") > -1 || 
						(((String)key).toUpperCase()).indexOf("MPARM") > -1 ){
					
					if("java.lang.String".equals(value.getClass().getName())){
						if("GET".equals(m_method)){
							if(m_url.indexOf("localhost") > 0)   
								resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(value) + "' />\n";
							else											// 서버(제우스)
								resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(value) + "' />\n";
						}else{
							if(m_url.indexOf("localhost") > 0)   
								resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(value) + "' />\n";
							else											// 서버(제우스)
								resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(value) + "' />\n";
						}
					}else if("[Ljava.lang.String;".equals(value.getClass().getName())){
						String[] temp = (String[])value;
						for (int i = 0; i < temp.length; i++) {
							if("GET".equals(m_method)){
								if(m_url.indexOf("localhost") > 0)   
									resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(temp[i]) + "' />\n";
								else											// 서버(제우스)
									resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(temp[i]) + "' />\n";    
							}else{
								if(m_url.indexOf("localhost") > 0)   
									resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(temp[i]) + "' />\n";
								else											// 서버(제우스)
									resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(temp[i]) + "' />\n";    
							}
							
						}
					}else if("java.lang.Integer".equals(value.getClass().getName())){
						if("GET".equals(m_method)){
							if(m_url.indexOf("localhost") > 0)   
								resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(value) + "' />\n";
							else											// 서버(제우스)
								resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(value) + "' />\n";
						}else{
							if(m_url.indexOf("localhost") > 0)   
								resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(value) + "' />\n";
							else											// 서버(제우스)
								resultHtml += "<input type='hidden' name='" +key+ "' value='" + HtmlHelper.txt2html(value) + "' />\n";
						}
					}else{
						
					}
				}
			}
			
		}
		return resultHtml;
	}
	
	/**
	 * history param
	 * @param 
	 * @return String
	 * @throws   
	*/
	public void makeUrlParamFindHistory(){
		String refer = req.getHeader("referer");
		
		if(null != refer) {
			String historyUrl = refer.split("\\?")[0];
			
			HttpSession session = req.getSession();
			session.setMaxInactiveInterval(1*60*60);	//세션 1시간 설정
			
			HashMap historyMap = null;
			
			if(session.getAttribute("historyMap") != null){
				historyMap = (HashMap)session.getAttribute("historyMap");
			} else {
				historyMap = new HashMap();
			}
			
			historyMap.put(historyUrl, makeHiddenFind());
			
			session.setAttribute("historyMap", historyMap);
		}
	}
	
	/**
	 * history param
	 * @param 
	 * @return String
	 * @throws   
	*/
	public String getUrlParamFindHistory(String url){
		HttpSession session = req.getSession();
		
		HashMap historyMap = null;
		
		if(session.getAttribute("historyMap") != null){
			historyMap = (HashMap)session.getAttribute("historyMap");
		} else {
			historyMap = new HashMap();
		}
		
		String param = StaticUtil.nullToBlank((String)historyMap.get(url));
		
		return param;
	}
	
	
	/**
	 * makeUrlParamFind
	 * @param 
	 * @return String
	 * @throws   
	*/
	public String makeUrlParamFind(){
		String resultHtml = "";
		String prefix = "";
		
		if(this.get("menuId") != null){
			if("".equals(resultHtml)){
				resultHtml += "menuId=" + HtmlHelper.txt2html(this.get("menuId"));
			}else{
				resultHtml += "&amp;menuId=" + HtmlHelper.txt2html(this.get("menuId"));
			}
		}
		
		if(this.get("beginRow") != null){
			if("".equals(resultHtml)){
				resultHtml += "beginRow=" + HtmlHelper.txt2html(this.get("beginRow"));
			}else{
				resultHtml += "&amp;beginRow=" + HtmlHelper.txt2html(this.get("beginRow"));
			}
		}
		
		if(this.get("rowPerPage") != null){
			if("".equals(resultHtml)){
				resultHtml += "rowPerPage=" + HtmlHelper.txt2html(this.get("rowPerPage"));
			}else{
				resultHtml += "&amp;rowPerPage=" + HtmlHelper.txt2html(this.get("rowPerPage"));
			}
		}

		for (Object key : this.keySet()) {
			Object value = this.get(key);
			
			if(value != null){
			
				if((((String)key).toUpperCase()).indexOf("FIND") > -1 || 
						(((String)key).toUpperCase()).indexOf("MPARM") > -1 ){
					
					if("".equals(resultHtml)){
						prefix = "";
					}else{
						prefix = "&amp;";
					}
					
					if("java.lang.String".equals(value.getClass().getName())){
						if("GET".equals(m_method)){
							try{
								if(m_url.indexOf("localhost") > 0)   
									resultHtml += prefix + key + "=" + URLEncoder.encode(HtmlHelper.txt2html(value), "UTF-8");
								else											// 서버(제우스)
									resultHtml += prefix + key + "=" + URLEncoder.encode(HtmlHelper.txt2html(value), "UTF-8");
							}catch(Exception e){
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
								e.printStackTrace();
							}
						}else{
							if(m_url.indexOf("localhost") > 0)   
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
							else											// 서버(제우스)
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
						}
					}else if("[Ljava.lang.String;".equals(value.getClass().getName())){
						String[] temp = (String[])value;
						for (int i = 0; i < temp.length; i++) {
							
							if("".equals(resultHtml)){
								prefix = "";
							}else{
								prefix = "&amp;";
							}
							
							if("GET".equals(m_method)){
								try{
									if(m_url.indexOf("localhost") > 0)   
										resultHtml += prefix + key + "=" + URLEncoder.encode(HtmlHelper.txt2html(temp[i]), "UTF-8");
									else											// 서버(제우스)
										resultHtml += prefix + key + "=" + URLEncoder.encode(HtmlHelper.txt2html(temp[i]), "UTF-8");
								}catch(Exception e){
									resultHtml += prefix + key + "=" + HtmlHelper.txt2html(temp[i]);
									e.printStackTrace();
								}
							}else{
								if(m_url.indexOf("localhost") > 0)   
									resultHtml += prefix + key + "=" + HtmlHelper.txt2html(temp[i]);
								else											// 서버(제우스)
									resultHtml += prefix + key + "=" + HtmlHelper.txt2html(temp[i]);    
							}
							
						}
					}else if("java.lang.Integer".equals(value.getClass().getName())){
						if("GET".equals(m_method)){
							if(m_url.indexOf("localhost") > 0)   
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
							else											// 서버(제우스)
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
						}else{
							if(m_url.indexOf("localhost") > 0)   
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
							else											// 서버(제우스)
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
						}
					}else{
						
					}
				}
			}
		}
		return resultHtml;
	}
	
	/**
	 * makeUrlParamFind
	 * @param 
	 * @return String
	 * @throws   
	*/
	public String makeUrlParamFindExceptBeginRow(){
		String resultHtml = "";
		String prefix = "";
		
		if(this.get("menuId") != null){
			if("".equals(resultHtml)){
				resultHtml += "menuId=" + HtmlHelper.txt2html(this.get("menuId"));
			}else{
				resultHtml += "&amp;menuId=" + HtmlHelper.txt2html(this.get("menuId"));
			}
		}
		
		if(this.get("rowPerPage") != null){
			if("".equals(resultHtml)){
				resultHtml += "rowPerPage=" + HtmlHelper.txt2html(this.get("rowPerPage"));
			}else{
				resultHtml += "&amp;rowPerPage=" + HtmlHelper.txt2html(this.get("rowPerPage"));
			}
		}
		
		for (Object key : this.keySet()) {
			Object value = this.get(key);
			
			if(value != null){
				if((((String)key).toUpperCase()).indexOf("FIND") > -1 || 
						(((String)key).toUpperCase()).indexOf("MPARM") > -1 ){
					
					if("".equals(resultHtml)){
						prefix = "";
					}else{
						prefix = "&amp;";
					}
					
					if("java.lang.String".equals(value.getClass().getName())){
						if("GET".equals(m_method)){
							if(m_url.indexOf("localhost") > 0)   
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
							else											// 서버(제우스)
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
						}else{
							if(m_url.indexOf("localhost") > 0)   
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
							else											// 서버(제우스)
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
						}
					}else if("[Ljava.lang.String;".equals(value.getClass().getName())){
						String[] temp = (String[])value;
						for (int i = 0; i < temp.length; i++) {
							
							if("".equals(resultHtml)){
								prefix = "";
							}else{
								prefix = "&amp;";
							}
							
							if("GET".equals(m_method)){
								if(m_url.indexOf("localhost") > 0)   
									resultHtml += prefix + key + "=" + HtmlHelper.txt2html(temp[i]);
								else											// 서버(제우스)
									resultHtml += prefix + key + "=" + HtmlHelper.txt2html(temp[i]);    
							}else{
								if(m_url.indexOf("localhost") > 0)   
									resultHtml += prefix + key + "=" + HtmlHelper.txt2html(temp[i]);
								else											// 서버(제우스)
									resultHtml += prefix + key + "=" + HtmlHelper.txt2html(temp[i]);    
							}
							
						}
					}else if("java.lang.Integer".equals(value.getClass().getName())){
						if("GET".equals(m_method)){
							if(m_url.indexOf("localhost") > 0)   
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
							else											// 서버(제우스)
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
						}else{
							if(m_url.indexOf("localhost") > 0)   
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
							else											// 서버(제우스)
								resultHtml += prefix + key + "=" + HtmlHelper.txt2html(value);
						}
					}else{
						
					}
				}
			}
		}
		return resultHtml;
	}
	
	
	
	/**  
	 * 파일 복사  
	 * @param source 복사원본파일의 폴더경로(ex) /temp)   
	 * @param target 복사사본파일의 폴더경로(ex) /bbs)  
	 */ 
	public void fileCopy(String source, String target) {  
		FileHelper csFileHelper = new FileHelper();
		ArrayList fileInfoList = (ArrayList)this.get("FileInfoList");
		
		// 파일정보 없는 경우 중단
		if(fileInfoList == null || fileInfoList.size() < 1){
			return;
		}
		
		FileInfoVO fileInfoVO = null;
		
		//root path
		FileConfig fileConfig		= FileConfig.getInstance();
		String  stRealRootPath      = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");  // D:/00.workspace/00.bsc_workspace/BSC/WebContent
		String	stFileRootPath 	  	= fileConfig.getProperty("FILE_ROOT_PATH");            // /storage
		//ServletContext servletContext = req.getSession().getServletContext();
		String	stLocalFileRootPath = stFileRootPath;
		stLocalFileRootPath         = stRealRootPath + stFileRootPath;	
		stLocalFileRootPath         = stLocalFileRootPath.replace("\\", "/");
		String tempFile             = null;  // temp 파일 저장 경로 
	    String saveFile             = null;  // 실제로 저장된 파일의 경로 
	    String stMaskFileName       = null;
		
		//today 
		CalendarHelper csCal		= new CalendarHelper();
		String 	stToday				= csCal.getStrDate();	// 오늘 날짜( YYYYMMDD )
		
		//make folder
		String folderStr            = target + "/" +stToday +"/";
		File urlFile                = new File(stLocalFileRootPath + folderStr);
		urlFile.mkdirs();
		
		try {
			
			if(fileInfoList != null && fileInfoList.size() > 0){
				
				for(int i=0; i<fileInfoList.size(); i++){
					
					fileInfoVO = (FileInfoVO)fileInfoList.get(i);
					
					//file 
					tempFile = fileInfoVO.getFileUploadPath();
					tempFile = tempFile.replaceAll("/storage", "");
					
					//full url
					tempFile = stLocalFileRootPath + tempFile;
					saveFile = stLocalFileRootPath + folderStr + fileInfoVO.getMaskFileName();
					
					stMaskFileName = csFileHelper.copyFile(
							tempFile,
							saveFile,
                          	false, true );
					
					fileInfoVO.setMaskFileName(stMaskFileName);
					fileInfoVO.setFileUploadPath(stFileRootPath + folderStr + stMaskFileName);
					this.put(fileInfoVO.getFileObjectName(), fileInfoVO);
				}
			}
		} catch (Exception e) {   
			e.printStackTrace(); 
			logger.error(e.toString());
		} finally {   
		}// try- catch- finally

	}
	
	/**  
	 * 파일 복사  
	 * @param source 복사원본파일의 폴더경로(ex) /temp)   
	 * @param target 복사사본파일의 폴더경로(ex) /bbs)  
	 */ 
	public void fileCopyBackground(String source, String fileName, String year, String scDeptId) {  
		FileHelper csFileHelper = new FileHelper();
		ArrayList fileInfoList = (ArrayList)this.get("FileInfoList");
		
		// 파일정보 없는 경우 중단
		if(fileInfoList == null || fileInfoList.size() < 1){
			return;
		}
		
		FileInfoVO fileInfoVO = null;
		
		//root path
		FileConfig fileConfig		= FileConfig.getInstance();
		String  stRealRootPath      = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");  	 // D:/00.workspace/00.bsc_workspace/BSC/WebContent
		String  stWebRealRootPath   = fileConfig.getProperty("FILE_WEB_ROOT_PATH");          // D:/00.workspace/00.bsc_workspace/BSC/WebContent
		String	stFileRootPath 	  	= fileConfig.getProperty("FILE_ROOT_PATH");              // /storage
		String	stBackgroundPath 	= fileConfig.getProperty("FILE_BACKGROUND_ROOT_PATH");   // /images/flash/VBOXML
		//ServletContext servletContext = req.getSession().getServletContext();
		String	stLocalFileRootPath = stRealRootPath + stFileRootPath;
		String	stLocalBackgroundRootPath = stWebRealRootPath + stBackgroundPath;	
		//String	stLocalBackgroundRootPath = "/package/kgshome/kgsDomain7" + stBackgroundPath //운영반영 때만
		stLocalFileRootPath         = stLocalFileRootPath.replace("\\", "/");
		String tempFile             = null;  // temp 파일 저장 경로 
	    String saveFile             = null;  // 실제로 저장된 파일의 경로 
	    String stMaskFileName       = null;
	    String postFix="_";
	    
	    if("VBOX_STRATEGY_BG".equals(fileName)){
	    	postFix="";
	    }
	    
	    String saveTargetFileName   = fileName + postFix + year + scDeptId + ".jpg";
		File urlFile                = new File(stLocalFileRootPath);
		
		try {
			
			if(fileInfoList != null && fileInfoList.size() > 0){
				
				for(int i=0; i<fileInfoList.size(); i++){
					
					fileInfoVO = (FileInfoVO)fileInfoList.get(i);

					//file 
					tempFile = fileInfoVO.getFileUploadPath();
					tempFile = tempFile.replaceAll("/storage", "");
					
					//full url
					tempFile = stLocalFileRootPath + tempFile;
					saveFile = stLocalBackgroundRootPath + "/" + saveTargetFileName;
					
					//System.out.println("tempFile=======>" + tempFile);
					//System.out.println("saveFile=======>" + saveFile);

					csFileHelper.deleteFile(saveFile);
					
					stMaskFileName = csFileHelper.copyFile(tempFile, saveFile, true, true);
					
					fileInfoVO.setMaskFileName(stMaskFileName);
					fileInfoVO.setFileUploadPath(stBackgroundPath + "/" + stMaskFileName);
					
					//System.out.println("stMaskFileName=======>" + stMaskFileName);
					//System.out.println("setFileUploadPath=======>" + stBackgroundPath + "/" + stMaskFileName);
					
					this.put(fileInfoVO.getFileObjectName(), fileInfoVO);
					
					ftpSendFile(stLocalBackgroundRootPath, stMaskFileName, stLocalBackgroundRootPath, stMaskFileName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace(); 
			logger.error(e.toString());
		} finally {   
		}// try- catch- finally
	}
	
	public void ftpSendFile(String sourcePath, String sourceFilename, String targetPath, String targetFilename) {
		FtpUtil ftpUtil = new FtpUtil();
		
		//targetPath = "/www/ftpTest";  //test
		
		CommonConfig commonConfig = CommonConfig.getInstance();
		String server = commonConfig.getProperty("FTP_URL");
		String user = commonConfig.getProperty("FTP_ID");
		String password = commonConfig.getProperty("FTP_PWD");
		
		try {
			ftpUtil.setServer(server);
			ftpUtil.setUser(user);
			ftpUtil.setPassword(password);
			
			ftpUtil.setSourcePath(sourcePath);  
			ftpUtil.setTargetPath(targetPath);  
			
			ftpUtil.setTargetFilename(sourceFilename);
			ftpUtil.setSourceFilename(targetFilename); 
			ftpUtil.upFile();
		} catch (Exception e) {
			e.printStackTrace(); 
			logger.error(e.toString());
		} finally {   
		}
	}
	
	/**  
	 * 파일 삭제  
	 * @param name 삭제할 파일명을 포함한 절대 경로   
	 */ 
	public void fileDelete(String name) {  

		try {
			
			File file                  = new File(name);
			file.delete();
		} catch (Exception e) {   
			e.printStackTrace(); 
			logger.error(e.toString());
		} finally {   
		}// try- catch- finally

	}
	
	/**  
	 * 문자열 객체 가져오기  
	 * @param key hashMap key
	 */ 
	public String getString(Object key){
		if(this.get(key) != null){
			try{
				return (String)this.get(key);
			}catch(Exception e){
				try{
					return "" + this.get(key);
				}catch(Exception ex){
					
				}
			}
		}
		return "";
		
	}
	
	/**  
	 * 문자열 객체 가져오기  
	 * @param key hashMap key
	 */ 
	public String getString(Object key, String intiData){
		String result = this.getString(key);

		if(result == null || "".equals(result)){
			result = intiData;
		}
		return result;
		
	}
	
	/**  
	 * 문자열 객체 가져오기(txt -> html)  
	 * @param key hashMap key
	 */ 
	public String txt2html(Object key){
		return HtmlHelper.txt2html(this.getString(key));
	}
	
	/**  
	 * 문자열 객체 가져오기(txt -> html)  
	 * @param key hashMap key
	 */ 
	public String txt2html(Object key, String intiData){
		return HtmlHelper.txt2html(this.getString(key, intiData));
	}
	
	/**  
	 * 문자열 배열 객체 가져오기  
	 * @param key hashMap key
	 */ 
	public String[] getStringArray(Object key){
		Object temp = this.get(key);
		String[] result = null;
		if(temp != null){
			if(temp.getClass().getName().indexOf("java.lang.String") > -1){
				if(temp != null){
					if(temp.getClass().isArray()){
						result = (String[])temp;
					}else{
						result = new String[1];
						result[0] = (String)temp;
					}
				}
			}else if(temp.getClass().getName().indexOf("java.lang.Integer") > -1){
				if(temp != null){
					if(temp.getClass().isArray()){
						int[] tempInt = (int[])temp;
						result = new String[tempInt.length];
						for(int i=0; i<tempInt.length; i++){
							result[i] = "" + tempInt[i];
						}
					}else{
						result = new String[1];
						result[0] = "" + (Integer)temp;
					}
				}
			}
		}
		return result;
	}
		
	/**  
	 * int 객체 가져오기  
	 * @param key hashMap key
	 */ 
	public int getInt(Object key){
		if(this.get(key) != null){
			if("java.lang.String".equals(this.get(key).getClass().getName())){
				return Integer.parseInt((String)this.get(key));
			}else{
				return (Integer)this.get(key);
			}
		}else{
			return 0;
		}
	}
	
	/**  
	 * int 객체 가져오기  
	 * @param key hashMap key
	 */ 
	public int getInt(Object key, int intiData){
		if(this.get(key) != null && !"".equals(this.get(key))){
			if("java.lang.String".equals(this.get(key).getClass().getName())){
				return Integer.parseInt((String)this.get(key));
			}else{
				return (Integer)this.get(key);
			}
		}else{  
			return intiData;
		}
	}
	
	/**  
	 * query sort 문자열 가져오기  
	 * @param 
	 */ 
	public String getSortQueryString(){
		String result = "";
		if(this.get("sortQueryString") != null){
			result = (String)this.get("sortQueryString");
		}
		return result;
	}
	
	/**  
	 * query sort make 문자열 가져오기  
	 * @param 
	 */ 
	public String getSortMark(String colName){
		String result = "";
		String sortColName = "";
		String sortMode = "";
		if(this.get("findSortCol") != null && this.get("findSortMode") != null){
			sortColName = (String)this.get("findSortCol");
			sortMode = (String)this.get("findSortMode");
			if(colName.equals(sortColName)){
				if("ASC".equals(sortMode.toUpperCase())){
					result = "▲";
				}else{
					result = "▼";
				}
			}
		}
		return result;
	}
	
	/**  
	 * select 선택 문자열 가져오기  
	 * @param 
	 */
	public String getSelected(String colName, String selectedValue){
		String result = "";
		String temp = "";
		if(this.get(colName) != null){
			if("java.lang.String".equals(this.get(colName).getClass().getName())){
				temp = (String)this.get(colName);
			}else if("java.lang.Integer".equals(this.get(colName).getClass().getName())){
				temp = "" + this.get(colName);
			}else{
				return result;
			}
			if(temp != null && selectedValue.equals(temp)){
				result = " selected=\"selected\" ";
			}
		}
		return result;
	}
	
	/**  
	 * check 선택 문자열 가져오기  
	 * @param 
	 */ 
	public String getChecked(String colName, String selectedValue){
		String result = "";
		String temp = "";
		if(this.get(colName) != null){
			if("java.lang.String".equals(this.get(colName).getClass().getName())){
				temp = (String)this.get(colName);
			}else if("java.lang.Integer".equals(this.get(colName).getClass().getName())){
				temp = "" + this.get(colName);
			}else{
				return result;
			}
			if(temp != null && selectedValue.equals(temp)){
				result = " checked=\"checked\" ";
			}
		}
		return result;
	}
	
	/**  
	 * 숫자만 리턴
	 * @param 
	 */ 
	public String getToNumber(String colName){
		StringBuffer result = new StringBuffer();
		String temp = getString(colName);
		String num = "0123456789";
		
		for (int i=0; i<temp.length(); i++) {
			if (num.indexOf(temp.charAt(i)) > -1) {
				result.append(temp.charAt(i));
			}
		}
		
		return result.toString();
	}
	
	/**  
	 * 숫자만 리턴
	 * @param 
	 */ 
	public String getToNumber(String colName, String initData){
		StringBuffer result = new StringBuffer();
		String resultString = "";
		String temp = getString(colName,initData);
		if(!initData.equals(temp)){		
			String num = "0123456789";
			for (int i=0; i<temp.length(); i++) {
				if (num.indexOf(temp.charAt(i)) > -1) {
					result.append(temp.charAt(i));
				}
			}
			
			resultString = result.toString();
		}else{
			resultString = temp;	
		}
		 
		return resultString;
	}
	
	/**  
     * 원하는 문자를 삭제하여 리턴
     * @param 
     */ 
    public String getReplaceNull(String str, String delStr){
        return str.replace(delStr, "");
    }
    
    /**  
     * varchar4000 처리를 위해 array 로 쪼개서 디비 저장하기 위한 method
     * colName을 받아서 colName + "VarcharArray" 로 생성(List)
     * @param 
     */ 
    public void setVarcharArray(String colName){
    	ArrayList list = new ArrayList();
    	int charLength = 500;
    	
    	Object obj = this.get(colName);
    	if(obj != null && !obj.getClass().isArray()){
    		String temp = this.getString(colName);
    		
    		if(!"".equals(temp)){
    			while (temp.length() > 0) {
    				if(charLength < temp.length()){
    					list.add(temp.substring(0, charLength));
        				temp = temp.substring(charLength);
    				}else{
    					list.add(temp);
    					temp = "";
    				}
    				
    	        }

            }
    	}
    	
    	this.put(colName + "VarcharArray", list);
    }
	
	/**  
	 * 파일 복사(이미지)  
	 * @param source 복사원본파일의 폴더경로(ex) /temp)   
	 * @param target 복사사본파일의 폴더경로(ex) /bbs)  
	 */ 
	public void fileCopyImage(String source, String target) {  
		FileHelper csFileHelper 	= new FileHelper();
		ArrayList fileInfoList = (ArrayList)this.get("FileInfoList");
		
		// 파일정보 없는 경우 중단
		if(fileInfoList == null || fileInfoList.size() < 1){
			return;
		}
		
		FileInfoVO fileInfoVO = null;
		
		//root path
		FileConfig fileConfig		= FileConfig.getInstance();
		String  stRealRootPath      = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		String	stFileRootPath 	  	= fileConfig.getProperty("FILE_ROOT_PATH");

		String  stWebRootPath      = fileConfig.getProperty("FILE_WEB_ROOT_PATH");
		String	stImgRootPath 	  	= fileConfig.getProperty("FILE_IMG_ROOT_PATH");
		
		//ServletContext servletContext = req.getSession().getServletContext();
		String	stLocalFileRootPath   = stFileRootPath;
		stLocalFileRootPath           = stRealRootPath + stFileRootPath;	
		stLocalFileRootPath           = stLocalFileRootPath.replace("\\", "/");

		String	stUpFileRootPath   = stImgRootPath;
		stUpFileRootPath           = stWebRootPath + stImgRootPath;	
		stUpFileRootPath           = stUpFileRootPath.replace("\\", "/");
		
		String tempFile         = null;                 // temp 파일 저장 경로 
	    String saveFile         = null;                 // 실제로 저장된 파일의 경로 
	    String stMaskFileName   = null;
				
		//make folder
		String folderStr              = target + "/";
		File urlFile                  = new File(stUpFileRootPath + folderStr);
		urlFile.mkdirs();
		
		try {
			
			if(fileInfoList != null && fileInfoList.size() > 0){
				
				for(int i=0; i<fileInfoList.size(); i++){
					
					fileInfoVO = (FileInfoVO)fileInfoList.get(i);
					
					//file 
					tempFile = fileInfoVO.getFileUploadPath();
					tempFile = tempFile.replaceAll("/atis", "");
					
					//full url
					tempFile = stLocalFileRootPath + tempFile;
					saveFile = stUpFileRootPath + folderStr + fileInfoVO.getMaskFileName();

					stMaskFileName = csFileHelper.copyFile(
							tempFile,
							saveFile,
                          	false, true );
					
					fileInfoVO.setMaskFileName(stMaskFileName);
					fileInfoVO.setFileUploadPath(stFileRootPath + folderStr + stMaskFileName);
					this.put(fileInfoVO.getFileObjectName(), fileInfoVO);
					
				}
			}
		} catch (Exception e) {   
			e.printStackTrace(); 
			logger.error(e.toString());
		} finally {   
		}// try- catch- finally

	}
	
	/**  
	 *  RDA 암호화 개인키 생성    
	 */ 
	public void getPrivateKey(){

        try{

	            int KEY_SIZE = 1024;
	
	            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
	            generator.initialize(KEY_SIZE);
	            KeyPair keyPair = generator.genKeyPair();
	            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	
	            PublicKey publicKey = keyPair.getPublic();
	            PrivateKey privateKey = keyPair.getPrivate();
	
	            // 세션에 공개키의 문자열을 키로하여 개인키를 저장한다.
	            HttpSession session = req.getSession();
	            session.setAttribute("__rsaPrivateKey__", privateKey);
	
	            // 공개키를 문자열로 변환하여 JavaScript RSA 라이브러리 넘겨준다.
	            RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
	
	            String publicKeyModulus = publicSpec.getModulus().toString(16);
	            String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
	            
	            this.put("publicKeyModulus", publicKeyModulus);
	            this.put("publicKeyExponent", publicKeyExponent);

          }catch(Exception e){
 	    	logger.debug("RSA암호화 개인키 생성 오류");
 	    	e.printStackTrace();
 	    }
	}
	
	/**
	 * RSA 복호화
	 * @param key
	 * @return
	 */
    public String decryptRsa(String key)  {
    	
    	//암호화 키 추출
    	HttpSession session = req.getSession();
        PrivateKey privateKey = (PrivateKey) session.getAttribute(rsaPrivateKey);
        
    	String securedValue = (String)this.getString(key);
        String decryptedValue = "";
        
        try {
            System.out.println("will decrypt : " + securedValue);
            Cipher cipher = Cipher.getInstance("RSA");
            byte[] encryptedBytes = hexToByteArray(securedValue);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
            return decryptedValue;

        }catch (Exception e)
        {

            logger.debug(e.toString());
            logger.debug(e.getMessage());

        }
          return decryptedValue;

    }

    /**
     * 16진 문자열을 byte 배열로 변환한다.
     */
    private static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            return new byte[]{};
        }

        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte)Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }
    
    /**
     * 세션에 저장된 RSA 개인키값을 삭제 - 재사용방지
     * @param key
     */
    public void removeSessionAttr(){
    	HttpSession session = req.getSession();
    	session.removeAttribute(rsaPrivateKey); // 키의 재사용을 막는다. 항상 새로운 키를 받도록 강제.
    	
    }
    
    /**  
     * ACTION 객체에서 리턴값을 지정
     * @param 
     */  
	public void addList(String key, Object value) {
		hash.put(key, value);
	}
	
	/**  
     * ArrayList 객체에서 Default String을 반환
     * @param 
     */
	public String getDefaultValue(String listNm, String columnNm, int index) {
		
		ArrayList list = (ArrayList)hash.get(listNm);
		
		if(null == list || 0 == list.size()) return null;
		
		String value = "";
		HashMap data = new HashMap();
		
		for(int i = 0; i < list.size(); i++) {
			if(i == index) {
				data = (HashMap)list.get(i);
				value = (String)data.get(columnNm);
				break;
			}
		}
		
		return value;
	}
	
	
	/**  
     * 현재 URL에서 PACKAGE와 CLASS명을 가져온다.
     * @param 
     */  
	public void setUrlParam() {
		String url[] = req.getRequestURI().replace('.', '/').split("/");
		
		String methodName = ""; 
		String className = "";
		String packageName = "";
		String systemName = "";
		
		if(url.length > 1) {
			methodName = url[url.length - 2];
		}
		
		if(url.length > 2) {
			className = url[url.length - 3];
		}
		
		if(url.length > 3) {
			packageName = url[url.length - 4];
		}
		
		if(url.length > 4) {
			systemName = url[url.length - 5];
		}
	
		String suffix = "vm";
		
		/*
		logger.debug("==================================================");
		logger.debug("req.getRequestURI() ==> " + req.getRequestURI());
		logger.debug("url[url.length - 2] ==> " + url[url.length - 2]);
		logger.debug("url[url.length - 3] ==> " + url[url.length - 3]);
		logger.debug("url[url.length - 4] ==> " + url[url.length - 4]);
		logger.debug("url[url.length - 5] ==> " + url[url.length - 5]);
		logger.debug("==================================================");
		*/
		
		String classBigName = "";
		String classSmallName = "";
		
		if(null != className && 0 < className.length()) {
			classBigName = className.substring(0,1).toUpperCase() + className.substring(1); 
		}
		
		if(null != className && 0 < className.length()) {
			classSmallName = className.substring(0,1).toLowerCase() + className.substring(1); 
		}
		
		if(!"".equals(StaticUtil.nullToBlank((String)this.get("suffix")))) {
			suffix = (String)this.get("suffix");
		}
		
		String actionName = NAMESPACE + "." + systemName + "." + packageName + "." + classBigName + "Action";
		String mainUrl = "/WEB-INF/vl/" + systemName + "/" + packageName + "/" + classSmallName + "/" + methodName + "." + suffix;
		
		/*
		logger.debug("====================================================================="); 
		logger.debug("req.getRequestURI()===>"+req.getRequestURI());
		logger.debug("methodName===>"+methodName);
		logger.debug("systemName===>"+systemName);
		logger.debug("packageName===>"+packageName);
		logger.debug("className===>"+className);
		logger.debug("classBigName===>"+classBigName);
		logger.debug("classSmallName===>"+classSmallName);
		logger.debug("actionName===>"+actionName);
		logger.debug("mainUrl===>"+mainUrl);
		logger.debug("=====================================================================");
		*/
		
		this.put("methodName", methodName);
		this.put("packageName", packageName);
		this.put("className", className);
		this.put("classBigName", classBigName);
		this.put("classSmallName", classSmallName);
		this.put("actionName", actionName);
		this.put("mainUrl", mainUrl);
	}
}
