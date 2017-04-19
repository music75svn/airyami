/*************************************************************************
* CLASS 명      : CommissionEducation
* 작 업 자      : 안요한
* 작 업 일      : 2013년 8월 14일 
* 기    능      : 안전관리교육실적(가중치) 조회
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    안요한      2013년 8월 14일             최 초 작 업 
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

public class FrgnAuthSptAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 시스템항목관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap frgnAuthSptList(SearchMap searchMap) {

    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
    		searchMap.put("findDeptId", getStr("bsc.popup.frgnAuthSpt.getScDeptMapping", searchMap));
    	}
    	
    	searchMap.addList("deptList", getList("bsc.popup.frgnAuthSpt.getDeptList", searchMap));

    	searchMap.addList("itemInfo", getList("bsc.popup.frgnAuthSpt.getItem", searchMap));

        return searchMap;
    }

    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap frgnAuthSptList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.popup.frgnAuthSpt.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 부서 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("deptList", getList("bsc.popup.frgnAuthSpt.getDeptList", searchMap));

    	if("".equals(searchMap.getString("findDeptId"))) {
        	searchMap.put("findDeptId", searchMap.getDefaultValue("deptList", "DEPT_ID", 0));
        }

        return searchMap;
    }
    

    /**
     * 시스템항목관리 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap systemItemModify(SearchMap searchMap) {

    	String stMode = searchMap.getString("mode");

    	if(!"ADD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.system.systemItem.getDetail", searchMap));
    	}

        return searchMap;
    }

    /**
     * 시스템항목관리 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap systemItemProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        return searchMap;
    }
    
    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel(SearchMap searchMap) {
    	String excelFileName = "해외인증지원건수";
    	String excelTitle = "해외인증지원건수";
    	
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
    	excelInfoList.add(new ExcelVO("년도",		"YEAR", 			"center"));
    	excelInfoList.add(new ExcelVO("월", 		"MON", 				"center"));
    	excelInfoList.add(new ExcelVO("부서명", 	"DEPT_NM", 			"center"));
    	excelInfoList.add(new ExcelVO("수입구분",	"RPT_TC_NM", 		"center"));
    	excelInfoList.add(new ExcelVO("업무구분", 	"ROLE_TC_NM", 		"center"));
    	excelInfoList.add(new ExcelVO("검사구분", 	"INSP_KIND_TC_NM", 	"center"));
    	excelInfoList.add(new ExcelVO("가스법구분",	"LAW_TC_NM", 		"center"));
    	excelInfoList.add(new ExcelVO("대분류", 	"STIC_LAG_TC_NM", 	"center"));
    	excelInfoList.add(new ExcelVO("중분류", 	"STIC_MID_TC_NM", 	"center"));
    	excelInfoList.add(new ExcelVO("소분류",	"STIC_SML_TC_NM", 	"center"));
    	excelInfoList.add(new ExcelVO("건수", 	"INSP_QTY", 		"center"));
    	
    	/*
    	if (searchMap.get("findItemCd").equals("S028D001")) {	// 집행액
    		excelInfoList.add(new ExcelVO("집행액", "BGR_RSLT_QUAT", "center"));
    	} else {												// 배정액
    		excelInfoList.add(new ExcelVO("배정액", "APRV_QUAT", "center"));
    	}
    	*/
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.frgnAuthSpt.getList", searchMap));
    	
    	return searchMap;
    }

}

