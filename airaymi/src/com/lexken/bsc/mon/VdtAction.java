/*************************************************************************
* CLASS 명      : VdtAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 11월 28일 
* 기    능      : VDT 모니터링
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    하윤식      2012년 11월 28일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.mon;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class VdtAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * VDT 모니터링 조회
     * @param      
     * @return String
     * @throws 
     */
    public SearchMap vdtList(SearchMap searchMap) {
    	
    	searchMap.addList("strategyList", getList("bsc.mon.vdt.getStrategyList", searchMap));
    	
    	if("".equals(searchMap.getString("findStrategyId"))) {
        	searchMap.put("findStrategyId", searchMap.getDefaultValue("strategyList", "STRATEGY_ID", 0));
        }

        return searchMap;
    }
    
    /**
     * VDT 모니터링 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap vdtList_xml(SearchMap searchMap) {
        
    	searchMap.addList("strategyInfo", getDetail("bsc.mon.vdt.getStrategyDetail", searchMap));
    	
    	searchMap.addList("csfList", getList("bsc.mon.vdt.getCsfList", searchMap));
    	
    	searchMap.addList("kpiList", getList("bsc.mon.vdt.getKpiList", searchMap));

        return searchMap;
    }
    
}





