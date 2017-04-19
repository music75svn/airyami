/*************************************************************************
* CLASS 명  	: StaticFactory
* 작 업 자  	: 하윤식
* 작 업 일  	: 2012년 6월 23일 
* 기    능  	: Static 맵핑
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일          변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식      2012년 6월 23일 		  최 초 작 업 
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.bsc.module.CommModuleAction;
import com.lexken.framework.codeUtil.CodeUtil;
import com.lexken.framework.scDeptUtil.ScDeptUtilDAO;
import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.struts2.IsBoxActionSupport;


public class StaticFactory extends IsBoxActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Logger
	private final Log logger = LogFactory.getLog(getClass());
	
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
        
        String url[] = req.getRequestURI().replace('.', '/').split("/");
		String methodName = url[url.length - 2];
		
		/**********************************
         * Code 데이터 Static Memory reload
         **********************************/
		if("codeUtilReLoad".equals(methodName)) {
			CodeUtil.reSetCodeUtil();
		}
		
		/**********************************
         * ScDept 데이터 Static Memory reload
         **********************************/
		if("scDeptUtilReLoad".equals(methodName)) {
			ScDeptUtilDAO.getInstance().reSetCodeUtil();
		}
        
        	 
		return null;
	}
	
}
