/*************************************************************************
* CLASS 명      : ResearchResultImproveSysAction
* 작 업 자      : 김기현
* 작 업 일      : 2013년 8월 11일 
* 기    능      : 시스템 연계데이터 조회
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김기현      2013년 8월 11일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.popup;
    
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

public class PlanRegStoreAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 시스템 연계데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap planRegStoreList(SearchMap searchMap) {
    	
    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
    		searchMap.put("findDeptId", getStr("bsc.popup.planRegStore.getScDeptMapping", searchMap));
    	}

    	searchMap.addList("deptList", getList("bsc.popup.planRegStore.getDeptList", searchMap));

    	searchMap.addList("itemInfo", getList("bsc.popup.planRegStore.getItem", searchMap));

        return searchMap;
    }
    
    /**
     * 시스템 연계데이터 조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap planRegStoreList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.popup.planRegStore.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 시스템 연계데이터 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap planRegStoreProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("GET".equals(stMode)) {
        }

         return searchMap;
    }

    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel(SearchMap searchMap) {
    	String excelFileName = "계획검사정착률(계획등록업소수)";
    	String excelTitle = "계획검사정착률(계획등록업소수)";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

    	excelSearchInfoList.add(new ExcelVO("년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("월", (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("부서", (String)searchMap.get("deptNm")));
    	excelSearchInfoList.add(new ExcelVO("항목", (String)searchMap.get("itemNm")));

    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("년도","YEAR", "center"));
    	excelInfoList.add(new ExcelVO("월", "MON", "center"));
    	excelInfoList.add(new ExcelVO("부서코드", "BR_CD", "left"));
    	excelInfoList.add(new ExcelVO("부서명", "BR_NM", "left"));
    	excelInfoList.add(new ExcelVO("사번", "EMPN", "left"));
    	excelInfoList.add(new ExcelVO("성명", "EMPN_NM", "left"));
    	excelInfoList.add(new ExcelVO("허가구분", "PERM_NM", "left"));
    	excelInfoList.add(new ExcelVO("정기검사 검가건수", "C1_INSP_QTY", "left"));
    	excelInfoList.add(new ExcelVO("정기검사 계획건수", "C1_PLAN_QTY", "left"));
    	excelInfoList.add(new ExcelVO("정기검사 계획등록률", "C1_P", "left"));
    	excelInfoList.add(new ExcelVO("정기점검 검사건수", "C2_INSP_QTY", "left"));
    	excelInfoList.add(new ExcelVO("정기점검 계획건수", "C2_PLAN_QTY", "left"));
    	excelInfoList.add(new ExcelVO("정기점검 계획등록률", "C2_P", "left"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);

    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.planRegStore.getList", searchMap));

    	return searchMap;
    }
    
    
}
