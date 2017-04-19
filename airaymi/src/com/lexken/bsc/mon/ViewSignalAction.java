/*************************************************************************
* CLASS 명      : ViewSignalAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 9월 5일 
* 기    능      : 신호등현황
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    정철수      2012년 9월 5일             최 초 작 업 
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

public class ViewSignalAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 신호등현황 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap viewSignalList(SearchMap searchMap) {
    	String findYear = (String)searchMap.get("findYear");
    	
		if( "".equals(StaticUtil.nullToBlank(findYear)) ) {
    		searchMap.put("findYear", (String)searchMap.get("year") );
    	}
    	
    	searchMap.addList("list", getList("bsc.mon.viewSignal.getList", searchMap));
        return searchMap;
    }
    
    /**
     * 신호등현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap viewSignalList_xml(SearchMap searchMap) {
        
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
