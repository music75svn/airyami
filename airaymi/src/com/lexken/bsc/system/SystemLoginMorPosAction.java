/*************************************************************************
* CLASS 명      : SystemLoginMorPosAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 7월 15일 
* 기    능      : 시스템 사용현황(직위별)
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      2012년 7월 15일             최 초 작 업 
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

public class SystemLoginMorPosAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 시스템 사용현황(직위별) 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemLoginMorPosList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 시스템 사용현황(직위별) 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemLoginMorPosList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.system.systemLoginMorPos.getList", searchMap));
        return searchMap;
    }

    /**
     * 시스템사용현황(직위별) 차트(도넛) 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemLoginMorPosChart_1_xml(SearchMap searchMap) {
    	searchMap.addList("chartList", getList("bsc.system.systemLoginMorPos.getChar1List", searchMap));
    	return searchMap;
    }
    
    /**
     * 시스템사용현황(직위별) 차트(막대) 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemLoginMorPosChart_2_xml(SearchMap searchMap) {
    	searchMap.addList("chartList", getList("bsc.system.systemLoginMorPos.getChar2List", searchMap));
    	return searchMap;
    }
    
    /**
     * 시스템 사용현황(직위별) 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemLoginMorPosModify(SearchMap searchMap) {
        return searchMap;
    }
    
    /**
     * 시스템 사용현황(직위별) 상세화면(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemLoginMorPosModify_xml(SearchMap searchMap) {
    	
    	searchMap.addList("detail", getList("bsc.system.systemLoginMorPos.getDetail", searchMap));
    	return searchMap;
    }

}
