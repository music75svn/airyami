/*************************************************************************
* CLASS 명      : IntellectualProperty
* 작 업 자      : 안요한
* 작 업 일      : 2013년 08월 13일
* 기    능      : 지적재산권 실적 점수 산출 조회
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    안요한      해외사업 수입            최 초 작 업
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

public class IntellectualPropertyAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 지적재산권 실적 점수 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap intellectualPropertyList(SearchMap searchMap) {

    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.intellectualProperty.getScDeptMapping", searchMap));
		}

    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findOutput")))) {
    		searchMap.put("findOutput", "01");
    	}

    	searchMap.addList("itemInfo", getList("bsc.popup.intellectualProperty.getItem", searchMap));

        searchMap.addList("deptInfo", getList("bsc.popup.intellectualProperty.getDeptInfo", searchMap));

        return searchMap;
    }

    /**
     * 지적재산권 실적 점수 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap intellectualPropertyList_xml(SearchMap searchMap) {

		searchMap.addList("list", getList("bsc.popup.intellectualProperty.getList", searchMap));


        return searchMap;
    }

public SearchMap intellectualPropertyList2_xml(SearchMap searchMap) {

		searchMap.addList("list", getList("bsc.popup.intellectualProperty.getList2", searchMap));

        return searchMap;
    }

/**
 * 엑셀다운로드
 * @param
 * @return String
 * @throws
 */
public SearchMap excel(SearchMap searchMap) {
	String excelFileName = "지적재산권실적(건)";
	String excelTitle = "지적재산권실적(건)";

	/************************************************************************************
	 * 조회조건 설정
	 ************************************************************************************/
	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

	excelSearchInfoList.add(new ExcelVO("년도", (String)searchMap.get("yearNm")));
	excelSearchInfoList.add(new ExcelVO("월", (String)searchMap.get("monNm")));
	excelSearchInfoList.add(new ExcelVO("부서", (String)searchMap.get("deptNm")));
	excelSearchInfoList.add(new ExcelVO("항목", (String)searchMap.get("itemNm")));
	excelSearchInfoList.add(new ExcelVO("산출기준", (String)searchMap.get("outPutNm")));

	/****************************************************************************************************
	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
	 ****************************************************************************************************/
	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
	if (searchMap.get("output").equals("01")) {
	excelInfoList.add(new ExcelVO("년도","YEAR", "center"));
	excelInfoList.add(new ExcelVO("부서코드", "EXEC_DEPT_CD", "center"));
	excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "left"));
	excelInfoList.add(new ExcelVO("특허출원구분명", "PAT_TC_NM", "left"));
	excelInfoList.add(new ExcelVO("산업재산권명", "PROP_RGHT_NM", "left"));
	excelInfoList.add(new ExcelVO("발명자명", "INVR_NM_EMPN_NM", "center"));
	excelInfoList.add(new ExcelVO("진행상태", "APRV_TC_NM", "center"));
	excelInfoList.add(new ExcelVO("과제명", "SUBJ_NM", "left"));
	excelInfoList.add(new ExcelVO("점수", "SCORE", "center"));
	} else {												// 배정액
		excelInfoList.add(new ExcelVO("년도","YEAR", "center"));
		excelInfoList.add(new ExcelVO("부서코드", "SUBJ_NO", "left"));
		excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "left"));
		excelInfoList.add(new ExcelVO("학술논문구분코드명", "SCIT_TC_NM", "left"));
		excelInfoList.add(new ExcelVO("학술지 논문명", "SCMG_NM", "left"));
		excelInfoList.add(new ExcelVO("학술대회명", "SCMG_AREA_NM", "left"));
		excelInfoList.add(new ExcelVO("주저자 사번", "MAIN_EMPN", "center"));
		excelInfoList.add(new ExcelVO("주저자", "MAIN_EMPN_NM", "center"));
		excelInfoList.add(new ExcelVO("과제명", "SUBJ_NM", "left"));
		excelInfoList.add(new ExcelVO("논문발표게재일", "PPR_ANN_DATE", "center"));
		excelInfoList.add(new ExcelVO("점수", "SCORE", "center"));
	}

	searchMap.put("excelFileName", excelFileName);
	searchMap.put("excelTitle", excelTitle);
	searchMap.put("excelSearchInfoList", excelSearchInfoList);
	searchMap.put("excelInfoList", excelInfoList);
	if (searchMap.get("output").equals("01")) {
	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.intellectualProperty.getList", searchMap));
	} else {
		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.intellectualProperty.getList2", searchMap));
	}
	return searchMap;
}


}
