/*************************************************************************
* CLASS 명  	: LoginManager
* 작 업 자  	: 박소라
* 작 업 일  	: 2009.06.01
* 기    능  	: 인증관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    박소라		 2009.06.01			  최 초 작 업 
**************************************************************************/
 
package com.lexken.framework.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lexken.framework.common.RefererUrlCheck;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.util.CalendarHelper;
import com.lexken.framework.util.StaticUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class LoginManager {
	private final Log logger = LogFactory.getLog(getClass());

	private HttpServletRequest 	req			= null;
	private HttpServletResponse res			= null;

	private LoginVO loginVO = new LoginVO();		// 로그인 정보

	private HashMap menuGroupLevMap = new HashMap();
	
	/**
	 * 생성자 #1
	 * @param req, res
	 * @return   
	 */
	public LoginManager(HttpServletRequest req, HttpServletResponse res) {
		this.req = req;
		this.res = res;
		
		// 세션 가져오기
		getLoginSession();
		
		// 웹페이지에서 변수 설정
		this.setAttribute(this.req);
		
		//사용자 로그 입력
		if(loginVO != null){
			String loginUserId = loginVO.getUser_id();
			
			if(loginUserId != null && !"".equals(loginUserId)){
				SearchMap searchMap = new SearchMap(req, res);
				searchMap.put("loginUserId", loginUserId);
				
				SecureService secureService = SecureService.getInstance();
				//secureService.insertUserLog(searchMap);
			}
		}
	}
	
	/**
	 * 생성자 #2
	 * @param req, res, menuId, type
	 * @return   
	 */
	public LoginManager(HttpServletRequest req, HttpServletResponse res, String menuId, String type) {
		
		try {
			this.req = req;
			this.res = res;
			
			// 세션 가져오기
			getLoginSession();
			
			loginVO.setFailure(false);
			
			String currentUrl = getCurrentUrl();
			
			// ----------------------------------
			// referer 체크 
			// ----------------------------------
			if(currentUrl.indexOf("mainLev") < 0) {
				String headerReferer = req.getHeader("referer");
				
				if(RefererUrlCheck.chkRefererUrl(headerReferer)){
					loginVO.setFailure(false);
					setTSVClear();
				}else{
					if(this.chkTSV()){
						loginVO.setFailure(false);
					}else{
						String errorMsg = ErrorMessages.format(ErrorMessages.REFERER_ERROR_MESSAGE, loginVO.getUser_nm());
						//StaticUtil.jsAlertRedirect(StaticUtil.kscToAsc(errorMsg), req.getAttribute("HTTP_DOMAIN_URL").toString() + req.getContextPath() +  "/secure/secure/loginForm.vw", this.res.getOutputStream());
						StaticUtil.jsAlertRedirect(StaticUtil.kscToAsc(errorMsg), setKgsProperties() + req.getContextPath() +  "/secure/loginForm.vw", this.res.getOutputStream());
						loginVO.setFailure(true);
					}
				}
			}
			
			if(!loginVO.isFailure()) {
				
				// 로그인 여부 체크
				if(!loginCheck()) loginVO.setFailure(true);
			}
			
			String isPageFirstLoadingYN = (String)req.getParameter("isPageFirstLoadingYN");
			
			// 로그인 여부 체크
			if(!loginVO.isFailure()) {
				// ----------------------------------
				// 메뉴권한 체크
				// ----------------------------------
				boolean mainView = false;
				String url = getCurrentUrl();
				if(null != url) {
					if(url.indexOf("mainList") > 0) {
						mainView = true;
					}
				}
				
				if("template".equals(type) && !mainView && "Y".equals(isPageFirstLoadingYN)) {
					if(!menuAuthCheck(menuId)) loginVO.setFailure(true);
				}
				
				// ----------------------------------
				// 웹페이지에서 변수 설정
				// ----------------------------------
				this.setAttribute(this.req);
			}
		} catch(Exception e) {
			loginVO.setFailure(true);
			loginVO.setLogin(false);
			logger.error(e.toString());
		}
	}
	
	/**
	 * 생성자 #3
	 * @param req, res, menuId, refererchk
	 * @return   
	 */
	public LoginManager(HttpServletRequest req, HttpServletResponse res, String menuId, boolean refererchk) {
		
		try {
			this.req = req;
			this.res = res;
			
			//세션 가져오기
			getLoginSession();
			
			loginVO.setFailure(false);
			
			// ----------------------------------
			// referer 체크
			// ----------------------------------
			
			if(!menuId.equals("ADMIN_TEMPLATE") && refererchk) {
				String headerReferer = req.getHeader("referer");
								
				if(RefererUrlCheck.chkRefererUrl(headerReferer)){
					loginVO.setFailure(false);
					setTSVClear();
				}else{
					if(this.chkTSV()){
						loginVO.setFailure(false);
					}else{
						String errorMsg = ErrorMessages.format(ErrorMessages.REFERER_ERROR_MESSAGE, loginVO.getUser_nm());
						loginVO.setFailure(true);
					}
				}
			}
			
			if(!loginVO.isFailure()) {
				// 로그인 여부 체크
				if(!loginCheck()) loginVO.setFailure(true);
			}
			
			// 로그인 여부 체크
			if(!loginVO.isFailure()) {
				// ----------------------------------
				//  페이지 네비게이션 세팅
				// ----------------------------------
				SecureService secureService = SecureService.getInstance();
				List pageNaviList = null;
				pageNaviList = secureService.getPageNavigator(menuId);
				loginVO.setPageNaviList(pageNaviList);
				
				// ----------------------------------
				// 메뉴권한 체크
				// ----------------------------------
				if(!menuId.equals("ADMIN_TEMPLATE")) {		// 어드민 템플릿은 제외
					if(!menuAuthCheck(menuId)) loginVO.setFailure(true);
				}
				
				// ----------------------------------
				// 웹페이지에서 변수 설정
				// ----------------------------------
				this.setAttribute(this.req);
			}
		} catch(Exception e) {
			loginVO.setFailure(true);
			loginVO.setLogin(false);
			logger.error(e.toString());
		}
	}
	
	/**
	 * 생성자 #8
	 * @param req, res, menuId, refererchk
	 * @return   
	 */
	public LoginManager(HttpServletRequest req, HttpServletResponse res, String menuId, boolean refererchk, boolean loginchk) {
		
		try {
			this.req = req;
			this.res = res;
			
			// 세션 가져오기
			getLoginSession();

			this.setAttribute(this.req);
			
			loginVO.setFailure(false);
			
			// ----------------------------------
			// referer 체크
			// ----------------------------------
			
			if(!menuId.equals("ADMIN_TEMPLATE") && refererchk) {
				String headerReferer = req.getHeader("referer");

				if(RefererUrlCheck.chkRefererUrl(headerReferer)){
					loginVO.setFailure(false);
					setTSVClear();
				}else{
					if(this.chkTSV()){
						loginVO.setFailure(false);
					}else{
						String errorMsg = ErrorMessages.format(ErrorMessages.REFERER_ERROR_MESSAGE, loginVO.getUser_nm());
						StaticUtil.jsAlertRedirect(StaticUtil.kscToAsc(errorMsg), req.getAttribute("HTTP_DOMAIN_URL").toString() + req.getContextPath() +  "/bsc/mon/main/mainList.vw?type=template", this.res.getOutputStream());
						loginVO.setFailure(true);
					}
				}
			}
			
			//로그인체크 사용시 
			if (loginchk) {
				if(!loginVO.isFailure()) {
					// 로그인 여부 체크
					if(!loginCheck()) loginVO.setFailure(true);
				}
				
				// 로그인 여부 체크
				if(!loginVO.isFailure()) {
					// ----------------------------------
					//  페이지 네비게이션 세팅
					// ----------------------------------
					SecureService secureService = SecureService.getInstance();
					List pageNaviList = null;
					pageNaviList = secureService.getPageNavigator(menuId);
					loginVO.setPageNaviList(pageNaviList);
					
					// ----------------------------------
					// 메뉴권한 체크
					// ----------------------------------
					if(!menuId.equals("ADMIN_TEMPLATE")) {		// 어드민 템플릿은 제외
						if(!menuAuthCheck(menuId)) loginVO.setFailure(true);
					}
					
					// ----------------------------------
					// 웹페이지에서 변수 설정
					// ----------------------------------
					//this.setAttribute(this.req);
				}
			}
			
		} catch(Exception e) {
			loginVO.setFailure(true);
			loginVO.setLogin(false);
			logger.error(e.toString());
		}
	}
	
	/**
	 * 생성자 #4
	 * @param req, res, searchMap
	 * @return   
	 */
	public LoginManager(HttpServletRequest req, HttpServletResponse res, SearchMap searchMap) {
		
		
		this(req, res, searchMap.getString("findPgmId"), searchMap.getString("type"));
		
		searchMap.put("loginUserId", this.getUser_id());
		searchMap.put("loginUserNm", this.getUser_nm());
		searchMap.put("loginVO", this.getLoginVO());
		
		//사용자 로그 입력
		//SecureService secureService = SecureService.getInstance();
		//secureService.insertUserLog(searchMap);
	}
	
	/**
	 * 생성자 #5
	 * @param req, res, searchMap, refererchk
	 * @return   
	 */
	public LoginManager(HttpServletRequest req, HttpServletResponse res, SearchMap searchMap, boolean refererchk) {
		this(req, res, searchMap.getString("menuId"), refererchk);
		
		searchMap.put("loginUserId", this.getUser_id());
		searchMap.put("loginMenuGroupLev", this.getMenuGroupLev(searchMap.getString("menuId")));
		searchMap.put("loginVO", this.getLoginVO());
	}
	
	/**
	 * 생성자 #6 (Chart 전용)
	 * @param req, res, refererchk
	 * @return   
	 */
	public LoginManager(HttpServletRequest req, HttpServletResponse res, boolean refererchk) {
		
		try {
			this.req = req;
			this.res = res;
			
			// 세션 가져오기
			getLoginSession();
			
			loginVO.setFailure(false);

			String userAgent = (req.getHeader("user-agent")).toUpperCase();
	        if (userAgent.indexOf("FIREFOX") > 0 || userAgent.indexOf("SAFARI") > 0) {
	        	refererchk = false;
	        }
			
			// ----------------------------------
			// referer 체크
			// ----------------------------------
			if(refererchk) {
				String headerReferer = req.getHeader("referer");

				if(RefererUrlCheck.chkRefererUrl(headerReferer)){
					loginVO.setFailure(false);
					setTSVClear();
				}else{
					if(this.chkTSV()){
						loginVO.setFailure(false); 
					}else{
						String errorMsg = ErrorMessages.format(ErrorMessages.REFERER_ERROR_MESSAGE, loginVO.getUser_nm());
						StaticUtil.jsAlertRedirect(StaticUtil.kscToAsc(errorMsg), req.getAttribute("HTTP_DOMAIN_URL").toString() + req.getContextPath() +  "/bsc/mon/main/mainList.vw?type=template", this.res.getOutputStream());
						loginVO.setFailure(true);
					}
				}
			}

		} catch(Exception e) {
			loginVO.setFailure(true);
			loginVO.setLogin(false);
			logger.error(e.toString());
		}
	}
	
	/**
	 * 생성자 #7
	 * @param req, res, searchMap, refererchk
	 * @return   
	 */
	public LoginManager(HttpServletRequest req, HttpServletResponse res, SearchMap searchMap, boolean refererchk, boolean loginchk) {
		this(req, res, searchMap.getString("menuId"), refererchk, loginchk);
		
		searchMap.put("loginUserId", this.getUser_id());
		searchMap.put("loginMenuGroupLev", this.getMenuGroupLev(searchMap.getString("menuId")));
		searchMap.put("loginVO", this.getLoginVO());
	}
	
	/**
	 * 로그인 인증후 Session에 저장한다.
	 * @param 
	 * @return   
	 */
	public void setLoginSession(){
		HttpSession session = req.getSession();
		session.setMaxInactiveInterval(1*60*60);	//세션 1시간 설정
		session.setAttribute("loginVO", loginVO);
	}
	
	/**
	 * 로그아웃 처리 한다.
	 * @param 
	 * @return   
	 */
	public void delLoginSession(){
		HttpSession session = req.getSession();
		session.removeAttribute("loginVO");
		session.removeAttribute("menuList");
		session.removeAttribute("menuGroupLevMap");
		session.removeAttribute("findHighMenuId");
		session.setMaxInactiveInterval(0);
		
		loginVO.setReturnUrl("");
	}

	/**
	 * 세션값을 읽는다.
	 * @param 
	 * @return   
	 */
	public void getLoginSession(){	
		
		loginVO = new LoginVO();
		
		loginVO.setLogin(false); // 로그인 여부(true, false)
		
		HttpSession session = req.getSession();
		if(session.getAttribute("loginVO") != null){
			loginVO = (LoginVO)session.getAttribute("loginVO");
			loginVO.setLogin(true); // 로그인 여부(true, false)
		}
	}
	
	/**
	 * 현재 URL을 구한다	
	 * @param 
	 * @return String 
	 */
	public String getCurrentUrl(){
		String rurl 		= "";
		String str 			= "";
		String queryString 	= "";
		String strValue 	= "";
		
		if(req.getRequestURI() != null)
			rurl = req.getRequestURI();
		
		for(Enumeration paraName = req.getParameterNames(); paraName.hasMoreElements();){
			str 		= (paraName.nextElement()).toString();
			strValue 	= req.getParameter(str);
			queryString = queryString + str + "=" + strValue + "&";
		}
		
		rurl = rurl + "?" + queryString;
		
		return rurl;
	}
	
	/**
	 * 로그인 여부 판단
	 * 로그인하지 않았으면 로그인 화면으로 이동한다. 
	 * @throws IOException
	 */
	public boolean loginCheck() throws IOException{		
		boolean is_auth = true;
		
		if(!loginVO.isLogin()){
			is_auth = false;
			String returnUrl = this.getCurrentUrl();
			loginVO.setReturnUrl(returnUrl);
			res.sendRedirect(req.getContextPath());
		}
		return is_auth;
	}

	/**
	 * 메뉴사용 권한 체크
	 * @param menuId
	 * @return boolean  
	 * @throws IOException  
	 */
	public boolean menuAuthCheck(String menuId) throws IOException {
		// ================================
		// 정의
		// ================================		
		boolean gradeChk = false;					// 메뉴 접근 권한
		HashMap hashMap = new HashMap();
		
		String menuUrl = req.getRequestURI();
		
		hashMap.put("userId", loginVO.getUser_id()); // 사용자 아이디
		hashMap.put("menuId", menuId);   		     // 메뉴 아이디
		
		// ================================
		// 메뉴접근권한 가져오기
		// ================================
		SecureService secureService = SecureService.getInstance();
		gradeChk = secureService.getMenuGradeFlag(hashMap);
		
		// 메뉴권한 미존재
		if(gradeChk == false) {
			String errorMsg = ErrorMessages.format(ErrorMessages.USER_NO_AUTHORITY_MESSAGE, loginVO.getUser_nm());
			StaticUtil.jsAlertRedirect(StaticUtil.kscToAsc(errorMsg), req.getAttribute("HTTP_DOMAIN_URL").toString() + req.getContextPath() +  "/bsc/mon/main/mainList.vw?type=template", this.res.getOutputStream());
			return false;
		}
		
		return gradeChk;
	}
	
	/**
	 * 메뉴 리스트를 가져온다
	 * @param 
	 * @return   
	 */
	public ArrayList<MenuVO> getMenuList(){	
		
		ArrayList<MenuVO> menuList = null;
		
		HttpSession session = req.getSession();
		if(session.getAttribute("menuList") != null){
			menuList = (ArrayList<MenuVO>)session.getAttribute("menuList");
		}
		
		if(menuList == null || menuList.size() < 1){
			SecureService secureService = SecureService.getInstance();
			menuList = secureService.getMenuList(loginVO.getUser_id());
			if(menuList != null && menuList.size() > 0){
				session.setAttribute("menuList", menuList);
			}
		}
		
		return menuList;
	}


	/**
	 * Set Attribute 
	 * @param req     
	 */
	private void setAttribute (HttpServletRequest req) {
		
		//로그인정보
		this.req.setAttribute("CS_LOGIN", loginVO);

		//메뉴정보
		if(loginVO != null){
			ArrayList<MenuVO> menuList = this.getMenuList();
			if(menuList != null && menuList.size() > 0){
				this.req.setAttribute("CS_MENU_LIST",	menuList);
			}
		}
	}
	
	//2010.10.24 박재현 추가 SSL -> HTTP 로 이동시 레퍼러 체크 안되는 부분 보완
    public void setTSV(){
    	String tempTime = "" + System.currentTimeMillis();
    	req.getSession().setAttribute("TSV", tempTime);
    	req.setAttribute("TSV", tempTime);
    }
    
    public boolean chkTSV(){
    	boolean result = false;
    	String tempTime1 = (String)req.getSession().getAttribute("TSV");
    	String tempTime2 = (String)req.getParameter("TSV");
    	if(tempTime1 != null && tempTime2 != null && !"".equals(tempTime1) && !"".equals(tempTime2)){
    		if(tempTime1.equals(tempTime2)){
    			result = true;
    		}
    	}
    	setTSVClear();
    	return result;
    }
    
    public void setTSVClear(){
    	req.getSession().setAttribute("TSV", "");
		req.setAttribute("TSV", "");
    }
	
	/**
	 * getLoginVO
	 * @return
	 */
	public LoginVO getLoginVO() {
		return loginVO;
	}
	
	/**
	 * isLogin
	 * @return
	 */
	public boolean isLogin() {
		return loginVO.isLogin();
	}	

	/**
	 * isFailure
	 * @return
	 */
	public boolean isFailure() {
		return loginVO.isFailure();
	}
	
	public String getReturnUrl() {
		return loginVO.getReturnUrl();
	}
	
	public String getAdminType() {
		return loginVO.getAdminType();
	}
	
	public String getUser_id() {
		return loginVO.getUser_id();
	}

	public String getUser_nm() {
		return loginVO.getUser_nm();
	}

	public String getEmail() {
		return loginVO.getEmail();
	}

	public void addPageNaviList(String addName){
		loginVO.addPageNaviList(addName);
	}
	
	public String getMenuGroupLev(String menuId) {
		return (String)menuGroupLevMap.get(menuId);
	}
	
	public String setKgsProperties() {
		String reVal = "";
		
		Properties propertie = new Properties();		
		java.io.InputStream props = com.lexken.framework.config.CommonConfig.class.getResourceAsStream("common.properties");
		
		try
        {
            propertie.load(props);
            
            reVal = propertie.getProperty("HTTP_IP_URL");
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
		
		return reVal;
	}
}

