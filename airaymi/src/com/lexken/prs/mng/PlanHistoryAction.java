/*************************************************************************
* CLASS 명      : PlanHistoryAction
* 작 업 자      : 김효은
* 작 업 일      : 2013년 12월 10일 
* 기    능      : 성과계획서(팝업)
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김효은      2013년 12월 10일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.mng;
    
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

public class PlanHistoryAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 성과계획서(팝업) 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap planHistoryList(SearchMap searchMap) {
    	
    	/**********************************
         * evalUserId 디폴트 조회조건 설정
         **********************************/
    	String findYear = (String)searchMap.get("findYear");
    	String evalMembEmpn = (String)searchMap.get("evalMembEmpn");
    	String evalMembEmpnSeq = (String)searchMap.get("evalMembEmpnSeq");
    	
    	searchMap.addList("member", getList("prs.mng.planHistory.getFindMember", searchMap));
    	
		if("".equals(StaticUtil.nullToBlank(findYear))) {
    		searchMap.put("findYear", searchMap.getDefaultValue("member", "YEAR", 0));
    	}
		if("".equals(StaticUtil.nullToBlank(evalMembEmpn))) {
			searchMap.put("findEmpn", searchMap.getDefaultValue("member", "EVAL_MEMB_EMPN", 0));
		}
		if("".equals(StaticUtil.nullToBlank(evalMembEmpnSeq))) {
			searchMap.put("findEmpnSeq", searchMap.getDefaultValue("member", "EVAL_MEMB_EMPN_SEQ", 0));
		}
    	
        return searchMap;
    }
    
    /**
     * 성과계획서(팝업) 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap planHistoryList_xml(SearchMap searchMap) {
    	
    	 if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpn")))) {
     		searchMap.put("evalMembEmpn",  (String)searchMap.getString("findEmpn"));
     	}
     	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpnSeq")))) {
     		searchMap.put("evalMembEmpnSeq", (String)searchMap.getString("findEmpnSeq"));
     	}
		
        searchMap.addList("list", getList("prs.mng.planHistory.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 성과계획서(팝업) 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap planHistoryDetailList(SearchMap searchMap) {
    	searchMap.addList("membersTable", getDetail("prs.mng.planMng.getFindList", searchMap));//목록리스트
    	searchMap.addList("planList", getList("prs.mng.planHistory.getPlanList", searchMap));
        return searchMap;
    }
}
