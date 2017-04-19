/*************************************************************************
* CLASS 명      : SystemLoginMorDailyAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 6월 20일
* 기    능      : 시스템사용현황(일별)
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      2012년 6월 20일             최 초 작 업
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

public class SystemLoginMorDailyAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 시스템사용현황(일별) 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap systemLoginMorDailyList(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 시스템사용현황(일별) 그리드 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap systemLoginMorDailyList_xml(SearchMap searchMap) {
    	
    	String findScDeptId = searchMap.txt2html("findScDeptId");
    	
    	if("".equals(findScDeptId)){
    		findScDeptId = "D000001";
    	}
    	
    	searchMap.put("findScDeptId", findScDeptId);

        searchMap.addList("list", getList("bsc.system.systemLoginMorDaily.getList", searchMap));
        return searchMap;
    }

    /**
     * 시스템사용현황(일별)  상세 그리드 데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap systemLoginMorDailyModify(SearchMap searchMap) {

    	return searchMap;
    }

    /**
     * 시스템사용현황(일별) 상세 그리드 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap systemLoginMorDailyModify_xml(SearchMap searchMap) {
    	
    	String findScDeptId = searchMap.txt2html("findScDeptId");
    	
    	if("".equals(findScDeptId)){
    		findScDeptId = "D000001";
    	}
    	
    	searchMap.put("findScDeptId", findScDeptId);

    		searchMap.addList("detail", getList("bsc.system.systemLoginMorDaily.getDetail", searchMap));

    	return searchMap;
    }

    /**
     * 시스템사용현황(일별) 차트 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap systemLoginMorDailyChart_xml(SearchMap searchMap) {
    	
    	String findScDeptId = searchMap.txt2html("findScDeptId");
    	
    	if("".equals(findScDeptId)){
    		findScDeptId = "D000001";
    	}
    	
    	searchMap.put("findScDeptId", findScDeptId);

    	searchMap.addList("chartList", getList("bsc.system.systemLoginMorDaily.getChartList", searchMap));
        return searchMap;
    }

}
