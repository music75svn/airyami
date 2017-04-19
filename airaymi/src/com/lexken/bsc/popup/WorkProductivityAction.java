/*************************************************************************
* CLASS 명      : workProductivity
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

public class WorkProductivityAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 시스템항목관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap workProductivityList(SearchMap searchMap) {
    	
    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
    		searchMap.put("findDeptId", getStr("bsc.popup.workProductivity.getScDeptMapping", searchMap));
    	}
    	
    	searchMap.addList("deptList", getList("bsc.popup.workProductivity.getDeptList", searchMap));
    	
    	searchMap.addList("itemInfo", getList("bsc.popup.workProductivity.getItem", searchMap));

        return searchMap;
    }

    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap workProductivityList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.popup.workProductivity.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel(SearchMap searchMap) {
    	String excelFileName = "업무생산성(처리건수)";
    	String excelTitle = "업무생산성(처리건수)";
    	
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
    	excelInfoList.add(new ExcelVO("부서명", 	"DEPT_NM", 			"center"));
    	excelInfoList.add(new ExcelVO("평가기준", 	"INSP_KIND_TC_NM", 	"center"));
    	excelInfoList.add(new ExcelVO("항목", 	"ROLE_TC_NM", 		"center"));
    	excelInfoList.add(new ExcelVO("연계", 	"LAW_TC_NM", 		"center"));
    	excelInfoList.add(new ExcelVO("대상시설", 	"STIC_LAG_TC_NM", 	"center"));
    	excelInfoList.add(new ExcelVO("상세시설", 	"STIC_MID_TC_NM", 	"center"));
    	excelInfoList.add(new ExcelVO("처리건수", 	"REQ_QTY", 			"center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	String deptId = searchMap.getString("deptId");
    	searchMap.put("findDeptId", deptId);
    	
    	ArrayList list = (ArrayList)getList("bsc.popup.workProductivity.getList", searchMap);
    	
    	searchMap.put("excelDataList", list);
    	
    	return searchMap;
    }

}
