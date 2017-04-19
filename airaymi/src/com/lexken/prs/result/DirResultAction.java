/*************************************************************************
* CLASS 명      : DirResultAction
* 작 업 자      : 방승현
* 작 업 일      : 2013년 06월 24일 
* 기    능      : 임원평가-평가대상자 선정
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    방 승 현      2013년 06월 25일    최 초 작 업 
**************************************************************************/
package com.lexken.prs.result;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;

public class DirResultAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가대상자 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap dirEvalResultList(SearchMap searchMap) {
    	// 마감여부
    	searchMap.addList("evalYn", getDetail("prs.result.dirResult.getResultList", searchMap));    
    	
    	return searchMap;
    }
    
    /**
     * 평가대상자 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap dirEvalResultList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.result.dirResult.getResultList_xml", searchMap));

        
        return searchMap;
    }
       
    
}
