/*************************************************************************
* CLASS 명  	: SecureAction
* 작 업 자  	: 하윤식
* 작 업 일  	: 2012.06.01
* 기    능  	: 인증관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작   업   일        변 경 내 용                비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식		 2012.06.01		     최 초 작 업 
**************************************************************************/

package com.lexken.framework.login;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.HttpRequest;
import com.lexken.framework.common.SHA1;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.config.CommonConfig;
import com.lexken.framework.struts2.IsBoxActionSupport;
import com.lexken.framework.util.CalendarHelper;
import com.lexken.framework.util.StaticUtil;


public class SecureAction  extends IsBoxActionSupport {
	private final Log logger = LogFactory.getLog(getClass());
	
	// SecureService
	private SecureService secureService = new SecureService();
	
	private static final String YEAR = (new CalendarHelper()).modifyDate(0, -1, 0,"yyyy");
	//private static final String YEAR = "2013";
	
	/**
	 * 로그인 폼
	 * @return String
	 */
	public String loginForm() {
		/**********************************
		 * 정의
		 **********************************/
		LoginVO loginVO 	= new LoginVO();
		String	returnUrl	= "";
		String	errCode	    = "";
		String  errMsg      = "";
		
		/**********************************
		 * Request
		 **********************************/
		HttpRequest request = new HttpRequest(req);
		returnUrl	= request.getString("returnUrl", "");
		errCode	    = request.getString("errCode", "");
		
		String encUserId = request.getString("encUserId", "");
		String encUserPasswd = request.getString("pass", "");
		
		if (!"".equals(encUserPasswd)) {
			BASE64Decoder dec = new BASE64Decoder();
			try {
				encUserPasswd = new String( dec.decodeBuffer( encUserPasswd ) );
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		if (!"".equals(errCode)) {
			errMsg = ErrorMessages.LOGIN_INFO_DUP_MESSAGE;
		}
		
		/**********************************
		 * 기본값 셋팅
		 **********************************/
		loginVO.setReturnUrl(returnUrl);
		req.getSession().removeAttribute("menuList");
		
		/**********************************
		 * 로그인 방법
		 **********************************/
		String ssoGubun = StaticUtil.nullToBlank(req.getParameter("loginFlag"));
		if(!ssoGubun.equals("none")) {
			ssoGubun = "SSO";
		}
		
		//임시사용 SSO 적용시 삭제 처리 해야 함
		ssoGubun = "none";
			
		/**********************************
		 * return
		 **********************************/
		req.setAttribute("context", req.getContextPath());
		req.setAttribute("loginVO", loginVO);
		req.setAttribute("errMsg", errMsg);
		req.setAttribute("ssoGubun", ssoGubun);
		
		if (!"".equals(encUserId)) {
			try {
				loginDo(encUserId, encUserPasswd);
			} catch (IOException ioe) {
				ioe.printStackTrace();
				req.setAttribute("errMsg", errMsg);
			}
		}
		return this.goLoginPage();
	}
	
	/**
	 * 로그인 처리(가스피아에서 ID, Password 받아서 로그인 처리
	 * @return String
	 * @throws IOException 
	 */
	public String loginDo(String userId, String password) throws IOException {
		/**********************************
		 * Request 값 읽기
		 **********************************/
		SearchMap searchMap = new SearchMap(req, res); 
		if(!searchMap.bfileUpload) return null;

		/**********************************
		 * 인증 관련
		 **********************************/
		LoginManager login = new LoginManager(req, res);
		
		/**********************************
		 * 세션 지우기
		 **********************************/
		login.delLoginSession();

		/**********************************
		 * 정의
		 **********************************/
		String returnUrl 	= "";	
		String errMsg		= "";
		
		/**********************************
		 * SSL적용
		 **********************************/
		CommonConfig commonConfig	 = CommonConfig.getInstance();
		String       http_domain_url = commonConfig.getProperty("HTTP_DOMAIN_URL");
		
		/**********************************
		 * 연도설정
		 **********************************/		
		searchMap.put("year", YEAR);
		
		/**********************************
		 * 로그인
		 **********************************/
		String cryptPasswd = "";
		String passwd = searchMap.getString("password");
		SHA1 SHACrypt = new SHA1();
		cryptPasswd = SHACrypt.crypt(password);
		searchMap.put("cryptPasswd", password);
		searchMap.put("userId", userId);
		searchMap.put("loginFlag", "LINK");
		int intDataCnt = 0;
		intDataCnt = secureService.getUserInfoCount(searchMap);
		
		/**********************************
		 * 로그인 실패
		 **********************************/
		if(intDataCnt < 1) {
			errMsg = ErrorMessages.USER_UNDEFINED_MESSAGE;
			StaticUtil.jsAlertBack(StaticUtil.kscToAsc(errMsg), res.getOutputStream());
			return null;
		}
		
		/**********************************
		 * Login 상세정보 읽음
		 **********************************/
		LoginVO loginTempVO = secureService.getUserInfo(searchMap);
		
		/**********************************
		 * Login 정보처리
		 **********************************/
		if(loginTempVO == null) { // 아이디 미존재
			errMsg = ErrorMessages.USER_UNDEFINED_MESSAGE;
			StaticUtil.jsAlertBack(StaticUtil.kscToAsc(errMsg), res.getOutputStream());
			return null;
		} 
		
		/**********************************
		 * 권한정보 상세정보 읽음
		 **********************************/
		ArrayList authGrpList = (ArrayList)secureService.getAuthGrpList(searchMap);
		
		/**********************************
		 * 로그인후 이동 화면
		 **********************************/
		if("".equals(StaticUtil.nullToBlank(loginTempVO.getDashboard_url()))) {
			returnUrl = searchMap.getString("returnUrl", req.getContextPath() + "/bsc/mon/main/mainLev4List.vw?type=template&amp;loginId=" + searchMap.getString("userId"));
		} else if("admin1".equals(loginTempVO.getUser_id())){ // admin1 계정으로 접속했을때 메인화면이 보이지 않음.
			returnUrl = searchMap.getString("returnUrl", req.getContextPath() + "/bsc/mon/main1/mainLev5List.vw?type=template&amp;loginId=" + searchMap.getString("userId"));
		} else {
			returnUrl = searchMap.getString("returnUrl", req.getContextPath() + "/bsc/mon/main/" + loginTempVO.getDashboard_url() + ".vw?type=template&amp;loginId=" + searchMap.getString("userId"));
		}
		

		/**********************************
		 * LoginVO 에 담기
		 **********************************/
		LoginVO loginVO = login.getLoginVO();
		loginVO.setSabun(loginTempVO.getSabun());
		loginVO.setUser_id(loginTempVO.getUser_id());
		loginVO.setUser_reg_id(loginTempVO.getUser_reg_id());
		loginVO.setName_han(loginTempVO.getName_han());
		loginVO.setUser_nm(loginTempVO.getUser_nm());
		loginVO.setPasswd(loginTempVO.getPasswd());
		loginVO.setDept_cd(loginTempVO.getDept_cd());
		loginVO.setDept_id(loginTempVO.getDept_id());
		loginVO.setDept_nm(loginTempVO.getDept_nm());
		loginVO.setJikgub_cd(loginTempVO.getJikgub_cd());
		loginVO.setJikgub_nm(loginTempVO.getJikgub_nm());
		loginVO.setPos_cd(loginTempVO.getPos_cd());
		loginVO.setPos_nm(loginTempVO.getPos_nm());
		loginVO.setEmail(loginTempVO.getEmail());
		loginVO.setBeing_yn(loginTempVO.getBeing_yn());
		loginVO.setSort_order(loginTempVO.getSort_order());
		loginVO.setCreate_dt(loginTempVO.getCreate_dt());
		loginVO.setSc_dept_id(loginTempVO.getSc_dept_id());
		loginVO.setSc_dept_nm(loginTempVO.getSc_dept_nm());
		loginVO.setSc_dept_grp_id(loginTempVO.getSc_dept_grp_id());
		loginVO.setUp_sc_dept_id(loginTempVO.getUp_sc_dept_id());
		loginVO.setUp_sc_dept_nm(loginTempVO.getUp_sc_dept_nm());
		loginVO.setDashboard_url(loginTempVO.getDashboard_url());
		loginVO.setAuthList(authGrpList);
		
		loginVO.setDept_slump_id(loginTempVO.getDept_slump_id());
		
		String temp = "";
        temp = req.getHeader("WL-Proxy-Client-IP");
        
        if(temp == null || "".equals(temp)){
            temp = req.getRemoteAddr();
        }

        loginVO.setLogin_ip(temp);

        /**********************************
		 * 세션에 세팅
		 **********************************/
		login.setLoginSession();
		
		searchMap.put("loginVO", loginVO);
		int result = secureService.insertLoginLog(searchMap);
		res.sendRedirect(returnUrl); 
		return null;
	}
	
	/**
	 * 로그인 처리
	 * @return String
	 * @throws IOException 
	 */
	public String loginDo() throws IOException {
		/**********************************
		 * Request 값 읽기
		 **********************************/
		SearchMap searchMap = new SearchMap(req, res); 
		if(!searchMap.bfileUpload) return null;

		/**********************************
		 * 인증 관련
		 **********************************/
		LoginManager login = new LoginManager(req, res);
		
		/**********************************
		 * 세션 지우기
		 **********************************/
		login.delLoginSession();

		/**********************************
		 * 정의
		 **********************************/
		String returnUrl 	= "";	
		String errMsg		= "";
		
		/**********************************
		 * SSL적용
		 **********************************/
		CommonConfig commonConfig	 = CommonConfig.getInstance();
		String       http_domain_url = commonConfig.getProperty("HTTP_DOMAIN_URL");
		
		/**********************************
		 * 연도설정
		 **********************************/		
		searchMap.put("year", YEAR);
		
		/**********************************
		 * 로그인
		 **********************************/
		String cryptPasswd = "";
		String passwd = searchMap.getString("passwd");
		SHA1 SHACrypt = new SHA1();
		cryptPasswd = SHACrypt.crypt(passwd);
		//searchMap.put("cryptPasswd", cryptPasswd);
		searchMap.put("cryptPasswd", passwd);
		
		int intDataCnt = 0;
		intDataCnt = secureService.getUserInfoCount(searchMap);
		
		/**********************************
		 * 로그인 실패
		 **********************************/
		if(intDataCnt < 1) {
			errMsg = ErrorMessages.USER_UNDEFINED_MESSAGE;
			StaticUtil.jsAlertBack(StaticUtil.kscToAsc(errMsg), res.getOutputStream());
			return null;
		}
		
		/**********************************
		 * Login 상세정보 읽음
		 **********************************/
		LoginVO loginTempVO = secureService.getUserInfo(searchMap);
		
		/**********************************
		 * Login 정보처리
		 **********************************/
		if(loginTempVO == null) { // 아이디 미존재
			errMsg = ErrorMessages.USER_UNDEFINED_MESSAGE;
			StaticUtil.jsAlertBack(StaticUtil.kscToAsc(errMsg), res.getOutputStream());
			return null;
		} 
		
		/**********************************
		 * 권한정보 상세정보 읽음
		 **********************************/
		ArrayList authGrpList = (ArrayList)secureService.getAuthGrpList(searchMap);
		
		/**********************************
		 * 로그인후 이동 화면
		 **********************************/
		if("".equals(StaticUtil.nullToBlank(loginTempVO.getDashboard_url()))) {
			returnUrl = searchMap.getString("returnUrl", req.getContextPath() + "/bsc/mon/main/mainLev4List.vw?type=template&amp;loginId=" + searchMap.getString("userId"));
		} else if("admin1".equals(loginTempVO.getUser_id())){ // admin1 계정으로 접속했을때 메인화면이 보이지 않음.
			returnUrl = searchMap.getString("returnUrl", req.getContextPath() + "/bsc/mon/main1/mainLev5List.vw?type=template&amp;loginId=" + searchMap.getString("userId"));
		} else {
			returnUrl = searchMap.getString("returnUrl", req.getContextPath() + "/bsc/mon/main/" + loginTempVO.getDashboard_url() + ".vw?type=template&amp;loginId=" + searchMap.getString("userId"));
		}
		

		/**********************************
		 * LoginVO 에 담기
		 **********************************/
		LoginVO loginVO = login.getLoginVO();
		loginVO.setSabun(loginTempVO.getSabun());
		loginVO.setUser_id(loginTempVO.getUser_id());
		loginVO.setUser_reg_id(loginTempVO.getUser_reg_id());
		loginVO.setName_han(loginTempVO.getName_han());
		loginVO.setUser_nm(loginTempVO.getUser_nm());
		loginVO.setPasswd(loginTempVO.getPasswd());
		loginVO.setDept_cd(loginTempVO.getDept_cd());
		loginVO.setDept_id(loginTempVO.getDept_id());
		loginVO.setDept_nm(loginTempVO.getDept_nm());
		loginVO.setJikgub_cd(loginTempVO.getJikgub_cd());
		loginVO.setJikgub_nm(loginTempVO.getJikgub_nm());
		loginVO.setPos_cd(loginTempVO.getPos_cd());
		loginVO.setPos_nm(loginTempVO.getPos_nm());
		loginVO.setEmail(loginTempVO.getEmail());
		loginVO.setBeing_yn(loginTempVO.getBeing_yn());
		loginVO.setSort_order(loginTempVO.getSort_order());
		loginVO.setCreate_dt(loginTempVO.getCreate_dt());
		loginVO.setSc_dept_id(loginTempVO.getSc_dept_id());
		loginVO.setSc_dept_nm(loginTempVO.getSc_dept_nm());
		loginVO.setSc_dept_grp_id(loginTempVO.getSc_dept_grp_id());
		loginVO.setUp_sc_dept_id(loginTempVO.getUp_sc_dept_id());
		loginVO.setUp_sc_dept_nm(loginTempVO.getUp_sc_dept_nm());
		loginVO.setDashboard_url(loginTempVO.getDashboard_url());
		loginVO.setAuthList(authGrpList);
		
		loginVO.setDept_slump_id(loginTempVO.getDept_slump_id());
		
		String temp = "";
        temp = req.getHeader("WL-Proxy-Client-IP");
        
        if(temp == null || "".equals(temp)){
            temp = req.getRemoteAddr();
        }

        loginVO.setLogin_ip(temp);

        /**********************************
		 * 세션에 세팅
		 **********************************/
		login.setLoginSession();
		
		searchMap.put("loginVO", loginVO);
		int result = secureService.insertLoginLog(searchMap);
		res.sendRedirect(returnUrl); 
		return null;
	}
	
	/**
	 * 로그아웃 처리
	 * @return String
	 * @throws IOException 
	 */
	public String logout() {
		/**********************************
		 * Request 값 읽기
		 **********************************/
		SearchMap searchMap = new SearchMap(req, res);
		
		/**********************************
		 * 인증 관련
		 **********************************/		
		LoginManager login = new LoginManager(req, res);
		
		/**********************************
		 * 세션 지우기
		 **********************************/
		login.delLoginSession();

		try {
			res.sendRedirect(req.getContextPath() + "/secure/loginForm.vw");
		} catch(Exception e) {
			
		}
		
		return "logout";
	}
	
	public String error() {
		/**********************************
		 * Request값 읽기
		 **********************************/
		SearchMap searchMap = new SearchMap(req, res); 
		String errorMessage = req.getParameter("ERROR_MESSAGE");
		req.setAttribute("searchMap", searchMap);
		req.setAttribute("errorMessage", errorMessage);
		return "ERROR_PAGE";
	}
	
}
