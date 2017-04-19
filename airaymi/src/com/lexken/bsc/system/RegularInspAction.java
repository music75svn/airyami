/*************************************************************************
* CLASS 명      : RegularInspAction
* 작 업 자      : 김기현
* 작 업 일      : 2012년 6월 26일 
* 기    능      : 시스템 연계데이터 조회
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김기현      2012년 6월 26일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.system;
    
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

public class RegularInspAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 시스템 연계데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap regularInspList(SearchMap searchMap) {
    	
    	//최상위 평가조직 조회
        searchMap.addList("deptInfo", getList("bsc.system.regularInsp.getDeptInfo", searchMap));

        return searchMap;
    }
    
    /**
     * 시스템 연계데이터 조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap regularInspList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.system.regularInsp.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 시스템 연계데이터 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap regularInspProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("GET".equals(stMode)) {
        }
        
         return searchMap;
    }
    
    
}
