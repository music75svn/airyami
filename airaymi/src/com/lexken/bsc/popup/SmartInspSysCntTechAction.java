/*************************************************************************
* CLASS 명      : ProEduAction
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

public class SmartInspSysCntTechAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 시스템항목관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap smartInspSysCntTechList(SearchMap searchMap) {
    	
    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
    		searchMap.put("findDeptId", getStr("bsc.popup.smartInspSysCntTech.getScDeptMapping", searchMap));
    	}
    	
    	searchMap.addList("deptList", getList("bsc.popup.smartInspSysCntTech.getDeptList", searchMap));

    	searchMap.addList("itemInfo", getList("bsc.popup.smartInspSysCntTech.getItem", searchMap));

        return searchMap;
    }

    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap smartInspSysCntTechList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.popup.smartInspSysCntTech.getList", searchMap));

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
    	String excelFileName = "스마트검사시스템검사처리건수";
    	String excelTitle = "스마트검사시스템검사처리건수";
    	
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
    	excelInfoList.add(new ExcelVO("수입구분",	"INSP_DATE", 		"center"));
    	excelInfoList.add(new ExcelVO("가중치합", 	"CNT_TOTAL", 		"center"));
    	
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
    	
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.smartInspSysCntTech.getList", searchMap));
    	
    	return searchMap;
    }

}
