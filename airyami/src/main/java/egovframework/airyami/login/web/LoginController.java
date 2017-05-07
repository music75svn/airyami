/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.airyami.login.web;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import egovframework.airyami.cmm.service.CommCodeService;
import egovframework.airyami.cmm.util.AuthCheck;
import egovframework.airyami.cmm.util.CommonUtils;
import egovframework.airyami.cmm.util.ValueMap;
import egovframework.airyami.login.service.LoginService;
import egovframework.com.cmm.EgovMessageSource;



/**
 * 
 * 사이트의 로그인 / 로그아웃을 관리한다.
 * @author 배수한
 * @since 2017.03.30
 * @version 1.0
 * @see
 *
 * <pre>
 *   
 *     수정일                  수정자                              수정내용
 *	-----------    ---------    ---------------------------
 *  2017.03.30      배수한             최초 생성
 *
 * </pre>
 */

@Controller
public class LoginController {
	protected Log log = LogFactory.getLog(this.getClass());
	private Logger logger = Logger.getLogger(this.getClass());
    
	/** LoginService */
    @Resource(name = "loginService")
    private LoginService loginService;
    
    /** CommCodeService */
    @Resource(name = "commCodeService")
    private CommCodeService commCodeService;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
    
    /** Transaction */    
    @Resource(name="txManager")
    PlatformTransactionManager transactionManager;
    
//    @Autowired
//	private DWiseValidator dWiseValidator;
    
    /**
     * admin 로그인페이지 이동 
     */
    @RequestMapping(value="/login/adminLogin.do")
    public String adminlogin(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug(" board.save.success.msg1 :  " + egovMessageSource.getMessage("common.save.msg"));
    	log.info("/login/adminLogin param 1111:: " + params);
    	CommonUtils.setModelByParams(model, params);	
    	
    	
    	ValueMap result = new ValueMap();
    	ValueMap testInfo = loginService.getTest(params);
    	log.info("/login/adminLogin param 2222:: " + testInfo);
    	
    	
    	return "login/adminLogin";
    }
    
    
    /**
     * 로그인페이지 이동 
     */
    @RequestMapping(value="/login/login.do")
    public String goLoginLogin(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("/login/login.do param 1111:: " + params);
    	CommonUtils.setModelByParams(model, params);
    	
    	ValueMap result = new ValueMap();
    	ValueMap testInfo = loginService.getTest(params);
    	log.info("param 2222:: " + testInfo);
    	
    	
    	
    	return "login/login";
    	//return "cmm/uat/uia/EgovLoginUsr";
    }
    
    /**
     * 로그인페이지2 이동 
     */
    @RequestMapping(value="/admin/main.do")
    public String goMain(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	CommonUtils.setModelByParams(model, params, request);
    	
    	return "/main";
    }
    
    	
    /**
     * @Description  로그인 처리
     * @param model
     * @return null
     * @exception Exception
     */
    @RequestMapping(value="/login/checkAdminUser.do")
    public String checkAdminUser(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model)
    				throws Exception {
    	AuthCheck idCk = new AuthCheck (request, response);
    	if(idCk.isLogin())
    		idCk.logOut();
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("checkAdminUser.do   param :: " + params);
    	
    	String checkResult = "";
    	String failCnt = "";		// 패스워드 실패 횟수.
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	Properties pInfo 		= new Properties();
    	
    	// Transaction Setting
    	DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    	def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    	
    	TransactionStatus status = transactionManager.getTransaction(def);
    	
    	try{
    		
    		if(!CommonUtils.isNull((String)params.get("locale"))){
        		String locale = (String)params.get("locale");
        		Locale locales = null;
        		// 넘어온 파라미터에 ko가 있으면 Locale의 언어를 한국어로 바꿔준다.
        		// 그렇지 않을 경우 영어로 설정한다.
        		if (locale.matches("ko")) {
        			locales = new Locale("ko", "KR");
        		} else {
        			locales = new Locale("en", "US");
        		}
        		log.debug("changeLocale :: " + locales);
        		
        		HttpSession session = request.getSession();
        		
        		// 세션에 존재하는 Locale을 새로운 언어로 변경해준다.
        		session.setAttribute(
        				SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locales);
        		log.debug("CommonUtils.getLocale(request)::: " + CommonUtils.getLocale(request));
        		log.debug("SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME :: " + session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME));
        		//log.debug(" board.save.success.msg1 :  " + egovMessageSource.getMessage("common.save.msg", locales));
        		//log.debug(" Locale.KOREAN :  " + Locale.KOREAN);
        		log.debug(" board.save.success.msg3 :  " + egovMessageSource.getMessage("common.save.msg", CommonUtils.getLocale(request)));
        		//log.debug(" board.save.success.msg2 :  " + egovMessageSource.getMessage("common.save.msg", null, Locale.KOREAN));
        	}
//        	{
//        		String locale = "en";
//        		Locale locales = null;
//        		// 넘어온 파라미터에 ko가 있으면 Locale의 언어를 한국어로 바꿔준다.
//        		// 그렇지 않을 경우 영어로 설정한다.
//        		if (locale.matches("ko")) {
//        			locales = new Locale("ko", "KR");
//        		} else {
//        			locales = new Locale("en", "US");
//        		}
//        		log.debug("changeLocale :: " + locales);
//        		
//        		HttpSession session = request.getSession();
//        		
//        		// 세션에 존재하는 Locale을 새로운 언어로 변경해준다.
//        		session.setAttribute(
//        				SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locales);
//        		log.debug("CommonUtils.getLocale(request)::: " + CommonUtils.getLocale(request));
//        		log.debug("SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME :: " + session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME));
//        		log.debug(" board.save.success.msg3 :  " + egovMessageSource.getMessage("common.save.msg", CommonUtils.getLocale(request)));
//        		//log.debug(" board.save.success.msg :  " + egovMessageSource.getMessage("common.save.msg", null, Locale.ENGLISH));
//        		
//        	}
    		//ValueMap loginInfo = loginService.getLoginInfo(params);
    		ValueMap loginInfo = loginService.getAdminLoginInfo(params);
    		
    		// 로그인 정보가 없는 경우
    		
    		if(loginInfo == null){
    			checkResult = "1"; 		// 아이디 미 존재 값
    		}
    		else if(loginInfo.isEmpty()){
    			checkResult = "1"; 		// 아이디 미 존재 값
    		}
    		else{
    			// 패스워드 실패한도가 넘은경우
    			if("N".equals(loginInfo.getString("FAIL_CNT_YN"))){
    				checkResult = "2";
    			}
    			else{
    				// 패스워드 틀린경우
    				if("N".equals(loginInfo.getString("PASS_YN"))){
    					checkResult = "3";
    					loginService.updateFailCnt(params);
    				}
    				// 정상적 로그인의 경우
    				else{
    					checkResult = "10";
    					// session에 필요한 값만 정리
    					pInfo.setProperty( AuthCheck.USER_ID, 		loginInfo.getString("USER_ID"));
    					pInfo.setProperty( AuthCheck.USER_NM, 		loginInfo.getString("USER_NM"));
    					pInfo.setProperty( AuthCheck.LOGIN_EMAIL, 	CommonUtils.NVL(loginInfo.getString("USER_EMAIL")));
    					pInfo.setProperty( AuthCheck.USER_GROUP, 	"A");	// ADMIN SITE
    					
    					idCk.putSession (pInfo);	// 세션 생성
    					params.put("LOGIN_YN", "Y");
    					loginService.updateFailCnt(params);
    					
    					
						String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
						if (CommonUtils.isNull(ip)) {
							ip = request.getRemoteAddr();
						}
						
						logger.debug("ip :: " + ip);
    					params.clear();
    					params.put( "MENU_ID", "LOGIN" ); //메뉴 ID
    					params.put( "USER_ID", idCk.getUser_id() );
    					params.put( "LOGIN_IP", CommonUtils.getClientIP(request));
//	    	    		commonService.insertMenuUse(params);
//    					loginService.insertLoginLog(params);
    					
    				}
    			}
    		}
    		
    		
    		result.put("checkResult", checkResult);
    		result.put("ds_loginInfo", loginInfo);
    		transactionManager.commit(status);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		transactionManager.rollback(status);
    		success = false;
    		//result.put("msg", "로그인처리중 에러가 발생하였습니다.");
    		result.put("msg", egovMessageSource.getMessage("fail.common.msg"));
    		log.info(e.getMessage());
    	}
    	
    	log.debug("checkResult :: " + checkResult);
    	result.put("success", success);
    	response.setContentType("text/xml;charset=UTF-8");
    	response.getWriter().println(CommonUtils.setJsonResult(result));
    	
    	return null;
    } 
    
    
    /**
     * @Description  로그인 처리
     * @param model
     * @return null
     * @exception Exception
     */
    @RequestMapping(value="/login/checkUser.do")
    public String checkUser(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model)
    				throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("checkUser.do   param :: " + params);
    	
    	AuthCheck idCk = new AuthCheck (request, response);
    	String checkResult = "";
    	String failCnt = "";		// 패스워드 실패 횟수.
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	Properties pInfo 		= new Properties();
    	
    	// Transaction Setting
    	DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    	def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    	
    	TransactionStatus status = transactionManager.getTransaction(def);
    	
    	try{
    		ValueMap loginInfo = loginService.getLoginInfo(params);
    		
    		// 로그인 정보가 없는 경우
    		
    		if(loginInfo == null){
    			checkResult = "1"; 		// 아이디 미 존재 값
    		}
    		else if(loginInfo.isEmpty()){
    			checkResult = "1"; 		// 아이디 미 존재 값
    		}
    		else{
    			// 패스워드 실패한도가 넘은경우
    			if("N".equals(loginInfo.getString("FAIL_CNT_YN"))){
    				checkResult = "2";
    			}
    			else{
    				// 패스워드 틀린경우
    				if("N".equals(loginInfo.getString("PASS_YN"))){
    					checkResult = "3";
    					loginService.updateFailCnt(params);
    				}
    				// 정상적 로그인의 경우
    				else{
    					checkResult = "10";
    					// session에 필요한 값만 정리
    					pInfo.setProperty( AuthCheck.USER_ID, 		loginInfo.getString("USER_ID"));
    					pInfo.setProperty( AuthCheck.USER_NM, 		loginInfo.getString("USER_NM"));
    					pInfo.setProperty( AuthCheck.LOGIN_EMAIL, 	CommonUtils.NVL(loginInfo.getString("USER_EMAIL")));
    					//pInfo.setProperty( AuthCheck.USER_GROUP, 	loginInfo.getString("USER_GROUP"));
    					
    					idCk.putSession (pInfo);	// 세션 생성
    					params.put("LOGIN_YN", "Y");
    					loginService.updateFailCnt(params);
    					
    					
    					String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    					if (CommonUtils.isNull(ip)) {
    						ip = request.getRemoteAddr();
    					}
    					
    					logger.debug("ip :: " + ip);
    					params.clear();
    					params.put( "MENU_ID", "LOGIN" ); //메뉴 ID
    					params.put( "USER_ID", idCk.getUser_id() );
    					params.put( "LOGIN_IP", CommonUtils.getClientIP(request));
//	    	    		commonService.insertMenuUse(params);
    					//loginService.insertLoginLog(params);
    					
    				}
    			}
    			
    		}
    		
    		
    		result.put("checkResult", checkResult);
    		result.put("ds_loginInfo", loginInfo);
    		transactionManager.commit(status);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		transactionManager.rollback(status);
    		success = false;
    		result.put("msg", "로그인처리중 에러가 발생하였습니다.");
    		log.info(e.getMessage());
    	}
    	
    	
    	result.put("success", success);
    	response.setContentType("text/xml;charset=UTF-8");
    	response.getWriter().println(CommonUtils.setJsonResult(result));
    	
    	return null;
    } 
    
    /**
     * @Description  로그아웃 처리
     * @return null
     * @exception Exception
     */
    @RequestMapping(value="/login/logout.do")
    public String logOut(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map params = CommonUtils.getRequestMap(request);
    	AuthCheck idCk = new AuthCheck (request, response);
    	CommonUtils.setModelByParams(model, params, request);
    	
    	if(!CommonUtils.isNull(idCk.getUser_id())){
    		params.clear();
    		params.put( "MENU_ID", "LOGOUT" ); //메뉴 ID
    		params.put( "USER_ID", idCk.getUser_id() );
//    		commonService.insertMenuUse(params);
    	}
    	
    	idCk.logOut();
    	
    	ValueMap result = new ValueMap();
    	
//    	result.put("success", true);
//    	response.setContentType("text/xml;charset=UTF-8");
//    	response.getWriter().println(CommonUtils.setJsonResult(result));
    	
    	return "/main";
    }

}
