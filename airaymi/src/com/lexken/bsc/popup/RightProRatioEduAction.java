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

public class RightProRatioEduAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 시스템항목관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap rightProRatioEduList(SearchMap searchMap) {

    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
    		searchMap.put("findDeptId", getStr("bsc.popup.rightProRatioEdu.getScDeptMapping", searchMap));
    	}

    	searchMap.addList("deptList", getList("bsc.popup.rightProRatioEdu.getDeptList", searchMap));

    	searchMap.addList("itemInfo", getList("bsc.popup.rightProRatioEdu.getItem", searchMap));

        return searchMap;
    }

    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap rightProRatioEduList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.popup.rightProRatioEdu.getList", searchMap));

        return searchMap;
    }

    /**
     * 부서 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap deptList_ajax(SearchMap searchMap) {

    	searchMap.addList("deptList", getList("bsc.popup.rightProRatioEdu.getDeptList", searchMap));

    	if("".equals(searchMap.getString("findDeptId"))) {
        	searchMap.put("findDeptId", searchMap.getDefaultValue("deptList", "DEPT_ID", 0));
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
    	String excelFileName = "계약업무적기처리율";
    	String excelTitle = "계약업무적기처리율";

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
    	excelInfoList.add(new ExcelVO("년도",			"YEAR", 				"center"));
    	excelInfoList.add(new ExcelVO("월", 			"MON", 					"center"));
    	excelInfoList.add(new ExcelVO("구매유형", 		"BUY_KIND_TC_N_NM", 	"center"));
    	excelInfoList.add(new ExcelVO("사업장명",		"BULC_CD_N", 			"center"));
    	excelInfoList.add(new ExcelVO("구매부서명", 	"BUY_DEPT_CD_N", 		"center"));
    	excelInfoList.add(new ExcelVO("의뢰부서명", 	"BUY_RQST_DEPT_CD_NM", 	"center"));
    	excelInfoList.add(new ExcelVO("계약종류",		"AGRE_KIND_TC_NM", 		"center"));
    	excelInfoList.add(new ExcelVO("계약구분", 		"BUY_METH_TC_NM", 		"center"));
    	excelInfoList.add(new ExcelVO("구매의뢰일자", 	"BUY_RQST_DATE", 		"center"));
    	excelInfoList.add(new ExcelVO("계약일자",		"AGRE_DATE", 			"center"));
    	excelInfoList.add(new ExcelVO("계약시작일자", 	"AGRE_ST_DATE", 		"center"));
    	excelInfoList.add(new ExcelVO("계약제목", 		"AGRE_TITL", 			"center"));
    	excelInfoList.add(new ExcelVO("총계약금액", 	"TOT_AMT", 				"center"));
    	excelInfoList.add(new ExcelVO("거래처", 		"CUST_CD_NM", 			"center"));

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

    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.rightProRatioEdu.getList", searchMap));

    	return searchMap;
    }

}
