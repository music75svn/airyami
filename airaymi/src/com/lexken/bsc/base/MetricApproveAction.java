/*************************************************************************
* CLASS 명      : MetricApproveAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 7월 18일 
* 기    능      : 지표승인 진행관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식      2012년 7월 18일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.base;
    
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class MetricApproveAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 지표승인 진행관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricApproveList(SearchMap searchMap) {
    	
    	searchMap.addList("closeYn", getStr("bsc.base.metric.getCloseYn", searchMap));

        return searchMap;
    }
    
    /**
     * 지표승인 진행관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricApproveList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.base.metricApprove.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 지표승인 진행관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricApproveModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.base.metricApprove.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 지표승인 진행관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricApproveProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode)) {
            returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
        
        /**********************************
         * 마감/마감취소
         **********************************/
        if("CLOSE".equals(stMode)) {
            searchMap = closeDB(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 마감실행
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap closeDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	/**********************************
	         * 데이터 등록여부 확인
	         **********************************/
	        int cnt = getInt("bsc.base.metricApprove.getCloseCount", searchMap);
	        
	        if(0 < cnt) {
	        	returnMap = updateData("bsc.base.metricApprove.updateData", searchMap);
	        } else {
	            returnMap = insertData("bsc.base.metricApprove.insertData", searchMap);
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
    
    
    /**
     * 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    /*
	    try {
	        String[] metricApproveIds = searchMap.getString("metricApproveIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        for (int i = 0; i < metricApproveIds.length; i++) {
	            searchMap.put("metricApproveId", metricApproveIds[i]);
	            returnMap = updateData("bsc.base.metricApprove.deleteData", searchMap);
	        }
	        
	    } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setEndTransaction();
        }
	    */   
        
        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;    
    }

    /**
     *  Validation 체크(무결성 체크)
     * @param SearchMap
     * @return HashMap
     */
    private HashMap validChk(SearchMap searchMap) {
        HashMap returnMap         = new HashMap();
        int     resultValue        = 0;
        
        //Validation 체크 추가
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
    
}
