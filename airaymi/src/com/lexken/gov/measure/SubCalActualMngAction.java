/*************************************************************************
* CLASS 명      : SubCalActualMngAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 11월 12일 
* 기    능      : 세부요소입력
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    하윤식      2012년 11월 12일         최 초 작 업 
**************************************************************************/
package com.lexken.gov.measure;
    
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
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class SubCalActualMngAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 세부요소입력 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap subCalActualMngList(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	/**********************************
         * 세부요소 입력자 조회
         **********************************/
    	searchMap.addList("insertUserList", getList("gov.measure.subCalActualMng.getInsertUserList", searchMap));
        
    	/**********************************
         * 권한별 처리
         **********************************/
    	if(!loginVO.chkAuthGrp("01") && !loginVO.chkAuthGrp("60") && !loginVO.chkAuthGrp("10")) {
    		searchMap.put("findInsertUserId", searchMap.get("loginUserId"));
    	}
    	
        return searchMap;
    }
    
    /**
     * 세부요소입력 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap subCalActualMngList_xml(SearchMap searchMap) {
    	
    	/**********************************
         * 세부요소 조회
         **********************************/
        searchMap.addList("list", getList("gov.measure.subCalActualMng.getList", searchMap));
        

        return searchMap;
    }

    /**
     * 세부요소입력 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap subCalActualMngProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 세부요소입력 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        /**********************************
         * Parameter setting
         **********************************/
        String[] mainSubCalIds = searchMap.getStringArray("mainSubCalIds");
        String[] subCalIds = searchMap.getStringArray("subCalIds");
        String[] actuals = searchMap.getStringArray("actuals");
        String[] mons = searchMap.getStringArray("mons");
        
        try {
        	setStartTransaction();
        
        	/**********************************
             * 실적삭제
             **********************************/
        	if(null != mainSubCalIds && 0 < mainSubCalIds.length) {
		        for (int i = 0; i < mainSubCalIds.length; i++) {
		            searchMap.put("subCalId", mainSubCalIds[i]);
		            returnMap = insertData("gov.measure.subCalActualMng.deleteData", searchMap, true);
		        }
        	}
        	
        	/**********************************
             * 실적입력
             **********************************/
        	if(null != subCalIds && 0 < subCalIds.length) {
		        for (int i = 0; i < subCalIds.length; i++) {
		            searchMap.put("subCalId", subCalIds[i]);
		            searchMap.put("mon", mons[i]);
		            searchMap.put("actual", actuals[i]);
		            
		            if(!"".equals(StaticUtil.nullToBlank(actuals[i]))) { 
		            	returnMap = insertData("gov.measure.subCalActualMng.insertData", searchMap);
		            }
		        }
        	}
        	
        	/**********************************
             * 실적집계
             **********************************/
        	returnMap = insertData("gov.measure.subCalActualMng.execDB", searchMap);
        	
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
