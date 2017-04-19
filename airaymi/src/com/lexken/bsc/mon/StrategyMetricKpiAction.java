/*************************************************************************
* CLASS 명      : StrategyMetricKpiAction
* 작 업 자      : 한봉준
* 작 업 일      : 2012년 8월 29일 
* 기    능      : 전략과제KPI리스트
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    한봉준      2012년 8월 29일             최 초 작 업 
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
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class StrategyMetricKpiAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 전략과제KPI리스트 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strategyMetricKpiList(SearchMap searchMap) {

    	/************************************************************************************
    	 * 성과조직이름 가져오기
    	 ************************************************************************************/
		searchMap.put("year", searchMap.get("findYear"));
		searchMap.put("scDeptId", searchMap.get("findScDeptId"));
		searchMap.put("findScDeptNm", getStr("bsc.module.commModule.getScDeptNm", searchMap));

    	/************************************************************************************
    	 * 전략과제이름 가져오기
    	 ************************************************************************************/
		searchMap.put("findStrategyNm", getStr("bsc.mon.strategyMatrix.getStrategyNm", searchMap));

        return searchMap;
    }
    
    /**
     * 전략과제KPI리스트 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strategyMetricKpiList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.mon.strategyMetricKpi.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 전략과제KPI차트 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strategyMetricKpiChart_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.mon.strategyMetricKpi.getChartList", searchMap));

        return searchMap;
    }
}
