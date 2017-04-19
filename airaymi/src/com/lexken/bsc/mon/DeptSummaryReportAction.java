/*************************************************************************
* CLASS 명      : DeptSummaryReportAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 9월 1일 
* 기    능      : 부서별요약보고서
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      2012년 9월 1일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.mon;
    
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

public class DeptSummaryReportAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 부서별요약보고서 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptSummaryReportList(SearchMap searchMap) {
    	
        //성과조직명
    	searchMap.addList("scDeptNm", getStr("bsc.module.commModule.getScDeptNm", searchMap));

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
    
}
