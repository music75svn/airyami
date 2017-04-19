/*************************************************************************
* CLASS 명      : contributeAction
* 작 업 자      : 현걸욱
* 작 업 일      : 2012년 11월 5일
* 기    능      : 계량지표총괄표
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    현걸욱      2012년 11월 5일         최 초 작 업
**************************************************************************/
package com.lexken.man.mon;

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

public class ContributeAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 계량지표총괄표 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap contributeList(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 경영목표기여도 차트 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap manChart_xml(SearchMap searchMap) {

    	//차트 범주
    	searchMap.addList("manList", getList("man.mon.contribute.getManList", searchMap));

        return searchMap;
    }
    
    /**
     * 전략과제 차트
     * @param searchMap
     * @return
     */
    public SearchMap strategyChart_xml(SearchMap searchMap) {
    	//차트 범주
    	searchMap.addList("stratList", getList("man.mon.contribute.getStrategyList", searchMap));
    	if ("".equals(searchMap.get("manId"))) {
    		searchMap.put("manNm", "전체");
    	} else {
    		searchMap.put("manNm", getStr("man.mon.contribute.getManNm", searchMap));
    	}

        return searchMap;
    }
    
    /**
     * CSF 차트
     * @param searchMap
     * @return
     */
    public SearchMap csfChart_xml(SearchMap searchMap) {
    	//차트 범주
    	searchMap.addList("csfList", getList("man.mon.contribute.getCsfList", searchMap));
    	
    	String manNm = "";
    	String stratNm = "";
    	
    	if (!"".equals(searchMap.get("manId"))) {
    		manNm = getStr("man.mon.contribute.getManNm", searchMap);
    	}
    	if (!"".equals(searchMap.get("stratId"))) {
    		stratNm = getStr("man.mon.contribute.getStratNm", searchMap);
    	} else {
    		stratNm = "전체";
    	}
    	
    	if ("".equals(manNm)) {
    		searchMap.put("title", "전체");
    	} else {
    		searchMap.put("title", manNm + " - " + stratNm);
    	}

        return searchMap;
    }
}
