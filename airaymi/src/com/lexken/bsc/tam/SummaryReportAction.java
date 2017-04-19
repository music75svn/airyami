/*************************************************************************
* CLASS 명      : SummaryReportAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 8월 6일 
* 기    능      : 부서별요약보고서관리 
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      2012년 8월 6일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.tam;
    
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

public class SummaryReportAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 부서별요약보고서관리  조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap summaryReportList(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	String findScDeptId = (String)searchMap.get("findScDeptId");
    	
    	if("".equals(StaticUtil.nullToBlank(findScDeptId))) {
            //성과조직
    		searchMap.put("findScDeptId", loginVO.getSc_dept_id());
    	}
    	
        //요약
    	searchMap.addList("summary", getDetail("bsc.tam.summaryReport.getSummary", searchMap));
    	
    	//신호등구간
    	searchMap.addList("signalList", getList("bsc.tam.summaryReport.getSignalList", searchMap));
    	
    	//전략방향
    	searchMap.addList("strategyDirList", getList("bsc.tam.summaryReport.getStrategyDirList", searchMap));
    	
    	//전략과제
    	searchMap.addList("strategyList", getList("bsc.tam.summaryReport.getStrategyList", searchMap));
    	
    	//지표
    	searchMap.addList("metricList", getList("bsc.tam.summaryReport.getMetricList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 부서별요약보고서관리  상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap summaryReportModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.tam.summaryReport.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 부서별요약보고서관리  등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap summaryReportProcess(SearchMap searchMap) {
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
     * 부서별요약보고서관리  등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	returnMap = updateData("bsc.tam.summaryReport.deleteData", searchMap, true);
        	returnMap = insertData("bsc.tam.summaryReport.insertData", searchMap);
        	
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
