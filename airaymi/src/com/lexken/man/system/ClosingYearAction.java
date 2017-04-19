/*************************************************************************
* CLASS 명      : ClosingYearAction
* 작 업 자      : 현걸욱
* 작 업 일      : 2012년 11월 2일 
* 기    능      : 년마감
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    현걸욱      2012년 11월 2일         최 초 작 업 
**************************************************************************/
package com.lexken.man.system;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class ClosingYearAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 년마감관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap closingYearList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 년마감관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap closingYearList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("man.system.closingYear.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 년마감관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap closingYearProcess(SearchMap searchMap) {
        
    	HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 년마감관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
    	
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = insertData("man.system.closingYear.insertData", searchMap);
        
        } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setEndTransaction();
        }
        
        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;    
    }
    
    /**
     * 년마감관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
    	
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        int existCnt = getInt("man.system.closingYear.getListCnt", searchMap);
	        
	        if(existCnt > 0){
	        	returnMap = updateData("man.system.closingYear.updateData", searchMap);
	        } else {
	        	returnMap = insertData("man.system.closingYear.insertData", searchMap);
	        }
	        
        } catch (Exception e) {
        	
        	logger.error(e.toString());
        	setRollBackTransaction();
        	
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        
        } finally {
        	setEndTransaction();
        }
        
        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;    
    }    
    
}
