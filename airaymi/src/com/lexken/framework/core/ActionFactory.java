/*************************************************************************
* CLASS 명  	: ActionFactory
* 작 업 자  	: 하윤식
* 작 업 일  	: 2012년 5월 1일 
* 기    능  	: Action 맵핑
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작    업    일           변 경 내 용         비 고
* ----  --------  -----------------  -------------------------   --------
*   1    하윤식      2012년 5월 1일 		  최 초 작 업 
**************************************************************************/
package com.lexken.framework.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.bsc.module.CommModuleAction;
import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.login.SecureService;
import com.lexken.framework.struts2.IsBoxActionSupport;

import com.lexken.framework.common.ExcelUtil;
import com.lexken.framework.util.CalendarHelper;

public class ActionFactory extends IsBoxActionSupport {

	private static final long serialVersionUID = 1L;
	
	// Logger
	private final Log logger = LogFactory.getLog(getClass());

	// 사용자로그
	private SecureService secureService = new SecureService();

	
	/**
	 * @throws ClassNotFoundException 
	 * Action Mapping
	 * @param      
	 * @return String  
	 * @throws 
	 */
	public String process() throws IOException {

        /**********************************
         * Request값 읽기
         **********************************/
        SearchMap searchMap = new SearchMap(req, res); 
        if(!searchMap.bfileUpload) return null;
        String returnUrl = "";

        HashMap returnMap = new HashMap();
        String methodName = (String)searchMap.getString("methodName");
		String actionName = (String)searchMap.getString("actionName");
		String mainUrl = (String)searchMap.getString("mainUrl");
		String type = (String)searchMap.getString("type");
  
		HttpSession session = req.getSession();
		session.setAttribute("context", (String)req.getContextPath());

        /**********************************
         * 인증관련
         **********************************/
        LoginManager login = null;

        if("popup".equals(type)) {
        	login= new LoginManager(req, res);
        } else {
        	login= new LoginManager(req, res, searchMap);
        }
        
        if(login.isFailure()) return null;
        
        
        /**********************************
		 * 사용자로그관련
		 **********************************/
		searchMap.put("session", req.getSession());
		int result = secureService.insertUserLog(searchMap);
		
		try {
			Class cls = Class.forName(actionName);
			Object clsInstance = cls.newInstance();
			
			Method method = cls.getDeclaredMethod(methodName, new Class[]{searchMap.getClass()});
			searchMap = (SearchMap)method.invoke(clsInstance, searchMap);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("methodName : " + methodName);
			logger.error("actionName : " + actionName);
			logger.error("mainUrl : " + mainUrl);
			logger.error("type : " + type);
			logger.error(e);
			returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
			searchMap.addList("returnMap", returnMap);
			return "ERROR_PAGE";
		}
		
		/**********************************
		 * Return
		 **********************************/	
		req.setAttribute("searchMap", searchMap);
		
		Set<Entry<String, Object>> s = searchMap.hash.entrySet();
		Iterator<Entry<String, Object>> it = s.iterator();
		
		while(it.hasNext()) {
			Map.Entry m = it.next();
			String key = (String)m.getKey();
			Object value = (Object)m.getValue();
			req.setAttribute(key, value);
		}
		
		if("template".equals(type)) {
			return "mainTemplate";
		} else if("excel".equals(type)) {
			try {
				ExcelUtil excel = ExcelUtil.getInstance();
				excel.execlDown(res, searchMap);
			} catch(Exception e) {
				e.printStackTrace();
				logger.error(e);
			}
			return null;
		} else if("excelUi".equals(type)) {
			try {
				RequestDispatcher rd = req.getRequestDispatcher(mainUrl);
				CalendarHelper chelper = new CalendarHelper();
				
				String fileName = (String) searchMap.get("excelNm");
				res.setContentType("application/vnd.ms-excel; charset=utf-8");
				res.setHeader("Content-Disposition", "attachment;  filename="+fileName+"_"+chelper.getTime()+".xls");
				rd.forward(req,res);
				
			} catch(Exception e) {
				e.printStackTrace();
				logger.error(e);
			}
			return null;	
		} else {
			try {
				RequestDispatcher rd = req.getRequestDispatcher(mainUrl);
				rd.forward(req,res);
			} catch(Exception e) {
				e.printStackTrace();
				logger.error(e);
			}
			return null;
		}
	}
	
}
