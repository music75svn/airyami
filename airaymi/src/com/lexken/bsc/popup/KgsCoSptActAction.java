/*************************************************************************
* CLASS 명      : kgsCoSptAct
* 작 업 자      : 김기현
* 작 업 일      : 2012년 6월 18일
* 기    능      : 시스템항목관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김기현      시스템항목관리            최 초 작 업
**************************************************************************/
package com.lexken.bsc.popup;

import java.sql.SQLException;
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

public class KgsCoSptActAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 시스템항목관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap kgsCoSptActList(SearchMap searchMap) {
    	
    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
    		searchMap.put("findDeptId", getStr("bsc.popup.kgsCoSptAct.getScDeptMapping", searchMap));
    	}

    	searchMap.addList("deptList", getList("bsc.popup.kgsCoSptAct.getDeptList", searchMap));

    	searchMap.addList("itemInfo", getList("bsc.popup.kgsCoSptAct.getItem", searchMap));

        return searchMap;
    }

    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap kgsCoSptActList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.popup.kgsCoSptAct.getList", searchMap));

        return searchMap;
    }

    /**
     * 시스템 연계데이터 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap kgsCoSptActProcess(SearchMap searchMap) {
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
    	String excelFileName = "기업지원실적(전사)";
    	String excelTitle = "기업지원실적(전사)";
    	
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
    	
    	if(!"S073D005".equals((String)searchMap.get("findItemCd"))){
	    	excelInfoList.add(new ExcelVO("부서명", 	"DEPT_NM", 			"center"));
	    	excelInfoList.add(new ExcelVO("종류",		"RPT_TC_NM", 		"center"));
	    	excelInfoList.add(new ExcelVO("교육구분", 	"ROLE_TC_NM", 		"center"));
	    	excelInfoList.add(new ExcelVO("검사구분", 	"INSP_KIND_TC_NM", 	"center"));
	    	excelInfoList.add(new ExcelVO("가스법구분", "LAW_TC_NM", 		"center"));
	    	excelInfoList.add(new ExcelVO("대분류", 	"STIC_LAG_TC_NM", 	"center"));
	    	excelInfoList.add(new ExcelVO("중분류", 	"STIC_MID_TC_NM", 	"center"));
	    	excelInfoList.add(new ExcelVO("소분류", 	"STIC_SML_TC_NM", 	"center"));
	    	excelInfoList.add(new ExcelVO("건수", 	"INSP_QTY", 		"center"));
    	}else{
    		excelInfoList.add(new ExcelVO("부서명", 	"DEPT_NM", 			"center"));
	    	excelInfoList.add(new ExcelVO("시설구분",	"EQUI_NM", 			"center"));
	    	excelInfoList.add(new ExcelVO("업체명", 	"CO_NM", 			"center"));
	    	excelInfoList.add(new ExcelVO("사업자번호", "BSNO", 			"center"));
	    	excelInfoList.add(new ExcelVO("주소", 	"ADDR", 			"center"));
	    	excelInfoList.add(new ExcelVO("수입종류", 	"INSP_NM", 			"center"));
	    	excelInfoList.add(new ExcelVO("검사계획일", "INSP_PLAN_DATE", 	"center"));
	    	excelInfoList.add(new ExcelVO("실제검사일", "REAL_INSP_DATE", 	"center"));
    	}
    
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	ArrayList list = (ArrayList)getList("bsc.popup.kgsCoSptAct.getList", searchMap);
    	
    	searchMap.put("excelDataList", list);
    	
    	return searchMap;
    }

}
